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

package de.codemakers.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    
    public static final String REGEX_ANONYMOUS_CLASS_NAME = "(.+\\.)?(.+)\\$(\\d+)";
    public static final Pattern PATTERN_ANONYMOUS_CLASS_NAME = Pattern.compile(REGEX_ANONYMOUS_CLASS_NAME);
    
    public static final boolean isEmpty(String string) {
        return string.isEmpty();
    }
    
    public static final boolean isNotEmpty(String string) {
        return !string.isEmpty();
    }
    
    public static final int count(String string, String text) {
        if (string == null) {
            return -1;
        }
        if (string.isEmpty() || !string.contains(text)) {
            return 0;
        }
        int count = 0;
        int lastIndex = -text.length();
        while ((lastIndex = string.indexOf(text, lastIndex + text.length())) != -1) {
            count++;
        }
        return count;
    }
    
    public static final String classToSimpleName(Class<?> clazz) {
        if (clazz == null) {
            return "" + null;
        }
        if (clazz.isAnonymousClass()) {
            final Matcher matcher = PATTERN_ANONYMOUS_CLASS_NAME.matcher(clazz.getName());
            if (matcher.matches()) {
                return matcher.group(2);
            }
        }
        return clazz.getSimpleName();
    }
    
}
