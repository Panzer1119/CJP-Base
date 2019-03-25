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

import de.codemakers.base.exceptions.RethrownRuntimeException;
import de.codemakers.base.util.tough.ToughFunction;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class InputStreamConverter implements ToughFunction<InputStream, InputStream> {
    
    protected ToughFunction<InputStream, InputStream> inputStreamFunction;
    
    public InputStreamConverter() {
        this(null);
    }
    
    public InputStreamConverter(ToughFunction<InputStream, InputStream> inputStreamFunction) {
        this.inputStreamFunction = inputStreamFunction;
    }
    
    public ToughFunction<InputStream, InputStream> getInputStreamFunction() {
        return inputStreamFunction;
    }
    
    public InputStreamConverter setInputStreamFunction(ToughFunction<InputStream, InputStream> inputStreamFunction) {
        this.inputStreamFunction = inputStreamFunction;
        return this;
    }
    
    public InputStreamConverter addInputStreamFunction(ToughFunction<InputStream, InputStream> inputStreamFunction) {
        if (this.inputStreamFunction == null || inputStreamFunction == null) {
            this.inputStreamFunction = inputStreamFunction;
        } else {
            final ToughFunction<InputStream, InputStream> temp = this.inputStreamFunction;
            this.inputStreamFunction = (inputStream) -> inputStreamFunction.apply(temp.apply(inputStream));
        }
        return this;
    }
    
    @Override
    public InputStream apply(InputStream inputStream) throws Exception {
        if (inputStreamFunction == null || inputStream == null) {
            return null;
        }
        return inputStreamFunction.apply(inputStream);
    }
    
    public <T extends InputStream> T convert(Class<T> clazz, InputStream inputStream) {
        return (T) applyWithoutException(inputStream);
    }
    
    public <T extends InputStream> T convert(InputStream inputStream) {
        return (T) applyWithoutException(inputStream);
    }
    
    public InputStreamConverter toBufferedInputStream() {
        return addInputStreamFunction(BufferedInputStream::new);
    }
    
    public InputStreamConverter toBufferedInputStream(final int bufferSize) {
        return addInputStreamFunction((inputStream) -> new BufferedInputStream(inputStream, bufferSize));
    }
    
    public InputStreamConverter toDataInputStream() {
        return addInputStreamFunction(DataInputStream::new);
    }
    
    public InputStreamConverter toObjectInputStream() {
        try {
            return addInputStreamFunction(ObjectInputStream::new);
        } catch (Exception ex) {
            throw new RethrownRuntimeException(ex);
        }
    }
    
    public InputStreamConverter toDeflaterInputStream() {
        return addInputStreamFunction(DeflaterInputStream::new);
    }
    
    public InputStreamConverter toDeflaterInputStream(Deflater deflater) {
        return addInputStreamFunction((inputStream) -> new DeflaterInputStream(inputStream, deflater));
    }
    
    public InputStreamConverter toInflaterInputStream() {
        return addInputStreamFunction(InflaterInputStream::new);
    }
    
    public InputStreamConverter toInflaterInputStream(Inflater inflater) {
        return addInputStreamFunction((inputStream) -> new InflaterInputStream(inputStream, inflater));
    }
    
    public InputStreamConverter toCipherInputStream(Cipher cipher) {
        return addInputStreamFunction((inputStream) -> new CipherInputStream(inputStream, cipher));
    }
    
    public InputStreamBuilder toInputStreamBuilder(InputStream inputStream) {
        return new InputStreamBuilder(convert(inputStream));
    }
    
    @Override
    public String toString() {
        return "InputStreamConverter{" + "inputStreamFunction=" + inputStreamFunction + '}';
    }
    
}
