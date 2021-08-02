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

package de.codemakers.lang;

import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.file.AdvancedFile;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class AdvancedLocalizer extends Localizer {
    
    protected final FileLocalizer fileLocalizer;
    protected final List<Localizer> localizers = new CopyOnWriteArrayList<>();
    
    public AdvancedLocalizer() {
        this(new PropertiesLocalizer());
    }
    
    public AdvancedLocalizer(AdvancedFile advancedFile) {
        this(new PropertiesLocalizer(advancedFile));
    }
    
    protected AdvancedLocalizer(FileLocalizer fileLocalizer) {
        this.fileLocalizer = Objects.requireNonNull(fileLocalizer, "fileLocalizer");
    }
    
    public AdvancedFile getAdvancedFile() {
        return fileLocalizer.getAdvancedFile();
    }
    
    public AdvancedLocalizer setAdvancedFile(AdvancedFile advancedFile) {
        fileLocalizer.setAdvancedFile(advancedFile);
        return this;
    }
    
    public FileLocalizer getFileLocalizer() {
        return fileLocalizer;
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
        return fileLocalizer.localizeWithArguments(name, defaultValue, arguments);
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
        return fileLocalizer.localizeWithArguments(name, defaultValueSupplier, arguments);
    }
    
    @Override
    public String getKeyLanguageTag() {
        return fileLocalizer.getKeyLanguageTag();
    }
    
    @Override
    public String getKeyLanguageNameEnglish() {
        return fileLocalizer.getKeyLanguageNameEnglish();
    }
    
    @Override
    public String getKeyLanguageNameLocal() {
        return fileLocalizer.getKeyLanguageNameLocal();
    }
    
    @Override
    public String getLanguageTag() {
        return fileLocalizer.getLanguageTag();
    }
    
    @Override
    public String getLanguageNameEnglish() {
        return fileLocalizer.getLanguageNameEnglish();
    }
    
    @Override
    public String getLanguageNameLocal() {
        return fileLocalizer.getLanguageNameLocal();
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
        final AdvancedLocalizer advancedLocalizer = new AdvancedLocalizer((FileLocalizer) fileLocalizer.copy());
        advancedLocalizer.localizers.addAll(localizers);
        return advancedLocalizer;
    }
    
    @Override
    public void set(Copyable copyable) {
        final AdvancedLocalizer advancedLocalizer = Require.clazz(copyable, AdvancedLocalizer.class);
        if (advancedLocalizer != null) {
            setAdvancedFile(advancedLocalizer.getAdvancedFile());
            fileLocalizer.unloadWithoutException();
            fileLocalizer.set(advancedLocalizer.getFileLocalizer());
            localizers.clear();
            localizers.addAll(advancedLocalizer.localizers);
        }
    }
    
    @Override
    public boolean load() throws Exception {
        boolean good = true;
        fileLocalizer.unload();
        if (!fileLocalizer.load()) {
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
        if (!fileLocalizer.unload()) {
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
        return "AdvancedLocalizer{" + "fileLocalizer=" + fileLocalizer + ", localizers=" + localizers + '}';
    }
    
}
