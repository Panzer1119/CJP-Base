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

import de.codemakers.base.util.tough.ToughRunnable;

public abstract class AbstractTimerJob implements Comparable<AbstractTimerJob>, ToughRunnable {
    
    final Object lock = new Object();
    
    protected final long created;
    protected TimerJobState state = TimerJobState.CREATED;
    /**
     * The next time in millis this job gets executed
     */
    protected long nextExecutionTime = -1;
    /**
     * Period in millis for repeating tasks
     * <br>
     * + = Fixed-rate execution
     * <br>
     * 0 = Non-repeating job
     * <br>
     * - = Fixed-delay execution
     */
    protected long period = 0;
    
    public AbstractTimerJob() {
        this(System.currentTimeMillis());
    }
    
    public AbstractTimerJob(long created) {
        this.created = created;
    }
    
    public boolean cancel() {
        synchronized (lock) {
            boolean result = (state == TimerJobState.SCHEDULED);
            state = TimerJobState.CANCELLED;
            return result;
        }
    }
    
    public long getCreated() {
        return created;
    }
    
    public TimerJobState getState() {
        return state;
    }
    
    public AbstractTimerJob setState(TimerJobState state) {
        this.state = state;
        return this;
    }
    
    public long getNextExecutionTime() {
        return nextExecutionTime;
    }
    
    public AbstractTimerJob setNextExecutionTime(long nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
        return this;
    }
    
    public long getPeriod() {
        return period;
    }
    
    public AbstractTimerJob setPeriod(long period) {
        this.period = period;
        return this;
    }
    
    @Override
    public int compareTo(AbstractTimerJob abstractTimerJob) {
        return Long.compare(nextExecutionTime, abstractTimerJob.nextExecutionTime);
    }
    
    @Override
    public String toString() {
        return "AbstractTimerJob{" + "lock=" + lock + ", created=" + created + ", state=" + state + ", nextExecutionTime=" + nextExecutionTime + ", period=" + period + '}';
    }
    
}
