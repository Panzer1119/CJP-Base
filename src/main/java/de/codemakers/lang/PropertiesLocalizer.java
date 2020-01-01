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

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.file.exceptions.isnot.FileIsNotExistingException;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesLocalizer extends Localizer {
    
    private final Properties properties;
    
    public PropertiesLocalizer() {
        this(new Properties());
    }
    
    public PropertiesLocalizer(Properties properties) {
        this.properties = Objects.requireNonNull(properties, "properties");
    }
    
    public PropertiesLocalizer(AdvancedFile advancedFile) {
        this();
        loadFromFileWithoutException(advancedFile);
    }
    
    public boolean loadFromFileWithoutException(AdvancedFile advancedFile) {
        return loadFromFile(advancedFile, null);
    }
    
    public boolean loadFromFile(AdvancedFile advancedFile, ToughConsumer<Throwable> failure) {
        try {
            return loadFromFile(advancedFile);
        } catch (Exception ex) {
            if (failure == null) {
                Logger.handleError(ex);
            } else {
                failure.acceptWithoutException(ex);
            }
            return false;
        }
    }
    
    public boolean loadFromFile(AdvancedFile advancedFile) throws Exception {
        Objects.requireNonNull(advancedFile);
        if (!advancedFile.exists()) {
            throw new FileIsNotExistingException(advancedFile.getAbsolutePath());
        }
        try (final InputStream inputStream = advancedFile.createInputStream()) {
            properties.load(inputStream);
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public PropertiesLocalizer clear() {
        properties.clear();
        return this;
    }
    
    public Properties getProperties() {
        return properties;
    }
    
    @Override
    public String localizeWithArguments(String name, String defaultValue, Object... arguments) {
        return properties.getProperty(name, defaultValue);
    }
    
    @Override
    public String localizeWithArguments(String name, ToughSupplier<String> defaultValueSupplier, Object... arguments) {
        final String temp = properties.getProperty(name);
        return (temp != null || defaultValueSupplier == null) ? temp : defaultValueSupplier.getWithoutException();
    }
    
    @Override
    public String getLanguageNameLocal() {
        return properties.getProperty(KEY_LANGUAGE_NAME_LOCAL, getLanguageNameEnglish());
    }
    
    @Override
    public String getLanguageNameEnglish() {
        return properties.getProperty(KEY_LANGUAGE_NAME_ENGLISH, getLanguageTag());
    }
    
    @Override
    public String getLanguageTag() {
        return properties.getProperty(KEY_LANGUAGE_TAG);
    }
    
    @Override
    public boolean addLocalizer(Localizer localizer) {
        final PropertiesLocalizer propertiesLocalizer = Require.clazz(localizer, PropertiesLocalizer.class, "localizer is not an instance of " + PropertiesLocalizer.class.getSimpleName());
        properties.putAll(propertiesLocalizer.properties);
        return true;
    }
    
    @Override
    public PropertiesLocalizer copy() {
        return new PropertiesLocalizer((Properties) properties.clone());
    }
    
    @Override
    public void set(Copyable copyable) {
        final PropertiesLocalizer propertiesLocalizer = Require.clazz(copyable, PropertiesLocalizer.class);
        if (propertiesLocalizer != null) {
            properties.clear();
            properties.putAll(propertiesLocalizer.properties);
        }
    }
    
    @Override
    public boolean load() throws Exception {
        return false;
    }
    
    @Override
    public boolean unload() throws Exception {
        clear();
        return properties.isEmpty();
    }
    
    @Override
    public String toString() {
        return "PropertiesLocalizer{" + "properties=" + properties + '}';
    }
    
}
