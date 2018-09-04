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

import de.codemakers.security.util.EasyCryptUtil;

import java.util.Random;

public class TimeUtil {
    
    public static final long TIMESTAMP_OFFSET = 22;
    public static final long RANDOM_OFFSET = 9;
    public static final long RANDOM_OFFSET_2 = 41;
    private static final Random RANDOM = new Random(EasyCryptUtil.getSecurestRandom().nextLong());
    
    public static final long createId() {
        return createId(System.currentTimeMillis());
    }
    
    public static final long createId(long timestamp) {
        return createId(timestamp, getShortenedRandom());
    }
    
    public static final long createId(long timestamp, int random) {
        return (timestamp << TIMESTAMP_OFFSET) | random;
    }
    
    public static final long getTimestamp(long id) {
        return id >>> TIMESTAMP_OFFSET;
    }
    
    public static final int getRandom(long id) {
        return (int) ((id << RANDOM_OFFSET_2) >>> RANDOM_OFFSET_2);
    }
    
    public static final int shortenToRandom(int random) {
        return random >>> RANDOM_OFFSET;
    }
    
    public static final int getShortenedRandom() {
        return shortenToRandom(RANDOM.nextInt());
    }
    
}
