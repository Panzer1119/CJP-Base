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

package de.codemakers.security.util;

import de.codemakers.base.logger.Logger;

import java.security.MessageDigest;
import java.util.Arrays;

public class HashUtil {
    
    public static final MessageDigest SHA_256;
    
    static {
        MessageDigest sha_256 = null;
        try {
            sha_256 = MessageDigest.getInstance("SHA-256");
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        SHA_256 = sha_256;
    }
    
    public static byte[] hashSHA256(byte[] data) {
        if (data == null) {
            return null;
        }
        return SHA_256.digest(data);
    }
    
    public static boolean isDataValidSHA256(byte[] data, byte[] hash) {
        if (hash == null || data == null) {
            return hash == data;
        }
        return Arrays.equals(hashSHA256(data), hash);
    }
    
}
