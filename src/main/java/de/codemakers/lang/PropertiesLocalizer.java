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

import de.codemakers.base.logger.Logger;
import de.codemakers.io.file.AdvancedFile;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesLocalizer implements Localizer {
    
    private final Properties properties = new Properties();
    
    public PropertiesLocalizer() {
    }
    
    public PropertiesLocalizer(AdvancedFile advancedFile) {
        loadFromFile(advancedFile);
    }
    
    public PropertiesLocalizer loadFromFile(AdvancedFile advancedFile) {
        Objects.requireNonNull(advancedFile);
        properties.clear();
        try (final InputStream inputStream = advancedFile.createInputStream()) {
            properties.load(inputStream);
        } catch (Exception ex) {
            Logger.handleError(ex, "tried loading " + PropertiesLocalizer.class.getSimpleName() + " from \"" + advancedFile + "\"");
        }
        return this;
    }
    
    public PropertiesLocalizer clear() {
        properties.clear();
        return this;
    }
    
    public Properties getProperties() {
        return properties;
    }
    
    @Override
    public String localize(String name, String defaultValue, Object... arguments) {
        return properties.getProperty(name, defaultValue);
    }
    
    @Override
    public String getLanguageNameLocal() {
        return properties.getProperty(KEY_LANGUAGE_NAME_LOCAL, getLanguageNameEnglish());
    }
    
    @Override
    public String getLanguageNameEnglish() {
        return properties.getProperty(KEY_LANGUAGE_NAME_ENGLISH, getLanguageCode());
    }
    
    @Override
    public String getLanguageCode() {
        return properties.getProperty(KEY_LANGUAGE_CODE);
    }
    
    @Override
    public String toString() {
        return "PropertiesLocalizer{" + "properties=" + properties + '}';
    }
    
}
