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

import java.util.Arrays;

public class PreciseDataDelta extends DataDelta {
    
    protected byte[] indices;
    
    public PreciseDataDelta(byte[] delta) {
        super(delta);
    }
    
    public PreciseDataDelta(byte[] delta, int length, int offset) {
        super(delta, length, offset);
    }
    
    public PreciseDataDelta(byte[] data_old, byte[] data) {
        super(data_old, data);
        if (data_old != null && data != null) {
            final byte[] bytes = new byte[delta.length];
            this.indices = new byte[(data.length + 7) / 8];
            int index = 0;
            for (int i = 0; i < delta.length; i++) {
                if (delta[i] != 0) {
                    bytes[index] = delta[i];
                    this.indices[i / 8] |= (1 << (i % 8));
                    index++;
                }
            }
            delta = new byte[index];
            System.arraycopy(bytes, 0, delta, 0, index);
        }
    }
    
    @Override
    public byte[] getDelta() {
        if (getLength() >= 0) {
            final byte[] bytes = new byte[getLength()];
            int index = 0;
            for (int i = 0; i < bytes.length; i++) {
                if ((this.indices[i / 8] & (1 << (i % 8))) != 0) {
                    bytes[i] = delta[index];
                    index++;
                }
            }
            return bytes;
        }
        return super.getDelta();
    }
    
    @Override
    public DataDelta setDelta(byte[] delta) {
        return super.setDelta(delta);
    }
    
    @Override
    public PreciseDataDelta setLength(int length) {
        super.setLength(length);
        return this;
    }
    
    @Override
    public PreciseDataDelta setOffset(int offset) {
        super.setOffset(offset);
        return this;
    }
    
    @Override
    public PreciseDataDelta copy() {
        return new PreciseDataDelta(delta, length, offset);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "indices=" + Arrays.toString(indices) + ", length=" + length + ", offset=" + offset + ", delta=" + Arrays.toString(delta) + '}';
    }
    
    @Override
    public long getBitSize() {
        return super.getBitSize() + Byte.SIZE * indices.length;
    }
    
}
