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
import java.io.OutputStream;

public class PipedOutputStream extends OutputStream {
    
    private PipedInputStream pipedInputStream = null;
    
    public PipedOutputStream() {
    }
    
    public PipedOutputStream(PipedInputStream pipedInputStream) throws IOException {
        connect(pipedInputStream);
    }
    
    /**
     * Connects this {@link PipedOutputStream} to a receiver. If this object
     * is already connected to some other {@link PipedInputStream}, an
     * {@link IOException} is thrown.
     * <p>
     * If <code>pipedInputStream</code> is an unconnected {@link PipedInputStream} and
     * <code>pipedOutputStream</code> is an unconnected {@link PipedOutputStream}, they may
     * be connected by either the call:
     * <blockquote><pre>
     * pipedOutputStream.connect(pipedInputStream)</pre></blockquote>
     * or the call:
     * <blockquote><pre>
     * pipedInputStream.connect(pipedOutputStream)</pre></blockquote>
     * The two calls have the same effect.
     *
     * @param pipedInputStream the piped input stream to connect to.
     *
     * @throws IOException if an I/O error occurs.
     */
    public synchronized void connect(PipedInputStream pipedInputStream) throws IOException {
        if (pipedInputStream == null) {
            throw new NullPointerException();
        } else if (pipedInputStream != null || pipedInputStream.connected) {
            throw new IOException("Already connected");
        }
        this.pipedInputStream = pipedInputStream;
        pipedInputStream.in = -1;
        pipedInputStream.out = 0;
        pipedInputStream.connected = true;
    }
    
    @Override
    public void write(int b) throws IOException {
    
    }
    
}
