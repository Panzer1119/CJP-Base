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
    
    // // Final values
    // Paths etc.
    public static final String LANG_PATH = "lang/";
    public static final AdvancedFile LANG_FOLDER = new AdvancedFile(Standard.MAIN_FOLDER, LANG_PATH);
    public static final String LANG_FILE_EXTENSION = "lang";
    
    // Language + Region
    public static final Locale LOCALE_ENGLISH_US = Locale.US;
    public static final Locale LOCALE_ENGLISH_UK = Locale.UK;
    public static final Locale LOCALE_GERMAN_DE = Locale.GERMANY;
    public static final Locale LOCALE_DEFAULT = Locale.getDefault();
    // Language
    public static final Locale LOCALE_LANGUAGE_ENGLISH = Locale.ENGLISH;
    public static final Locale LOCALE_LANGUAGE_GERMAN = Locale.GERMAN;
    public static final Locale LOCALE_LANGUAGE_DEFAULT = Locale.forLanguageTag(LOCALE_DEFAULT.getLanguage());
    // Files
    public static final AdvancedFile FILE_ENGLISH_US = getFileByLocale(LOCALE_ENGLISH_US);
    public static final AdvancedFile FILE_ENGLISH_UK = getFileByLocale(LOCALE_ENGLISH_UK);
    public static final AdvancedFile FILE_GERMAN_DE = getFileByLocale(LOCALE_GERMAN_DE);
    public static final AdvancedFile FILE_DEFAULT = getFileByLocale(LOCALE_DEFAULT);
    
    // Templates etc.
    public static final String TEMPLATE_FAILED_TO_LOAD_LOCALIZER = "Failed to load language file for %s: \"%s\"";
    
    // // Instances
    // LanguageReloader
    private static final LanguageReloader DEFAULT_LANGUAGE_RELOADER = new LanguageReloader();
    private static LanguageReloader LANGUAGE_RELOADER = DEFAULT_LANGUAGE_RELOADER;
    // AdvancedLocalizer
    private static final AdvancedLocalizer LOCALIZER_ENGLISH_US = new AdvancedLocalizer(FILE_ENGLISH_US);
    private static final AdvancedLocalizer LOCALIZER_ENGLISH_UK = new AdvancedLocalizer(FILE_ENGLISH_UK);
    private static final AdvancedLocalizer LOCALIZER_GERMAN_DE = new AdvancedLocalizer(FILE_GERMAN_DE);
    private static final AdvancedLocalizer LOCALIZER_DEFAULT = new AdvancedLocalizer(FILE_DEFAULT);
    private static Localizer LOCALIZER = LOCALIZER_DEFAULT;
    
    public static Locale getLocaleEnglishUs() {
        return LOCALE_ENGLISH_US;
    }
    
    public static Locale getLocaleEnglishUk() {
        return LOCALE_ENGLISH_UK;
    }
    
    public static Locale getLocaleGermanDe() {
        return LOCALE_GERMAN_DE;
    }
    
    public static Locale getLocaleDefault() {
        return LOCALE_DEFAULT;
    }
    
    public static Locale getLocaleLanguageEnglish() {
        return LOCALE_LANGUAGE_ENGLISH;
    }
    
    public static Locale getLocaleLanguageGerman() {
        return LOCALE_LANGUAGE_GERMAN;
    }
    
    public static Locale getLocaleLanguageDefault() {
        return LOCALE_LANGUAGE_DEFAULT;
    }
    
    public static AdvancedFile getFileEnglishUs() {
        return FILE_ENGLISH_US;
    }
    
    public static AdvancedFile getFileEnglishUk() {
        return FILE_ENGLISH_UK;
    }
    
    public static AdvancedFile getFileGermanDe() {
        return FILE_GERMAN_DE;
    }
    
    public static AdvancedFile getFileDefault() {
        return FILE_DEFAULT;
    }
    
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
    
    public static AdvancedFile getFileByLocale(Locale locale) {
        return getFileByLocale(LANG_FOLDER, locale);
    }
    
    public static AdvancedFile getFileByLocale(AdvancedFile parent, Locale locale) {
        return new AdvancedFile(parent, getFileNameByLocale(locale));
    }
    
    public static String getFileNameByLocale(Locale locale) {
        return locale.toLanguageTag() + "." + LANG_FILE_EXTENSION;
    }
    
    public static void initLocalizers() {
        getLocalizerEnglishUs().load((ex) -> Logger.logError(String.format(TEMPLATE_FAILED_TO_LOAD_LOCALIZER, LOCALE_ENGLISH_US.toLanguageTag(), FILE_ENGLISH_US.getAbsolutePath()), ex));
        getLocalizerEnglishUk().load((ex) -> Logger.logError(String.format(TEMPLATE_FAILED_TO_LOAD_LOCALIZER, LOCALE_ENGLISH_UK.toLanguageTag(), FILE_ENGLISH_UK.getAbsolutePath()), ex));
        getLocalizerGermanDe().load((ex) -> Logger.logError(String.format(TEMPLATE_FAILED_TO_LOAD_LOCALIZER, LOCALE_GERMAN_DE.toLanguageTag(), FILE_GERMAN_DE.getAbsolutePath()), ex));
        getLocalizerDefault().load((ex) -> Logger.logError(String.format(TEMPLATE_FAILED_TO_LOAD_LOCALIZER, LOCALE_DEFAULT.toLanguageTag(), FILE_DEFAULT.getAbsolutePath()), ex));
    }
    
    public static AdvancedLocalizer getLocalizerEnglishUs() {
        return LOCALIZER_ENGLISH_US;
    }
    
    public static AdvancedLocalizer getLocalizerEnglishUk() {
        return LOCALIZER_ENGLISH_UK;
    }
    
    public static AdvancedLocalizer getLocalizerGermanDe() {
        return LOCALIZER_GERMAN_DE;
    }
    
    public static AdvancedLocalizer getLocalizerDefault() {
        return LOCALIZER_DEFAULT;
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
        return LOCALIZER.localizeWithArguments(name, () -> LOCALIZER_DEFAULT.localizeWithArguments(name, () -> LOCALIZER_ENGLISH_US.localizeWithArguments(name, arguments), arguments), arguments);
    }
    
    public static String localize(String name, String defaultValue) {
        return LOCALIZER.localize(name, defaultValue);
    }
    
    public static String localize(String name) {
        return LOCALIZER.localize(name, () -> LOCALIZER_DEFAULT.localize(name, () -> LOCALIZER_ENGLISH_US.localize(name)));
    }
    
}
