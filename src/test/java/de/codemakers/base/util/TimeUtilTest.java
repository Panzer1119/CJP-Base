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

import java.security.SecureRandom;

public class TimeUtilTest {
    
    public static final void main(String[] args) throws Exception {
        final long timestamp = System.currentTimeMillis();
        final int random = TimeUtil.shortenToRandom(SecureRandom.getInstanceStrong().nextInt());
        final long id = TimeUtil.createId(timestamp, random);
        System.out.println(String.format(" timestamp: %s%n    random: %s%n        id: %s", longS(timestamp), longS(random), longS(id)));
        final long timestamp_ = TimeUtil.getTimestamp(id);
        final long random_ = TimeUtil.getRandom(id);
        System.out.println();
        System.out.println(String.format(" timestamp: %s%ntimestamp_: %s", longS(timestamp), longS(timestamp_)));
        System.out.println(String.format("    random: %s%n   random_: %s", longS(random), longS(random_)));
        System.out.println();
    }
    
    public static final String longS(long l) {
        String l_ = Long.toBinaryString(l);
        while (l_.length() < Long.SIZE) {
            l_ = "0" + l_;
        }
        return l_.substring(0, Long.SIZE / 2) + "#" + l_.substring(Long.SIZE / 2);
    }
    
}
