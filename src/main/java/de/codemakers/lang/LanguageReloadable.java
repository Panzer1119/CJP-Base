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

import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.interfaces.Reloadable;
import de.codemakers.base.util.tough.ToughConsumer;

public interface LanguageReloadable extends Reloadable {
    
    default boolean reload() throws Exception {
        return reloadLanguage();
    }
    
    boolean reloadLanguage() throws Exception;
    
    default boolean reloadLanguage(ToughConsumer<Throwable> failure) {
        try {
            return reloadLanguage();
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean reloadLanguageWithoutException() {
        return reloadLanguage(null);
    }
    
    default ReturningAction<Boolean> reloadLanguageAction() {
        return new ReturningAction<>(this::reloadLanguage);
    }
    
    boolean unloadLanguage() throws Exception;
    
    default boolean unloadLanguage(ToughConsumer<Throwable> failure) {
        try {
            return unloadLanguage();
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean unloadLanguageWithoutException() {
        return unloadLanguage(null);
    }
    
    default ReturningAction<Boolean> unloadLanguageAction() {
        return new ReturningAction<>(this::unloadLanguage);
    }
    
}
