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
        System.out.println(ExpiringTrustedSecureDataTest.class.getName());
        final ExpiringTrustedSecureDataManager expiringTrustedSecureDataManager = new ExpiringTrustedSecureDataManager(5, TimeUnit.SECONDS);
        System.out.println(expiringTrustedSecureDataManager);
        expiringTrustedSecureDataManager.start();
        System.out.println(expiringTrustedSecureDataManager);
        final String text_1 = "This is a sentence!";
        System.out.println(text_1);
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        System.out.println(keyPairGenerator);
        keyPairGenerator.initialize(1024, SecureRandom.getInstanceStrong());
        System.out.println(keyPairGenerator);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        System.out.println(keyPair);
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        System.out.println(keyGenerator);
        keyGenerator.init(256, SecureRandom.getInstanceStrong());
        System.out.println(keyGenerator);
        final SecretKey secretKey = keyGenerator.generateKey();
        System.out.println(secretKey);
        final ExpiringTrustedSecureData expiringTrustedSecureData = new ExpiringTrustedSecureData(text_1.getBytes(), EasyCryptUtil.encryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.signerOfSHA512withRSA(keyPair.getPrivate()));
        System.out.println(expiringTrustedSecureData);
        //expiringTrustedSecureData.createTimestamp(EasyCryptUtil.signerOfSHA256withRSA(keyPair.getPrivate()));
        expiringTrustedSecureData.createTimestamp(EasyCryptUtil.encryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.signerOfSHA512withRSA(keyPair.getPrivate()));
        System.out.println(expiringTrustedSecureData);
        System.out.println("=========================================================================================");
        final ExpiringTrustedSecureData expiringTrustedSecureData_c_1 = expiringTrustedSecureData.copy();
        System.out.println(expiringTrustedSecureData_c_1);
        //if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_1, EasyCryptUtil.verifierOfSHA256withRSA(keyPair.getPublic()))) {
        if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_1, EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic()))) {
            System.out.println("ACCEPTED 1");
            System.out.println("VALID 1: " + expiringTrustedSecureData_c_1.verifyWithoutException(EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic())));
            System.out.println(new String(expiringTrustedSecureData_c_1.getData()));
            final String text_2 = new String(expiringTrustedSecureData_c_1.decryptWithoutException(EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey)));
            System.out.println("1: " + text_2);
        } else {
            System.out.println("REJECTED 1");
        }
        System.out.println("=========================================================================================");
        System.out.println(expiringTrustedSecureDataManager);
        System.out.println("=========================================================================================");
        final ExpiringTrustedSecureData expiringTrustedSecureData_c_2 = expiringTrustedSecureData.copy();
        System.out.println(expiringTrustedSecureData_c_2);
        /*
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println(expiringTrustedSecureDataManager);
        */
        Thread.sleep(6000);
        /*
        System.out.println(expiringTrustedSecureDataManager);
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        System.out.println("=========================================================================================");
        */
        //if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_2, EasyCryptUtil.verifierOfSHA256withRSA(keyPair.getPublic()))) {
        if (expiringTrustedSecureDataManager.acceptExpiringTrustedSecureData(expiringTrustedSecureData_c_2, EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey), EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic()))) {
            System.out.println("ACCEPTED 2");
            System.out.println("VALID 2: " + expiringTrustedSecureData_c_2.verifyWithoutException(EasyCryptUtil.verifierOfSHA512withRSA(keyPair.getPublic())));
            System.out.println(new String(expiringTrustedSecureData_c_2.getData()));
            final String text_2 = new String(expiringTrustedSecureData_c_2.decryptWithoutException(EasyCryptUtil.decryptorOfCipher(Cipher.getInstance("AES"), secretKey)));
            System.out.println("2: " + text_2);
        } else {
            System.out.println("REJECTED 2");
        }
        System.out.println("=========================================================================================");
        System.out.println(expiringTrustedSecureDataManager);
        System.out.println("==========================================");
        expiringTrustedSecureDataManager.stop();
        System.out.println(expiringTrustedSecureDataManager);
        System.out.println("==========================================");
        expiringTrustedSecureDataManager.clear();
        System.out.println(expiringTrustedSecureDataManager);
    }
    
}
