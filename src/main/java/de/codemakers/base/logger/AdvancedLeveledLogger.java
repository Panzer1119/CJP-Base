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

public abstract class AdvancedLeveledLogger extends AdvancedLogger {
    
    /**
     * Value = [%2$s]%3$s%4$s%5$s: %1$s
     */
    public static final String DEFAULT_LEVELED_LOG_FORMAT = "[%2$s]%3$s%4$s%5$s: %1$s";
    
    public AdvancedLeveledLogger() {
        this.logFormat = DEFAULT_LEVELED_LOG_FORMAT;
    }
    
    @Override
    public void log(Object object) {
        super.log(object);
    }
    
    @Override
    public void log(Object object, Instant timestamp) {
        super.log(object, timestamp);
    }
    
    @Override
    public void log(Object object, Instant timestamp, Thread thread) {
        super.log(object, timestamp, thread);
    }
    
    @Override
    public void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement) {
        super.log(object, timestamp, thread, stackTraceElement);
    }
    
    public void log(Object object, Instant timestamp, Thread thread, StackTraceElement stackTraceElement, LogLevel logLevel) {
        super.log(object, timestamp, thread, stackTraceElement);
    }
    
}
