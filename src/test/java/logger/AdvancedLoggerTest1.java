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

package logger;

import de.codemakers.base.logger.AdvancedLogger;
import de.codemakers.base.logger.Logger;

import java.time.Instant;

public class AdvancedLoggerTest1 {
    
    public static final void main(String[] args) {
        final AdvancedLogger advancedLogger = new AdvancedLogger() {
            @Override
            public void log(Object object, Object... arguments) {
                Logger.DEFAULT_LOGGER.log(object, arguments);
            }
    
            @Override
            public void logErr(Object object, Throwable throwable, Object... arguments) {
                Logger.DEFAULT_LOGGER.logErr(object, throwable, arguments);
            }
    
            @Override
            public void handleError(Throwable throwable) {
                Logger.DEFAULT_LOGGER.handleError(throwable);
            }
        };
        advancedLogger.log("Test 1.1", null, Thread.currentThread(), new Exception().getStackTrace()[0]);
        advancedLogger.log("Test 1.2", null, Thread.currentThread(), null);
        advancedLogger.log("Test 1.3", null, null, new Exception().getStackTrace()[0]);
        advancedLogger.log("Test 2.1", null, Thread.currentThread());
        advancedLogger.log("Test 2.2", null, null);
        advancedLogger.log("Test 3.1", Instant.now());
        advancedLogger.log("Test 3.2");
    }
    
}
