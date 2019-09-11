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
import de.codemakers.base.util.tough.ToughFunction;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipedStream {
    
    protected InputStream inputStream = new PipedInputStream();
    protected OutputStream outputStream = Standard.silentError(() -> new PipedOutputStream((PipedInputStream) inputStream));
    
    public <R extends InputStream> R convertInputStream(ToughFunction<InputStream, R> toughFunction) {
        final R r = toughFunction.applyWithoutException(inputStream);
        inputStream = r;
        return r;
    }
    
    public <R extends OutputStream> R convertOutputStream(ToughFunction<OutputStream, R> toughFunction) {
        final R r = toughFunction.applyWithoutException(outputStream);
        outputStream = r;
        return r;
    }
    
    public <T extends InputStream> T getInputStream(Class<T> clazz) {
        return (T) inputStream;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }
    
    public <T extends OutputStream> T getOutputStream(Class<T> clazz) {
        return (T) outputStream;
    }
    
    public OutputStream getOutputStream() {
        return outputStream;
    }
    
    @Override
    public String toString() {
        return "PipedStream{" + "inputStream=" + inputStream + ", outputStream=" + outputStream + '}';
    }
    
}
