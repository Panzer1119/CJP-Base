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

import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.file.AdvancedFile;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class AdvancedLocalizer extends Localizer {
    
    protected final AdvancedFile advancedFile;
    protected final PropertiesLocalizer propertiesLocalizer;
    protected final List<Localizer> localizers = new CopyOnWriteArrayList<>();
    
    public AdvancedLocalizer(AdvancedFile advancedFile) {
        this(advancedFile, new PropertiesLocalizer());
    }
    
    protected AdvancedLocalizer(AdvancedFile advancedFile, PropertiesLocalizer propertiesLocalizer) {
        this.advancedFile = advancedFile;
        this.propertiesLocalizer = propertiesLocalizer;
    }
    
    public AdvancedFile getAdvancedFile() {
        return advancedFile;
    }
    
    public PropertiesLocalizer getPropertiesLocalizer() {
        return propertiesLocalizer;
    }
    
    public List<Localizer> getLocalizers() {
        return localizers;
    }
    
    @Override
    public String localizeWithArguments(String name, String defaultValue, Object... arguments) {
        if (!localizers.isEmpty()) {
            String temp = null;
            for (Localizer localizer : localizers) {
                temp = localizer.localizeWithArguments(name, (String) null, arguments);
                if (temp != null) {
                    return temp;
                }
            }
        }
        return propertiesLocalizer.localizeWithArguments(name, defaultValue, arguments);
    }
    
    @Override
    public String localizeWithArguments(String name, ToughSupplier<String> defaultValueSupplier, Object... arguments) {
        if (!localizers.isEmpty()) {
            String temp = null;
            for (Localizer localizer : localizers) {
                temp = localizer.localizeWithArguments(name, (ToughSupplier<String>) null, arguments);
                if (temp != null) {
                    return temp;
                }
            }
        }
        return propertiesLocalizer.localizeWithArguments(name, defaultValueSupplier, arguments);
    }
    
    @Override
    public String getLanguageNameLocal() {
        return propertiesLocalizer.getLanguageNameLocal();
    }
    
    @Override
    public String getLanguageNameEnglish() {
        return propertiesLocalizer.getLanguageNameEnglish();
    }
    
    @Override
    public String getLanguageCode() {
        return propertiesLocalizer.getLanguageCode();
    }
    
    @Override
    public boolean addLocalizer(Localizer localizer) {
        return localizers.add(Objects.requireNonNull(localizer, "localizer"));
    }
    
    public AdvancedLocalizer addLocalizer(Localizer localizer, int index) {
        localizers.add(index, Objects.requireNonNull(localizer, "localizer"));
        return this;
    }
    
    @Override
    public boolean removeLocalizer(Localizer localizer) {
        return localizers.remove(Objects.requireNonNull(localizer, "localizer"));
    }
    
    @Override
    public AdvancedLocalizer copy() {
        final AdvancedLocalizer advancedLocalizer = new AdvancedLocalizer(advancedFile.copy(), propertiesLocalizer.copy());
        advancedLocalizer.localizers.addAll(localizers);
        return advancedLocalizer;
    }
    
    @Override
    public void set(Copyable copyable) {
        final AdvancedLocalizer advancedLocalizer = Require.clazz(copyable, AdvancedLocalizer.class);
        if (advancedLocalizer != null) {
            //AdvancedFile is a final value...
            propertiesLocalizer.clear();
            propertiesLocalizer.set(advancedLocalizer.propertiesLocalizer);
            localizers.clear();
            localizers.addAll(advancedLocalizer.localizers);
        }
    }
    
    @Override
    public boolean load() throws Exception {
        boolean good = true;
        if (!propertiesLocalizer.loadFromFile(advancedFile)) {
            good = false;
        }
        for (Localizer localizer : localizers) {
            if (!localizer.load()) {
                good = false;
            }
        }
        return good;
    }
    
    @Override
    public boolean unload() throws Exception {
        boolean good = true;
        if (!propertiesLocalizer.unload()) {
            good = false;
        }
        for (Localizer localizer : localizers) {
            if (!localizer.unload()) {
                good = false;
            }
        }
        return good;
    }
    
    @Override
    public String toString() {
        return "AdvancedLocalizer{" + "advancedFile=" + advancedFile + ", propertiesLocalizer=" + propertiesLocalizer + ", localizers=" + localizers + '}';
    }
    
}
