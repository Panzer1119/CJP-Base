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

package de.codemakers.base;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.os.OSUtil;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughFunction;
import de.codemakers.base.util.tough.ToughRunnable;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.lang.Localizer;
import de.codemakers.lang.PropertiesLocalizer;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class Standard {
    
    public static final String NAME = Standard.class.getName();
    public static final PrintStream SYSTEM_OUTPUT_STREAM = System.out;
    public static final PrintStream SYSTEM_ERROR_STREAM = System.err;
    public static final InputStream SYSTEM_INPUT_STREAM = System.in;
    
    public static final URL RUNNING_JAR_URL = Standard.class.getProtectionDomain().getCodeSource().getLocation();
    public static final URI RUNNING_JAR_URI;
    public static final String RUNNING_JAR_PATH_STRING = RUNNING_JAR_URL.getPath();
    public static final boolean RUNNING_JAR_IS_JAR = RUNNING_JAR_URL.getPath().toLowerCase().endsWith(".jar");
    public static final File RUNNING_JAR_FILE = new File(RUNNING_JAR_URL.getPath());
    public static final AdvancedFile RUNNING_JAR_ADVANCED_FILE = new AdvancedFile(RUNNING_JAR_URL.getPath());
    
    public static final String MAIN_PATH = "/de/codemakers/";
    public static final AdvancedFile MAIN_FOLDER = new AdvancedFile(AdvancedFile.PREFIX_INTERN + MAIN_PATH);
    public static final String ICONS_PATH = "icons/";
    public static final AdvancedFile ICONS_FOLDER = new AdvancedFile(MAIN_FOLDER, ICONS_PATH);
    public static final String LANG_PATH = "lang/";
    public static final AdvancedFile LANG_FOLDER = new AdvancedFile(MAIN_FOLDER, LANG_PATH);
    public static final String LANG_FILE_EXTENSION = "lang";
    
    static {
        URI RUNNING_JAR_URI_ = null;
        try {
            RUNNING_JAR_URI_ = RUNNING_JAR_URL.toURI();
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        RUNNING_JAR_URI = RUNNING_JAR_URI_;
        try {
            ((PropertiesLocalizer) Localizer.DEFAULT_LOCALIZER).loadFromFile(new AdvancedFile(LANG_FOLDER, Locale.getDefault().getLanguage() + "." + LANG_FILE_EXTENSION));
        } catch (Exception ex) {
            Logger.logError("Failed to load language file for local language \"" + Locale.getDefault() + "\"", ex);
        }
        try {
            ((PropertiesLocalizer) Localizer.ENGLISH_LOCALIZER).loadFromFile(new AdvancedFile(LANG_FOLDER, Locale.ENGLISH.getLanguage() + "." + LANG_FILE_EXTENSION));
        } catch (Exception ex) {
            Logger.logError("Failed to load language file for english", ex);
        }
    }
    
    public static final File getInternFileFromAbsolutePath(String path) {
        Objects.requireNonNull(path);
        return new File(RUNNING_JAR_PATH_STRING + path);
    }
    
    public static final File getInternFileFromRelative(Class<?> clazz, String path) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(path);
        return new File(RUNNING_JAR_PATH_STRING + File.separator + clazz.getPackage().getName().replaceAll("\\.", OSUtil.CURRENT_OS_HELPER.getFileSeparatorRegex()) + File.separator + path);
    }
    
    public static final void async(ToughRunnable toughRunnable) {
        new Thread(toughRunnable::runWithoutException).start();
    }
    
    public static final Throwable silentError(ToughRunnable toughRunnable) {
        try {
            toughRunnable.run();
            return null;
        } catch (Exception ex) {
            return ex;
        }
    }
    
    public static final <T> void silentError(ToughConsumer<T> toughConsumer, T input) {
        try {
            toughConsumer.accept(input);
        } catch (Exception ex) {
        }
    }
    
    public static final <T, R> R silentError(ToughFunction<T, R> toughFunction, T input) {
        try {
            return toughFunction.apply(input);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static final <R> R silentError(ToughSupplier<R> toughSupplier) {
        try {
            return toughSupplier.get();
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static <L extends Localizer> L getDefaultLocalizer() {
        return (L) Localizer.DEFAULT_LOCALIZER;
    }
    
    public static <L extends Localizer> L getEnglishLocalizer() {
        return (L) Localizer.ENGLISH_LOCALIZER;
    }
    
    public static String localizeWithArguments(String name, String defaultValue, Object... arguments) {
        return Localizer.DEFAULT_LOCALIZER.localizeWithArguments(name, defaultValue, arguments);
    }
    
    public static String localize(String name, String defaultValue) {
        return Localizer.DEFAULT_LOCALIZER.localize(name, defaultValue);
    }
    
    public static String localizeWithArguments(String name, Object... arguments) {
        return Localizer.DEFAULT_LOCALIZER.localizeWithArguments(name, () -> Localizer.ENGLISH_LOCALIZER.localizeWithArguments(name, arguments), arguments);
    }
    
    public static String localize(String name) {
        return Localizer.DEFAULT_LOCALIZER.localize(name, () -> Localizer.ENGLISH_LOCALIZER.localize(name));
    }
    
}
