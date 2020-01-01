/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

import de.codemakers.base.util.tough.ToughBiFunction;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public abstract class AdvancedLeveledLogger extends AdvancedLogger {
    
    /**
     * Value = "{@link Logger#LOG_FORMAT_VAR_TIMESTAMP}{@link Logger#LOG_FORMAT_VAR_THREAD}{@link Logger#LOG_FORMAT_VAR_SOURCE}{@link Logger#LOG_FORMAT_VAR_LOG_LEVEL}: {@link Logger#LOG_FORMAT_VAR_OBJECT}"
     */
    public static final String DEFAULT_LEVELED_LOG_FORMAT = Logger.LOG_FORMAT_VAR_TIMESTAMP + Logger.LOG_FORMAT_VAR_THREAD + Logger.LOG_FORMAT_VAR_SOURCE + Logger.LOG_FORMAT_VAR_LOG_LEVEL + ": " + Logger.LOG_FORMAT_VAR_OBJECT;
    public static final ToughBiFunction<LogLevel, AdvancedLogger, String> DEFAULT_LOG_LEVEL_FORMATTER = (logLevel, advancedLogger) -> logLevel == null ? "" : "[" + logLevel.getNameMid() + "]";
    
    protected LogLevel minimumLogLevel = LogLevel.INFO;
    protected ToughBiFunction<LogLevel, AdvancedLogger, String> logLevelFormatter = DEFAULT_LOG_LEVEL_FORMATTER;
    
    public AdvancedLeveledLogger() {
        this.logFormat = DEFAULT_LEVELED_LOG_FORMAT;
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
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (minimumLogLevel.isThisLevelMoreImportant(logLevel)) {
            return;
        }
        final LeveledLogEntry leveledLogEntry = new LeveledLogEntry(object, timestamp, thread, stackTraceElement, logLevel);
        preFinal(leveledLogEntry);
        logFinal(leveledLogEntry);
        postFinal(leveledLogEntry);
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
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (minimumLogLevel.isThisLevelMoreImportant(logLevel)) {
            return;
        }
        final LeveledLogEntry leveledLogEntry = new LeveledLogEntry(object, timestamp, thread, stackTraceElement, throwable, true, logLevel);
        preFinal(leveledLogEntry);
        logFinal(leveledLogEntry);
        postFinal(leveledLogEntry);
    }
    
    @Override
    protected Map<String, Object> createValueMap(LogEntry logEntry) {
        if (logEntry instanceof LeveledLogEntry) {
            final LeveledLogEntry leveledLogEntry = (LeveledLogEntry) logEntry;
            return createValueMap(leveledLogEntry.getObject(), leveledLogEntry.getTimestamp(), leveledLogEntry.getThread(), leveledLogEntry.getStackTraceElement(), leveledLogEntry.getLogLevel());
        }
        return super.createValueMap(logEntry);
    }
    
    protected Map<String, Object> createValueMap(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        final Map<String, Object> map = createValueMap(object, timestamp, thread, stackTraceElement);
        map.put(Logger.LOG_FORMAT_LOG_LEVEL, formatLogLevel(logLevel));
        return map;
    }
    
    protected String formatLogLevel(LogLevel logLevel) {
        return logLevelFormatter.applyWithoutException(logLevel, this);
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
    
    public ToughBiFunction<LogLevel, AdvancedLogger, String> getLogLevelFormatter() {
        return logLevelFormatter;
    }
    
    public AdvancedLeveledLogger setLogLevelFormatter(ToughBiFunction<LogLevel, AdvancedLogger, String> logLevelFormatter) {
        this.logLevelFormatter = Objects.requireNonNull(logLevelFormatter, "logLevelFormatter");
        return this;
    }
    
    /**
     * Gets the {@link java.lang.String} used to format the log Message
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLeveledLogger#DEFAULT_LEVELED_LOG_FORMAT}
     *
     * @return Log format
     */
    @Override
    public final String getLogFormat() {
        return super.getLogFormat();
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the log Message
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLeveledLogger#DEFAULT_LEVELED_LOG_FORMAT}
     *
     * @param logFormat Log format
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLeveledLogger} object
     */
    @Override
    public AdvancedLeveledLogger setLogFormat(String logFormat) {
        return (AdvancedLeveledLogger) super.setLogFormat(logFormat);
    }
    
}
