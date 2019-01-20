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

package de.codemakers.security.util;

import de.codemakers.base.logger.Logger;

import javax.crypto.SecretKeyFactory;

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
    }
    
}
