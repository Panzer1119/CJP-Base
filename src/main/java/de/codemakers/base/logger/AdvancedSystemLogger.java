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

import de.codemakers.base.Standard;

import java.io.PrintStream;
import java.time.Instant;
import java.util.Arrays;

/**
 * Standard {@link de.codemakers.base.logger.AdvancedLogger}, this implementation uses the original {@link java.lang.System} {@link java.io.PrintStream}s
 * to log messages with timestamps and more
 */
public class AdvancedSystemLogger extends AdvancedLogger {
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.Standard#SYSTEM_OUTPUT_STREAM}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param arguments Not used here
     */
    @Override
    public final void log(Object object, Object... arguments) {
        if (arguments.length >= 1) {
            if (arguments[0] == null || arguments[0] instanceof Instant) {
                if (arguments.length >= 2) {
                    if (arguments[1] == null || arguments[1] instanceof Thread) {
                        if (arguments.length >= 3) {
                            if (arguments[2] == null || arguments[2] instanceof StackTraceElement) {
                                if (arguments.length > 3) {
                                    System.arraycopy(arguments, 0, arguments, 1, arguments.length - 3);
                                    log(String.format(object + "", Arrays.copyOf(arguments, arguments.length - 3)), (Instant) arguments[0], (Thread) arguments[1], (StackTraceElement) arguments[2]);
                                } else {
                                    log(object, (Instant) arguments[0], (Thread) arguments[1], (StackTraceElement) arguments[2]);
                                }
                            } else {
                                System.arraycopy(arguments, 0, arguments, 1, arguments.length - 2);
                                log(String.format(object + "", Arrays.copyOf(arguments, arguments.length - 2)), (Instant) arguments[0], (Thread) arguments[1]);
                            }
                        } else {
                            log(object, (Instant) arguments[0], (Thread) arguments[1]);
                        }
                    } else {
                        System.arraycopy(arguments, 0, arguments, 1, arguments.length - 1);
                        log(String.format(object + "", Arrays.copyOf(arguments, arguments.length - 1)), (Instant) arguments[0]);
                    }
                } else {
                    log(object, (Instant) arguments[0]);
                }
            } else {
                log(String.format(object + "", arguments));
            }
        } else {
            log(object);
        }
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.Standard#SYSTEM_ERROR_STREAM}
     * <br>
     * It uses {@link java.lang.Throwable#printStackTrace(PrintStream)} to print the Error
     *
     * @param object {@link java.lang.Object} to get logged (e.g. some explaining text)
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param arguments Not used here
     */
    @Override
    public final void logErr(Object object, Throwable throwable, Object... arguments) {
        if (arguments != null && arguments.length > 0) {
            Standard.SYSTEM_ERROR_STREAM.println(String.format(object + "", arguments));
        } else {
            Standard.SYSTEM_ERROR_STREAM.println(object);
        }
        if (throwable != null) {
            throwable.printStackTrace(Standard.SYSTEM_ERROR_STREAM);
        }
    }
    
    /**
     * Handles an Error using the {@link de.codemakers.base.Standard#SYSTEM_ERROR_STREAM}
     * <br>
     * It uses {@link java.lang.Throwable#printStackTrace(PrintStream)} to print the Error
     *
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     */
    @Override
    public final void handleError(Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace(Standard.SYSTEM_ERROR_STREAM);
        }
    }
    
    /**
     * Handles the internally generated Log Messages
     *
     * @param object Log Message
     */
    @Override
    protected final void logFinal(Object object) {
        Standard.SYSTEM_OUTPUT_STREAM.println(object);
    }
    
}
