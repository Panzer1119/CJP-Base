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

import de.codemakers.base.CJP;
import de.codemakers.base.util.ArrayUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AdvancedLogger implements ILogger {
    
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    
    private ZoneId zoneId = ZoneId.systemDefault();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private String logFormat = "[%2$s]%3$s%4$s: %1$s";
    private String stackTraceFormat = "%1$s.%2$s(%3$s:%4$s)";
    
    public void log(Object object) {
        log(object, Instant.now(), Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()));
    }
    
    public void log(Object object, Instant timestamp) {
        log(object, timestamp, Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()));
    }
    
    public void log(Object object, Instant timestamp, Thread thread) {
        log(object, timestamp, thread, cutStackTrace(new Exception().getStackTrace()));
    }
    
    public void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        logFinal(String.format(logFormat, object, dateTimeFormatter.format(ZonedDateTime.ofInstant(timestamp, zoneId)), formatThread(thread), formatStackTraceElement(stackTraceElement)));
    }
    
    protected abstract void logFinal(Object object);
    
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
        return String.format("[" + stackTraceFormat + "]", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber());
    }
    
    public final ZoneId getZoneId() {
        return zoneId;
    }
    
    public final AdvancedLogger setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        return this;
    }
    
    public final DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
    
    public final AdvancedLogger setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }
    
    public final String getLogFormat() {
        return logFormat;
    }
    
    public final AdvancedLogger setLogFormat(String logFormat) {
        this.logFormat = logFormat;
        return this;
    }
    
    public final String getStackTraceFormat() {
        return stackTraceFormat;
    }
    
    public final AdvancedLogger setStackTraceFormat(String stackTraceFormat) {
        this.stackTraceFormat = stackTraceFormat;
        return this;
    }
    
    public static final StackTraceElement cutStackTrace(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements == null || stackTraceElements.length == 0) {
            return null;
        }
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (ArrayUtil.arrayContains(CJP.CJP_LOGGER_CLASS_NAMES, stackTraceElement.getClassName())) {
                continue;
            }
            return stackTraceElement;
        }
        return null;
    }
    
}
