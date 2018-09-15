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
    public static final void log(Object object, Object... arguments) {
        if (LOGGER != null) {
            LOGGER.log(object, arguments);
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
