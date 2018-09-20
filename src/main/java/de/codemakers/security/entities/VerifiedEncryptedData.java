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

package de.codemakers.security.entities;

import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.security.interfaces.Signer;
import de.codemakers.security.interfaces.Verifiable;
import de.codemakers.security.interfaces.Verifier;

import java.util.Arrays;
import java.util.Objects;

public class VerifiedEncryptedData extends EncryptedData implements Verifiable {
    
    protected byte[] signature;
    
    public VerifiedEncryptedData(byte[] data) {
        this(data, (byte[]) null);
    }
    
    public VerifiedEncryptedData(byte[] data, Signer signer) {
        this(data, signer.signWithoutException(data));
    }
    
    public VerifiedEncryptedData(byte[] data, byte[] signature) {
        super(data);
        this.signature = signature;
    }
    
    public byte[] getSignature() {
        return signature;
    }
    
    public VerifiedEncryptedData setSignature(byte[] signature) {
        this.signature = signature;
        return this;
    }
    
    @Override
    public Copyable copy() {
        return new VerifiedEncryptedData(data, signature);
    }
    
    @Override
    public boolean verify(Verifier verifier) throws Exception {
        Objects.requireNonNull(verifier);
        return verifier.verify(getData(), getSignature());
    }
    
    @Override
    public boolean verify(Verifier verifier, byte[] data_signature) throws Exception {
        Objects.requireNonNull(verifier);
        Objects.requireNonNull(data_signature);
        return verifier.verify(getData(), data_signature);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final VerifiedEncryptedData that = (VerifiedEncryptedData) o;
        return Arrays.equals(signature, that.signature);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(signature);
        return result;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "signature=" + Arrays.toString(signature) + ", data=" + Arrays.toString(data) + '}';
    }
    
}
