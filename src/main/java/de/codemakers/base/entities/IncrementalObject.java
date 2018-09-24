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

import de.codemakers.base.entities.data.DataDelta;
import de.codemakers.base.entities.data.IncrementalData;
import de.codemakers.io.SerializationUtil;

import java.io.Serializable;

public class IncrementalObject<T extends Serializable> extends IncrementalData {
    
    private transient T object = null;
    
    public IncrementalObject(byte[] data) {
        super(data);
    }
    
    public IncrementalObject(T object) {
        super(object == null ? null : SerializationUtil.objectToBytesWithoutException(object));
        this.object = object;
    }
    
    public DataDelta changeObject(T object) {
        return changeData(object == null ? null : SerializationUtil.objectToBytesWithoutException(object));
    }
    
    public T getObject() {
        return object;
    }
    
    @Override
    public IncrementalObject<T> incrementData(DataDelta dataDelta) {
        super.incrementData(dataDelta);
        object = getData() == null ? null : (T) SerializationUtil.bytesToObjectWithoutException(getData());
        return this;
    }
    
    @Override
    public IncrementalObject<T> setData(byte[] data) {
        super.setData(data);
        return this;
    }
    
    @Override
    public IncrementalObject<T> copy() {
        return new IncrementalObject<>(data);
    }
    
}
