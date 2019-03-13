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

import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.tough.ToughSupplier;

import java.util.Locale;

public abstract class Localizer implements Copyable {
    
    public static final String KEY_LANGUAGE_NAME_LOCAL = "language_name_local";
    public static final String KEY_LANGUAGE_NAME_ENGLISH = "language_name_english";
    public static final String KEY_LANGUAGE_CODE = "language_code";
    
    public abstract String localizeWithArguments(String name, String defaultValue, Object... arguments);
    
    public abstract String localizeWithArguments(String name, ToughSupplier<String> defaultValueSupplier, Object... arguments);
    
    public String localize(String name, String defaultValue) {
        return localizeWithArguments(name, defaultValue);
    }
    
    public String localize(String name, ToughSupplier<String> defaultValueSupplier) {
        return localizeWithArguments(name, defaultValueSupplier);
    }
    
    public String localizeWithArguments(String name, Object... arguments) {
        return localizeWithArguments(name, name, arguments);
    }
    
    public String localize(String name) {
        return localize(name, name);
    }
    
    public abstract String getLanguageNameLocal();
    
    public abstract String getLanguageNameEnglish();
    
    public abstract String getLanguageCode();
    
    public Locale getLocale() {
        return Locale.forLanguageTag(getLanguageCode());
    }
    
    public abstract Localizer addLocalizer(Localizer localizer);
    
}
