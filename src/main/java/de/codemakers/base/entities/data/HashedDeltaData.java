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

package de.codemakers.base.entities.data;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;

import java.security.MessageDigest;
import java.util.Arrays;

public abstract class HashedDeltaData extends DeltaData {
    
    public static final int HASH_SIZE_BYTES = 32;
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
    
    protected byte[] hash = null;
    
    public HashedDeltaData() {
        super();
    }
    
    public HashedDeltaData(long version) {
        super(version);
    }
    
    public HashedDeltaData(byte[] data_new) {
        super(data_new);
    }
    
    public HashedDeltaData(byte[] data_new, long version) {
        super(data_new, version);
    }
    
    public HashedDeltaData(int length, byte[] data_new) {
        super(length, data_new);
    }
    
    public HashedDeltaData(int length, byte[] data_new, long version) {
        super(length, data_new, version);
    }
    
    public HashedDeltaData(byte[] data_old, byte[] data_new) {
        super(data_old, data_new);
    }
    
    public HashedDeltaData(byte[] data_old, byte[] data_new, long version) {
        super(data_old, data_new, version);
    }
    
    public static byte[] hash(byte[] data_new) {
        if (data_new == null) {
            return null;
        }
        return SHA_256.digest(data_new);
    }
    
    @Override
    public HashedDeltaData setVersion(long version) {
        super.setVersion(version);
        return this;
    }
    
    @Override
    public HashedDeltaData setLength(int length) {
        super.setLength(length);
        return this;
    }
    
    @Override
    public HashedDeltaData setDataNew(byte[] data_new) {
        super.setDataNew(data_new);
        return this;
    }
    
    public byte[] getHash() {
        return hash;
    }
    
    public HashedDeltaData setHash(byte[] hash) {
        if (hash.length != HASH_SIZE_BYTES) {
            throw new IllegalArgumentException();
        }
        this.hash = hash;
        return this;
    }
    
    public HashedDeltaData generateHash(byte[] data_new) {
        setHash(hash(data_new));
        return this;
    }
    
    @Override
    public void set(Copyable copyable) {
        final HashedDeltaData deltaData = Require.clazz(copyable, HashedDeltaData.class);
        if (deltaData != null) {
            setLength(deltaData.length);
            setDataNew(deltaData.data_new);
            setHash(deltaData.hash);
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "hash=" + Arrays.toString(hash) + ", version=" + version + ", length=" + length + ", data_new=" + Arrays.toString(data_new) + '}';
    }
    
    @Override
    public long getBitSize() {
        return super.getBitSize() + (hash == null ? 0 : Byte.SIZE * hash.length);
    }
    
}
