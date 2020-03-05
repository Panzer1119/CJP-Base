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

package de.codemakers.swing.frame;

import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.base.util.AbstractFormatBuilder;
import de.codemakers.base.util.StringUtil;

public class JFrameTitleFormatBuilder extends AbstractFormatBuilder<JFrameTitleFormatBuilder> {
    
    public JFrameTitleFormatBuilder() {
        super("");
    }
    
    public JFrameTitleFormatBuilder(String format) {
        super(format);
    }
    
    public JFrameTitleFormatBuilder appendName() {
        format.append(JFrameManager.TITLE_FORMAT_VAR_NAME);
        return this;
    }
    
    public JFrameTitleFormatBuilder appendVersion() {
        format.append(JFrameManager.TITLE_FORMAT_VAR_VERSION);
        return this;
    }
    
    public JFrameTitleFormatBuilder appendIDE() {
        format.append(JFrameManager.TITLE_FORMAT_VAR_IDE);
        return this;
    }
    
    public JFrameTitleFormatBuilder appendPrefix() {
        format.append(JFrameManager.TITLE_FORMAT_VAR_PREFIX);
        return this;
    }
    
    public JFrameTitleFormatBuilder appendSuffix() {
        format.append(JFrameManager.TITLE_FORMAT_VAR_SUFFIX);
        return this;
    }
    
    @Override
    protected String checkAndCorrectText(String text) {
        return StringUtil.escapeStringSubstitutorVariableCalls(text);
    }
    
    @Override
    public String example() {
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public String finish() throws Exception {
        throw new AbstractMethodError();
    }
    
    @Override
    public String toString() {
        return "JFrameTitleFormatBuilder{" + "format='" + format + '\'' + ", checkAndCorrectAppendedText=" + checkAndCorrectAppendedText + '}';
    }
    
}
