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

import de.codemakers.base.util.interfaces.Formattable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Deprecated
public class LogEntry implements Formattable<Void>, Serializable {
    
    protected final Object object; //Object
    protected final Instant timestamp; //Timestamp
    protected final Thread thread; //Thread
    protected final StackTraceElement stackTraceElement; //Source
    protected final Throwable throwable;
    
    public LogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        this(object, timestamp, thread, stackTraceElement, null);
    }
    
    public LogEntry(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, Throwable throwable) {
        this.object = object;
        this.timestamp = timestamp;
        this.thread = thread;
        this.stackTraceElement = stackTraceElement;
        this.throwable = throwable;
    }
    
    public Object getObject() {
        return object;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public Thread getThread() {
        return thread;
    }
    
    public StackTraceElement getStackTraceElement() {
        return stackTraceElement;
    }
    
    public Throwable getThrowable() {
        return throwable;
    }
    
    public boolean hasError() {
        return throwable != null;
    }
    
    public boolean hasNoError() {
        return throwable == null;
    }
    
    @Override
    public String format(Void format) throws Exception {
        return Objects.toString(object);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(object, timestamp, thread, stackTraceElement, throwable);
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final LogEntry logEntry)) {
            return false;
        }
        return Objects.equals(this.object, logEntry.object) && Objects.equals(timestamp, logEntry.timestamp) && Objects.equals(thread, logEntry.thread) && Objects
                .equals(stackTraceElement, logEntry.stackTraceElement) && Objects.equals(throwable, logEntry.throwable);
    }
    
    @Override
    public String toString() {
        return "LogEntry{" + "object=" + object + ", timestamp=" + timestamp + ", thread=" + thread + ", stackTraceElement=" + stackTraceElement + ", throwable=" + throwable + '}';
    }
    
}
