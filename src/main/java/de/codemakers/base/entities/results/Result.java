/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

package de.codemakers.base.entities.results;

import de.codemakers.base.exceptions.RethrownRuntimeException;
import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.ByteSerializable;
import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.tough.ToughRunnable;
import de.codemakers.io.SerializationUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Objects;

public class Result implements ByteSerializable, Copyable {
    
    protected boolean successful;
    protected Throwable throwable;
    
    public Result(ToughRunnable runnable) {
        try {
            runnable.run();
            this.successful = true;
        } catch (Exception ex) {
            this.throwable = ex;
        }
    }
    
    public Result(boolean successful, Throwable throwable) {
        this.successful = successful;
        this.throwable = throwable;
    }
    
    public final boolean wasSuccessful() {
        return successful;
    }
    
    public final boolean wasNotSuccessful() {
        return !successful;
    }
    
    public final Throwable getThrowable() {
        return throwable;
    }
    
    public final boolean hasThrowable() {
        return throwable != null;
    }
    
    public final boolean hasNoThrowable() {
        return throwable == null;
    }
    
    public final void throwError() {
        if (throwable != null) {
            throw new RethrownRuntimeException(throwable);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Result)) {
            return false;
        }
        final Result result = (Result) o;
        return successful == result.successful && Objects.equals(throwable, result.throwable);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(successful, throwable);
    }
    
    @Override
    public String toString() {
        return "Result{" + "successful=" + successful + ", throwable=" + throwable + '}';
    }
    
    @Override
    public Result copy() {
        return new Result(successful, throwable);
    }
    
    @Override
    public void set(Copyable copyable) {
        final Result result = Require.clazz(copyable, Result.class);
        if (result != null) {
            successful = result.successful;
            throwable = result.throwable;
        }
    }
    
    @Override
    public byte[] toBytes() throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeBoolean(successful);
        final byte[] temp = throwable == null ? null : SerializationUtil.objectToBytes(throwable);
        dataOutputStream.writeInt(arrayLength(temp));
        if (throwable != null) {
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
        final int length = dataInputStream.readInt();
        if (length >= 0) {
            final byte[] temp = new byte[length];
            dataInputStream.read(temp);
            throwable = Require.clazz(SerializationUtil.bytesToObject(temp), Throwable.class);
        }
        dataInputStream.close();
        return true;
    }
    
}
