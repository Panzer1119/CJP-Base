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

import java.io.Serializable;
import java.util.Arrays;

public abstract class DeltaData implements Serializable, Copyable {
    
    protected int length;
    protected byte[] data_new;
    
    public DeltaData() {
    }
    
    public DeltaData(byte[] data_new) {
        this(data_new == null ? -1 : data_new.length, data_new);
    }
    
    public DeltaData(int length, byte[] data_new) {
        this.length = length;
        this.data_new = data_new;
    }
    
    public abstract byte[] getData();
    
    public abstract DeltaData setData(byte[] data_new);
    
    public abstract byte[] getData(byte[] data_old);
    
    public abstract DeltaData setData(byte[] data_old, byte[] data_new);
    
    public int getLength() {
        return length;
    }
    
    public DeltaData setLength(int length) {
        this.length = length;
        return this;
    }
    
    public byte[] getDataNew() {
        return data_new;
    }
    
    public DeltaData setDataNew(byte[] data_new) {
        this.data_new = data_new;
        return this;
    }
    
    @Override
    public void set(Copyable copyable) {
        final DeltaData deltaData = Require.clazz(copyable, DeltaData.class);
        if (deltaData != null) {
            setLength(deltaData.length);
            setDataNew(deltaData.data_new);
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "length=" + length + ", data_new=" + Arrays.toString(data_new) + '}';
    }
    
    public long getBitSize() {
        return Integer.SIZE * 2 + Byte.SIZE * data_new.length;
    }
    
}
