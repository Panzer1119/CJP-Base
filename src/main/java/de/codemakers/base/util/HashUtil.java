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

package de.codemakers.base.util;

import de.codemakers.base.util.interfaces.Hasher;
import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;
import net.openhft.hashing.LongHashFunction;

public class HashUtil {
    
    public static final Hasher XX_HASHER_64 = createXXHasher64();
    public static final Hasher XX_HASHER_32_SAFE = createSafeXXHasher32();
    public static final Hasher XX_HASHER_32_FASTEST = createFastestXXHasher32();
    
    public static Hasher createXXHasher64() {
        return (data) -> ConvertUtil.longToByteArray(LongHashFunction.xx().hashBytes(data));
    }
    
    public static Hasher createXXHasher64(long seed) {
        return (data) -> ConvertUtil.longToByteArray(LongHashFunction.xx(seed).hashBytes(data));
    }
    
    public static Hasher createSafeXXHasher32() {
        return createSafeXXHasher32(0);
    }
    
    public static Hasher createSafeXXHasher32(int seed) {
        final XXHash32 xxHash32 = XXHashFactory.safeInstance().hash32();
        return (data) -> ConvertUtil.intToByteArray(xxHash32.hash(data, 0, data.length, seed));
    }
    
    public static Hasher createFastestXXHasher32() {
        return createFastestXXHasher32(0);
    }
    
    public static Hasher createFastestXXHasher32(int seed) {
        final XXHash32 xxHash32 = XXHashFactory.fastestInstance().hash32();
        return (data) -> ConvertUtil.intToByteArray(xxHash32.hash(data, 0, data.length, seed));
    }
    
}
