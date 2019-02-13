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
import de.codemakers.security.interfaces.Signer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Arrays;

public class Test {
    
    public static final void main(String[] args) throws Exception {
        Logger.log("Test");
        final KeyPairGenerator keyPairGenerator = EllipticCurveUtil.createKeyPairGeneratorEC(SecureRandom.getInstanceStrong(), 256);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Logger.log("keyPair=" + keyPair);
        Logger.log("keyPair.getPrivate()=" + keyPair.getPrivate());
        Logger.log("keyPair.getPublic()=" + keyPair.getPublic());
        Logger.log("keyPair.getPrivate().getAlgorithm()=" + keyPair.getPrivate().getAlgorithm());
        Logger.log("keyPair.getPublic().getAlgorithm()=" + keyPair.getPublic().getAlgorithm());
        final Signer signer = EasyCryptUtil.signerOfSHA256withECDSA(keyPair.getPrivate());
        Logger.log("signer=" + signer);
        Logger.log("signed:" + Arrays.toString(signer.sign("test".getBytes())));
        Logger.log("signed.length  :" + signer.sign("test".getBytes()).length);
        Logger.log("signed.length*8:" + (signer.sign("test".getBytes()).length * 8));
        final boolean success = EasyCryptUtil.publicKeyCanVerifyDataSignedWithPrivateKeyECDSA(keyPair.getPrivate(), keyPair.getPublic());
        Logger.log("success=" + success);
    }
    
}
