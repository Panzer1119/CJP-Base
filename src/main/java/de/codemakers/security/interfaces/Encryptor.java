/*
 *     Copyright 2018 Paul Hagedorn (Panzer1119)
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

package de.codemakers.security.interfaces;

import de.codemakers.base.logger.Logger;

import java.util.function.Consumer;

public interface Encryptor extends Cryptor {

    byte[] encrypt(byte[] data) throws Exception;
    
    @Override
    default byte[] crypt(byte[] data) throws Exception {
        return encrypt(data);
    }
    
    default byte[] encrypt(byte[] data, Consumer<Throwable> failure) {
        try {
            return encrypt(data);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default byte[] encryptWithoutException(byte[] data) {
        return encrypt(data, null);
    }
    
}
