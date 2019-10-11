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

package de.codemakers.base.util;

import com.google.gson.JsonObject;
import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.io.file.AdvancedFile;

import java.util.ArrayList;
import java.util.List;

public class JsonSettingsTest {
    
    public static void main(String[] args) {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINE);
        final AdvancedFile advancedFile = new AdvancedFile(args[0]);
        Logger.logDebug("advancedFile=" + advancedFile);
        Logger.logDebug("advancedFile.exists()=" + advancedFile.exists());
        final JsonSettings jsonSettings_1 = new JsonSettings(advancedFile);
        Logger.logDebug("jsonSettings_1=" + jsonSettings_1);
        jsonSettings_1.setProperty("debug", true);
        jsonSettings_1.setProperty("port", 1234);
        jsonSettings_1.setProperty("inetAddress", "127.0.0.1");
        Logger.logDebug("jsonSettings_1=" + jsonSettings_1);
        Logger.logDebug("jsonSettings_1.saveSettings()=" + jsonSettings_1.saveSettings());
        Logger.logDebug("jsonSettings_1=" + jsonSettings_1);
        final JsonSettings jsonSettings_1_1 = jsonSettings_1.getOrCreateSubSettings("subSettings_1");
        Logger.logDebug("jsonSettings_1_1=" + jsonSettings_1_1);
        jsonSettings_1_1.setProperty("subKey", "subValue");
        Logger.logDebug("jsonSettings_1_1=" + jsonSettings_1_1);
        Logger.logDebug("jsonSettings_1_1.saveSettings()=" + jsonSettings_1_1.saveSettings());
        Logger.logDebug("jsonSettings_1_1=" + jsonSettings_1_1);
        Logger.logDebug("jsonSettings_1=" + jsonSettings_1);
        //
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arguments.add("argument_" + i);
        }
        Logger.logDebug("arguments=" + arguments);
        jsonSettings_1.setPropertyList("arguments", arguments);
        Logger.logDebug("jsonSettings_1=" + jsonSettings_1);
        jsonSettings_1.saveSettings();
        arguments = jsonSettings_1.getPropertyListStrings("arguments");
        Logger.logDebug("arguments=" + arguments);
        //
        List<JsonObject> objects_1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", "object_" + i);
            jsonObject.addProperty("random", Math.random());
            objects_1.add(jsonObject);
        }
        Logger.logDebug("objects_1=" + objects_1);
        jsonSettings_1.setPropertyList("objects_1", objects_1);
        Logger.logDebug("jsonSettings_1=" + jsonSettings_1);
        jsonSettings_1.saveSettings();
        objects_1 = jsonSettings_1.getPropertyListJsonObjects("objects_1");
        Logger.logDebug("objects_1=" + objects_1);
    }
    
}
