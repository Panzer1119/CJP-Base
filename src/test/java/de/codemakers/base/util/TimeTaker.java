/*
 *     Copyright 2018 Paul Hagedorn (Panzer1119)
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

package de.codemakers.base.util;

import java.util.ArrayList;
import java.util.List;

public class TimeTaker {
    
    private boolean started = false;
    private boolean paused = false;
    private long start = 0;
    private long pause = 0;
    private long stop = 0;
    private final List<Long> pauses = new ArrayList<>(); //FIXME Maybe just make a global variable, which gets added to every time this is unpaused
    
    public TimeTaker() {
        this(-1);
    }
    
    public TimeTaker(long start) {
        this.start = start;
    }
    
    public long getStart() {
        return start;
    }
    
    public long getPause() {
        return pause;
    }
    
    public long getStop() {
        return stop;
    }
    
    public boolean reset() {
        paused = false;
        started = false;
        start = 0;
        pause = 0;
        stop = 0;
        pauses.clear();
        return true;
    }
    
    public long start() {
        if (started) {
            return start;
        }
        /*if (paused) {
            unpause();
        }*/
        started = true;
        start = System.currentTimeMillis();
        return start;
    }
    
    public long pause() {
        if (!started) {
            return -1;
        }
        if (paused) {
            return pause;
        }
        pause = System.currentTimeMillis();
        paused = true;
        return pause;
    }
    
    public boolean unpause() {
        if (!started || !paused) {
            return false;
        }
        final long now = System.currentTimeMillis();
        pauses.add(now - pause);
        paused = false;
        return true;
    }
    
    public long stop() {
        if (!started) {
            return stop;
        }
        if (paused) {
            unpause();
        }
        stop = System.currentTimeMillis();
        started = false;
        return stop;
    }
    
    public long getDuration() {
        final long now = System.currentTimeMillis();
        final long pause_all = getPauses();
        System.out.println("pause_all=" + pause_all);
        if (!started) {
            return (stop - start) - pause_all;
        } else {
            if (paused) {
                return (now - start) - pause_all - (now - pause);
            } else {
                return (now - start) - pause_all;
            }
        }
    }
    
    protected long getPauses() {
        return pauses.stream().reduce(0L, (pause_all_, pause_) -> pause_all_ + pause_);
    }
    
}
