/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
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

import de.codemakers.base.Standard;

import java.io.PrintStream;

/**
 * Standard {@link de.codemakers.base.logger.ILogger}, this implementation uses the original {@link java.lang.System} {@link java.io.PrintStream}s
 * to log messages and more
 */
public class SystemLogger implements ILogger {
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.Standard#SYSTEM_OUTPUT_STREAM}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param arguments Not used here
     */
    @Override
    public final void log(Object object, Object... arguments) {
        if (arguments != null && arguments.length > 0) {
            Standard.SYSTEM_OUTPUT_STREAM.println(String.format(object + "", arguments));
        } else {
            Standard.SYSTEM_OUTPUT_STREAM.println(object);
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
    public final void logError(Object object, Throwable throwable, Object... arguments) {
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
     * @param message Additional message (may be null)
     */
    @Override
    public void handleError(Throwable throwable, String message) {
        if (throwable != null) {
            if (message != null) {
                Standard.SYSTEM_ERROR_STREAM.println(message);
            }
            throwable.printStackTrace(Standard.SYSTEM_ERROR_STREAM);
        }
    }
    
}
