/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.base.logging;

import org.apache.logging.log4j.Level;

import java.time.Instant;
import java.util.Objects;

@Deprecated
public class LeveledLogEntry extends LogEntry {
    
    protected final Level level;
    
    public LeveledLogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, Throwable throwable, Level level) {
        super(object, timestamp, thread, stackTraceElement, throwable);
        this.level = level;
    }
    
    public Level getLevel() {
        return level;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final LeveledLogEntry leveledLogEntry)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        return level == leveledLogEntry.level;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), level);
    }
    
    @Override
    public String toString() {
        return "LeveledLogEntry{" + "level=" + level + ", object=" + object + ", timestamp=" + timestamp + ", thread=" + thread + ", stackTraceElement=" + stackTraceElement + ", throwable=" + throwable + '}';
    }
    
}
