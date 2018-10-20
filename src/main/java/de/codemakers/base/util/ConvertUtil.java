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

import java.nio.ByteBuffer;
import java.util.Objects;

public class ConvertUtil {
    
    public static byte[] doubleToByteArray(double value) {
        return ByteBuffer.allocate(Double.BYTES).putDouble(value).array();
    }
    
    public static double byteArrayToDouble(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != Double.BYTES) {
            throw new IllegalArgumentException(String.format("A %s has %d bits, which means %d bytes, not %d bytes", Double.class.getSimpleName(), Double.SIZE, Double.BYTES, array.length));
        }
        return ByteBuffer.wrap(array).getDouble();
    }
    
    public static byte[] longToByteArray(long value) {
        return ByteBuffer.allocate(Long.BYTES).putLong(value).array();
    }
    
    public static long byteArrayToLong(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != Long.BYTES) {
            throw new IllegalArgumentException(String.format("A %s has %d bits, which means %d bytes, not %d bytes", Long.class.getSimpleName(), Long.SIZE, Long.BYTES, array.length));
        }
        return ByteBuffer.wrap(array).getLong();
    }
    
    public static byte[] floatToByteArray(float value) {
        return ByteBuffer.allocate(Float.BYTES).putFloat(value).array();
    }
    
    public static float byteArrayToFloat(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != Float.BYTES) {
            throw new IllegalArgumentException(String.format("An %s has %d bits, which means %d bytes, not %d bytes", Float.class.getSimpleName(), Float.SIZE, Float.BYTES, array.length));
        }
        return ByteBuffer.wrap(array).getFloat();
    }
    
    public static byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(Integer.BYTES).putLong(value).array();
    }
    
    public static int byteArrayToInt(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != Integer.BYTES) {
            throw new IllegalArgumentException(String.format("An %s has %d bits, which means %d bytes, not %d bytes", Integer.class.getSimpleName(), Integer.SIZE, Integer.BYTES, array.length));
        }
        return ByteBuffer.wrap(array).getInt();
    }
    
    public static byte[] shortToByteArray(short value) {
        return ByteBuffer.allocate(Short.BYTES).putShort(value).array();
    }
    
    public static short byteArrayToShort(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != Short.BYTES) {
            throw new IllegalArgumentException(String.format("A %s has %d bits, which means %d bytes, not %d bytes", Short.class.getSimpleName(), Short.SIZE, Short.BYTES, array.length));
        }
        return ByteBuffer.wrap(array).getShort();
    }
    
    public static byte[] charToByteArray(char value) {
        return ByteBuffer.allocate(Character.BYTES).putChar(value).array();
    }
    
    public static char byteArrayToChar(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != Character.BYTES) {
            throw new IllegalArgumentException(String.format("A %s has %d bits, which means %d bytes, not %d bytes", Character.class.getSimpleName(), Character.SIZE, Character.BYTES, array.length));
        }
        return ByteBuffer.wrap(array).getChar();
    }
    
    public static byte[] byteToByteArray(byte value) {
        return new byte[] {value};
    }
    
    public static byte byteArrayToBytes(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != 1) {
            throw new IllegalArgumentException(String.format("A %s has %d bits, which means %d bytes, not %d bytes", Byte.class.getSimpleName(), 8, 1, array.length));
        }
        return array[0];
    }
    
}
