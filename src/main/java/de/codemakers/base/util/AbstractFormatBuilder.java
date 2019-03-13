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

package de.codemakers.base.util;

import de.codemakers.base.util.interfaces.Finishable;

import java.util.Objects;

public abstract class AbstractFormatBuilder<C extends AbstractFormatBuilder> implements Finishable<String> {
    
    public static final char NEW_LINE = '\n';
    
    protected StringBuilder format;
    protected boolean checkAndCorrectAppendedText = true;
    
    public AbstractFormatBuilder() {
        this("");
    }
    
    public AbstractFormatBuilder(String format) {
        this(new StringBuilder(format));
    }
    
    public AbstractFormatBuilder(StringBuilder format) {
        this.format = Objects.requireNonNull(format, "format");
    }
    
    public C appendText(String text) {
        if (checkAndCorrectAppendedText) {
            text = checkAndCorrectText(text);
        }
        format.append(text);
        return (C) this;
    }
    
    protected abstract String checkAndCorrectText(String text);
    
    public C appendNewLine() {
        format.append(NEW_LINE);
        return (C) this;
    }
    
    public abstract String example();
    
    public String toFormat() {
        return format.toString();
    }
    
    public C setFormat(String format) {
        //this.format = new StringBuilder(format);
        this.format.setLength(0);
        appendText(format);
        return (C) this;
    }
    
    public boolean isCheckAndCorrectAppendedText() {
        return checkAndCorrectAppendedText;
    }
    
    public C setCheckAndCorrectAppendedText(boolean checkAndCorrectAppendedText) {
        this.checkAndCorrectAppendedText = checkAndCorrectAppendedText;
        return (C) this;
    }
    
    @Override
    public String toString() {
        return "AbstractFormatBuilder{" + "format='" + format + '\'' + ", checkAndCorrectAppendedText=" + checkAndCorrectAppendedText + '}';
    }
    
}
