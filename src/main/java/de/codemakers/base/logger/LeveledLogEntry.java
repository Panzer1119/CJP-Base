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

package de.codemakers.base.logger;

import java.time.Instant;
import java.util.Objects;

public class LeveledLogEntry extends LogEntry {
    
    protected final LogLevel logLevel; //LogLevel
    
    public LeveledLogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        this(object, timestamp, thread, stackTraceElement, LogLevel.INFO);
    }
    
    public LeveledLogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        super(object, timestamp, thread, stackTraceElement);
        this.logLevel = logLevel;
    }
    
    public LeveledLogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, Throwable throwable) {
        this(object, timestamp, thread, stackTraceElement, throwable, LogLevel.INFO);
    }
    
    public LeveledLogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, Throwable throwable, LogLevel logLevel) {
        super(object, timestamp, thread, stackTraceElement, throwable);
        this.logLevel = logLevel;
    }
    
    public LeveledLogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, Throwable throwable, boolean bad) {
        this(object, timestamp, thread, stackTraceElement, throwable, bad, LogLevel.INFO);
    }
    
    public LeveledLogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, Throwable throwable, boolean bad, LogLevel logLevel) {
        super(object, timestamp, thread, stackTraceElement, throwable, bad);
        this.logLevel = logLevel;
    }
    
    public LogLevel getLogLevel() {
        return logLevel;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !(object instanceof LeveledLogEntry)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        final LeveledLogEntry leveledLogEntry = (LeveledLogEntry) object;
        return logLevel == leveledLogEntry.logLevel;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), logLevel);
    }
    
    @Override
    public String toString() {
        return "LeveledLogEntry{" + "logLevel=" + logLevel + ", object=" + object + ", timestamp=" + timestamp + ", thread=" + thread + ", stackTraceElement=" + stackTraceElement + ", throwable=" + throwable + ", bad=" + bad + '}';
    }
    
}
