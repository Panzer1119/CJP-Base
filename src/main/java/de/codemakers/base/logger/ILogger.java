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

/**
 * Logger Interface
 */
public interface ILogger {
    
    /**
     * Logs an {@link java.lang.Object}
     *
     * @param object {@link java.lang.Object} to get logged (e.g. a {@link java.lang.String})
     * @param arguments Arguments
     */
    void log(Object object, Object... arguments);
    
    /**
     * Logs an {@link java.lang.Object} and a {@link java.lang.Throwable} as an Error
     *
     * @param object {@link java.lang.Object} to get logged (e.g. some explaining text)
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param arguments Arguments
     */
    void logError(Object object, Throwable throwable, Object... arguments);
    
    /**
     * Logs an {@link java.lang.Object} as an Error
     *
     * @param object {@link java.lang.Object} to get logged (e.g. some explaining text)
     * @param arguments Arguments
     */
    default void logError(Object object, Object... arguments) {
        logError(object, null, arguments);
    }
    
    /**
     * Handles an Error with a custom message
     *
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     * @param message Additional message (may be null)
     */
    void handleError(Throwable throwable, String message);
    
    /**
     * Handles an Error
     *
     * @param throwable Error (e.g. an {@link java.lang.Exception})
     */
    default void handleError(Throwable throwable) {
        handleError(throwable, null);
    }
    
}
