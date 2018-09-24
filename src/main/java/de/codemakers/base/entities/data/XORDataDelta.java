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

import de.codemakers.base.util.ArrayUtil;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;

import java.util.Arrays;

public class XORDataDelta extends DataDelta {
    
    protected byte[] indices;
    
    public XORDataDelta(byte[] data_old, byte[] data_new) {
        super();
        this.length = data_new == null ? -1 : data_new.length;
        setData(data_old, data_new);
    }
    
    private XORDataDelta(int length, byte[] data_new, byte[] indices) {
        super(length, data_new);
        this.indices = indices;
    }
    
    @Override
    public byte[] getData() {
        if (data_new == null) {
            return null;
        }
        final byte[] bytes = new byte[this.length];
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            if ((this.indices[i / 8] & (1 << (i % 8))) != 0) {
                bytes[i] = data_new[index];
                index++;
            }
        }
        return bytes;
    }
    
    @Override
    public XORDataDelta setData(byte[] data_new) {
        if (data_new == null) {
            this.data_new = null;
            this.indices = null;
            return this;
        }
        this.data_new = new byte[this.length];
        this.indices = new byte[(this.length + 7) / 8];
        int index = 0;
        for (int i = 0; i < data_new.length; i++) {
            if (data_new[i] != 0) {
                this.data_new[index] = data_new[i];
                this.indices[i / 8] |= (1 << (i % 8));
                index++;
            }
        }
        this.data_new = Arrays.copyOf(this.data_new, index);
        return this;
    }
    
    @Override
    public byte[] getData(byte[] data_old) {
        if (data_old == null || data_new == null) {
            return this.data_new;
        }
        return ArrayUtil.xorBytes(data_old, getData(), getLength() > data_old.length);
    }
    
    @Override
    public XORDataDelta setData(byte[] data_old, byte[] data_new) {
        if (data_old == null || data_new == null) {
            this.data_new = data_new;
            this.indices = null;
            return this;
        }
        return setData(ArrayUtil.xorBytes(data_old, data_new, data_new.length > data_old.length));
    }
    
    public byte[] getIndices() {
        return indices;
    }
    
    public XORDataDelta setIndices(byte[] indices) {
        this.indices = indices;
        return this;
    }
    
    @Override
    public XORDataDelta copy() {
        return new XORDataDelta(length, data_new, indices);
    }
    
    @Override
    public void set(Copyable copyable) {
        final XORDataDelta dataDelta = Require.clazz(copyable, XORDataDelta.class);
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
