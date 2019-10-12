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
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TunnelInputStream extends InputStream {
    
    public static final int DEFAULT_BUFFER_SIZE = TunnelOutputStream.DEFAULT_BUFFER_SIZE;
    public static final int MAXIMUM_STREAMS_PER_TUNNEL = TunnelOutputStream.MAXIMUM_STREAMS_PER_TUNNEL;
    
    protected final InputStream inputStream;
    protected final BiMap<Byte, EndableInputStream> inputStreams = Maps.synchronizedBiMap(HashBiMap.create());
    private final int bufferSize;
    private final Queue<Byte>[] queues = new Queue[MAXIMUM_STREAMS_PER_TUNNEL];
    
    public TunnelInputStream(InputStream inputStream) {
        this(inputStream, DEFAULT_BUFFER_SIZE);
    }
    
    public TunnelInputStream(InputStream inputStream, int bufferSize) {
        this.inputStream = inputStream;
        this.bufferSize = bufferSize;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }
    
    public BiMap<Byte, EndableInputStream> getInputStreams() {
        return inputStreams;
    }
    
    public EndableInputStream getInputStream(byte id) {
        return inputStreams.get(id);
    }
    
    public byte getId(EndableInputStream endableInputStream) {
        return inputStreams.inverse().get(endableInputStream);
    }
    
    public byte getLowestId() {
        return inputStreams.keySet().stream().sorted().findFirst().orElse(Byte.MIN_VALUE);
    }
    
    public byte getHighestId() {
        return inputStreams.keySet().stream().sorted().skip(inputStreams.size() - 1).findFirst().orElse(Byte.MIN_VALUE);
    }
    
    protected synchronized byte getNextId() {
        byte id = Byte.MIN_VALUE;
        while (inputStreams.containsKey(id)) {
            if (id == Byte.MAX_VALUE) {
                throw new ArrayIndexOutOfBoundsException("There is no id left for another " + EndableInputStream.class.getSimpleName());
            }
            id++;
        }
        return id;
    }
    
    @Override
    public synchronized int read() throws IOException {
        return inputStream.read();
    }
    
    @Override
    public synchronized void close() throws IOException {
        inputStream.close();
    }
    
    protected synchronized byte readIntern() throws IOException {
        final byte id = (byte) (read() & 0xFF);
        final byte[] buffer = new byte[bufferSize];
        read(buffer);
        Queue<Byte> queue = queues[id];
        if (queue == null) {
            //TODO What if we got data for a no longer/never existing InputStream? //Create a new Queue?
            queue = (queues[id] = new ConcurrentLinkedQueue<>()); //TODO Because getOrCreateInputStream already creates queues, but only for new InputStreams...
        }
        for (byte b : buffer) {
            queue.add(b);
        }
        return id;
    }
    
    protected synchronized int read(byte id) throws IOException {
        if (!inputStreams.containsKey(id)) {
            throw new StreamClosedException("There is no " + EndableInputStream.class.getSimpleName() + " with the id " + id);
        }
        final Queue<Byte> queue = queues[id];
        while (queue.isEmpty()) {
            readIntern();
        }
        return queue.remove();
    }
    
    protected synchronized int available(byte id) throws IOException {
        final Queue<Byte> queue = queues[id];
        if (queue == null) {
            return -1;
        }
        return queue.size();
    }
    
    protected synchronized long skip(byte id, long n) throws IOException {
        final Queue<Byte> queue = queues[id];
        if (queue == null) {
            return 0;
        }
        final long remove = Math.min(queue.size(), n);
        for (long l = 0; l < remove; l++) {
            queue.remove();
        }
        return remove;
    }
    
    protected synchronized void removeInputStream(byte id) {
        inputStreams.remove(id);
        if (queues[id] != null) {
            queues[id].clear();
            queues[id] = null;
        }
    }
    
    public synchronized EndableInputStream getOrCreateInputStream() {
        return getOrCreateInputStream(getNextId());
    }
    
    public synchronized EndableInputStream getOrCreateInputStream(byte id) {
        if (inputStreams.containsKey(id)) {
            return inputStreams.get(id);
        }
        queues[id] = new ConcurrentLinkedQueue<>();
        final InputStream inputStream = new InputStream() {
            private boolean closed = false;
            
            @Override
            public synchronized int read() throws IOException {
                if (closed) {
                    throw new StreamClosedException("There is no " + EndableInputStream.class.getSimpleName() + " with the id " + id);
                }
                return TunnelInputStream.this.read(id);
            }
            
            @Override
            public synchronized long skip(long n) throws IOException {
                if (closed) {
                    throw new StreamClosedException("There is no " + EndableInputStream.class.getSimpleName() + " with the id " + id);
                }
                return TunnelInputStream.this.skip(id, n);
            }
            
            @Override
            public synchronized int available() throws IOException {
                if (closed) {
                    throw new StreamClosedException("There is no " + EndableInputStream.class.getSimpleName() + " with the id " + id);
                }
                return TunnelInputStream.this.available(id);
            }
            
            @Override
            public synchronized void close() throws IOException {
                if (closed) {
                    throw new StreamClosedException("There is no " + EndableInputStream.class.getSimpleName() + " with the id " + id);
                }
                TunnelInputStream.this.removeInputStream(id);
                closed = true;
            }
        };
        final EndableInputStream endableInputStream = new EndableInputStream(inputStream);
        inputStreams.put(id, endableInputStream);
        return endableInputStream;
    }
    
}
