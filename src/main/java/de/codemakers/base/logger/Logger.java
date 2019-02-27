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

import org.apache.commons.text.StringSubstitutor;

import java.time.Instant;

/**
 * Logger Class for default Instances
 */
public class Logger {
    
    /**
     * Default {@link de.codemakers.base.logger.ILogger} Instance
     */
    public static final ILogger DEFAULT_LOGGER = createDefaultLogger();
    /**
     * Default {@link de.codemakers.base.logger.AdvancedLogger} Instance
     */
    public static final AdvancedLogger DEFAULT_ADVANCED_LOGGER = createDefaultAdvancedLogger();
    /**
     * Default {@link de.codemakers.base.logger.AdvancedLeveledSystemLogger} Instance
     */
    public static final AdvancedLeveledSystemLogger DEFAULT_ADVANCED_LEVELED_LOGGER = createDefaultAdvancedLeveledSystemLogger();
    
    public static ILogger LOGGER = DEFAULT_ADVANCED_LEVELED_LOGGER;
    //TODO Add javadoc
    public static final String LOG_FORMAT_TIMESTAMP = "timestamp";
    public static final String LOG_FORMAT_VAR_TIMESTAMP = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_TIMESTAMP + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOG_FORMAT_THREAD = "thread";
    public static final String LOG_FORMAT_VAR_THREAD = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_THREAD + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOG_FORMAT_LOCATION = "location";
    public static final String LOG_FORMAT_VAR_LOCATION = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_LOCATION + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOG_FORMAT_LOG_LEVEL = "loglevel";
    public static final String LOG_FORMAT_VAR_LOG_LEVEL = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_LOG_LEVEL + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOG_FORMAT_OBJECT = "object";
    public static final String LOG_FORMAT_VAR_OBJECT = StringSubstitutor.DEFAULT_VAR_START + LOG_FORMAT_OBJECT + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOCATION_FORMAT_CLASS = "class";
    public static final String LOCATION_FORMAT_VAR_CLASS = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_CLASS + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOCATION_FORMAT_METHOD = "method";
    public static final String LOCATION_FORMAT_VAR_METHOD = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_METHOD + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOCATION_FORMAT_FILE = "file";
    public static final String LOCATION_FORMAT_VAR_FILE = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_FILE + StringSubstitutor.DEFAULT_VAR_END;
    public static final String LOCATION_FORMAT_LINE = "line";
    public static final String LOCATION_FORMAT_VAR_LINE = StringSubstitutor.DEFAULT_VAR_START + LOCATION_FORMAT_LINE + StringSubstitutor.DEFAULT_VAR_END;
    
    private static final ILogger createDefaultLogger() {
        return new SystemLogger();
    }
    
    private static final AdvancedLogger createDefaultAdvancedLogger() {
        return new AdvancedSystemLogger();
    }
    
    private static final AdvancedLeveledSystemLogger createDefaultAdvancedLeveledSystemLogger() {
        return new AdvancedLeveledSystemLogger();
    }
    
    public static <L extends ILogger> L getLogger() {
        return (L) LOGGER;
    }
    
    public static void setLogger(ILogger logger) {
        LOGGER = logger;
    }
    
    public static <L extends ILogger> L getLogger(Class<L> clazz) {
        return (L) LOGGER;
    }
    
    public static boolean isLoggerAssignableFrom(Class<? extends ILogger> clazz) {
        return clazz.isAssignableFrom(LOGGER.getClass());
    }
    
    public static AdvancedLogger getLoggerAsAdvancedLogger() {
        return (AdvancedLogger) LOGGER;
    }
    
    public static AdvancedLeveledLogger getLoggerAsAdvancedLeveledLogger() {
        return (AdvancedLeveledLogger) LOGGER;
    }
    
    public static AdvancedLeveledSystemLogger getLoggerAsAdvancedLeveledSystemLogger() {
        return (AdvancedLeveledSystemLogger) LOGGER;
    }
    
    public static ILogger getDefaultLogger() {
        return DEFAULT_LOGGER;
    }
    
    public static AdvancedLogger getDefaultAdvancedLogger() {
        return DEFAULT_ADVANCED_LOGGER;
    }
    
    public static AdvancedLeveledSystemLogger getDefaultAdvancedLeveledLogger() {
        return DEFAULT_ADVANCED_LEVELED_LOGGER;
    }
    
    /**
     * Logs an {@link java.lang.Object} with {@link de.codemakers.base.logger.LogLevel#DEBUG} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     */
    public static void logDebug(Object object) {
        log(object, LogLevel.DEBUG);
    }
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param arguments Arguments
     */
    public static void log(Object object, Object... arguments) {
        if (LOGGER != null) {
            LOGGER.log(object, arguments);
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     */
    public static void log(Object object) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).log(object);
            } else {
                LOGGER.log(object);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param logLevel {@link de.codemakers.base.logger.LogLevel}
     */
    public static void log(Object object, LogLevel logLevel) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLeveledLogger) {
                ((AdvancedLeveledLogger) LOGGER).log(object, Instant.now(), Thread.currentThread(), AdvancedLogger.cutStackTrace(new Exception().getStackTrace()), logLevel);
            } else {
                LOGGER.log(object);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param timestamp Timestamp
     */
    public static void log(Object object, Instant timestamp) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).log(object, timestamp);
            } else {
                LOGGER.log(object, timestamp);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param timestamp Timestamp
     * @param thread Thread
     */
    public static void log(Object object, Instant timestamp, Thread thread) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).log(object, timestamp, thread);
            } else {
                LOGGER.log(object, thread, thread);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param timestamp Timestamp
     * @param thread Thread
     * @param stackTraceElement StackTraceElement (used to determine the source of the {@link de.codemakers.base.logger.Logger#log(Object, Instant, Thread, StackTraceElement)} call)
     */
    public static void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).log(object, timestamp, thread, stackTraceElement);
            } else {
                LOGGER.log(object, timestamp, thread, stackTraceElement);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param timestamp Timestamp
     * @param thread Thread
     * @param stackTraceElement StackTraceElement (used to determine the source of the {@link de.codemakers.base.logger.Logger#log(Object, Instant, Thread, StackTraceElement)} call)
     * @param logLevel {@link de.codemakers.base.logger.LogLevel}
     */
    public static void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLeveledLogger) {
                ((AdvancedLeveledLogger) LOGGER).log(object, timestamp, thread, stackTraceElement, logLevel);
            } else if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).log(object, timestamp, thread, stackTraceElement);
            } else {
                LOGGER.log(object, thread, thread, stackTraceElement, logLevel);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} with {@link de.codemakers.base.logger.LogLevel#WARNING} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     */
    public static void logWarning(Object object) {
        log(object, LogLevel.WARNING);
    }
    
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. some explaining text)
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param arguments Arguments
     */
    public static final void logError(Object object, Throwable throwable, Object... arguments) {
        if (LOGGER != null) {
            LOGGER.logError(object, throwable, arguments);
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     */
    public static void logError(Object object, Throwable throwable) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logError(object, throwable);
            } else {
                LOGGER.logError(object, throwable);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param timestamp Timestamp
     */
    public static void logError(Object object, Throwable throwable, Instant timestamp) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logError(object, throwable, timestamp);
            } else {
                LOGGER.logError(object, throwable, timestamp);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param timestamp Timestamp
     * @param thread Thread
     */
    public static void logError(Object object, Throwable throwable, Instant timestamp, Thread thread) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logError(object, throwable, timestamp, thread);
            } else {
                LOGGER.logError(object, throwable, thread, thread);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param timestamp Timestamp
     * @param thread Thread
     * @param stackTraceElement StackTraceElement (used to determine the source of the {@link de.codemakers.base.logger.Logger#log(Object, Instant, Thread, StackTraceElement)} call)
     */
    public static void logError(Object object, Throwable throwable, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logError(object, throwable, timestamp, thread, stackTraceElement);
            } else {
                LOGGER.logError(object, throwable, timestamp, thread, stackTraceElement);
            }
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param timestamp Timestamp
     * @param thread Thread
     * @param stackTraceElement StackTraceElement (used to determine the source of the {@link de.codemakers.base.logger.Logger#log(Object, Instant, Thread, StackTraceElement)} call)
     * @param logLevel {@link de.codemakers.base.logger.LogLevel}
     */
    public static void logError(Object object, Throwable throwable, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLeveledLogger) {
                ((AdvancedLeveledLogger) LOGGER).logError(object, throwable, timestamp, thread, stackTraceElement, logLevel);
            } else if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logError(object, throwable, timestamp, thread, stackTraceElement);
            } else {
                LOGGER.logError(object, throwable, thread, thread, stackTraceElement, logLevel);
            }
        }
    }
    
    /**
     * Handles an Error using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param message Additional message (may be null)
     */
    public static final void handleError(Throwable throwable, String message) {
        if (LOGGER != null) {
            LOGGER.handleError(throwable, message);
        }
    }
    
    /**
     * Handles an Error using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     */
    public static final void handleError(Throwable throwable) {
        if (LOGGER != null) {
            LOGGER.handleError(throwable);
        }
    }
    
}
