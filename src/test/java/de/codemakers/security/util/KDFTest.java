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

package de.codemakers.security.util;

import de.codemakers.base.logger.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Arrays;

public class KDFTest {
    
    public static final void main(String[] args) throws Exception {
        final SecretKeyFactory secretKeyFactory_1 = KDFUtil.createPBKDF2WithHmacSHA_1();
        Logger.log("secretKeyFactory_1=" + secretKeyFactory_1);
        final SecretKeyFactory secretKeyFactory_256 = KDFUtil.createPBKDF2WithHmacSHA_256();
        Logger.log("secretKeyFactory_256=" + secretKeyFactory_256);
        final SecretKeyFactory secretKeyFactory_384 = KDFUtil.createPBKDF2WithHmacSHA_384();
        Logger.log("secretKeyFactory_384=" + secretKeyFactory_384);
        final SecretKeyFactory secretKeyFactory_512 = KDFUtil.createPBKDF2WithHmacSHA_512();
        Logger.log("secretKeyFactory_512=" + secretKeyFactory_512);
        final String password = "Test1234";
        Logger.log("password=" + password);
        final byte[] salt = EasyCryptUtil.generateSecureRandomBytes(32);
        Logger.log("salt=" + Arrays.toString(salt));
        final int iterations = 100000;
        Logger.log("iterations=" + iterations);
        final int length = 512;
        Logger.log("length=" + length);
        final PBEKeySpec keySpec_512 = new PBEKeySpec(password.toCharArray(), salt, iterations, length);
        Logger.log("keySpec_512=" + keySpec_512);
        final long start = System.currentTimeMillis();
        final SecretKey secretKey_512 = secretKeyFactory_512.generateSecret(keySpec_512);
        final long stop = System.currentTimeMillis();
        keySpec_512.clearPassword();
        final long duration = stop - start;
        Logger.log("Time taken: " + duration + " ms");
        Logger.log("secretKey_512=" + secretKey_512);
        Logger.log("secretKey_512=" + Arrays.toString(secretKey_512.getEncoded()));
        Logger.log("secretKey_512=" + secretKey_512.getEncoded().length);
        Logger.log("secretKey_512=" + secretKey_512.getEncoded().length * 8);
    }
    
}
