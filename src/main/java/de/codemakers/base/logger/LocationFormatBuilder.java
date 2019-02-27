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

public class LocationFormatBuilder {
    
    protected String format;
    
    public LocationFormatBuilder() {
        this("");
    }
    
    public LocationFormatBuilder(String format) {
        this.format = format;
    }
    
    public LocationFormatBuilder appendText(String text) {
        //checkText(text); //FIXME
        format += text;
        return this;
    }
    
    protected void checkText(String text) {
        if (text.contains(StringSubstitutor.DEFAULT_VAR_START)) { //FIXME Is this good? Maybe it is escaped with another "$"
            //throw new IllegalArgumentException();
        }
    }
    
    public LocationFormatBuilder appendClassName() {
        format += Logger.LOCATION_FORMAT_VAR_CLASS;
        return this;
    }
    
    public LocationFormatBuilder appendMethodName() {
        format += Logger.LOCATION_FORMAT_VAR_METHOD;
        return this;
    }
    
    public LocationFormatBuilder appendFileName() {
        format += Logger.LOCATION_FORMAT_VAR_FILE;
        return this;
    }
    
    public LocationFormatBuilder appendLineNumber() {
        format += Logger.LOCATION_FORMAT_VAR_LINE;
        return this;
    }
    
    public String toFormat() {
        return format;
    }
    
    public LocationFormatBuilder setFormat(String format) {
        this.format = format;
        return this;
    }
    
    @Override
    public String toString() {
        return "LocationFormatBuilder{" + "format='" + format + '\'' + '}';
    }
    
}
