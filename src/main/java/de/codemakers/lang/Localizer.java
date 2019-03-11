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

package de.codemakers.lang;

public interface Localizer {
    
    String KEY_LANGUAGE_NAME_LOCAL = "language_name_local";
    String KEY_LANGUAGE_NAME_ENGLISH = "language_name_english";
    String KEY_LANGUAGE_CODE = "language_code";
    
    String localize(String name, String defaultValue, Object... arguments);
    
    default String localize(String name, Object... arguments) {
        return localize(name, name, arguments);
    }
    
    String getLanguageNameLocal();
    
    String getLanguageNameEnglish();
    
    String getLanguageCode();
    
}
