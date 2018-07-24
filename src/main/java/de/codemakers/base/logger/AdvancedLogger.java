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

/**
 * Abstract AdvancedLogger Class
 */
public abstract class AdvancedLogger implements ILogger {
    
    /**
     * Just an empty byte array
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    /**
     * Value = {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME}
     */
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    /**
     * Value = [%2$s]%3$s%4$s: %1$s
     */
    public static final String DEFAULT_LOG_FORMAT = "[%2$s]%3$s%4$s: %1$s";
    /**
     * Value = %1$s.%2$s(%3$s:%4$s)
     */
    public static final String DEFAULT_STACK_TRACE_ELEMENT_FORMAT = "%1$s.%2$s(%3$s:%4$s)";
    
    protected ZoneId zoneId = ZoneId.systemDefault();
    protected DateTimeFormatter dateTimeFormatter = DEFAULT_DATE_TIME_FORMATTER;
    protected String logFormat = DEFAULT_LOG_FORMAT;
    protected String stackTraceElementFormat = DEFAULT_STACK_TRACE_ELEMENT_FORMAT;
    
    /**
     * Logs an {@link java.lang.Object} with an default {@link java.time.Instant} derived from {@link Instant#now()}, {@link java.lang.Thread} derived from {@link Thread#currentThread()} and {@link java.lang.StackTraceElement} derived from {@link Exception#getStackTrace()}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     */
    public void log(Object object) {
        log(object, Instant.now(), Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()));
    }
    
    /**
     * Logs an {@link java.lang.Object} with an custom {@link java.time.Instant}, default {@link java.lang.Thread} derived from {@link Thread#currentThread()} and default {@link java.lang.StackTraceElement} derived from {@link Exception#getStackTrace()}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param timestamp Timestamp
     */
    public void log(Object object, Instant timestamp) {
        log(object, timestamp, Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()));
    }
    
    /**
     * Logs an {@link java.lang.Object} with an custom {@link java.time.Instant}, custom {@link java.lang.Thread} and default {@link java.lang.StackTraceElement} derived from {@link Exception#getStackTrace()}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param timestamp Timestamp
     * @param thread Thread
     */
    public void log(Object object, Instant timestamp, Thread thread) {
        log(object, timestamp, thread, cutStackTrace(new Exception().getStackTrace()));
    }
    
    /**
     * Logs an {@link java.lang.Object} with an custom {@link java.time.Instant}, {@link java.lang.Thread} and {@link java.lang.StackTraceElement}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param timestamp Timestamp
     * @param thread Thread
     * @param stackTraceElement StackTraceElement (used to determine the source of the {@link de.codemakers.base.logger.AdvancedLogger#log(Object, Instant, Thread, StackTraceElement)} call)
     */
    public void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        logFinal(String.format(logFormat, object, dateTimeFormatter.format(ZonedDateTime.ofInstant(timestamp, zoneId)), formatThread(thread), formatStackTraceElement(stackTraceElement)));
    }
    
    abstract void logFinal(Object object);
    
    String formatThread(Thread thread) {
        if (thread == null) {
            return "";
        }
        return "[" + thread.getName() + "]";
    }
    
    String formatStackTraceElement(StackTraceElement stackTraceElement) {
        if (stackTraceElement == null) {
            return "";
        }
        return String.format("[" + stackTraceElementFormat + "]", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber());
    }
    
    /**
     * Returns the {@link java.time.ZoneId} used for the {@link java.time.Instant}
     *
     * @return ZoneId
     */
    public final ZoneId getZoneId() {
        return zoneId;
    }
    
    /**
     * Sets the {@link java.time.ZoneId} used for the {@link java.time.Instant}
     *
     * @param zoneId {@link java.time.ZoneId}
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public final AdvancedLogger setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        return this;
    }
    
    /**
     * Returns the {@link java.time.format.DateTimeFormatter} used to format the {@link java.time.Instant} and {@link java.time.ZoneId}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_DATE_TIME_FORMATTER}
     *
     * @return {@link java.time.format.DateTimeFormatter}
     */
    public final DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
    
    /**
     * Sets the {@link java.time.format.DateTimeFormatter} used to format the {@link java.time.Instant} and {@link java.time.ZoneId}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_DATE_TIME_FORMATTER}
     *
     * @param dateTimeFormatter {@link java.time.format.DateTimeFormatter}
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public final AdvancedLogger setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the logged Message
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_LOG_FORMAT}
     *
     * @return Log Message Format
     */
    public final String getLogFormat() {
        return logFormat;
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the logged Message
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_LOG_FORMAT}
     *
     * @param logFormat Log Message Format
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public final AdvancedLogger setLogFormat(String logFormat) {
        this.logFormat = logFormat;
        return this;
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the {@link java.lang.StackTraceElement}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_STACK_TRACE_ELEMENT_FORMAT}
     *
     * @return {@link java.lang.StackTraceElement} Format
     */
    public final String getStackTraceElementFormat() {
        return stackTraceElementFormat;
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the {@link java.lang.StackTraceElement}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_STACK_TRACE_ELEMENT_FORMAT}
     *
     * @param stackTraceElementFormat {@link java.lang.StackTraceElement} Format
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public final AdvancedLogger setStackTraceElementFormat(String stackTraceElementFormat) {
        this.stackTraceElementFormat = stackTraceElementFormat;
        return this;
    }
    
    static final StackTraceElement cutStackTrace(StackTraceElement[] stackTraceElements) {
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
