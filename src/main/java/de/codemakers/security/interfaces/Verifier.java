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

package de.codemakers.security.interfaces;

import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

@FunctionalInterface
public interface Verifier {
    
    boolean verify(byte[] data, byte[] data_signature) throws Exception;
    
    default boolean verify(byte[] data, byte[] data_signature, ToughConsumer<Throwable> failure) {
        try {
            return verify(data, data_signature);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean verifyWithoutException(byte[] data, byte[] data_signature) {
        return verify(data, data_signature, null);
    }
    
    default ReturningAction<Boolean> verifyAction(byte[] data, byte[] data_signature) {
        return new ReturningAction<>(() -> verify(data, data_signature));
    }
    
}
