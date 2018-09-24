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

package de.codemakers.base.util;

import java.util.Objects;

public class ArrayUtil {
    
    /**
     * Searches for an Object
     *
     * @param array Array to be searched
     * @param t Object to search for
     * @param <T> Type of the Array and Object
     *
     * @return <tt>true</tt> if the Array contains the Object
     */
    public static final <T> boolean arrayContains(T[] array, T t) {
        if (array == null || array.length == 0) {
            return false;
        }
        for (T t_ : array) {
            if (Objects.equals(t_, t)) {
                return true;
            }
        }
        return false;
    }
    
    public static byte[] xorBytes(byte[] bytes_1, byte[] bytes_2) {
        return xorBytes(bytes_1, bytes_2, true);
    }
    
    public static byte[] xorBytes(byte[] bytes_1, byte[] bytes_2, boolean takeLarger) {
        if (bytes_1 == null || bytes_2 == null) {
            return null;
        }
        final byte[] bytes = new byte[takeLarger ? Math.max(bytes_1.length, bytes_2.length) : Math.min(bytes_1.length, bytes_2.length)];
        for (int i = 0; i < bytes.length; i++) {
            if (i >= bytes_2.length) {
                bytes[i] = bytes_1[i];
            } else if (i >= bytes_1.length) {
                bytes[i] = bytes_2[i];
            } else {
                bytes[i] = (byte) (bytes_1[i] ^ bytes_2[i]);
            }
        }
        return bytes;
    }
    
}
