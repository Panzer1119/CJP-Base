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

import de.codemakers.base.Standard;
import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.io.file.AdvancedFile;

import java.util.Locale;
import java.util.Objects;

public class LanguageUtil {
    
    public static final String LANG_PATH = "lang/";
    public static final AdvancedFile LANG_FOLDER = new AdvancedFile(Standard.MAIN_FOLDER, LANG_PATH);
    public static final String LANG_FILE_EXTENSION = "lang";
    
    public static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
    public static final Locale LOCALE_DEFAULT = Locale.getDefault();
    public static final AdvancedFile LANG_FILE_ENGLISH = getLangFile(LOCALE_ENGLISH.getLanguage());
    public static final AdvancedFile LANG_FILE_DEFAULT = getLangFile(LOCALE_DEFAULT.getLanguage());
    
    private static final LanguageReloader DEFAULT_LANGUAGE_RELOADER = new LanguageReloader();
    private static LanguageReloader LANGUAGE_RELOADER = DEFAULT_LANGUAGE_RELOADER;
    private static final AdvancedLocalizer ENGLISH_LOCALIZER = new AdvancedLocalizer(LANG_FILE_ENGLISH);
    private static final AdvancedLocalizer DEFAULT_LOCALIZER = new AdvancedLocalizer(LANG_FILE_DEFAULT);
    private static Localizer LOCALIZER = DEFAULT_LOCALIZER;
    
    public static LanguageReloader getDefaultLanguageReloader() {
        return DEFAULT_LANGUAGE_RELOADER;
    }
    
    public static <L extends LanguageReloader> L getLanguageReloader() {
        return (L) LANGUAGE_RELOADER;
    }
    
    public static <L extends LanguageReloader> L getLanguageReloader(Class<L> clazz) {
        return (L) LANGUAGE_RELOADER;
    }
    
    public static void setLanguageReloader(LanguageReloader languageReloader) {
        LANGUAGE_RELOADER = Objects.requireNonNull(languageReloader, "languageReloader");
    }
    
    public static boolean reloadLanguage() throws Exception {
        return LANGUAGE_RELOADER.reloadLanguage();
    }
    
    public static boolean reloadLanguage(ToughConsumer<Throwable> failure) {
        return LANGUAGE_RELOADER.reloadLanguage(failure);
    }
    
    public static boolean reloadLanguageWithoutException() {
        return LANGUAGE_RELOADER.reloadLanguageWithoutException();
    }
    
    public static ReturningAction<Boolean> reloadLanguageAction() {
        return LANGUAGE_RELOADER.reloadLanguageAction();
    }
    
    public static boolean unloadLanguage() throws Exception {
        return LANGUAGE_RELOADER.unloadLanguage();
    }
    
    public static boolean unloadLanguage(ToughConsumer<Throwable> failure) {
        return LANGUAGE_RELOADER.unloadLanguage(failure);
    }
    
    public static boolean unloadLanguageWithoutException() {
        return LANGUAGE_RELOADER.unloadLanguageWithoutException();
    }
    
    public static ReturningAction<Boolean> unloadLanguageAction() {
        return LANGUAGE_RELOADER.unloadLanguageAction();
    }
    
    public static AdvancedFile getLangFile(String language) {
        return new AdvancedFile(LANG_FOLDER, language + "." + LANG_FILE_EXTENSION);
    }
    
    public static void initLocalizers() {
        getEnglishLocalizer().load((ex) -> Logger.logError("Failed to load language file for english language \"" + LOCALE_ENGLISH + "\"", ex));
        getDefaultLocalizer().load((ex) -> Logger.logError("Failed to load language file for default language \"" + LOCALE_DEFAULT + "\"", ex));
    }
    
    public static AdvancedLocalizer getEnglishLocalizer() {
        return ENGLISH_LOCALIZER;
    }
    
    public static AdvancedLocalizer getDefaultLocalizer() {
        return DEFAULT_LOCALIZER;
    }
    
    public static <L extends Localizer> L getLocalizer() {
        return (L) LOCALIZER;
    }
    
    public static <L extends Localizer> L getLocalizer(Class<L> clazz) {
        return (L) LOCALIZER;
    }
    
    public static void setLocalizer(Localizer localizer) {
        LOCALIZER = Objects.requireNonNull(localizer, "localizer");
        LANGUAGE_RELOADER.reloadLanguageWithoutException();
    }
    
    public static String localizeWithArguments(String name, String defaultValue, Object... arguments) {
        return LOCALIZER.localizeWithArguments(name, defaultValue, arguments);
    }
    
    public static String localizeWithArguments(String name, Object... arguments) {
        return LOCALIZER.localizeWithArguments(name, () -> DEFAULT_LOCALIZER.localizeWithArguments(name, () -> ENGLISH_LOCALIZER.localizeWithArguments(name, arguments), arguments), arguments);
    }
    
    public static String localize(String name, String defaultValue) {
        return LOCALIZER.localize(name, defaultValue);
    }
    
    public static String localize(String name) {
        return LOCALIZER.localize(name, () -> DEFAULT_LOCALIZER.localize(name, () -> ENGLISH_LOCALIZER.localize(name)));
    }
    
}
