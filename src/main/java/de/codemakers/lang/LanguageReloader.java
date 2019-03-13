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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class LanguageReloader implements LanguageReloadable {
    
    protected final List<LanguageReloadable> languageReloadables = new CopyOnWriteArrayList<>();
    protected boolean failOnError = true;
    
    public LanguageReloader() {
    }
    
    public LanguageReloader(Collection<LanguageReloadable> languageReloadables) {
        this.languageReloadables.addAll(languageReloadables);
    }
    
    public List<LanguageReloadable> getLanguageReloadables() {
        return languageReloadables;
    }
    
    public boolean isFailOnError() {
        return failOnError;
    }
    
    public LanguageReloader setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
        return this;
    }
    
    public boolean addLanguageReloadable(LanguageReloadable languageReloadable) {
        Objects.requireNonNull(languageReloadable, "languageReloadable");
        return languageReloadables.add(languageReloadable);
    }
    
    public boolean removeLanguageReloadable(LanguageReloadable languageReloadable) {
        Objects.requireNonNull(languageReloadable, "languageReloadable");
        return languageReloadables.remove(languageReloadable);
    }
    
    @Override
    public boolean reloadLanguage() throws Exception {
        boolean good = true;
        if (failOnError) {
            for (LanguageReloadable languageReloadable : languageReloadables) {
                if (!languageReloadable.reloadLanguage()) {
                    good = false;
                }
            }
        } else {
            for (LanguageReloadable languageReloadable : languageReloadables) {
                if (!languageReloadable.reloadLanguageWithoutException()) {
                    good = false;
                }
            }
        }
        return good;
    }
    
    @Override
    public String toString() {
        return "LanguageReloader{" + "languageReloadables=" + languageReloadables + ", failOnError=" + failOnError + '}';
    }
    
}
