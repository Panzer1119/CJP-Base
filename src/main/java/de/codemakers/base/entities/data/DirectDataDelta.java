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

import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;

import java.util.Arrays;

public class DirectDataDelta extends DataDelta {
    
    protected byte[] indices;
    
    public DirectDataDelta(byte[] data_old, byte[] data_new) {
        super();
        this.length = data_new == null ? -1 : data_new.length;
        setData(data_old, data_new);
    }
    
    private DirectDataDelta(int length, byte[] data_new, byte[] indices) {
        super(length, data_new);
        this.indices = indices;
    }
    
    @Override
    public byte[] getData() {
        if (data_new == null) {
            return null;
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public DirectDataDelta setData(byte[] data_new) {
        if (data_new == null) {
            this.data_new = null;
            this.indices = null;
            return this;
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public byte[] getData(byte[] data_old) {
        if (data_old == null || data_new == null) {
            return this.data_new;
        }
        final byte[] bytes = new byte[this.length];
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
    
    @Override
    public DirectDataDelta setData(byte[] data_old, byte[] data_new) {
        if (data_old == null || data_new == null) {
            this.data_new = data_new;
            this.indices = null;
            return this;
        }
        this.data_new = new byte[this.length];
        this.indices = new byte[(this.length + 7) / 8];
        int index = 0;
        for (int i = 0; i < data_new.length; i++) {
            if ((i >= data_old.length) || (data_new[i] != data_old[i])) {
                this.data_new[index] = data_new[i];
                this.indices[i / 8] |= (1 << (i % 8));
                index++;
            }
        }
        this.data_new = Arrays.copyOf(this.data_new, index);
        return this;
    }
    
    public byte[] getIndices() {
        return indices;
    }
    
    public DirectDataDelta setIndices(byte[] indices) {
        this.indices = indices;
        return this;
    }
    
    @Override
    public DirectDataDelta copy() {
        return new DirectDataDelta(length, data_new, indices);
    }
    
    @Override
    public void set(Copyable copyable) {
        final DirectDataDelta dataDelta = Require.clazz(copyable, DirectDataDelta.class);
        if (dataDelta != null) {
            setLength(dataDelta.length);
            setDataNew(dataDelta.data_new);
            setIndices(dataDelta.indices);
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "indices=" + Arrays.toString(indices) + ", length=" + length + ", data_new=" + Arrays.toString(data_new) + '}';
    }
    
    @Override
    public long getBitSize() {
        return Integer.SIZE * 2 + Byte.SIZE * data_new.length + Byte.SIZE * indices.length;
    }
    
}
