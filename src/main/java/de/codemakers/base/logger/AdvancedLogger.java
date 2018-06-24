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

package de.codemakers.base.logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AdvancedLogger implements ILogger {
    
    private ZoneId zoneId = ZoneId.systemDefault();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private String LOG_FORMAT = "[%2$s]%3$s%4$s: %1$s";
    private String STACK_TRACE_FORMAT = "%1$s.%2$s(%3$s:%4$s)";
    
    public void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        log(String.format(LOG_FORMAT, object, dateTimeFormatter.format(ZonedDateTime.ofInstant(timestamp, zoneId)), formatThread(thread), formatStackTraceElement(stackTraceElement)));
    }
    
    public String formatThread(Thread thread) {
        if (thread == null) {
            return "";
        }
        return "[" + thread.getName() + "]";
    }
    
    public String formatStackTraceElement(StackTraceElement stackTraceElement) {
        if (stackTraceElement == null) {
            return "";
        }
        return String.format("[" + STACK_TRACE_FORMAT + "]", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber());
    }

}
