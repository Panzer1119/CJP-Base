/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.io.streams;

import java.io.Closeable;
import java.io.IOException;

public interface AdvancedStream<T> extends Closeable {
    
    T asBuffered();
    T asInflated();
    T asDeflated();
    T asGZIP() throws IOException;
    byte[] toByteArray() throws IOException;
    
}
