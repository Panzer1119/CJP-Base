/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
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

package de.codemakers.i18n;

import de.codemakers.base.Standard;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtil {
    
    private static final String PATH_BASE = (Standard.MAIN_PATH + "i18n/").substring(1);
    private static final String PATH_CONSOLE = PATH_BASE + "console";
    private static final String PATH_LOG_LEVEL = PATH_BASE + "log_level";
    private static final String PATH_UI = PATH_BASE + "ui";
    
    private static ResourceBundle RESOURCE_BUNDLE_CONSOLE;
    private static ResourceBundle RESOURCE_BUNDLE_LOG_LEVEL;
    private static ResourceBundle RESOURCE_BUNDLE_UI;
    
    static {
        loadResourceBundles(Locale.getDefault());
    }
    
    public static void loadResourceBundles(Locale locale) {
        RESOURCE_BUNDLE_CONSOLE = ResourceBundle.getBundle(PATH_CONSOLE, locale);
        RESOURCE_BUNDLE_LOG_LEVEL = ResourceBundle.getBundle(PATH_LOG_LEVEL, locale);
        RESOURCE_BUNDLE_UI = ResourceBundle.getBundle(PATH_UI, locale);
        I18nReloadEventHandler.triggerReload(locale);
    }
    
    public static ResourceBundle getResourceBundleConsole() {
        return RESOURCE_BUNDLE_CONSOLE;
    }
    
    public static ResourceBundle getResourceBundleLogLevel() {
        return RESOURCE_BUNDLE_LOG_LEVEL;
    }
    
    public static ResourceBundle getResourceBundleUi() {
        return RESOURCE_BUNDLE_UI;
    }
    
    public static void addEventListener(I18nReloadEventListener eventListener) {
        I18nReloadEventHandler.addEventListener(eventListener);
    }
    
    public static void removeEventListener(I18nReloadEventListener eventListener) {
        I18nReloadEventHandler.removeEventListener(eventListener);
    }
    
    public static void triggerReload(Locale locale) {
        I18nReloadEventHandler.triggerReload(locale);
    }
    
}
