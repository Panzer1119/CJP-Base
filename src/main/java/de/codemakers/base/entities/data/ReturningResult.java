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

import de.codemakers.base.entities.Result;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.Returner;
import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.io.SerializationUtil;

import java.io.*;
import java.util.Objects;

public class ReturningResult<T> extends Result {
    
    protected T result;
    
    public ReturningResult(boolean successful, Throwable throwable, T result) {
        super(successful, throwable);
        this.result = result;
    }
    
    public final T getResult() {
        return result;
    }
    
    public final boolean hasResult() {
        return result != null;
    }
    
    public final boolean hasNoResult() {
        return result == null;
    }
    
    public final Returner<T> returner() {
        return new Returner<>(result);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ReturningResult)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final ReturningResult<?> that = (ReturningResult<?>) o;
        return Objects.equals(result, that.result);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), result);
    }
    
    @Override
    public String toString() {
        return "ReturningResult{" + "result=" + result + ", successful=" + successful + ", throwable=" + throwable + '}';
    }
    
    @Override
    public ReturningResult copy() {
        return new ReturningResult(successful, throwable, result);
    }
    
    @Override
    public void set(Copyable copyable) {
        super.set(copyable);
        final ReturningResult<T> returningResult = Require.clazz(copyable, ReturningResult.class);
        if (returningResult != null) {
            result = returningResult.result;
        }
    }
    
    @Override
    public byte[] toBytes() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeBoolean(successful);
        byte[] temp = throwable == null ? null : SerializationUtil.objectToBytes(throwable);
        dataOutputStream.writeInt(arrayLength(temp));
        if (throwable != null) {
            dataOutputStream.write(temp);
        }
        temp = result == null ? null : SerializationUtil.objectToBytes((Serializable) result);
        dataOutputStream.writeInt(arrayLength(temp));
        if (result != null) {
            dataOutputStream.write(temp);
        }
        dataOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public boolean fromBytes(byte[] bytes) throws Exception {
        Objects.requireNonNull(bytes);
        final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        successful = dataInputStream.readBoolean();
        int length = dataInputStream.readInt();
        if (length >= 0) {
            final byte[] temp = new byte[length];
            dataInputStream.read(temp);
            throwable = Require.clazz(SerializationUtil.bytesToObject(temp), Throwable.class);
        }
        length = dataInputStream.readInt();
        if (length >= 0) {
            final byte[] temp = new byte[length];
            dataInputStream.read(temp);
            result = (T) SerializationUtil.bytesToObject(temp);
        }
        dataInputStream.close();
        return true;
    }
    
}
