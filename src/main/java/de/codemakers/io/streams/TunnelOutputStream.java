/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import de.codemakers.io.streams.exceptions.StreamClosedException;

import java.io.IOException;
import java.io.OutputStream;

public class TunnelOutputStream extends OutputStream {
    
    protected final OutputStream outputStream;
    protected final BiMap<Byte, EndableOutputStream> outputStreams = Maps.synchronizedBiMap(HashBiMap.create());
    
    public TunnelOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public OutputStream getOutputStream() {
        return outputStream;
    }
    
    public BiMap<Byte, EndableOutputStream> getOutputStreams() {
        return outputStreams;
    }
    
    public EndableOutputStream getOutputStream(byte id) {
        return outputStreams.get(id);
    }
    
    public byte getId(EndableOutputStream endableOutputStream) {
        return outputStreams.inverse().get(endableOutputStream);
    }
    
    public byte getLowestId() {
        return outputStreams.keySet().stream().sorted().findFirst().orElse(Byte.MIN_VALUE);
    }
    
    public byte getHighestId() {
        return outputStreams.keySet().stream().sorted().skip(outputStreams.size() - 1).findFirst().orElse(Byte.MIN_VALUE);
    }
    
    protected synchronized byte getNextId() {
        byte id = Byte.MIN_VALUE;
        while (outputStreams.containsKey(id)) {
            if (id == Byte.MAX_VALUE) {
                throw new ArrayIndexOutOfBoundsException("There is no id left for another " + EndableOutputStream.class.getSimpleName());
            }
            id++;
        }
        return id;
    }
    
    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }
    
    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }
    
    @Override
    public void close() throws IOException {
        outputStream.close();
    }
    
    protected void write(byte id, int b) throws IOException {
        if (!outputStreams.containsKey(id)) {
            throw new StreamClosedException("There is no " + EndableOutputStream.class.getSimpleName() + " with the id " + id);
        }
        write(id & 0xFF);
        write(b);
    }
    
    public EndableOutputStream getOrCreateOutputStream() {
        return getOrCreateOutputStream(getNextId());
    }
    
    public EndableOutputStream getOrCreateOutputStream(byte id) {
        if (outputStreams.containsKey(id)) {
            return outputStreams.get(id);
        }
        final OutputStream outputStream = new OutputStream() {
            private boolean closed = false;
            
            @Override
            public void write(int b) throws IOException {
                if (closed) {
                    throw new StreamClosedException("There is no " + EndableOutputStream.class.getSimpleName() + " with the id " + id);
                }
                TunnelOutputStream.this.write(id, b);
            }
            
            @Override
            public void flush() throws IOException {
                if (closed) {
                    throw new StreamClosedException("There is no " + EndableOutputStream.class.getSimpleName() + " with the id " + id);
                }
                TunnelOutputStream.this.flush();
            }
            
            @Override
            public void close() throws IOException {
                if (closed) {
                    throw new StreamClosedException("There is no " + EndableOutputStream.class.getSimpleName() + " with the id " + id);
                }
                TunnelOutputStream.this.outputStreams.remove(id);
                closed = true;
            }
        };
        final EndableOutputStream endableOutputStream = new EndableOutputStream(outputStream);
        outputStreams.put(id, endableOutputStream);
        return endableOutputStream;
    }
    
}
