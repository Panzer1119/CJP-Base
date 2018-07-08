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
    
    private static final ILogger createDefaultLogger() {
        return new SystemLogger();
    }
    
    private static final AdvancedLogger createDefaultAdvancedLogger() {
        return new AdvancedSystemLogger();
    }
    
}
