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

package de.codemakers.io.streams;

import de.codemakers.base.entities.IncrementalObject;

import java.io.*;
import java.util.Objects;

public class IncrementalObjectOutputStream<T extends Serializable> extends ObjectOutputStream {
    
    private final ObjectOutputStream objectOutputStream;
    private final IncrementalObject<T> incrementalObject = new IncrementalObject(null);
    
    public IncrementalObjectOutputStream(OutputStream out) throws IOException {
        super();
        Objects.requireNonNull(out);
        this.objectOutputStream = new ObjectOutputStream(out);
    }
    
    @Override
    public void writeObjectOverride(Object object) throws IOException {
        objectOutputStream.writeObject(incrementalObject.changeObject((T) object));
    }
    
    @Override
    public void useProtocolVersion(int version) throws IOException {
        objectOutputStream.useProtocolVersion(version);
    }
    
    @Override
    public void writeUnshared(Object obj) throws IOException {
        objectOutputStream.writeUnshared(obj);
    }
    
    @Override
    public void defaultWriteObject() throws IOException {
        objectOutputStream.defaultWriteObject();
    }
    
    @Override
    public PutField putFields() throws IOException {
        return objectOutputStream.putFields();
    }
    
    @Override
    public void writeFields() throws IOException {
        objectOutputStream.writeFields();
    }
    
    @Override
    public void reset() throws IOException {
        objectOutputStream.reset();
    }
    
    @Override
    protected void annotateClass(Class<?> cl) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void annotateProxyClass(Class<?> cl) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Object replaceObject(Object obj) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected boolean enableReplaceObject(boolean enable) throws SecurityException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void writeStreamHeader() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void writeClassDescriptor(ObjectStreamClass desc) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void write(int val) throws IOException {
        objectOutputStream.write(val);
    }
    
    @Override
    public void write(byte[] buf) throws IOException {
        objectOutputStream.write(buf);
    }
    
    @Override
    public void write(byte[] buf, int off, int len) throws IOException {
        objectOutputStream.write(buf, off, len);
    }
    
    @Override
    public void flush() throws IOException {
        objectOutputStream.flush();
    }
    
    @Override
    protected void drain() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void close() throws IOException {
        objectOutputStream.close();
    }
    
    @Override
    public void writeBoolean(boolean val) throws IOException {
        objectOutputStream.writeBoolean(val);
    }
    
    @Override
    public void writeByte(int val) throws IOException {
        objectOutputStream.writeByte(val);
    }
    
    @Override
    public void writeShort(int val) throws IOException {
        objectOutputStream.writeShort(val);
    }
    
    @Override
    public void writeChar(int val) throws IOException {
        objectOutputStream.writeChar(val);
    }
    
    @Override
    public void writeInt(int val) throws IOException {
        objectOutputStream.writeInt(val);
    }
    
    @Override
    public void writeLong(long val) throws IOException {
        objectOutputStream.writeLong(val);
    }
    
    @Override
    public void writeFloat(float val) throws IOException {
        objectOutputStream.writeFloat(val);
    }
    
    @Override
    public void writeDouble(double val) throws IOException {
        objectOutputStream.writeDouble(val);
    }
    
    @Override
    public void writeBytes(String str) throws IOException {
        objectOutputStream.writeBytes(str);
    }
    
    @Override
    public void writeChars(String str) throws IOException {
        objectOutputStream.writeChars(str);
    }
    
    @Override
    public void writeUTF(String str) throws IOException {
        objectOutputStream.writeUTF(str);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "objectOutputStream=" + objectOutputStream + ", incrementalObject=" + incrementalObject + '}';
    }
    
}
