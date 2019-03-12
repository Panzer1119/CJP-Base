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

import java.io.IOException;
import java.io.InputStream;

public class PipedInputStream extends InputStream {
    
    /**
     * The default size of the pipe's circular input buffer.
     */
    public static final int DEFAULT_PIPE_SIZE = 1024;
    
    boolean connected = false;
    
    /**
     * The circular buffer into which incoming data is placed.
     */
    protected byte buffer[] = null;
    
    /**
     * The index of the position in the circular buffer at which the
     * next byte of data will be stored when received from the connected
     * {@link PipedOutputStream}. <code>in&lt;0</code> implies the buffer is empty,
     * <code>in==out</code> implies the buffer is full
     */
    protected int in = -1;
    
    /**
     * The index of the position in the circular buffer at which the next
     * byte of data will be read by this {@link PipedInputStream}.
     */
    protected int out = 0;
    
    public PipedInputStream() {
        this(DEFAULT_PIPE_SIZE);
    }
    
    public PipedInputStream(int pipeSize) {
        initPipe(pipeSize);
    }
    
    public PipedInputStream(PipedOutputStream pipedOutputStream) throws IOException {
        this(pipedOutputStream, DEFAULT_PIPE_SIZE);
    }
    
    public PipedInputStream(PipedOutputStream pipedOutputStream, int pipSize) throws IOException {
        connect(pipedOutputStream);
        initPipe(pipSize);
    }
    
    /**
     * Causes this {@link PipedInputStream} to be connected
     * to the {@link PipedOutputStream} <code>pipedOutputStream</code>.
     * If this object is already connected to some
     * other {@link PipedOutputStream}, an {@link IOException}
     * is thrown.
     * <p>
     * If <code>pipedOutputStream</code> is an
     * unconnected {@link PipedOutputStream} and <code>pipedInputStream</code>
     * is an unconnected {@link PipedInputStream}, they
     * may be connected by either the call:
     *
     * <pre><code>pipedInputStream.connect(pipedOutputStream)</code> </pre>
     * <p>
     * or the call:
     *
     * <pre><code>pipedOutputStream.connect(pipedInputStream)</code> </pre>
     * <p>
     * The two calls have the same effect.
     *
     * @param pipedOutputStream The piped output stream to connect to.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void connect(PipedOutputStream pipedOutputStream) throws IOException {
        pipedOutputStream.connect(this);
    }
    
    private void initPipe(int pipeSize) {
        if (pipeSize <= 0) {
            throw new IllegalArgumentException("Pipe Size <= 0");
        }
        buffer = new byte[pipeSize];
    }
    
    @Override
    public int read() throws IOException {
        return 0;
    }
}
