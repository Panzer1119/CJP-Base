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

package de.codemakers.base.logger;

import java.time.Instant;
import java.util.Objects;

public class LogEntry {
    
    private final Object logEntry;
    private final Instant timestamp;
    private final Thread thread;
    private final StackTraceElement stackTraceElement;
    private final Throwable throwable;
    private final boolean isBad;
    private final LogLevel logLevel;
    
    public LogEntry(Object logEntry, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, Throwable throwable, boolean isBad, LogLevel logLevel) {
        this.logEntry = logEntry;
        this.timestamp = timestamp;
        this.thread = thread;
        this.stackTraceElement = stackTraceElement;
        this.throwable = throwable;
        this.isBad = isBad;
        this.logLevel = logLevel;
    }
    
    public final Object getLogEntry() {
        return logEntry;
    }
    
    public final Instant getTimestamp() {
        return timestamp;
    }
    
    public final Thread getThread() {
        return thread;
    }
    
    public final StackTraceElement getStackTraceElement() {
        return stackTraceElement;
    }
    
    public final Throwable getThrowable() {
        return throwable;
    }
    
    public final boolean isBad() {
        return isBad;
    }
    
    public final LogLevel getLogLevel() {
        return logLevel;
    }
    
    public final boolean hasError() {
        return throwable != null;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "logEntry=" + logEntry + ", timestamp=" + timestamp + ", thread=" + thread + ", stackTraceElement=" + stackTraceElement + ", throwable=" + throwable + ", isBad=" + isBad + ", logLevel=" + logLevel + '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LogEntry logEntry1 = (LogEntry) o;
        return isBad == logEntry1.isBad && Objects.equals(logEntry, logEntry1.logEntry) && Objects.equals(timestamp, logEntry1.timestamp) && Objects.equals(thread, logEntry1.thread) && Objects.equals(stackTraceElement, logEntry1.stackTraceElement) && Objects.equals(throwable, logEntry1.throwable) && logLevel == logEntry1.logLevel;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(logEntry, timestamp, thread, stackTraceElement, throwable, isBad, logLevel);
    }
    
}
