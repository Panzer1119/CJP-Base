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

package de.codemakers.io;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.interfaces.Hasher;
import de.codemakers.base.util.tough.ToughBiConsumer;

import java.io.InputStream;

public class IOUtil {
    
    public static final int STANDARD_BUFFER_SIZE = 1024;
    
    public static boolean processInputStream(InputStream inputStream, ToughBiConsumer<byte[], Integer> bufferConsumer) {
        return processInputStream(inputStream, STANDARD_BUFFER_SIZE, bufferConsumer);
    }
    
    public static boolean processInputStream(InputStream inputStream, int bufferSize, ToughBiConsumer<byte[], Integer> bufferConsumer) {
        return processInputStream(inputStream, new byte[bufferSize], bufferConsumer);
    }
    
    public static boolean processInputStream(InputStream inputStream, final byte[] buffer, ToughBiConsumer<byte[], Integer> bufferConsumer) {
        try {
            int read = -1;
            while ((read = inputStream.read(buffer)) != -1) {
                bufferConsumer.accept(buffer, read);
            }
            inputStream.close();
            return true;
        } catch (Exception ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    public static byte[] hashInputStream(InputStream inputStream, Hasher hasher) {
        return hashInputStream(inputStream, STANDARD_BUFFER_SIZE, hasher);
    }
    
    public static byte[] hashInputStream(InputStream inputStream, int bufferSize, Hasher hasher) {
        if (!processInputStream(inputStream, bufferSize, (buffer, read) -> hasher.update(buffer, 0, read))) {
            return null;
        }
        return hasher.hashWithoutException();
    }
    
    public static Hasher loadInputStreamToHasher(InputStream inputStream, Hasher hasher) {
        return loadInputStreamToHasher(inputStream, STANDARD_BUFFER_SIZE, hasher);
    }
    
    public static Hasher loadInputStreamToHasher(InputStream inputStream, int bufferSize, Hasher hasher) {
        if (!processInputStream(inputStream, bufferSize, (buffer, read) -> hasher.update(buffer, 0, read))) {
            return null;
        }
        return hasher;
    }
    
}
