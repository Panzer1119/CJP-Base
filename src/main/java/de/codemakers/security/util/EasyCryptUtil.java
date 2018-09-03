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
import de.codemakers.security.interfaces.*;
import de.codemakers.security.interfaces.Signer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.*;
import java.util.Random;

public class EasyCryptUtil {
    
    public static final Signature SIGNATURE_SHA256withRSA;
    public static final byte[] RANDOM_TEST_BYTES = new byte[32];
    
    static {
        Signature signature = null;
        try {
            signature = Signature.getInstance("SHA256withRSA");
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        SIGNATURE_SHA256withRSA = signature;
        try {
            SecureRandom.getInstanceStrong().nextBytes(RANDOM_TEST_BYTES);
        } catch (Exception ex) {
            new Random().nextBytes(RANDOM_TEST_BYTES);
        }
    }
    
    public static final Cryptor cryptorOfCipher(Cipher cipher) {
        return (data) -> cipher.doFinal(data);
    }
    
    public static final Encryptor encryptorOfCipher(Cipher cipher) {
        return (data) -> cipher.doFinal(data);
    }
    
    public static final Encryptor encryptorOfCipher(Cipher cipher, SecretKey key) throws InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptorOfCipher(cipher);
    }
    
    public static final Encryptor encryptorOfCipher(Cipher cipher, SecretKey key, SecureRandom secureRandom) throws InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key, secureRandom);
        return encryptorOfCipher(cipher);
    }
    
    public static final Decryptor decryptorOfCipher(Cipher cipher) {
        return (data) -> cipher.doFinal(data);
    }
    
    public static final Decryptor decryptorOfCipher(Cipher cipher, SecretKey key) throws InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return decryptorOfCipher(cipher);
    }
    
    public static final Decryptor decryptorOfCipher(Cipher cipher, SecretKey key, SecureRandom secureRandom) throws InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, key, secureRandom);
        return decryptorOfCipher(cipher);
    }
    
    public static final Signer signerOfSignature(Signature signature) {
        return (data) -> {
            signature.update(data);
            return signature.sign();
        };
    }
    
    public static final Signer signerOfSignature(Signature signature, PrivateKey key) throws InvalidKeyException {
        signature.initSign(key);
        return signerOfSignature(signature);
    }
    
    public static final Verifier verifierOfSignature(Signature signature) {
        return (data, data_signature) -> {
            signature.update(data);
            return signature.verify(data_signature);
        };
    }
    
    public static final Verifier verifierOfSignature(Signature signature, PublicKey key) throws InvalidKeyException {
        signature.initVerify(key);
        return verifierOfSignature(signature);
    }
    
    public static final boolean publicKeyCanVerifyDataSignedWithPrivateKeyRSA(PrivateKey privateKey, PublicKey publicKey) throws InvalidKeyException {
        if (privateKey == null || publicKey == null) {
            return false;
        }
        assert privateKey.getAlgorithm().equals("RSA");
        assert publicKey.getAlgorithm().equals("RSA");
        final Signer signer = signerOfSignature(SIGNATURE_SHA256withRSA, privateKey);
        final Verifier verifier = verifierOfSignature(SIGNATURE_SHA256withRSA, publicKey);
        return verifier.verifyWithoutException(RANDOM_TEST_BYTES, signer.signWithoutException(RANDOM_TEST_BYTES));
    }
    
}
