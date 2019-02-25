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
import java.time.ZonedDateTime;

public abstract class AdvancedLeveledLogger extends AdvancedLogger {
    
    /**
     * Value = [%2$s]%3$s%4$s%5$s: %1$s
     */
    //public static final String DEFAULT_LEVELED_LOG_FORMAT = "[%2$s]%3$s%4$s%5$s: %1$s"; //FIXME TODO 1242545435
    //public static final String DEFAULT_LEVELED_LOG_FORMAT = "[timestamp][thread][location][loglevel]: [object]";
    //public static final String DEFAULT_LEVELED_LOG_FORMAT = "${timestamp}${thread}${location}${loglevel}: ${object}";
    public static final String DEFAULT_LEVELED_LOG_FORMAT = "[${timestamp}][${thread}][${location}][${loglevel}]: ${object}";
    
    protected LogLevel minimumLogLevel = LogLevel.INFO;
    
    public AdvancedLeveledLogger() {
        //this.logFormat = DEFAULT_LEVELED_LOG_FORMAT; //FIXME TODO 1242545435
        logFormatter.setFormatString(DEFAULT_LEVELED_LOG_FORMAT);
    }
    
    @Override
    public void log(Object object) {
        log(object, Instant.now(), Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()), LogLevel.INFO);
    }
    
    @Override
    public void log(Object object, Instant timestamp) {
        log(object, timestamp, Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()), LogLevel.INFO);
    }
    
    @Override
    public void log(Object object, Instant timestamp, Thread thread) {
        log(object, timestamp, thread, cutStackTrace(new Exception().getStackTrace()), LogLevel.INFO);
    }
    
    @Override
    public void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        log(object, timestamp, thread, stackTraceElement, LogLevel.INFO);
    }
    
    public void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        if (minimumLogLevel.isThisLevelMoreImportant(logLevel)) {
            return;
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        //logFinal(String.format(logFormat, object, dateTimeFormatter.format(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId())), formatThread(thread), formatStackTraceElement(stackTraceElement), formatLogLevel(logLevel)));
        logFormatter.reset();
        logFormatter.setValue("timestamp", dateTimeFormatter.format(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId())));
        logFormatter.setValue("thread", formatThread(thread));
        logFormatter.setValue("location", formatStackTraceElement(stackTraceElement));
        logFormatter.setValue("loglevel", formatLogLevel(logLevel));
        logFormatter.setValue("object", object);
        logFinal(logFormatter);
        //FIXME TODO 1242545435
    }
    
    @Override
    public void logError(Object object, Throwable throwable) {
        logError(object, throwable, Instant.now(), Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()), LogLevel.ERROR);
    }
    
    @Override
    public void logError(Object object, Throwable throwable, Instant timestamp) {
        logError(object, throwable, timestamp, Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()), LogLevel.ERROR);
    }
    
    @Override
    public void logError(Object object, Throwable throwable, Instant timestamp, Thread thread) {
        logError(object, throwable, timestamp, thread, cutStackTrace(new Exception().getStackTrace()), LogLevel.ERROR);
    }
    
    @Override
    public void logError(Object object, Throwable throwable, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        logError(object, throwable, timestamp, thread, stackTraceElement, LogLevel.ERROR);
    }
    
    public void logError(Object object, Throwable throwable, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        if (minimumLogLevel.isThisLevelMoreImportant(logLevel)) {
            return;
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        //logErrorFinal(String.format(logFormat, object, dateTimeFormatter.format(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId())), formatThread(thread), formatStackTraceElement(stackTraceElement), formatLogLevel(logLevel)), throwable);
        logFormatter.reset();
        logFormatter.setValue("timestamp", dateTimeFormatter.format(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId())));
        logFormatter.setValue("thread", formatThread(thread));
        logFormatter.setValue("location", formatStackTraceElement(stackTraceElement));
        logFormatter.setValue("loglevel", formatLogLevel(logLevel));
        logFormatter.setValue("object", object);
        logErrorFinal(logFormatter, throwable);
        //FIXME TODO 1242545435
    }
    
    protected String formatLogLevel(LogLevel logLevel) {
        if (logLevel == null) {
            return "";
        }
        return "[" + logLevel + "]";
    }
    
    public LogLevel getMinimumLogLevel() {
        return minimumLogLevel;
    }
    
    public AdvancedLeveledLogger setMinimumLogLevel(LogLevel minimumLogLevel) {
        if (minimumLogLevel == null) {
            minimumLogLevel = LogLevel.INFO;
        }
        this.minimumLogLevel = minimumLogLevel;
        return this;
    }
    
}
