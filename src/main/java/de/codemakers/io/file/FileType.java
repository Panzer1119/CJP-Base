/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
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

package de.codemakers.io.file;

public enum FileType {
    FILE (true, true),
    FILE_ABSOLUTE(true, true),
    FILE_RELATIVE(false, true),
    FILE_INTERN (true, false),
    FILE_INTERN_ABSOLUTE(true, false),
    FILE_INTERN_RELATIVE(false, false),
    CUSTOM(true, true),
    UNKNOWN(false, false);
    
    private final boolean isAbsolute;
    private final boolean isExtern;
    
    FileType(boolean isAbsolute, boolean isExtern) {
        this.isAbsolute = isAbsolute;
        this.isExtern = isExtern;
    }
    
    public final boolean isAbsolute() {
        return isAbsolute;
    }
    
    public final boolean isRelative() {
        return !isAbsolute;
    }
    
    public final boolean isExtern() {
        return isExtern;
    }
    
    public final boolean isIntern() {
        return !isExtern;
    }
    
    public final boolean isCustom() {
        return this == CUSTOM;
    }
    
    public final boolean isUnknown() {
        return this == UNKNOWN;
    }
    
    public static final FileType getFileType(boolean isAbsolute, boolean isExtern) {
        if (isAbsolute) {
            return isExtern ? FILE_ABSOLUTE : FILE_INTERN_ABSOLUTE;
        } else {
            return isExtern ? FILE_RELATIVE : FILE_INTERN_RELATIVE;
        }
    }
    
}
