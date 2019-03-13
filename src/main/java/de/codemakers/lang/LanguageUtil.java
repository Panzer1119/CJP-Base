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

import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.util.tough.ToughConsumer;

public class LanguageUtil {
    
    public static final LanguageReloader DEFAULT_LANGUAGE_RELOADER = new LanguageReloader();
    
    public static boolean addLanguageReloadable(LanguageReloadable languageReloadable) {
        return DEFAULT_LANGUAGE_RELOADER.addLanguageReloadable(languageReloadable);
    }
    
    public static boolean removeLanguageReloadable(LanguageReloadable languageReloadable) {
        return DEFAULT_LANGUAGE_RELOADER.removeLanguageReloadable(languageReloadable);
    }
    
    public static boolean reloadLanguage() throws Exception {
        return DEFAULT_LANGUAGE_RELOADER.reloadLanguage();
    }
    
    public static boolean reloadLanguage(ToughConsumer<Throwable> failure) {
        return DEFAULT_LANGUAGE_RELOADER.reloadLanguage(failure);
    }
    
    public static boolean reloadLanguageWithoutException() {
        return DEFAULT_LANGUAGE_RELOADER.reloadLanguageWithoutException();
    }
    
    public static ReturningAction<Boolean> reloadLanguageAction() {
        return DEFAULT_LANGUAGE_RELOADER.reloadLanguageAction();
    }
    
    public static boolean unloadLanguage() throws Exception {
        return DEFAULT_LANGUAGE_RELOADER.unloadLanguage();
    }
    
    public static boolean unloadLanguage(ToughConsumer<Throwable> failure) {
        return DEFAULT_LANGUAGE_RELOADER.unloadLanguage(failure);
    }
    
    public static boolean unloadLanguageWithoutException() {
        return DEFAULT_LANGUAGE_RELOADER.unloadLanguageWithoutException();
    }
    
    public static ReturningAction<Boolean> unloadLanguageAction() {
        return DEFAULT_LANGUAGE_RELOADER.unloadLanguageAction();
    }
    
}
