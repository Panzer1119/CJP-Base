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

import de.codemakers.base.CJP;
import de.codemakers.base.util.ArrayUtil;
import de.codemakers.base.util.StringUtil;
import de.codemakers.base.util.TimeUtil;
import de.codemakers.base.util.tough.ToughBiFunction;
import org.apache.commons.text.StringSubstitutor;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

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
    public static final DateTimeFormatter DATE_TIME_FORMATTER_ISO_OFFSET = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    /**
     * Value = {@link de.codemakers.base.util.TimeUtil#ISO_OFFSET_DATE_TIME_FIXED_LENGTH}
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER_DEFAULT = TimeUtil.ISO_OFFSET_DATE_TIME_FIXED_LENGTH;
    /**
     * Value = "{@link Logger#LOG_FORMAT_VAR_TIMESTAMP}{@link Logger#LOG_FORMAT_VAR_THREAD}{@link Logger#LOG_FORMAT_VAR_SOURCE}: {@link Logger#LOG_FORMAT_VAR_OBJECT}"
     */
    public static final String DEFAULT_LOG_FORMAT = Logger.LOG_FORMAT_VAR_TIMESTAMP + Logger.LOG_FORMAT_VAR_THREAD + Logger.LOG_FORMAT_VAR_SOURCE + ": " + Logger.LOG_FORMAT_VAR_OBJECT;
    /**
     * Value = "{@link Logger#SOURCE_FORMAT_VAR_CLASS}.{@link Logger#SOURCE_FORMAT_VAR_METHOD}({@link Logger#SOURCE_FORMAT_VAR_FILE}:{@link Logger#SOURCE_FORMAT_VAR_LINE})"
     */
    public static final String DEFAULT_SOURCE_FORMAT = Logger.SOURCE_FORMAT_VAR_CLASS + "." + Logger.SOURCE_FORMAT_VAR_METHOD + "(" + Logger.SOURCE_FORMAT_VAR_FILE + ":" + Logger.SOURCE_FORMAT_VAR_LINE + ")";
    //TODO Add javadoc
    public static final ToughBiFunction<ZonedDateTime, AdvancedLogger, String> DEFAULT_TIMESTAMP_FORMATTER = (timestamp, advancedLogger) -> timestamp == null ? "" : "[" + timestamp.format(advancedLogger.dateTimeFormatter) + "]";
    public static final ToughBiFunction<Thread, AdvancedLogger, String> DEFAULT_THREAD_FORMATTER = (thread, advancedLogger) -> thread == null ? "" : "[" + thread.getName() + "]";
    public static final ToughBiFunction<StackTraceElement, AdvancedLogger, String> DEFAULT_SOURCE_FORMATTER = (stackTraceElement, advancedLogger) -> stackTraceElement == null ? "" : "[" + StringSubstitutor.replace(advancedLogger.sourceFormat, createValueMap(stackTraceElement)) + "]";
    public static final ToughBiFunction<Object, AdvancedLogger, String> DEFAULT_OBJECT_FORMATTER = (object, advancedLogger) -> "" + object;
    
    protected TimeZone timeZone = TimeZone.getDefault();
    protected DateTimeFormatter dateTimeFormatter = DATE_TIME_FORMATTER_DEFAULT;
    protected String logFormat = DEFAULT_LOG_FORMAT;
    protected String sourceFormat = DEFAULT_SOURCE_FORMAT;
    protected ToughBiFunction<Throwable, String, Boolean> errorHandler = null;
    protected ToughBiFunction<ZonedDateTime, AdvancedLogger, String> timestampFormatter = DEFAULT_TIMESTAMP_FORMATTER;
    protected ToughBiFunction<Thread, AdvancedLogger, String> threadFormatter = DEFAULT_THREAD_FORMATTER;
    protected ToughBiFunction<StackTraceElement, AdvancedLogger, String> sourceFormatter = DEFAULT_SOURCE_FORMATTER;
    protected ToughBiFunction<Object, AdvancedLogger, String> objectFormatter = DEFAULT_OBJECT_FORMATTER;
    
    protected static final StackTraceElement cutStackTrace(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements == null || stackTraceElements.length == 0) {
            return null;
        }
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (ArrayUtil.arrayContains(CJP.getLoggerClassNames(), stackTraceElement.getClassName())) {
                continue;
            }
            return stackTraceElement;
        }
        return null;
    }
    
    protected abstract void logFinal(LogEntry logEntry);
    
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
        final LogEntry logEntry = new LogEntry(object, timestamp, thread, stackTraceElement);
        //logFinal(formatLogMessage(createValueMap(object, timestamp, thread, stackTraceElement))); //FIXME Remove this old line
        logFinal(logEntry);
        //TODO Maybe add method for writing LogEntries to file?
    }
    
    @Override
    public void log(Object object, Object... arguments) {
        if (arguments != null && arguments.length > 0) {
            log(String.format("" + object, arguments)); //FIXME Is this causing StackOverflows?
        } else {
            log(object); //FIXME Is this causing StackOverflows?
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} with an default {@link java.time.Instant} derived from {@link Instant#now()}, {@link java.lang.Thread} derived from {@link Thread#currentThread()} and {@link java.lang.StackTraceElement} derived from {@link Exception#getStackTrace()}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable {@link java.lang.Throwable} to get logged
     */
    public void logError(Object object, Throwable throwable) {
        logError(object, throwable, Instant.now(), Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()));
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} with an custom {@link java.time.Instant}, default {@link java.lang.Thread} derived from {@link Thread#currentThread()} and default {@link java.lang.StackTraceElement} derived from {@link Exception#getStackTrace()}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable {@link java.lang.Throwable} to get logged
     * @param timestamp Timestamp
     */
    public void logError(Object object, Throwable throwable, Instant timestamp) {
        logError(object, throwable, timestamp, Thread.currentThread(), cutStackTrace(new Exception().getStackTrace()));
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} with an custom {@link java.time.Instant}, custom {@link java.lang.Thread} and default {@link java.lang.StackTraceElement} derived from {@link Exception#getStackTrace()}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable {@link java.lang.Throwable} to get logged
     * @param timestamp Timestamp
     * @param thread Thread
     */
    public void logError(Object object, Throwable throwable, Instant timestamp, Thread thread) {
        logError(object, throwable, timestamp, thread, cutStackTrace(new Exception().getStackTrace()));
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} with an custom {@link java.time.Instant}, {@link java.lang.Thread} and {@link java.lang.StackTraceElement}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable {@link java.lang.Throwable} to get logged
     * @param timestamp Timestamp
     * @param thread Thread
     * @param stackTraceElement StackTraceElement (used to determine the source of the {@link de.codemakers.base.logger.AdvancedLogger#log(Object, Instant, Thread, StackTraceElement)} call)
     */
    public void logError(Object object, Throwable throwable, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        final LogEntry logEntry = new LogEntry(object, timestamp, thread, stackTraceElement, throwable, true);
        //logErrorFinal(formatLogMessage(createValueMap(object, timestamp, thread, stackTraceElement)), throwable); //FIXME Remove this old line
        logFinal(logEntry);
        //TODO Maybe add method for writing LogEntries to file?
    }
    
    @Override
    public void logError(Object object, Throwable throwable, Object... arguments) {
        if (arguments != null && arguments.length > 0) {
            logError(String.format(object + "", arguments), throwable); //FIXME Is this causing StackOverflows?
        } else {
            logError(object, throwable); //FIXME Is this causing StackOverflows?
        }
    }
    
    //FIXME (Re)Move this to (Leveled)LogEntry!!!
    @Deprecated
    protected Object formatLogMessage(Map<String, Object> valueMap) {
        try {
            return StringSubstitutor.replace(logFormat, valueMap);
        } catch (Exception ex) {
            return valueMap.get(Logger.LOG_FORMAT_OBJECT);
        }
    }
    
    protected Map<String, Object> createValueMap(LogEntry logEntry) {
        return createValueMap(logEntry.getObject(), logEntry.getTimestamp(), logEntry.getThread(), logEntry.getStackTraceElement());
    }
    
    protected Map<String, Object> createValueMap(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        final Map<String, Object> map = new HashMap<>();
        map.put(Logger.LOG_FORMAT_TIMESTAMP, formatTimestamp(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId())));
        map.put(Logger.LOG_FORMAT_THREAD, formatThread(thread));
        map.put(Logger.LOG_FORMAT_SOURCE, formatStackTraceElement(stackTraceElement));
        map.put(Logger.LOG_FORMAT_OBJECT, formatObject(object));
        return map;
    }
    
    protected static Map<String, Object> createValueMap(StackTraceElement stackTraceElement) {
        final Map<String, Object> map = new HashMap<>();
        map.put(Logger.SOURCE_FORMAT_CLASS, stackTraceElement.getClassName());
        map.put(Logger.SOURCE_FORMAT_METHOD, stackTraceElement.getMethodName());
        map.put(Logger.SOURCE_FORMAT_FILE, stackTraceElement.getFileName());
        map.put(Logger.SOURCE_FORMAT_LINE, stackTraceElement.getLineNumber());
        return map;
    }
    
    protected String formatTimestamp(ZonedDateTime zonedDateTime) {
        return StringUtil.escapeStringSubstitutorVariableCalls(timestampFormatter.applyWithoutException(zonedDateTime, this));
    }
    
    protected String formatThread(Thread thread) {
        return StringUtil.escapeStringSubstitutorVariableCalls(threadFormatter.applyWithoutException(thread, this));
    }
    
    protected String formatStackTraceElement(StackTraceElement stackTraceElement) {
        return StringUtil.escapeStringSubstitutorVariableCalls(sourceFormatter.applyWithoutException(stackTraceElement, this));
    }
    
    protected String formatObject(Object object) {
        return StringUtil.escapeStringSubstitutorVariableCalls(objectFormatter.applyWithoutException(object, this));
    }
    
    /**
     * Returns the {@link java.util.TimeZone} used for the {@link java.time.Instant}
     *
     * @return TimeZone
     */
    public final TimeZone getTimeZone() {
        return timeZone;
    }
    
    /**
     * Sets the {@link java.util.TimeZone} used for the {@link java.time.Instant}
     *
     * @param timeZone {@link java.util.TimeZone}
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public final AdvancedLogger setTimeZone(TimeZone timeZone) {
        this.timeZone = Objects.requireNonNull(timeZone, "timeZone");
        return this;
    }
    
    /**
     * Returns the {@link java.time.format.DateTimeFormatter} used to format the {@link java.time.Instant} and {@link java.time.ZoneId}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DATE_TIME_FORMATTER_DEFAULT}
     *
     * @return {@link java.time.format.DateTimeFormatter}
     */
    public final DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
    
    /**
     * Sets the {@link java.time.format.DateTimeFormatter} used to format the {@link java.time.Instant} and {@link java.time.ZoneId}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DATE_TIME_FORMATTER_DEFAULT}
     *
     * @param dateTimeFormatter {@link java.time.format.DateTimeFormatter}
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public final AdvancedLogger setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = Objects.requireNonNull(dateTimeFormatter, "dateTimeFormatter");
        return this;
    }
    
    /**
     * Gets the {@link java.lang.String} used to format the log Message
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_LOG_FORMAT}
     *
     * @return Log format
     */
    public String getLogFormat() {
        return logFormat;
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the log Message
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_LOG_FORMAT}
     *
     * @param logFormat Log format
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public AdvancedLogger setLogFormat(String logFormat) {
        this.logFormat = Objects.requireNonNull(logFormat, "logFormat");
        return this;
    }
    
    /**
     * Creates a {@link de.codemakers.base.logger.LogFormatBuilder} which is linked to this {@link de.codemakers.base.logger.AdvancedLogger}
     * <br>
     * The {@link de.codemakers.base.logger.LogFormatBuilder} is empty when created, but when you call {@link de.codemakers.base.logger.LogFormatBuilder#finish()} (or {@link de.codemakers.base.logger.LogFormatBuilder#finishWithoutException()})
     * the {@link de.codemakers.base.logger.LogFormatBuilder} will set the new created format as the new log format in this {@link de.codemakers.base.logger.AdvancedLogger}
     *
     * @return {@link de.codemakers.base.logger.LogFormatBuilder} linked to this {@link de.codemakers.base.logger.AdvancedLogger}
     */
    public LogFormatBuilder createLogFormatBuilder() {
        return new LogFormatBuilder() {
            @Override
            public String finish() throws Exception {
                final String temp = toFormat();
                setLogFormat(temp);
                return temp;
            }
        };
    }
    
    /**
     * Gets the {@link java.lang.String} used to format the {@link java.lang.StackTraceElement}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_SOURCE_FORMAT}
     *
     * @return Source format
     */
    public String getSourceFormat() {
        return sourceFormat;
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the {@link java.lang.StackTraceElement}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_SOURCE_FORMAT}
     *
     * @param sourceFormat Source format
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public AdvancedLogger setSourceFormat(String sourceFormat) {
        this.sourceFormat = Objects.requireNonNull(sourceFormat, "sourceFormat");
        return this;
    }
    
    /**
     * Creates a {@link de.codemakers.base.logger.SourceFormatBuilder} which is linked to this {@link de.codemakers.base.logger.AdvancedLogger}
     * <br>
     * The {@link de.codemakers.base.logger.SourceFormatBuilder} is empty when created, but when you call {@link de.codemakers.base.logger.SourceFormatBuilder#finish()} (or {@link de.codemakers.base.logger.SourceFormatBuilder#finishWithoutException()})
     * the {@link de.codemakers.base.logger.SourceFormatBuilder} will set the new created format as the new source format in this {@link de.codemakers.base.logger.AdvancedLogger}
     *
     * @return {@link de.codemakers.base.logger.SourceFormatBuilder} linked to this {@link de.codemakers.base.logger.AdvancedLogger}
     */
    public SourceFormatBuilder createSourceFormatBuilder() {
        return new SourceFormatBuilder() {
            @Override
            public String finish() throws Exception {
                final String temp = toFormat();
                setSourceFormat(temp);
                return temp;
            }
        };
    }
    
    /**
     * Gets the Error handler
     *
     * @return {@link de.codemakers.base.util.tough.ToughBiFunction} Error handler
     */
    public ToughBiFunction<Throwable, String, Boolean> getErrorHandler() {
        return errorHandler;
    }
    
    /**
     * Sets the Error handler
     *
     * @param errorHandler {@link de.codemakers.base.util.tough.ToughBiFunction} Error handler
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public AdvancedLogger setErrorHandler(ToughBiFunction<Throwable, String, Boolean> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }
    
    //TODO Add javadoc
    
    public ToughBiFunction<ZonedDateTime, AdvancedLogger, String> getTimestampFormatter() {
        return timestampFormatter;
    }
    
    public AdvancedLogger setTimestampFormatter(ToughBiFunction<ZonedDateTime, AdvancedLogger, String> timestampFormatter) {
        this.timestampFormatter = Objects.requireNonNull(timestampFormatter, "timestampFormatter");
        return this;
    }
    
    public ToughBiFunction<Thread, AdvancedLogger, String> getThreadFormatter() {
        return threadFormatter;
    }
    
    public AdvancedLogger setThreadFormatter(ToughBiFunction<Thread, AdvancedLogger, String> threadFormatter) {
        this.threadFormatter = Objects.requireNonNull(threadFormatter, "threadFormatter");
        return this;
    }
    
    public ToughBiFunction<StackTraceElement, AdvancedLogger, String> getSourceFormatter() {
        return sourceFormatter;
    }
    
    public AdvancedLogger setSourceFormatter(ToughBiFunction<StackTraceElement, AdvancedLogger, String> sourceFormatter) {
        this.sourceFormatter = Objects.requireNonNull(sourceFormatter, "sourceFormatter");
        return this;
    }
    
    public ToughBiFunction<Object, AdvancedLogger, String> getObjectFormatter() {
        return objectFormatter;
    }
    
    public AdvancedLogger setObjectFormatter(ToughBiFunction<Object, AdvancedLogger, String> objectFormatter) {
        this.objectFormatter = Objects.requireNonNull(objectFormatter, "objectFormatter");
        return this;
    }
    
    @Override
    public void handleError(Throwable throwable, String message) {
        if (errorHandler != null && errorHandler.applyWithoutException(throwable, message)) {
            return;
        }
        if (throwable != null) {
            if (message != null) {
                logError(message, throwable);
            } else {
                logError("Error handling", throwable);
            }
        } else if (message != null) {
            logError(message);
        }
    }
    
}
