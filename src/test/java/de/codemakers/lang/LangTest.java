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
import de.codemakers.base.logger.Logger;

import java.util.Locale;

public class LangTest {
    
    public static final void main(String[] args) throws Exception {
        if (false) {
            test();
            return;
        }
        final Locale locale = Locale.getDefault();
        Logger.log("locale=" + locale);
        Logger.log("locale.getCountry()=" + locale.getCountry());
        Logger.log("locale.getDisplayCountry()=" + locale.getDisplayCountry());
        Logger.log("locale.getDisplayLanguage()=" + locale.getDisplayLanguage());
        Logger.log("locale.getDisplayName()=" + locale.getDisplayName());
        Logger.log("locale.getDisplayScript()=" + locale.getDisplayScript());
        Logger.log("locale.getDisplayVariant()=" + locale.getDisplayVariant());
        Logger.log("locale.getISO3Country()=" + locale.getISO3Country());
        Logger.log("locale.getISO3Language()=" + locale.getISO3Language());
        Logger.log("locale.getLanguage()=" + locale.getLanguage());
        Logger.log("locale.getVariant()=" + locale.getVariant());
        Logger.log("locale.toLanguageTag()=" + locale.toLanguageTag());
        Logger.log(Locale.forLanguageTag("en"));
        final AdvancedLocalizer advancedLocalizer = LanguageUtil.getLocalizerDefault();
        Logger.log("advancedLocalizer=" + advancedLocalizer);
        Logger.log("advancedLocalizer.localize(Localizer.KEY_LANGUAGE_NAME_ENGLISH)=" + advancedLocalizer.localize(Localizer.KEY_LANGUAGE_NAME_ENGLISH));
        Logger.log("Standard.localize(\"test_123\")=" + Standard.localize("test_123"));
        Logger.log("Standard.localize(\"test_123\", null)=" + Standard.localize("test_123", null));
        Logger.log("Standard.localize(\"test_123\")=" + Standard.localize("test_1234"));
        //test();
    }
    
    private static void test() {
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Logger.log("locale=" + locale);
                Logger.log("locale.getCountry()=" + locale.getCountry());
                Logger.log("locale.getDisplayCountry()=" + locale.getDisplayCountry());
                Logger.log("locale.getDisplayLanguage()=" + locale.getDisplayLanguage());
                Logger.log("locale.getDisplayName()=" + locale.getDisplayName());
                Logger.log("locale.getDisplayScript()=" + locale.getDisplayScript());
                Logger.log("locale.getDisplayVariant()=" + locale.getDisplayVariant());
                Logger.log("locale.getISO3Country()=" + locale.getISO3Country());
                Logger.log("locale.getISO3Language()=" + locale.getISO3Language());
                Logger.log("locale.getLanguage()=" + locale.getLanguage());
                Logger.log("locale.getVariant()=" + locale.getVariant());
                Logger.log("locale.toLanguageTag()=" + locale.toLanguageTag());
                Logger.log("\n\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
