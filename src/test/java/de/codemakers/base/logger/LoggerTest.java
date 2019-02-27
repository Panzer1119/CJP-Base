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

public class LoggerTest {
    
    public static final void main(String[] args) {
        Logger.DEFAULT_ADVANCED_LEVELED_LOGGER.setLogFormat(AdvancedLogger.LOG_FORMAT_VAR_TIMESTAMP + AdvancedLogger.LOG_FORMAT_VAR_THREAD + AdvancedLeveledLogger.LOG_FORMAT_VAR_LOG_LEVEL + ": " + AdvancedLogger.LOG_FORMAT_VAR_OBJECT + "\n" + AdvancedLogger.LOG_FORMAT_VAR_LOCATION);
        Logger.log("Test");
        for (int i = 0; i < 100; i++) {
            Logger.log("i=" + i);
        }
        Logger.log("LogLevel.MINIMUM_NAME_LENGTH=" + LogLevel.MINIMUM_NAME_LENGTH);
        Logger.log("LogLevel.MAXIMUM_NAME_LENGTH=" + LogLevel.MAXIMUM_NAME_LENGTH);
        for (LogLevel logLevel : LogLevel.values()) {
            Logger.log("logLevel=" + logLevel);
            Logger.log("logLevel.getNameLeft() =" + logLevel.getNameLeft());
            Logger.log("logLevel.getNameRight()=" + logLevel.getNameRight());
            Logger.log("logLevel.getNameMid()  =" + logLevel.getNameMid());
            Logger.log("#####################");
        }
    }
    
}
