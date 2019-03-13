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
import de.codemakers.lang.AdvancedLocalizer;
import de.codemakers.lang.LanguageReloader;
import de.codemakers.lang.LanguageUtil;
import de.codemakers.lang.Localizer;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
    public static final String ICONS_FAT_COW_PATH = "FatCow/";
    public static final AdvancedFile ICONS_FAT_COW_FOLDER = new AdvancedFile(ICONS_FOLDER, ICONS_FAT_COW_PATH);
    public static final String ICONS_16x16_PATH = "16x16/";
    public static final AdvancedFile ICONS_FAT_COW_16x16_FOLDER = new AdvancedFile(ICONS_FAT_COW_FOLDER, ICONS_16x16_PATH);
    public static final String ICONS_32x32_PATH = "32x32/";
    public static final AdvancedFile ICONS_FAT_COW_32x32_FOLDER = new AdvancedFile(ICONS_FAT_COW_FOLDER, ICONS_32x32_PATH);
    public static final String LANG_PATH = LanguageUtil.LANG_PATH;
    public static final AdvancedFile LANG_FOLDER = LanguageUtil.LANG_FOLDER;
    public static final String LANG_FILE_EXTENSION = LanguageUtil.LANG_FILE_EXTENSION;
    
    public static final Map<Integer, ToughRunnable> SHUTDOWN_HOOKS = new ConcurrentHashMap<>();
    
    static {
        URI RUNNING_JAR_URI_ = null;
        try {
            RUNNING_JAR_URI_ = RUNNING_JAR_URL.toURI();
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        RUNNING_JAR_URI = RUNNING_JAR_URI_;
        LanguageUtil.initLocalizers();
        addShutdownHookAsSingleThread(() -> SHUTDOWN_HOOKS.values().forEach(ToughRunnable::runWithoutException)); //TODO Clone/Copy the values before execution? So that if a Shutdown Hook modifies #SHUTDOWN_HOOKS no unwanted behaviour is happening?
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
    
    public static Thread addShutdownHookAsSingleThread(ToughRunnable toughRunnable) {
        Objects.requireNonNull(toughRunnable);
        final Thread thread = new Thread(toughRunnable::runWithoutException);
        Runtime.getRuntime().addShutdownHook(thread);
        return thread;
    }
    
    public static boolean removeShutdownHook(Thread thread) {
        return Runtime.getRuntime().removeShutdownHook(thread);
    }
    
    public static int addShutdownHook(ToughRunnable toughRunnable) {
        Objects.requireNonNull(toughRunnable, "toughRunnable");
        int id = Integer.MIN_VALUE;
        while (SHUTDOWN_HOOKS.containsKey(id)) {
            id++;
        }
        SHUTDOWN_HOOKS.put(id, toughRunnable);
        return id;
    }
    
    public static ToughRunnable removeShutdownHook(int id) {
        return SHUTDOWN_HOOKS.remove(id);
    }
    
    public static LanguageReloader getDefaultLanguageReloader() {
        return LanguageUtil.getDefaultLanguageReloader();
    }
    
    public static <L extends LanguageReloader> L getLanguageReloader() {
        return LanguageUtil.getLanguageReloader();
    }
    
    public static <L extends LanguageReloader> L getLanguageReloader(Class<L> clazz) {
        return LanguageUtil.getLanguageReloader(clazz);
    }
    
    public static void setLanguageReloader(LanguageReloader languageReloader) {
        LanguageUtil.setLanguageReloader(languageReloader);
    }
    
    public static boolean reloadLanguage() {
        return LanguageUtil.reloadLanguageWithoutException();
    }
    
    public static boolean unloadLanguage() {
        return LanguageUtil.unloadLanguageWithoutException();
    }
    
    public static AdvancedLocalizer getEnglishLocalizer() {
        return LanguageUtil.getEnglishLocalizer();
    }
    
    public static AdvancedLocalizer getDefaultLocalizer() {
        return LanguageUtil.getDefaultLocalizer();
    }
    
    public static <L extends Localizer> L getLocalizer() {
        return LanguageUtil.getLocalizer();
    }
    
    public static <L extends Localizer> L getLocalizer(Class<L> clazz) {
        return LanguageUtil.getLocalizer(clazz);
    }
    
    public static void setLocalizer(Localizer localizer) {
        LanguageUtil.setLocalizer(localizer);
    }
    
    public static String localizeWithArguments(String name, String defaultValue, Object... arguments) {
        return LanguageUtil.localizeWithArguments(name, defaultValue, arguments);
    }
    
    public static String localizeWithArguments(String name, Object... arguments) {
        return LanguageUtil.localizeWithArguments(name, arguments);
    }
    
    public static String localize(String name, String defaultValue) {
        return LanguageUtil.localize(name, defaultValue);
    }
    
    public static String localize(String name) {
        return LanguageUtil.localize(name);
    }
    
    public static <R, T> R useWhenNotNull(T t, ToughFunction<T, R> toughFunction) {
        if (t == null || toughFunction == null) {
            return null;
        }
        return toughFunction.applyWithoutException(t);
    }
    
    public static <T> void useWhenNotNull(T t, ToughConsumer<T> toughConsumer) {
        if (t == null || toughConsumer == null) {
            return;
        }
        toughConsumer.acceptWithoutException(t);
    }
    
    public static Thread toughThread(ToughRunnable toughRunnable) {
        return new Thread(toughRunnable::runWithoutException);
    }
    
}
