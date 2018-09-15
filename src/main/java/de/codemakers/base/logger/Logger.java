/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
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
    public static final AdvancedLogger DEFAULT_ADVANCED_LEVELED_LOGGER = createDefaultAdvancedLeveledSystemLogger();
    
    public static ILogger LOGGER = DEFAULT_ADVANCED_LEVELED_LOGGER;
    
    private static final ILogger createDefaultLogger() {
        return new SystemLogger();
    }
    
    private static final AdvancedLogger createDefaultAdvancedLogger() {
        return new AdvancedSystemLogger();
    }
    
    private static final AdvancedLeveledSystemLogger createDefaultAdvancedLeveledSystemLogger() {
        return new AdvancedLeveledSystemLogger();
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
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. some explaining text)
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param arguments Arguments
     */
    public static final void logErr(Object object, Throwable throwable, Object... arguments) {
        if (LOGGER != null) {
            LOGGER.logErr(object, throwable, arguments);
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.logger.Logger#LOGGER}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     */
    public static void logErr(Object object, Throwable throwable) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logErr(object, throwable);
            } else {
                LOGGER.logErr(object, throwable);
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
    public static void logErr(Object object, Throwable throwable, Instant timestamp) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logErr(object, throwable, timestamp);
            } else {
                LOGGER.logErr(object, throwable, timestamp);
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
    public static void logErr(Object object, Throwable throwable, Instant timestamp, Thread thread) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logErr(object, throwable, timestamp, thread);
            } else {
                LOGGER.logErr(object, throwable, thread, thread);
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
    public static void logErr(Object object, Throwable throwable, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logErr(object, throwable, timestamp, thread, stackTraceElement);
            } else {
                LOGGER.logErr(object, throwable, timestamp, thread, stackTraceElement);
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
    public static void logErr(Object object, Throwable throwable, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        if (LOGGER != null) {
            if (LOGGER instanceof AdvancedLeveledLogger) {
                ((AdvancedLeveledLogger) LOGGER).logErr(object, throwable, timestamp, thread, stackTraceElement, logLevel);
            } else if (LOGGER instanceof AdvancedLogger) {
                ((AdvancedLogger) LOGGER).logErr(object, throwable, timestamp, thread, stackTraceElement);
            } else {
                LOGGER.logErr(object, throwable, thread, thread, stackTraceElement, logLevel);
            }
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
