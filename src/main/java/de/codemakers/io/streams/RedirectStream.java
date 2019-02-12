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

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.interfaces.Startable;
import de.codemakers.base.util.interfaces.Stoppable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class RedirectStream<I extends InputStream, O extends OutputStream> implements Startable, Stoppable {
    
    protected int period;
    protected final I inputStream;
    protected final O outputStream;
    protected boolean running = false;
    protected transient final byte[] buffer;
    protected transient Thread thread = null;
    
    public RedirectStream(I inputStream, O outputStream) {
        this(inputStream, outputStream, 100);
    }
    
    public RedirectStream(I inputStream, O outputStream, int period) {
        this(inputStream, outputStream, period, 1024);
    }
    
    public RedirectStream(I inputStream, O outputStream, int period, int bufferSize) {
        Objects.requireNonNull(inputStream);
        Objects.requireNonNull(outputStream);
        this.buffer = new byte[bufferSize];
        this.period = period;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    
    public int getBufferSize() {
        return buffer.length;
    }
    
    public int getPeriod() {
        return period;
    }
    
    public RedirectStream setPeriod(int period) {
        this.period = period;
        return this;
    }
    
    public I getInputStream() {
        return inputStream;
    }
    
    public O getOutputStream() {
        return outputStream;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    protected void update() throws Exception {
        inputStream.read(buffer);
        outputStream.write(buffer);
    }
    
    @Override
    public String toString() {
        return "RedirectStream{" + "bufferSize=" + getBufferSize() + ", period=" + period + ", inputStream=" + inputStream + ", outputStream=" + outputStream + ", thread=" + thread + '}';
    }
    
    @Override
    public boolean start() throws Exception {
        if (isRunning()) {
            return false;
        }
        thread = new Thread(() -> {
            try {
                while (running) {
                    update();
                    Thread.sleep(period);
                }
            } catch (Exception ex) {
                Logger.handleError(ex);
            }
        });
        running = true;
        thread.start();
        return isRunning();
    }
    
    @Override
    public boolean stop() throws Exception {
        if (!isRunning()) {
            return false;
        }
        running = false;
        thread.join();
        final boolean success = !thread.isAlive();
        thread = null;
        return success;
    }
    
}
