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

import de.codemakers.base.multiplets.Doublet;
import de.codemakers.base.util.tough.ToughBiFunction;

import java.lang.reflect.Array;
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
        return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
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
    
    public static byte[] booleanToByteArray(boolean value) {
        return new byte[] {(byte) (value ? 1 : 0)};
    }
    
    public static boolean byteArrayToBoolean(byte[] array) {
        Objects.requireNonNull(array);
        if (array.length != 1) {
            throw new IllegalArgumentException(String.format("A %s has %d bit, which means %f bytes, not %d bytes", Boolean.class.getSimpleName(), 1, 1.0 / 8.0, array.length));
        }
        return array[0] == 1;
    }
    
    public static boolean[] byteToBooleanArray(byte value) {
        return new boolean[] {((value >> 0) & 0x1) == 1, ((value >> 1) & 0x1) == 1, ((value >> 2) & 0x1) == 1, ((value >> 3) & 0x1) == 1, ((value >> 4) & 0x1) == 1, ((value >> 5) & 0x1) == 1, ((value >> 6) & 0x1) == 1, ((value >> 7) & 0x1) == 1};
    }
    
    public static byte booleanArrayToByte(boolean[] array) {
        Objects.requireNonNull(array);
        if (array.length != 8) {
            throw new IllegalArgumentException(String.format("A %s has %d bit, which means %d byte, not %d bytes", Byte.class.getSimpleName(), 8, 1, array.length / 8));
        }
        byte b = 0;
        b |= (array[0] ? 1 : 0) << 0;
        b |= (array[1] ? 1 : 0) << 1;
        b |= (array[2] ? 1 : 0) << 2;
        b |= (array[3] ? 1 : 0) << 3;
        b |= (array[4] ? 1 : 0) << 4;
        b |= (array[5] ? 1 : 0) << 5;
        b |= (array[6] ? 1 : 0) << 6;
        b |= (array[7] ? 1 : 0) << 7;
        return b;
    }
    
    public static boolean[] byteArrayToBooleanArray(byte[] array) {
        Objects.requireNonNull(array);
        final boolean[] output = new boolean[array.length * Byte.SIZE];
        for (int i = 0; i < array.length; i++) {
            final boolean[] temp = byteToBooleanArray(array[i]);
            System.arraycopy(temp, 0, output, i * Byte.SIZE, temp.length);
        }
        return output;
    }
    
    public static byte[] booleanArrayToByteArray(boolean[] array) {
        Objects.requireNonNull(array);
        if (array.length % 8 != 0) {
            throw new IllegalArgumentException(String.format("The array length needs to be a multiple of %d bytes, not a length of %d bytes", Byte.SIZE, array.length));
        }
        final byte[] output = new byte[array.length / Byte.SIZE];
        final boolean[] temp = new boolean[Byte.SIZE];
        for (int i = 0; i < output.length; i++) {
            System.arraycopy(array, i * Byte.SIZE, temp, 0, Byte.SIZE);
            output[i] = booleanArrayToByte(temp);
        }
        return output;
    }
    
    public static <T, R> R[] convertArray(T[] array, Class<R> clazz, ToughBiFunction<T, Doublet<T[], Integer>, R> converter) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(converter);
        if (array.length == 0) {
            return (R[]) Array.newInstance(clazz, 0);
        }
        final R[] output = (R[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < output.length; i++) {
            output[i] = converter.applyWithoutException(array[i], new Doublet<>(array, i));
        }
        return (R[]) array;
    }
    
    public static <T, R> R[] convertArrayFast(T[] array, Class<R> clazz, ToughBiFunction<T[], Integer, R> converter) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(converter);
        if (array.length == 0) {
            return (R[]) Array.newInstance(clazz, 0);
        }
        final R[] output = (R[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < output.length; i++) {
            output[i] = converter.applyWithoutException(array, i);
        }
        return (R[]) array;
    }
    
}
