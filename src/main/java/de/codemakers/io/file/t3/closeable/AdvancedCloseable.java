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

package de.codemakers.io.file.t3.closeable;

import de.codemakers.base.exceptions.CJPRuntimeException;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

public class AdvancedCloseable<T extends Closeable, D> implements Closeable {
    
    private final T closeable;
    private final D data;
    private boolean closed = false;
    
    public AdvancedCloseable(T closeable, D data) {
        Objects.requireNonNull(closeable);
        this.closeable = closeable;
        this.data = data;
    }
    
    public final T getCloseable() {
        return closeable;
    }
    
    public final D getData() {
        return data;
    }
    
    public final boolean isClosed() {
        return closed;
    }
    
    public void preClose(T closeable, D data) throws IOException {
    }
    
    public void postClose(T closeable, D data) throws IOException {
    }
    
    @Override
    public final void close() throws IOException {
        if (closed) {
            throw new CJPRuntimeException(getClass().getSimpleName() + " already closed");
        }
        preClose(closeable, data);
        closeable.close();
        postClose(closeable, data);
        closed = true;
    }
    
    public final void close(ToughConsumer<Throwable> failure) {
        try {
            close();
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
        }
    }
    
    public final void closeWithoutException() {
        close(null);
    }
    
    public <R> R close(Function<D, R> function, ToughConsumer<Throwable> failureClosing) throws Exception {
        R r = null;
        if (function != null) {
            r = function.apply(data);
        }
        close(failureClosing);
        return r;
    }
    
    public <R> R close(Function<D, R> function, ToughConsumer<Throwable> failureFunction, ToughConsumer<Throwable> failureClosing) {
        try {
            return close(function, failureClosing);
        } catch (Exception ex) {
            if (failureFunction != null) {
                failureFunction.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            close(failureClosing);
            return null;
        }
    }
    
    public <R> R closeWithoutException(Function<D, R> function, ToughConsumer<Throwable> failureFunction) {
        return close(function, failureFunction, null);
    }
    
    public <R> R closeWithoutException(Function<D, R> function) {
        return closeWithoutException(function, null);
    }
    
}
