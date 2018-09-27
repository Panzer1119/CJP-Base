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

package de.codemakers.base.util.interfaces;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.io.SerializationUtil;

import java.io.Serializable;
import java.util.Base64;

public interface ByteSerializable extends Serializable {
    
    byte NOT_NULL = 64;
    byte NULL = -64;
    
    default byte[] toBytes() throws Exception {
        return SerializationUtil.objectToBytes(this);
    }
    
    default byte[] toBytes(ToughConsumer<Throwable> failure) {
        try {
            return toBytes();
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default byte[] toBytesWithoutException() {
        return toBytes(null);
    }
    
    default String toBytesASBase64String() {
        final byte[] bytes = toBytesWithoutException();
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }
    
    default boolean fromBytes(byte[] bytes) throws Exception {
        if (bytes != null && this instanceof Copyable) {
            final Copyable copyable = Require.clazz(SerializationUtil.bytesToObject(bytes), Copyable.class);
            if (copyable != null) {
                ((Copyable) this).set(copyable);
                return true;
            }
        }
        return false;
    }
    
    default boolean fromBytes(byte[] bytes, ToughConsumer<Throwable> failure) {
        try {
            return fromBytes(bytes);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean fromBytesWithoutException(byte[] bytes) {
        return fromBytes(bytes, null);
    }
    
    default boolean fromBytesAsBase64String(String bytes) {
        return fromBytesWithoutException(bytes == null ? null : Base64.getDecoder().decode(bytes));
    }
    
    default byte resolveNull(Object object) {
        return object == null ? NULL : NOT_NULL;
    }
    
    default boolean isNull(byte b) {
        return b == NULL;
    }
    
    default boolean isNotNull(byte b) {
        return b == NOT_NULL;
    }
    
    default <T> int arrayLength(T[] array) {
        return array == null ? -1 : array.length;
    }
    
    default int arrayLength(byte[] array) {
        return array == null ? -1 : array.length;
    }
    
}
