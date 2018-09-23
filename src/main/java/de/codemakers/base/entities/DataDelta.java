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

package de.codemakers.base.entities;

import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;

import java.io.Serializable;
import java.util.Arrays;

public class DataDelta implements Serializable, Copyable {
    
    protected int length;
    protected byte[] data_new;
    protected byte[] indices;
    
    public DataDelta(byte[] data_new) {
        this(data_new, data_new == null ? -1 : data_new.length);
    }
    
    public DataDelta(byte[] data_new, int length) {
        this.length = length;
        this.data_new = data_new;
    }
    
    public DataDelta(byte[] data_old, byte[] data_new) {
        this.length = data_new == null ? -1 : data_new.length;
        if (data_old != null && data_new != null) {
            this.data_new = new byte[data_new.length];
            this.indices = new byte[(data_new.length + 7) / 8];
            int index = 0;
            for (int i = 0; i < data_new.length; i++) {
                if (i >= data_old.length) {
                    break;
                }
                if (data_new[i] != data_old[i]) {
                    this.data_new[index] = data_new[i];
                    this.indices[i / 8] |= (1 << (i % 8));
                    index++;
                }
            }
            this.data_new = Arrays.copyOf(this.data_new, index);
        } else {
            this.data_new = data_new;
        }
    }
    
    public DataDelta(byte[] data_new, int length, byte[] indices) {
        this.data_new = data_new;
        this.length = length;
        this.indices = indices;
    }
    
    public byte[] getData(byte[] data_old) {
        final byte[] bytes = new byte[length];
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            if ((this.indices[i / 8] & (1 << (i % 8))) != 0) {
                bytes[i] = data_new[index];
                index++;
            } else if (i < data_old.length) {
                bytes[i] = data_old[i];
            }
        }
        return bytes;
    }
    
    public byte[] getDataNew() {
        return data_new;
    }
    
    public DataDelta setDataNew(byte[] data_new) {
        this.data_new = data_new;
        return this;
    }
    
    public int getLength() {
        return length;
    }
    
    public DataDelta setLength(int length) {
        this.length = length;
        return this;
    }
    
    public byte[] getIndices() {
        return indices;
    }
    
    public DataDelta setIndices(byte[] indices) {
        this.indices = indices;
        return this;
    }
    
    @Override
    public DataDelta copy() {
        return new DataDelta(data_new, length, indices);
    }
    
    @Override
    public void set(Copyable copyable) {
        final DataDelta dataDelta = Require.clazz(copyable, DataDelta.class);
        if (dataDelta != null) {
            setLength(dataDelta.length);
            setDataNew(dataDelta.data_new);
            setIndices(dataDelta.indices);
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "length=" + length + ", data_new=" + Arrays.toString(data_new) + ", indices=" + Arrays.toString(indices) + '}';
    }
    
    public long getBitSize() {
        return Integer.SIZE * 2 + Byte.SIZE * data_new.length + Byte.SIZE * indices.length;
    }
    
}
