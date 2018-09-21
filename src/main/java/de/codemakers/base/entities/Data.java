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
import java.util.Objects;

public class Data implements Serializable, Copyable {
    
    protected byte[] data = null;
    
    public Data(byte[] data) {
        this.data = data;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public Data setData(byte[] data) {
        this.data = data;
        return this;
    }
    
    @Override
    public Copyable copy() {
        return new Data(getData());
    }
    
    @Override
    public void set(Copyable copyable) {
        Objects.requireNonNull(copyable);
        final Data data = Require.clazz(copyable, Data.class);
        if (data != null) {
            setData(data.getData());
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Data that = (Data) o;
        return Arrays.equals(data, that.data);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "data=" + Arrays.toString(data) + '}';
    }
    
}
