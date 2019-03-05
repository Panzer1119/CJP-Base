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

public class EndableInputStream extends InputStream {
    
    public static final byte NULL_BYTE = (byte) 0;
    public static final int NULL_BYTE_INT = NULL_BYTE & 0xFF;
    public static final byte ESCAPE_BYTE = Byte.MIN_VALUE;
    public static final int ESCAPE_BYTE_INT = ESCAPE_BYTE & 0xFF;
    public static final int CLOSED_INT = -2;
    public static final int ENDED_INT = -2;
    
    protected final InputStream inputStream;
    
    public EndableInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    @Override
    public int read() throws IOException {
        final int temp = inputStream.read();
        if (temp == ESCAPE_BYTE_INT) {
            return inputStream.read();
        } else if (temp == NULL_BYTE_INT) {
            //TODO Transmission ended
            return ENDED_INT;
        }
        return temp;
    }
    
}
