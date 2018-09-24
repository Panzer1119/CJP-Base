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
import de.codemakers.base.entities.ObjectHolder;
import de.codemakers.base.entities.data.DataDelta;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class IncrementalObjectInputStream<T extends Serializable> extends ObjectInputStream {
    
    private final ObjectInputStream objectInputStream;
    private final Map<Long, IncrementalObject<T>> incrementalObjects = new ConcurrentHashMap<>();
    
    public IncrementalObjectInputStream(InputStream in) throws IOException {
        super();
        Objects.requireNonNull(in);
        this.objectInputStream = new ObjectInputStream(in);
    }
    
    public T readIncrementalObject() throws IOException, ClassNotFoundException {
        final Object object = objectInputStream.readObject();
        if (object instanceof ObjectHolder) {
            if (((ObjectHolder) object).getObject() instanceof IncrementalObject) {
                final ObjectHolder<IncrementalObject<T>> objectHolder = (ObjectHolder<IncrementalObject<T>>) object;
                incrementalObjects.put(objectHolder.getId(), objectHolder.getObject());
                return objectHolder.getObject().getObject();
            } else if (((ObjectHolder) object).getObject() instanceof DataDelta) {
                final ObjectHolder<DataDelta> objectHolder = (ObjectHolder<DataDelta>) object;
                final IncrementalObject<T> incrementalObject = incrementalObjects.get(objectHolder.getId());
                return incrementalObject.incrementData(objectHolder.getObject()).getObject();
            }
            return (T) object; //FIXME This is not Exception-Friendly
        }
        return (T) object; //FIXME This is not Exception-Friendly
    }
    
    @Override
    protected Object readObjectOverride() throws IOException, ClassNotFoundException {
        return readIncrementalObject();
    }
    
    @Override
    public Object readUnshared() throws IOException, ClassNotFoundException {
        return objectInputStream.readUnshared();
    }
    
    @Override
    public void defaultReadObject() throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }
    
    @Override
    public GetField readFields() throws IOException, ClassNotFoundException {
        return objectInputStream.readFields();
    }
    
    @Override
    public void registerValidation(ObjectInputValidation obj, int prio) throws NotActiveException, InvalidObjectException {
        objectInputStream.registerValidation(obj, prio);
    }
    
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Class<?> resolveProxyClass(String[] interfaces) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Object resolveObject(Object obj) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected boolean enableResolveObject(boolean enable) throws SecurityException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void readStreamHeader() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected ObjectStreamClass readClassDescriptor() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int read() throws IOException {
        return objectInputStream.read();
    }
    
    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        return objectInputStream.read(buf, off, len);
    }
    
    @Override
    public int available() throws IOException {
        return objectInputStream.available();
    }
    
    @Override
    public void close() throws IOException {
        objectInputStream.close();
    }
    
    @Override
    public boolean readBoolean() throws IOException {
        return objectInputStream.readBoolean();
    }
    
    @Override
    public byte readByte() throws IOException {
        return objectInputStream.readByte();
    }
    
    @Override
    public int readUnsignedByte() throws IOException {
        return objectInputStream.readUnsignedByte();
    }
    
    @Override
    public char readChar() throws IOException {
        return objectInputStream.readChar();
    }
    
    @Override
    public short readShort() throws IOException {
        return objectInputStream.readShort();
    }
    
    @Override
    public int readUnsignedShort() throws IOException {
        return objectInputStream.readUnsignedShort();
    }
    
    @Override
    public int readInt() throws IOException {
        return objectInputStream.readInt();
    }
    
    @Override
    public long readLong() throws IOException {
        return objectInputStream.readLong();
    }
    
    @Override
    public float readFloat() throws IOException {
        return objectInputStream.readFloat();
    }
    
    @Override
    public double readDouble() throws IOException {
        return objectInputStream.readDouble();
    }
    
    @Override
    public void readFully(byte[] buf) throws IOException {
        objectInputStream.readFully(buf);
    }
    
    @Override
    public void readFully(byte[] buf, int off, int len) throws IOException {
        objectInputStream.readFully(buf, off, len);
    }
    
    @Override
    public int skipBytes(int len) throws IOException {
        return objectInputStream.skipBytes(len);
    }
    
    @Override
    public String readLine() throws IOException {
        return objectInputStream.readLine();
    }
    
    @Override
    public String readUTF() throws IOException {
        return objectInputStream.readUTF();
    }
    
    @Override
    public int read(byte[] b) throws IOException {
        return objectInputStream.read(b);
    }
    
    @Override
    public long skip(long n) throws IOException {
        return objectInputStream.skip(n);
    }
    
    @Override
    public synchronized void mark(int readlimit) {
        objectInputStream.mark(readlimit);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        objectInputStream.reset();
    }
    
    @Override
    public boolean markSupported() {
        return objectInputStream.markSupported();
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "objectInputStream=" + objectInputStream + ", incrementalObjects=" + incrementalObjects + '}';
    }
    
}
