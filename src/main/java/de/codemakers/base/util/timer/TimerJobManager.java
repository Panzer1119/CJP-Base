/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package de.codemakers.base.util.timer;

import de.codemakers.base.Standard;
import de.codemakers.base.entities.heap.DynamicBinaryMinHeap;
import de.codemakers.base.util.interfaces.Startable;
import de.codemakers.base.util.interfaces.Stoppable;

import java.util.concurrent.atomic.AtomicBoolean;

public class TimerJobManager implements Startable, Stoppable {
    
    protected final DynamicBinaryMinHeap<AbstractTimerJob> timerJobs = new DynamicBinaryMinHeap<>();
    protected final AtomicBoolean stopRequested = new AtomicBoolean(false);
    protected long updatePeriodMillis;
    protected Thread thread;
    
    public TimerJobManager() {
        this(100);
    }
    
    public TimerJobManager(long updatePeriodMillis) {
        this.updatePeriodMillis = updatePeriodMillis;
    }
    
    public void schedule(AbstractTimerJob timerJob) {
        schedule(timerJob, 0);
    }
    
    public void schedule(AbstractTimerJob timerJob, long delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Negative delay.");
        }
        scheduleIntern(timerJob, System.currentTimeMillis() + delay, 0);
    }
    
    public void scheduleAtFixedDelay(AbstractTimerJob timerJob, long period) {
        scheduleAtFixedDelay(timerJob, 0, period);
    }
    
    public void scheduleAtFixedDelay(AbstractTimerJob timerJob, long delay, long period) {
        if (delay < 0) {
            throw new IllegalArgumentException("Negative delay.");
        }
        if (period <= 0) {
            throw new IllegalArgumentException("Non-positive period.");
        }
        scheduleIntern(timerJob, System.currentTimeMillis() + delay, -period);
    }
    
    public void scheduleAtFixedRate(AbstractTimerJob timerJob, long period) {
        scheduleAtFixedRate(timerJob, 0, period);
    }
    
    public void scheduleAtFixedRate(AbstractTimerJob timerJob, long delay, long period) {
        if (delay < 0) {
            throw new IllegalArgumentException("Negative delay.");
        }
        if (period <= 0) {
            throw new IllegalArgumentException("Non-positive period.");
        }
        scheduleIntern(timerJob, System.currentTimeMillis() + delay, period);
    }
    
    private void scheduleIntern(AbstractTimerJob timerJob, long time, long period) {
        if (time < 0) {
            throw new IllegalArgumentException("Illegal execution time.");
        }
        //TODO #Timer.java what is it doing with the period?
        synchronized (timerJobs) {
            //thread.newTasksMayBeScheduled //FIXME Check if this manager has been cancelled
            synchronized (timerJob.lock) {
                if (timerJob.getState() != TimerJobState.CREATED) {
                    throw new IllegalStateException(timerJob.getClass().getSimpleName() + " already scheduled or cancelled");
                }
                timerJob.setNextExecutionTime(time);
                timerJob.setPeriod(period);
                timerJob.setState(TimerJobState.SCHEDULED);
            }
            timerJobs.add(timerJob);
            if (timerJobs.getMin() == timerJob) {
                timerJobs.notify();
            }
        }
    }
    
    public void cancel() {
        synchronized (timerJobs) {
            //thread.newTasksMayBeScheduled = false; //FIXME
            timerJobs.clear();
            timerJobs.notify(); //TODO Hmm? Why?
        }
    }
    
    public int purge() {
        int result = 0;
        synchronized (timerJobs) {
            for (int i = timerJobs.size(); i > 0; i--) {
                if (timerJobs.get(i).getState() == TimerJobState.CANCELLED) {
                    timerJobs.remove(i); //TODO quickRemove?
                    result++;
                }
            }
            if (result != 0) {
                //timerJobs.heapify(); //FIXME
            }
        }
        return result;
    }
    
    public boolean isStarted() {
        return thread != null && thread.isAlive();
    }
    
    public boolean isStopRequested() {
        return stopRequested.get();
    }
    
    public TimerJobManager setStopRequested(boolean stopRequested) {
        this.stopRequested.set(stopRequested);
        return this;
    }
    
    public long getUpdatePeriodMillis() {
        return updatePeriodMillis;
    }
    
    public TimerJobManager setUpdatePeriodMillis(long updatePeriodMillis) {
        this.updatePeriodMillis = updatePeriodMillis;
        return this;
    }
    
    @Override
    public boolean start() throws Exception {
        if (isStarted()) {
            return false;
        }
        setStopRequested(false);
        createThread();
        thread.start();
        return true;
    }
    
    private void createThread() {
        thread = Standard.toughThread(() -> {
            while (!isStopRequested()) {
                runIntern();
                //waitIntern(); //Waiting happens in the runIntern method
            }
        });
    }
    
    private void runIntern() throws Exception {
        AbstractTimerJob timerJob;
        boolean taskFired = false;
        synchronized (timerJobs) {
            if (timerJobs.isEmpty()) {
                return;
            }
            long currentTime = -1;
            long executionTime = -1;
            timerJob = timerJobs.getMin();
            synchronized (timerJob.lock) {
                if (timerJob.getState() == TimerJobState.CANCELLED) {
                    timerJobs.removeMin();
                    return;
                }
                currentTime = System.currentTimeMillis();
                executionTime = timerJob.getNextExecutionTime();
                if (taskFired = (executionTime <= currentTime)) {
                    timerJobs.removeMin();
                    if (timerJob.getPeriod() == 0) {
                        timerJob.setState(TimerJobState.EXECUTED);
                    } else {
                        timerJob.setNextExecutionTime(timerJob.getPeriod() < 0 ? currentTime - timerJob.getPeriod() : executionTime + timerJob.getPeriod());
                        timerJobs.add(timerJob);
                    }
                }
            }
            if (!taskFired) {
                timerJobs.wait(executionTime - currentTime);
            }
        }
        if (taskFired) {
            //timerJob.runWithoutException(); //TODO
            Standard.async(timerJob);
        }
    }
    
    private void waitIntern() throws Exception {
        Thread.sleep(updatePeriodMillis);
    }
    
    @Override
    public boolean stop() throws Exception {
        if (!isStarted()) {
            return false;
        }
        setStopRequested(true);
        stopIntern();
        return true;
    }
    
    private void stopIntern() throws Exception {
        synchronized (timerJobs) {
            timerJobs.notify();
        }
        if (thread != null) {
            //thread.interrupt(); //TODO ?
            thread = null;
        }
    }
    
    @Override
    public String toString() {
        return "TimerJobManager{" + "timerJobs=" + timerJobs + ", stopRequested=" + stopRequested + ", updatePeriodMillis=" + updatePeriodMillis + ", thread=" + thread + '}';
    }
    
}
