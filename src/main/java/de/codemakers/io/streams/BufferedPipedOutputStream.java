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

import de.codemakers.base.Standard;
import de.codemakers.io.streams.exceptions.StreamClosedException;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferedPipedOutputStream extends PipedOutputStream {
    
    protected final Object lock = new Object();
    protected final Queue<byte[]> buffer = new ConcurrentLinkedQueue<>();
    Thread thread = Standard.toughThread(this::loop);
    
    public BufferedPipedOutputStream() {
        super();
    }
    
    public BufferedPipedOutputStream(PipedInputStream pipedInputStream) throws IOException {
        super(pipedInputStream);
    }
    
    private void loop() throws Exception {
        Thread.currentThread().setName(this + "-Writing-" + Thread.class.getSimpleName());
        try {
            while (pipedInputStream != null && pipedInputStream.connected) {
                final byte[] temp = buffer.poll();
                if (temp == null) {
                    synchronized (lock) {
                        lock.wait();
                    }
                    continue;
                }
                super.write(temp, 0, temp.length);
                //lock.notify(); //TODO Is this creating a deadlock?
            }
            if (pipedInputStream != null) {
                throw new StreamClosedException();
            }
        } catch (InterruptedException ex) {
            //Nothing probably close was called
        }
    }
    
    @Override
    public synchronized void connect(PipedInputStream pipedInputStream) throws IOException {
        super.connect(pipedInputStream);
        thread.start();
    }
    
    @Override
    public void write(int b) throws IOException {
        buffer.add(new byte[] {(byte) (b & 0xFF)});
        synchronized (lock) {
            lock.notify();
        }
    }
    
    @Override
    public void write(byte[] data, int off, int len) throws IOException {
        final byte[] temp = new byte[len - off];
        System.arraycopy(data, off, temp, 0, len);
        buffer.add(temp);
        synchronized (lock) {
            lock.notify();
        }
    }
    
    @Override
    public synchronized void flush() throws IOException {
        synchronized (lock) {
            lock.notify();
            //lock.wait(); //TODO Is this creating a deadlock?
        }
        super.flush();
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        thread.interrupt(); //TODO Do more to end the thread? (Because what happens when the thread is blocked by the super.write method?)
    }
    
}
