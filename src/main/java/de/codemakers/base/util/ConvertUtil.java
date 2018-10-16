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

public class ConvertUtil {
    
    public static byte[] longToByteArray(long value) {
        return new byte[] {(byte) (value >> 56), (byte) (value >> 48), (byte) (value >> 40), (byte) (value >> 32), (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
    }
    
    public static long byteArrayToLong(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != 8) {
            throw new IllegalArgumentException("A Long has 64 bits, which means 8 bytes, not " + array.length + " bytes");
        }
        return ((long) array[0] << 56) | ((long) (array[1] & 0xFF) << 48) | ((long) (array[2] & 0xFF) << 40) | ((long) (array[3] & 0xFF) << 32) | ((long) (array[4] & 0xFF) << 24) | ((long) (array[5] & 0xFF) << 16) | ((long) (array[6] & 0xFF) << 8) | ((long) (array[7] & 0xFF));
    }
    
}
