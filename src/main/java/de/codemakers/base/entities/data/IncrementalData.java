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

import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.interfaces.Version;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class IncrementalData extends Data implements Version {
    
    private final AtomicLong version = new AtomicLong(Long.MIN_VALUE);
    
    public IncrementalData(byte[] data) {
        super(data);
    }
    
    public DeltaData changeData(byte[] data_new) {
        final byte[] data_old = getData();
        setData(data_new);
        return new XORDeltaData(data_old, data_new, version.incrementAndGet());
    }
    
    public ReturningAction<DeltaData> changeDataAction(byte[] data_new) {
        return new ReturningAction<>(() -> changeData(data_new));
    }
    
    public IncrementalData incrementData(DeltaData deltaData) {
        return incrementData(deltaData, false);
    }
    
    public IncrementalData incrementData(DeltaData deltaData, boolean forceIncrement) {
        Objects.requireNonNull(deltaData);
        if (!forceIncrement) {
            if (deltaData.getVersion() == getVersion()) {
                throw new IllegalArgumentException(deltaData.getClass().getName() + "'s version is the same as this version \"" + getVersion() + "\"");
            } else if (Math.abs(deltaData.getVersion() - getVersion()) != 1) {
                throw new IllegalArgumentException(deltaData.getClass().getName() + "'s version \"" + deltaData.getVersion() + "\" is not 1 offset from this version \"" + getVersion() + "\" (offset is " + Math.abs(deltaData.getVersion() - getVersion()) + ")");
            }
        }
        if (deltaData.getLength() < 0 || getLength() < 0) {
            setData(deltaData.data_new);
        } else {
            setData(deltaData.getData(getData()));
        }
        version.set(deltaData.getVersion());
        return this;
    }
    
    public int getLength() {
        return data == null ? -1 : data.length;
    }
    
    @Override
    public IncrementalData setData(byte[] data) {
        super.setData(data);
        return this;
    }
    
    @Override
    public IncrementalData copy() {
        return new IncrementalData(getData());
    }
    
    @Override
    public void set(Copyable copyable) {
        final IncrementalData incrementalData = Require.clazz(copyable, IncrementalData.class);
        if (incrementalData != null) {
            setData(incrementalData.getData());
        }
    }
    
    @Override
    public long getVersion() {
        return version.get();
    }
    
}