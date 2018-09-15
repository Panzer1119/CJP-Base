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

/**
 * Standard {@link de.codemakers.base.logger.AdvancedLogger}, this implementation uses the original {@link java.lang.System} {@link java.io.PrintStream}s
 * to log messages with timestamps and more
 */
public class AdvancedSystemLogger extends AdvancedLogger {
    
    @Override
    protected void logFinal(Object object) {
        Standard.SYSTEM_OUTPUT_STREAM.println(object);
    }
    
    @Override
    protected void logErrFinal(Object object, Throwable throwable) {
        Standard.SYSTEM_ERROR_STREAM.println(object);
        if (throwable != null) {
            throwable.printStackTrace(Standard.SYSTEM_ERROR_STREAM);
        }
    }
    
    @Override
    public void handleError(Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace(Standard.SYSTEM_ERROR_STREAM);
        }
    }
    
}
