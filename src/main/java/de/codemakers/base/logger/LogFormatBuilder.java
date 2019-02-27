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

import org.apache.commons.text.StringSubstitutor;

public class LogFormatBuilder {
    
    protected String format;
    
    public LogFormatBuilder() {
        this("");
    }
    
    public LogFormatBuilder(String format) {
        this.format = format;
    }
    
    public LogFormatBuilder appendText(String text) {
        //checkText(text); //FIXME
        format += text;
        return this;
    }
    
    protected void checkText(String text) {
        if (text.contains(StringSubstitutor.DEFAULT_VAR_START)) { //FIXME Is this good? Maybe it is escaped with another "$"
            //throw new IllegalArgumentException();
        }
    }
    
    public LogFormatBuilder appendTimestamp() {
        format += Logger.LOG_FORMAT_VAR_TIMESTAMP;
        return this;
    }
    
    public LogFormatBuilder appendThread() {
        format += Logger.LOG_FORMAT_VAR_THREAD;
        return this;
    }
    
    public LogFormatBuilder appendLocation() {
        format += Logger.LOG_FORMAT_VAR_LOCATION;
        return this;
    }
    
    public LogFormatBuilder appendLogLevel() {
        format += Logger.LOG_FORMAT_VAR_LOG_LEVEL;
        return this;
    }
    
    public LogFormatBuilder appendObject() {
        format += Logger.LOG_FORMAT_VAR_OBJECT;
        return this;
    }
    
    public String toFormat() {
        return format;
    }
    
    public LogFormatBuilder setFormat(String format) {
        this.format = format;
        return this;
    }
    
    @Override
    public String toString() {
        return "LogFormatBuilder{" + "format='" + format + '\'' + '}';
    }
    
}
