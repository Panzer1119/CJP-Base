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
    public static final String LOG_FORMAT_TIMESTAMP = "timestamp"; //FIXME TODO 1242545435
    public static final String LOG_FORMAT_VAR_TIMESTAMP = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_TIMESTAMP + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    public static final String LOG_FORMAT_THREAD = "thread"; //FIXME TODO 1242545435
    public static final String LOG_FORMAT_VAR_THREAD = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_THREAD + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    public static final String LOG_FORMAT_LOCATION = "location"; //FIXME TODO 1242545435
    public static final String LOG_FORMAT_VAR_LOCATION = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_LOCATION + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    public static final String LOG_FORMAT_OBJECT = "object"; //FIXME TODO 1242545435
    public static final String LOG_FORMAT_VAR_OBJECT = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_OBJECT + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_CLASS = "class"; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_VAR_CLASS = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_CLASS + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_METHOD = "method"; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_VAR_METHOD = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_METHOD + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_FILE = "file"; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_VAR_FILE = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_FILE + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_LINE = "line"; //FIXME TODO 1242545435
    public static final String LOCATION_FORMAT_VAR_LINE = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_LINE + StringSubstitutor.DEFAULT_VAR_END; //FIXME TODO 1242545435
    /**
     * Value = "{@link #LOG_FORMAT_VAR_TIMESTAMP}{@link #LOG_FORMAT_VAR_THREAD}{@link #LOG_FORMAT_VAR_LOCATION}: {@link #LOG_FORMAT_VAR_OBJECT}"
     */
    public static final String DEFAULT_LOG_FORMAT = LOG_FORMAT_VAR_TIMESTAMP + LOG_FORMAT_VAR_THREAD + LOG_FORMAT_VAR_LOCATION + ": " + LOG_FORMAT_VAR_OBJECT;
    /**
     * Value = "{@link #LOCATION_FORMAT_VAR_CLASS}.{@link #LOCATION_FORMAT_VAR_METHOD}({@link #LOCATION_FORMAT_VAR_FILE}:{@link #LOCATION_FORMAT_VAR_LINE})"
     */
    public static final String DEFAULT_LOCATION_FORMAT = LOCATION_FORMAT_VAR_CLASS + "." + LOCATION_FORMAT_VAR_METHOD + "(" + LOCATION_FORMAT_VAR_FILE + ":" + LOCATION_FORMAT_VAR_LINE + ")";
    
    protected TimeZone timeZone = TimeZone.getDefault();
    protected DateTimeFormatter dateTimeFormatter = DATE_TIME_FORMATTER_DEFAULT;
    protected String logFormat = DEFAULT_LOG_FORMAT;
    protected StringUtil.StringMapLookup logStringMapLookup = new StringUtil.StringMapLookup();
    //protected StringSubstitutor logStringSubstitutor = new StringSubstitutor(logStringMapLookup); //FIXME TODO 1242545435
    protected String locationFormat = DEFAULT_LOCATION_FORMAT;
    protected StringUtil.StringMapLookup locationStringMapLookup = new StringUtil.StringMapLookup();
    //protected StringSubstitutor locationStringSubstitutor = new StringSubstitutor(locationStringMapLookup); //FIXME TODO 1242545435
    protected ToughBiFunction<Throwable, String, Boolean> errorHandler = null;
    
    public AdvancedLogger() {
        //logFormatter.setReplaceNotExistingTagsWithNames(false); //FIXME TODO 1242545435
    }
    
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
        //setLogStringMapLookup(object, timestamp, thread, stackTraceElement);
        //logFinal(logFormatter.toString()); //FIXME TODO 1242545435
        logFinal(StringSubstitutor.replace(logFormat, createValueMap(object, timestamp, thread, stackTraceElement)));
    }
    
    @Override
    public void log(Object object, Object... arguments) {
        if (arguments != null && arguments.length > 0) {
            logFinal(String.format(object + "", arguments));
        } else {
            logFinal(object);
        }
    }
    
    protected abstract void logFinal(Object object);
    
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
        //setLogStringMapLookup(object, timestamp, thread, stackTraceElement);
        //logErrorFinal(logFormatter.toString(), throwable); //FIXME TODO 1242545435
        logErrorFinal(StringSubstitutor.replace(logFormat, createValueMap(object, timestamp, thread, stackTraceElement)), throwable);
    }
    
    //FIXME TODO 1242545435 METHOD NAME
    protected Map<String, Object> createValueMap(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        /*
        logFormatter.reset();
        logFormatter.setValue("timestamp", formatTimestamp(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId()))); //FIXME TODO 1242545435
        logFormatter.setValue("thread", formatThread(thread)); //FIXME TODO 1242545435
        logFormatter.setValue("location", formatStackTraceElement(stackTraceElement)); //FIXME TODO 1242545435
        logFormatter.setValue("object", object); //FIXME TODO 1242545435
        */
        //logStringMapLookup.clear(); //TODO necessary?
        //
        //FIXME einfach jedes Mal eine neue Map erstellen, weil das dann Thread sicher ist //NE ?! //DOCH!
        final Map<String, Object> map = new HashMap<>();
        map.put(LOG_FORMAT_TIMESTAMP, formatTimestamp(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId())));
        map.put(LOG_FORMAT_THREAD, formatThread(thread));
        map.put(LOG_FORMAT_LOCATION, formatStackTraceElement(stackTraceElement));
        map.put(LOG_FORMAT_OBJECT, formatObject(object));
        /*
        logStringSubstitutor.setVariableResolver(new StringUtil.StringMapLookup(map));
        logStringMapLookup.put("timestamp", formatTimestamp(ZonedDateTime.ofInstant(timestamp, timeZone.toZoneId())));
        logStringMapLookup.put("thread", formatThread(thread));
        logStringMapLookup.put("location", formatStackTraceElement(stackTraceElement));
        logStringMapLookup.put("object", "" + object);
        */
        return map;
    }
    
    @Override
    public void logError(Object object, Throwable throwable, Object... arguments) {
        if (arguments != null && arguments.length > 0) {
            logErrorFinal(String.format(object + "", arguments), throwable);
        } else {
            logErrorFinal(object, throwable);
        }
    }
    
    protected abstract void logErrorFinal(Object object, Throwable throwable);
    
    protected String formatTimestamp(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return "";
        }
        return "[" + dateTimeFormatter.format(zonedDateTime) + "]";
    }
    
    protected String formatThread(Thread thread) {
        if (thread == null) {
            return "";
        }
        return "[" + thread.getName() + "]";
    }
    
    protected String formatStackTraceElement(StackTraceElement stackTraceElement) {
        if (stackTraceElement == null) {
            return "";
        }
        /* //FIXME TODO 1242545435
        locationFormatter.reset();
        locationFormatter.setValue("class", stackTraceElement.getClassName()); //FIXME TODO 1242545435
        locationFormatter.setValue("method", stackTraceElement.getMethodName()); //FIXME TODO 1242545435
        locationFormatter.setValue("file", stackTraceElement.getFileName()); //FIXME TODO 1242545435
        locationFormatter.setValue("line", stackTraceElement.getLineNumber()); //FIXME TODO 1242545435
        return "[" + locationFormatter.toString() + "]";
        */
        //locationStringMapLookup.clear(); //TODO necessary?
        /*
        locationStringMapLookup.put("class", stackTraceElement.getClassName());
        locationStringMapLookup.put("method", stackTraceElement.getMethodName());
        locationStringMapLookup.put("file", stackTraceElement.getFileName());
        locationStringMapLookup.put("line", stackTraceElement.getLineNumber());
        */
        //return "[" + locationStringSubstitutor.replace(locationFormat) + "]";
        final Map<String, Object> map = new HashMap<>();
        map.put(LOCATION_FORMAT_CLASS, stackTraceElement.getClassName());
        map.put(LOCATION_FORMAT_METHOD, stackTraceElement.getMethodName());
        map.put(LOCATION_FORMAT_FILE, stackTraceElement.getFileName());
        map.put(LOCATION_FORMAT_LINE, stackTraceElement.getLineNumber());
        return "[" + StringSubstitutor.replace(locationFormat, map) + "]";
    }
    
    protected String formatObject(Object object) {
        return "" + object;
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
        this.timeZone = timeZone;
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
        this.dateTimeFormatter = dateTimeFormatter;
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
        this.logFormat = logFormat;
        return this;
    }
    
    /**
     * Gets the {@link java.lang.String} used to format the {@link java.lang.StackTraceElement}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_LOCATION_FORMAT}
     *
     * @return Location format
     */
    public final String getLocationFormat() {
        return locationFormat;
    }
    
    /**
     * Sets the {@link java.lang.String} used to format the {@link java.lang.StackTraceElement}
     * <br>
     * Default format is {@link de.codemakers.base.logger.AdvancedLogger#DEFAULT_LOCATION_FORMAT}
     *
     * @param locationFormat Location format
     *
     * @return A reference to this {@link de.codemakers.base.logger.AdvancedLogger} object
     */
    public AdvancedLogger setLocationFormat(String locationFormat) {
        this.locationFormat = locationFormat;
        return this;
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
