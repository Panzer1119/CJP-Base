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
import de.codemakers.base.util.ConvertUtil;
import de.codemakers.base.util.tough.ToughFunction;
import de.codemakers.base.util.tough.ToughPredicate;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.security.entities.TrustedSecureData;
import de.codemakers.security.interfaces.Signer;
import de.codemakers.security.interfaces.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EasyCryptUtil {
    
    public static final String ALGORITHM_AES = AESCryptUtil.ALGORITHM_AES;
    public static final String ALGORITHM_RSA = RSACryptUtil.ALGORITHM_RSA;
    public static final String ALGORITHM_SHA256withRSA = SecureHashUtil.ALGORITHM_SHA256withRSA;
    public static final String ALGORITHM_SHA256withECDSA = SecureHashUtil.ALGORITHM_SHA256withECDSA;
    
    //private static final Signature SIGNATURE_SHA256withRSA;
    //private static final Signature SIGNATURE_SHA256withECDSA;
    private static final byte[] RANDOM_TEST_BYTES = new byte[32];
    private static final Random SECUREST_RANDOM;
    
    static {
        //SIGNATURE_SHA256withRSA = createSignatureSHA256withRSA();
        //SIGNATURE_SHA256withECDSA = createSignatureSHA256withECDSA();
        Random random = null;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (Exception ex) {
            random = new Random();
        }
        if (random == null) {
            random = new Random();
        }
        SECUREST_RANDOM = random;
        SECUREST_RANDOM.nextBytes(RANDOM_TEST_BYTES);
    }
    
    /**
     * Never null
     *
     * @return never null Random Object
     */
    public static final Random getSecurestRandom() {
        return SECUREST_RANDOM;
    }
    
    public static final boolean hasSecureRandomInstanceStrong() {
        try {
            return SecureRandom.getInstanceStrong() != null;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static final Cryptor cryptorOfCipher(Cipher cipher) {
        return (data, iv) -> cipher.doFinal(data);
    }
    
    public static final Encryptor encryptorOfCipher(Cipher cipher) {
        return (data, iv) -> cipher.doFinal(data);
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
        return (data, iv) -> cipher.doFinal(data);
    }
    
    public static final Decryptor decryptorOfCipher(Cipher cipher, SecretKey key) throws InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return decryptorOfCipher(cipher);
    }
    
    public static final Decryptor decryptorOfCipher(Cipher cipher, SecretKey key, SecureRandom secureRandom) throws InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, key, secureRandom);
        return decryptorOfCipher(cipher);
    }
    
    public static final Signature createSignatureSHA256withRSA() {
        try {
            return Signature.getInstance(ALGORITHM_SHA256withRSA);
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    public static final Signature createSignatureSHA256withECDSA() {
        try {
            return Signature.getInstance(ALGORITHM_SHA256withECDSA);
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    public static final Signer signerOfSHA256withRSA(PrivateKey key) throws InvalidKeyException {
        return signerOfSignature(createSignatureSHA256withRSA(), key);
    }
    
    public static final Signer signerOfSHA256withECDSA(PrivateKey key) throws InvalidKeyException {
        return signerOfSignature(createSignatureSHA256withECDSA(), key);
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
    
    public static final Verifier verifierOfSHA256withRSA(PublicKey key) throws InvalidKeyException {
        return verifierOfSignature(createSignatureSHA256withRSA(), key);
    }
    
    public static final Verifier verifierOfSHA256withECDSA(PublicKey key) throws InvalidKeyException {
        return verifierOfSignature(createSignatureSHA256withECDSA(), key);
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
        assert privateKey.getAlgorithm().equals(RSACryptUtil.ALGORITHM_RSA);
        assert publicKey.getAlgorithm().equals(RSACryptUtil.ALGORITHM_RSA);
        return verifierCanVerifyDataSignedWithSigner(signerOfSHA256withRSA(privateKey), verifierOfSHA256withRSA(publicKey));
    }
    
    public static final boolean publicKeyCanVerifyDataSignedWithPrivateKeyECDSA(PrivateKey privateKey, PublicKey publicKey) throws InvalidKeyException {
        if (privateKey == null || publicKey == null) {
            return false;
        }
        assert privateKey.getAlgorithm().equals(EllipticCurveUtil.ALGORITHM_EC);
        assert publicKey.getAlgorithm().equals(EllipticCurveUtil.ALGORITHM_EC);
        return verifierCanVerifyDataSignedWithSigner(signerOfSHA256withECDSA(privateKey), verifierOfSHA256withECDSA(publicKey));
    }
    
    public static final boolean verifierCanVerifyDataSignedWithSigner(Signer signer, Verifier verifier) {
        if (signer == null || verifier == null) {
            return false;
        }
        return verifier.verifyWithoutException(RANDOM_TEST_BYTES, signer.signWithoutException(RANDOM_TEST_BYTES));
    }
    
    public static final TrustedSecureData createTimestampOfSHA256withRSA(PrivateKey key) throws InvalidKeyException {
        return createTimestampOfSHA256withRSA(null, key);
    }
    
    public static final TrustedSecureData createTimestampOfSHA256withRSA(Encryptor encryptor, PrivateKey key) throws InvalidKeyException {
        return createTimestamp(System.currentTimeMillis(), encryptor, signerOfSHA256withRSA(key));
    }
    
    public static final TrustedSecureData createTimestamp(Signer signer) {
        return createTimestamp(null, signer);
    }
    
    public static final TrustedSecureData createTimestamp(Encryptor encryptor, Signer signer) {
        return createTimestamp(System.currentTimeMillis(), encryptor, signer);
    }
    
    public static final TrustedSecureData createTimestampOfSHA256withRSA(long timestamp, PrivateKey key) throws InvalidKeyException {
        return createTimestampOfSHA256withRSA(timestamp, null, key);
    }
    
    public static final TrustedSecureData createTimestampOfSHA256withRSA(long timestamp, Encryptor encryptor, PrivateKey key) throws InvalidKeyException {
        return createTimestamp(timestamp, encryptor, signerOfSHA256withRSA(key));
    }
    
    public static final TrustedSecureData createTimestamp(long timestamp, Signer signer) {
        return createTimestamp(timestamp, null, signer);
    }
    
    public static final TrustedSecureData createTimestamp(long timestamp, Encryptor encryptor, Signer signer) {
        return encryptor == null ? new TrustedSecureData(ConvertUtil.longToByteArray(timestamp), signer) : new TrustedSecureData(ConvertUtil.longToByteArray(timestamp), encryptor, signer);
    }
    
    public static final boolean isValidOfSHA256withRSA(TrustedSecureData timestamp, PublicKey key) throws InvalidKeyException {
        return isValid(timestamp, verifierOfSHA256withRSA(key));
    }
    
    public static final boolean isValid(TrustedSecureData timestamp, Verifier verifier) {
        Objects.requireNonNull(timestamp);
        if (verifier == null) {
            return true;
        }
        return timestamp.verifyWithoutException(verifier);
    }
    
    public static final boolean isExpired(TrustedSecureData timestamp, ToughPredicate<Long> timeTester) {
        return isExpired(timestamp, timeTester, null, null);
    }
    
    public static final boolean isExpiredOfSHA256withRSA(TrustedSecureData timestamp, ToughPredicate<Long> timeTester, PublicKey publicKey) throws InvalidKeyException {
        return isExpiredOfSHA256withRSA(timestamp, timeTester, null, null, publicKey);
    }
    
    public static final boolean isExpiredOfSHA256withRSA(TrustedSecureData timestamp, ToughPredicate<Long> timeTester, Cipher cipher, SecretKey secretKey, PublicKey publicKey) throws InvalidKeyException {
        return isExpired(timestamp, timeTester, (cipher == null || secretKey == null) ? null : decryptorOfCipher(cipher, secretKey), publicKey == null ? null : verifierOfSHA256withRSA(publicKey));
    }
    
    public static final boolean isExpired(TrustedSecureData timestamp, ToughPredicate<Long> timeTester, Verifier verifier) {
        return isExpired(timestamp, timeTester, null, verifier);
    }
    
    public static final boolean isExpired(TrustedSecureData timestamp, ToughPredicate<Long> timeTester, Decryptor decryptor, Verifier verifier) {
        Objects.requireNonNull(timestamp);
        if (!isValid(timestamp, verifier)) {
            return true;
        }
        return timeTester == null || !timeTester.testWithoutException(ConvertUtil.byteArrayToLong(decryptor != null ? timestamp.decryptWithoutException(decryptor) : timestamp.getData()));
    }
    
    public static final ToughPredicate<Long> createTimePredicateOfMaximumErrorAndLockedTimestamp(long max_time_error, TimeUnit unit) {
        return createTimePredicateOfMaximumErrorAndLockedTimestamp(max_time_error, unit, System.currentTimeMillis());
    }
    
    public static final ToughPredicate<Long> createTimePredicateOfMaximumErrorAndLockedTimestamp(long max_time_error, TimeUnit unit, long timestamp) {
        return createTimePredicateOfMaximumError(max_time_error, unit, () -> timestamp);
    }
    
    public static final ToughPredicate<Long> createTimePredicateOfMaximumError(long max_time_error, TimeUnit unit) {
        return createTimePredicateOfMaximumError(max_time_error, unit, () -> System.currentTimeMillis());
    }
    
    public static final ToughPredicate<Long> createTimePredicateOfMaximumError(long max_time_error, TimeUnit unit, ToughSupplier<Long> timeSupplier) {
        Objects.requireNonNull(timeSupplier);
        if (unit == null) {
            unit = TimeUnit.MILLISECONDS;
        }
        final long max_time_error_millis = unit.toMillis(max_time_error);
        return (timestamp) -> Math.abs(timeSupplier.getWithoutException() - timestamp) <= max_time_error_millis;
    }
    
    public static final Cipher createCipher(String mode) {
        try {
            return Cipher.getInstance(mode);
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    public static final byte[] generateSecureRandomBytes(byte[] bytes) {
        try {
            return generateRandomBytes(bytes, SecureRandom.getInstanceStrong());
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    public static final byte[] generateRandomBytes(byte[] bytes, Random random) {
        random.nextBytes(bytes);
        return bytes;
    }
    
    public static final byte[] generateSecureRandomBytes(int bytes) {
        try {
            return generateRandomBytes(bytes, SecureRandom.getInstanceStrong());
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    public static final byte[] generateRandomBytes(int bytes, Random random) {
        final byte[] iv = new byte[bytes];
        random.nextBytes(iv);
        return iv;
    }
    
    public static final Encryptor createEncryptor(Cipher cipher, SecretKey secretKey, ToughFunction<byte[], AlgorithmParameterSpec> ivFunction) {
        if (ivFunction != null) {
            return (data, iv) -> {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivFunction.apply(iv));
                return cipher.doFinal(data);
            };
        } else {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } catch (Exception ex) {
                Logger.handleError(ex);
            }
            return (data, iv) -> cipher.doFinal(data);
        }
    }
    
    public static final Decryptor createDecryptor(Cipher cipher, SecretKey secretKey, ToughFunction<byte[], AlgorithmParameterSpec> ivFunction) {
        if (ivFunction != null) {
            return (data, iv) -> {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivFunction.apply(iv));
                return cipher.doFinal(data);
            };
        } else {
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            } catch (Exception ex) {
                Logger.handleError(ex);
            }
            return (data, iv) -> cipher.doFinal(data);
        }
    }
    
}
