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

import de.codemakers.base.Standard;

import java.io.PrintStream;

public class AdvancedLeveledSystemLogger extends AdvancedLeveledLogger {
    
    /**
     * Logs an {@link java.lang.Object} using the {@link de.codemakers.base.Standard#SYSTEM_OUTPUT_STREAM}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     */
    @Deprecated
    protected void logFinal(Object object) { //FIXME Remove this old method
        Standard.SYSTEM_OUTPUT_STREAM.println(object);
    }
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} using the {@link de.codemakers.base.Standard#SYSTEM_ERROR_STREAM}
     * <br>
     * It uses {@link java.lang.Throwable#printStackTrace(PrintStream)} to print the Error
     *
     * @param object {@link java.lang.Object} to get logged (e.g. some explaining text)
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     */
    @Deprecated
    protected void logErrorFinal(Object object, Throwable throwable) { //FIXME Remove this old method
        Standard.SYSTEM_ERROR_STREAM.println(object);
        if (throwable != null) {
            throwable.printStackTrace(Standard.SYSTEM_ERROR_STREAM);
        }
    }
    
    @Override
    protected void preFinal(LogEntry logEntry) {
        //TODO Writing LogEntries to Console?
    }
    
    @Override
    protected void logFinal(LogEntry logEntry) { //Writing LogEntries to System.out/System.err
        if (logEntry == null) {
            return; //FIXME Remove this? Or is this not a performance problem?
        }
        if (logEntry.isBad()) {
            Standard.SYSTEM_ERROR_STREAM.println(logEntry.formatWithoutException(this));
            if (logEntry.hasError()) {
                logEntry.getThrowable().printStackTrace(Standard.SYSTEM_ERROR_STREAM);
            }
        } else {
            Standard.SYSTEM_OUTPUT_STREAM.println(logEntry.formatWithoutException(this));
        }
    }
    
    @Override
    protected void postFinal(LogEntry logEntry) {
        //TODO Writing LogEntries to AdvancedFile?
    }
    
}
