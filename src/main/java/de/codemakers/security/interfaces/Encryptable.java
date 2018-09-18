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

import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

@FunctionalInterface
public interface Encryptable extends Cryptable {
    
    byte[] encrypt(Encryptor encryptor) throws Exception;
    
    @Override
    default byte[] crypt(Cryptor cryptor) throws Exception {
        return encrypt((Encryptor) cryptor);
    }
    
    default byte[] encrypt(Encryptor encryptor, ToughConsumer<Throwable> failure) {
        try {
            return encrypt(encryptor);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default byte[] encryptWithoutException(Encryptor encryptor) {
        return encrypt(encryptor, null);
    }
    
    default ReturningAction<byte[]> encryptAction(Encryptor encryptor) {
        return new ReturningAction<>(() -> encrypt(encryptor));
    }
    
}
