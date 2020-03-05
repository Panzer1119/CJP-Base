/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

package de.codemakers.math;

import de.codemakers.base.multiplets.Doublet;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class BaseConverter {
    
    public static final String CHARSET_BASE_2_STRING = "01";
    public static final char[] CHARSET_BASE_2 = CHARSET_BASE_2_STRING.toCharArray();
    
    public static final String CHARSET_BASE_3_STRING = "012";
    public static final char[] CHARSET_BASE_3 = CHARSET_BASE_3_STRING.toCharArray();
    
    public static final String CHARSET_BASE_4_STRING = "0123";
    public static final char[] CHARSET_BASE_4 = CHARSET_BASE_4_STRING.toCharArray();
    
    public static final String CHARSET_BASE_5_STRING = "01234";
    public static final char[] CHARSET_BASE_5 = CHARSET_BASE_5_STRING.toCharArray();
    
    public static final String CHARSET_BASE_6_STRING = "012345";
    public static final char[] CHARSET_BASE_6 = CHARSET_BASE_6_STRING.toCharArray();
    
    public static final String CHARSET_BASE_7_STRING = "0123456";
    public static final char[] CHARSET_BASE_7 = CHARSET_BASE_7_STRING.toCharArray();
    
    public static final String CHARSET_BASE_8_STRING = "01234567";
    public static final char[] CHARSET_BASE_8 = CHARSET_BASE_8_STRING.toCharArray();
    
    public static final String CHARSET_BASE_9_STRING = "012345678";
    public static final char[] CHARSET_BASE_9 = CHARSET_BASE_9_STRING.toCharArray();
    
    public static final String CHARSET_BASE_10_STRING = "0123456789";
    public static final char[] CHARSET_BASE_10 = CHARSET_BASE_10_STRING.toCharArray();
    
    public static final String CHARSET_BASE_11_STRING = "0123456789A";
    public static final char[] CHARSET_BASE_11 = CHARSET_BASE_11_STRING.toCharArray();
    
    public static final String CHARSET_BASE_12_STRING = "0123456789AB";
    public static final char[] CHARSET_BASE_12 = CHARSET_BASE_12_STRING.toCharArray();
    
    public static final String CHARSET_BASE_13_STRING = "0123456789ABC";
    public static final char[] CHARSET_BASE_13 = CHARSET_BASE_13_STRING.toCharArray();
    
    public static final String CHARSET_BASE_14_STRING = "0123456789ABCD";
    public static final char[] CHARSET_BASE_14 = CHARSET_BASE_14_STRING.toCharArray();
    
    public static final String CHARSET_BASE_15_STRING = "0123456789ABCDE";
    public static final char[] CHARSET_BASE_15 = CHARSET_BASE_15_STRING.toCharArray();
    
    public static final String CHARSET_BASE_16_STRING = "0123456789ABCDEF";
    public static final char[] CHARSET_BASE_16 = CHARSET_BASE_16_STRING.toCharArray();
    
    public static final String CHARSET_BASE_36_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final char[] CHARSET_BASE_36 = CHARSET_BASE_36_STRING.toCharArray();
    
    public static final String CHARSET_BASE_62_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final char[] CHARSET_BASE_62 = CHARSET_BASE_62_STRING.toCharArray();
    
    public static final char[] CHARSET_BASE_94_CUSTOM = generateCharset(94, (i) -> (char) (i + 33));
    
    public static char[] generateCharset(int length, Function<Integer, Character> generator) {
        Objects.requireNonNull(generator);
        final char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = generator.apply(i);
        }
        return chars;
    }
    
    public static final void main(String[] args) throws Exception {
        for (int i = 31; i <= 128; i++) {
            System.out.println(i + "=" + ((char) i));
        }
        System.out.println(Arrays.toString(CHARSET_BASE_94_CUSTOM));
        System.out.println("128  <=> " + convertDecimalToBase(128, 16));
        System.out.println("256  <=> " + convertDecimalToBase(256, 16));
        System.out.println("257  <=> " + convertDecimalToBase(257, 16));
        System.out.println("-457 <=> " + convertDecimalToBase(-457, 16));
        System.out.println("3567685368L <=> " + convertDecimalToBase(3567685368L, 16));
        System.out.println("3567685368L <=> " + convertDecimalToBase(3567685368L, 32));
        System.out.println("3567685368L <=> " + convertDecimalToBase(3567685368L, 64));
        System.out.println("3567685368L <=> " + convertDecimalToBase(3567685368L, 94));
        final long number_1 = 23756258L;
        final Doublet<int[], Boolean> number_1_2 = convertDecimalToBaseArray(number_1, 2); //2^1
        final Doublet<int[], Boolean> number_1_8 = convertDecimalToBaseArray(number_1, 8); // 2^3
        final Doublet<int[], Boolean> number_1_10 = convertDecimalToBaseArray(number_1, 10); // 2^3.3219
        final Doublet<int[], Boolean> number_1_16 = convertDecimalToBaseArray(number_1, 16); // 2^4
        final Doublet<int[], Boolean> number_1_32 = convertDecimalToBaseArray(number_1, 32); // 2^5
        final Doublet<int[], Boolean> number_1_64 = convertDecimalToBaseArray(number_1, 64); // 2^6
        final Doublet<int[], Boolean> number_1_94 = convertDecimalToBaseArray(number_1, 94); // 2^6.5546
        final Doublet<int[], Boolean> number_1_128 = convertDecimalToBaseArray(number_1, 128); // 2^7
        final Doublet<int[], Boolean> number_1_256 = convertDecimalToBaseArray(number_1, 256); // 2^8
        final Doublet<int[], Boolean> number_1_512 = convertDecimalToBaseArray(number_1, 512); // 2^9
        final Doublet<int[], Boolean> number_1_1024 = convertDecimalToBaseArray(number_1, 1024); // 2^10
        final Doublet<int[], Boolean> number_1_2048 = convertDecimalToBaseArray(number_1, 2048); // 2^11
        final Doublet<int[], Boolean> number_1_4096 = convertDecimalToBaseArray(number_1, 4096); // 2^12
        final Doublet<int[], Boolean> number_1_8192 = convertDecimalToBaseArray(number_1, 8192); // 2^13
        final Doublet<int[], Boolean> number_1_65536 = convertDecimalToBaseArray(number_1, 65536); // 2^16
        final Doublet<int[], Boolean> number_1_1048576 = convertDecimalToBaseArray(number_1, 1048576); // 2^20
        final Doublet<int[], Boolean> number_1_16777216 = convertDecimalToBaseArray(number_1, 16777216); // 2^24
        final Doublet<int[], Boolean> number_1_1073741824 = convertDecimalToBaseArray(number_1, 1073741824); // 2^30
        System.out.println("number_1           =" + number_1);
        System.out.println("number_1_2         =" + Arrays.toString(number_1_2.getA()));
        System.out.println("number_1_8         =" + Arrays.toString(number_1_8.getA()));
        System.out.println("number_1_10        =" + Arrays.toString(number_1_10.getA()));
        System.out.println("number_1_16        =" + Arrays.toString(number_1_16.getA()));
        System.out.println("number_1_32        =" + Arrays.toString(number_1_32.getA()));
        System.out.println("number_1_64        =" + Arrays.toString(number_1_64.getA()));
        System.out.println("number_1_94        =" + Arrays.toString(number_1_94.getA()));
        System.out.println("number_1_128       =" + Arrays.toString(number_1_128.getA()));
        System.out.println("number_1_256       =" + Arrays.toString(number_1_256.getA()));
        System.out.println("number_1_512       =" + Arrays.toString(number_1_512.getA()));
        System.out.println("number_1_1024      =" + Arrays.toString(number_1_1024.getA()));
        System.out.println("number_1_2048      =" + Arrays.toString(number_1_2048.getA()));
        System.out.println("number_1_4096      =" + Arrays.toString(number_1_4096.getA()));
        System.out.println("number_1_8192      =" + Arrays.toString(number_1_8192.getA()));
        System.out.println("number_1_65536     =" + Arrays.toString(number_1_65536.getA()));
        System.out.println("number_1_1048576   =" + Arrays.toString(number_1_1048576.getA()));
        System.out.println("number_1_16777216  =" + Arrays.toString(number_1_16777216.getA()));
        System.out.println("number_1_1073741824=" + Arrays.toString(number_1_1073741824.getA()));
    }
    
    public static char[] getChars(int radix) {
        final char[] charset;
        switch (radix) {
            case 2:
                charset = CHARSET_BASE_2;
                break;
            case 3:
                charset = CHARSET_BASE_3;
                break;
            case 4:
                charset = CHARSET_BASE_4;
                break;
            case 5:
                charset = CHARSET_BASE_5;
                break;
            case 6:
                charset = CHARSET_BASE_6;
                break;
            case 7:
                charset = CHARSET_BASE_7;
                break;
            case 8:
                charset = CHARSET_BASE_8;
                break;
            case 9:
                charset = CHARSET_BASE_9;
                break;
            case 10:
                charset = CHARSET_BASE_10;
                break;
            case 11:
                charset = CHARSET_BASE_11;
                break;
            case 12:
                charset = CHARSET_BASE_12;
                break;
            case 13:
                charset = CHARSET_BASE_13;
                break;
            case 14:
                charset = CHARSET_BASE_14;
                break;
            case 15:
                charset = CHARSET_BASE_15;
                break;
            case 16:
                charset = CHARSET_BASE_16;
                break;
            case 94:
                charset = CHARSET_BASE_94_CUSTOM;
                break;
            default:
                charset = generateCharset(radix, (i) -> (char) (i + 33));
                break;
        }
        return charset;
    }
    
    public static String convertDecimalToBase(long number, int radix) {
        if (radix < 2) {
            return null;
        }
        return convertDecimalToBase(number, getChars(radix));
    }
    
    public static String convertDecimalToBase(long number, char[] charset) {
        final int radix = charset.length;
        final boolean negative = number < 0;
        if (negative) {
            number *= -1;
        }
        String output = "";
        while (number > 0) {
            output = charset[(int) (number % radix)] + output;
            number /= radix;
        }
        return (negative ? "-" : "") + output;
    }
    
    public static Doublet<int[], Boolean> convertDecimalToBaseArray(long number, long radix) {
        final boolean negative = number < 0;
        if (negative) {
            number *= -1;
        }
        final int[] digits = new int[(int) (Math.log(number) / Math.log(radix)) + 1];
        int i = digits.length - 1;
        while (number > 0) {
            digits[i--] = (int) (number % radix);
            number /= radix;
        }
        return new Doublet<>(digits, negative);
    }
    
}
