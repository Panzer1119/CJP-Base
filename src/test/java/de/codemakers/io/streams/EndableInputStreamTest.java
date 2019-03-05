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

public class EndableInputStreamTest {
    
    public static final void main(String[] args) throws Exception {
        final EndableInputStream endableInputStream = new EndableInputStream(null);
        Logger.log("endableInputStream=" + endableInputStream);
        Logger.log("EndableInputStream.NULL_BYTE=" + EndableInputStream.NULL_BYTE);
        Logger.log("EndableInputStream.NULL_BYTE_INT=" + EndableInputStream.NULL_BYTE_INT);
        Logger.log("EndableInputStream.ESCAPE_BYTE=" + EndableInputStream.ESCAPE_BYTE);
        Logger.log("EndableInputStream.ESCAPE_BYTE_INT=" + EndableInputStream.ESCAPE_BYTE_INT);
        /*
        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
            Logger.log("b=" + b + ", converted=" + (b & 0xFF) + ", double converted=" + ((byte) ((b & 0xFF) & 0xFF)));
        }
        */
    }
    
}
