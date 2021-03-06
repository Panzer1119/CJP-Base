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

package de.codemakers.lang;

import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.interfaces.Loadable;
import de.codemakers.base.util.interfaces.Unloadable;
import de.codemakers.base.util.tough.ToughSupplier;

import java.util.Locale;

public abstract class Localizer implements Copyable, Loadable, Unloadable {
    
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
    
    protected abstract String getKeyLanguageTag();
    
    protected abstract String getKeyLanguageNameEnglish();
    
    protected abstract String getKeyLanguageNameLocal();
    
    public String getLanguageTag() {
        return localize(getKeyLanguageTag());
    }
    
    public String getLanguageNameEnglish() {
        return localize(getKeyLanguageNameEnglish(), this::getLanguageTag);
    }
    
    public String getLanguageNameLocal() {
        return localize(getKeyLanguageNameLocal(), this::getLanguageNameEnglish);
    }
    
    public Locale getLocale() {
        return Locale.forLanguageTag(getLanguageTag());
    }
    
    public abstract boolean addLocalizer(Localizer localizer);
    
    public boolean removeLocalizer(Localizer localizer) {
        throw new AbstractMethodError();
    }
    
}
