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

import java.time.Instant;

public class AdvancedLoggerTest2 {
    
    public static final void main(String[] args) {
        Logger.DEFAULT_ADVANCED_LOGGER.log("Test 1.1", null, Thread.currentThread(), new Exception().getStackTrace()[0]);
        Logger.DEFAULT_ADVANCED_LOGGER.log("Test 1.2", null, Thread.currentThread(), null);
        Logger.DEFAULT_ADVANCED_LOGGER.log("Test 1.3", null, null, new Exception().getStackTrace()[0]);
        Logger.DEFAULT_ADVANCED_LOGGER.log("Test 2.1", null, Thread.currentThread());
        Logger.DEFAULT_ADVANCED_LOGGER.log("Test 2.2", null, null);
        Logger.DEFAULT_ADVANCED_LOGGER.log("Test 3.1", Instant.now());
        Logger.DEFAULT_ADVANCED_LOGGER.log("Test 3.2");
    }
    
}
