/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
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

import de.codemakers.base.util.AbstractFormatBuilder;
import de.codemakers.base.util.StringUtil;
import org.apache.commons.text.StringSubstitutor;

@Deprecated
public class SourceFormatBuilder extends AbstractFormatBuilder<SourceFormatBuilder> {
    
    public SourceFormatBuilder() {
        super("");
    }
    
    public SourceFormatBuilder(String format) {
        super(format);
    }
    
    public SourceFormatBuilder appendClassName() {
        format.append(Logger.SOURCE_FORMAT_VAR_CLASS);
        return this;
    }
    
    public SourceFormatBuilder appendMethodName() {
        format.append(Logger.SOURCE_FORMAT_VAR_METHOD);
        return this;
    }
    
    public SourceFormatBuilder appendFileName() {
        format.append(Logger.SOURCE_FORMAT_VAR_FILE);
        return this;
    }
    
    public SourceFormatBuilder appendLineNumber() {
        format.append(Logger.SOURCE_FORMAT_VAR_LINE);
        return this;
    }
    
    @Override
    protected String checkAndCorrectText(String text) {
        return StringUtil.escapeStringSubstitutorVariableCalls(text);
    }
    
    @Override
    public String example() {
        return StringSubstitutor.replace(format, AdvancedLogger.createValueMap(AdvancedLogger.cutStackTrace(new Exception().getStackTrace())));
    }
    
    @Override
    public String finish() throws Exception {
        throw new AbstractMethodError();
    }
    
    @Override
    public String toString() {
        return "SourceFormatBuilder{" + "format='" + format + '\'' + ", checkAndCorrectAppendedText=" + checkAndCorrectAppendedText + '}';
    }
    
}
