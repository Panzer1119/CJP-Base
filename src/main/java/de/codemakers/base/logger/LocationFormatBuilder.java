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

import de.codemakers.base.util.AbstractFormatBuilder;
import de.codemakers.base.util.StringUtil;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class LocationFormatBuilder extends AbstractFormatBuilder<LocationFormatBuilder> {
    
    public LocationFormatBuilder() {
        super("");
    }
    
    public LocationFormatBuilder(String format) {
        super(format);
    }
    
    @Override
    protected String checkAndCorrectText(String text) {
        return StringUtil.escapeStringSubstitutorVariableCalls(text);
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
    
    @Override
    public String example() {
        final Map<String, Object> map = new HashMap<>();
        final StackTraceElement stackTraceElement = AdvancedLogger.cutStackTrace(new Exception().getStackTrace());
        map.put(Logger.LOCATION_FORMAT_CLASS, stackTraceElement.getClassName());
        map.put(Logger.LOCATION_FORMAT_METHOD, stackTraceElement.getMethodName());
        map.put(Logger.LOCATION_FORMAT_FILE, stackTraceElement.getFileName());
        map.put(Logger.LOCATION_FORMAT_LINE, stackTraceElement.getLineNumber());
        return StringSubstitutor.replace(format, map);
    }
    
    @Override
    public String toString() {
        return "LocationFormatBuilder{" + "format='" + format + '\'' + ", checkAndCorrectAppendedText=" + checkAndCorrectAppendedText + '}';
    }
    
    @Override
    public String finish() throws Exception {
        throw new AbstractMethodError();
    }
    
}
