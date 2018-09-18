/*
 *     Copyright 2018 Paul Hagedorn (Panzer1119)
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

package de.codemakers.io.file.closeable;

import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.io.AdvancedCloseable;

import java.io.Closeable;
import java.io.InputStream;
import java.util.function.Function;

public class AdvancedCloseableInputStream extends AdvancedCloseable<Closeable, InputStream> {
    
    public AdvancedCloseableInputStream(Closeable closeable, InputStream data) {
        super(closeable, data);
    }
    
    @Override
    public <R> R close(Function<InputStream, R> function, ToughConsumer<Throwable> failureClosing) throws Exception {
        return super.close(function, failureClosing);
    }
    
    @Override
    public <R> R close(Function<InputStream, R> function, ToughConsumer<Throwable> failureFunction, ToughConsumer<Throwable> failureClosing) {
        return super.close(function, failureFunction, failureClosing);
    }
    
    @Override
    public <R> R closeWithoutException(Function<InputStream, R> function, ToughConsumer<Throwable> failureFunction) {
        return super.closeWithoutException(function, failureFunction);
    }
    
    @Override
    public <R> R closeWithoutException(Function<InputStream, R> function) {
        return super.closeWithoutException(function);
    }
    
}
