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

import de.codemakers.base.util.ArrayUtil;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;

import java.io.Serializable;
import java.util.Arrays;

public class DataDelta implements Serializable, Copyable {
    
    protected int length;
    protected int offset;
    protected byte[] delta;
    
    public DataDelta(byte[] delta) {
        this(delta, delta == null ? -1 : delta.length, 0);
    }
    
    public DataDelta(byte[] delta, int length, int offset) {
        this.length = length;
        this.offset = offset;
        this.delta = delta;
    }
    
    public DataDelta(byte[] data_old, byte[] data) {
        this.length = data == null ? -1 : data.length;
        this.offset = 0;
        this.delta = xorBytes(data_old, data);
    }
    
    public static byte[] xorBytes(byte[] data_old, byte[] data) {
        if (data_old == null || data == null) {
            return data;
        }
        return ArrayUtil.xorBytes(data_old, data);
    }
    
    public byte[] getDelta() {
        return delta;
    }
    
    public DataDelta setDelta(byte[] delta) {
        this.delta = delta;
        return this;
    }
    
    public int getLength() {
        return length;
    }
    
    public DataDelta setLength(int length) {
        this.length = length;
        return this;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public DataDelta setOffset(int offset) {
        this.offset = offset;
        return this;
    }
    
    @Override
    public DataDelta copy() {
        return new DataDelta(delta, length, offset);
    }
    
    @Override
    public void set(Copyable copyable) {
        final DataDelta dataDelta = Require.clazz(copyable, DataDelta.class);
        if (dataDelta != null) {
            setLength(dataDelta.getLength());
            setOffset(dataDelta.getOffset());
            setDelta(dataDelta.getDelta());
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "length=" + length + ", offset=" + offset + ", delta=" + Arrays.toString(delta) + '}';
    }
    
    public long getBitSize() {
        return Integer.SIZE * 2 + Byte.SIZE * delta.length;
    }
    
}
