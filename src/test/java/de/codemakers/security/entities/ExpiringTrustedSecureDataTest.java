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

package de.codemakers.security.entities;

import de.codemakers.base.logger.Logger;
import de.codemakers.security.managers.ExpiringTrustedSecureDataManager;
import de.codemakers.security.util.EasyCryptUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class ExpiringTrustedSecureDataTest {
    
    public static final void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendLocation().appendText(": ").appendObject().finishWithoutException();
        Logger.log(ExpiringTrustedSecureDataTest.class.getName());
        final ExpiringTrustedSecureDataManager expiringTrustedSecureDataManager = new ExpiringTrustedSecureDataManager(5, TimeUnit.SECONDS);
        Logger.log(expiringTrustedSecureDataManager);
        expiringTrustedSecureDataManager.start();
        Logger.log(expiringTrustedSecureDataManager);
        final String text_1 = "This is a sentence!";
        Logger.log(text_1);
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        Logger.log(keyPairGenerator);
        keyPairGenerator.initialize(1024, SecureRandom.getInstanceStrong());
        Logger.log(keyPairGenerator);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Logger.log(keyPair);
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        Logger.log(keyGenerator);
        keyGenerator.init(256, SecureRandom.getInstanceStrong());
        Logger.log(keyGenerator);
        final SecretKey secretKey = keyGenerator.generateKey();
        Logger.log(secretKey);
        final ExpiringTrustedSecureData expiringTrustedSecureData = new ExpiringTrustedSecureData(text_1.getBytes(), EasyCryptUtil.encryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.signerOfSHA512withRSA(keyPair.getPrivate()));
        Logger.log(expiringTrustedSecureData);
        //expiringTrustedSecureData.createTimestamp(EasyCryptUtil.signerOfSHA256withRSA(keyPair.getPrivate()));
        expiringTrustedSecureData.createTimestamp(EasyCryptUtil.encryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.signerOfSHA512withRSA(keyPair.getPrivate()));
        Logger.log(expiringTrustedSecureData);
        Logger.log("=========================================================================================");
        final ExpiringTrustedSecureData expiringTrustedSecureData_c_1 = expiringTrustedSecureData.copy();
        Logger.log(expiringTrustedSecureData_c_1);
        //if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_1, EasyCryptUtil.verifierOfSHA256withRSA(keyPair.getPublic()))) {
        if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_1, EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic()))) {
            Logger.log("ACCEPTED 1");
            Logger.log("VALID 1: " + expiringTrustedSecureData_c_1.verifyWithoutException(EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic())));
            Logger.log(new String(expiringTrustedSecureData_c_1.getData()));
            final String text_2 = new String(expiringTrustedSecureData_c_1.decryptWithoutException(EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey)));
            Logger.log("1: " + text_2);
        } else {
            Logger.log("REJECTED 1");
        }
        Logger.log("=========================================================================================");
        Logger.log(expiringTrustedSecureDataManager);
        Logger.log("=========================================================================================");
        final ExpiringTrustedSecureData expiringTrustedSecureData_c_2 = expiringTrustedSecureData.copy();
        Logger.log(expiringTrustedSecureData_c_2);
        /*
        Logger.log("=========================================================================================");
        Logger.log("=========================================================================================");
        Logger.log("=========================================================================================");
        Logger.log(expiringTrustedSecureDataManager);
        */
        Thread.sleep(6000);
        /*
        Logger.log(expiringTrustedSecureDataManager);
        Logger.log("=========================================================================================");
        Logger.log("=========================================================================================");
        Logger.log("=========================================================================================");
        */
        //if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_2, EasyCryptUtil.verifierOfSHA256withRSA(keyPair.getPublic()))) {
        if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_2, EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic()))) {
            Logger.log("ACCEPTED 2");
            Logger.log("VALID 2: " + expiringTrustedSecureData_c_2.verifyWithoutException(EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic())));
            Logger.log(new String(expiringTrustedSecureData_c_2.getData()));
            final String text_2 = new String(expiringTrustedSecureData_c_2.decryptWithoutException(EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey)));
            Logger.log("2: " + text_2);
        } else {
            Logger.log("REJECTED 2");
        }
        Logger.log("=========================================================================================");
        Logger.log(expiringTrustedSecureDataManager);
        Logger.log("==========================================");
        expiringTrustedSecureDataManager.stop();
        Logger.log(expiringTrustedSecureDataManager);
        Logger.log("==========================================");
        expiringTrustedSecureDataManager.clear();
        Logger.log(expiringTrustedSecureDataManager);
    }
    
}
