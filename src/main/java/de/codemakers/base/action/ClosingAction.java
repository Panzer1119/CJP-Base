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

package de.codemakers.base.action;

import de.codemakers.base.CJP;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughSupplier;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Future;

public class ClosingAction<T extends Closeable> extends Action<ToughConsumer<T>, T> {
    
    protected final ToughSupplier<T> supplier;
    
    public ClosingAction(ToughSupplier<T> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier, "supplier may not be null!");
    }
    
    public ClosingAction(CJP cjp, ToughSupplier<T> supplier) {
        super(cjp);
        this.supplier = Objects.requireNonNull(supplier, "supplier may not be null!");
    }
    
    @Override
    public void queue(ToughConsumer<T> success, ToughConsumer<Throwable> failure) {
        cjp.getFixedExecutorService().submit(() -> run(success, failure));
    }
    
    @Override
    public void queueSingle(ToughConsumer<T> success, ToughConsumer<Throwable> failure) {
        cjp.getSingleExecutorService().submit(() -> run(success, failure));
    }
    
    protected void run(ToughConsumer<T> success, ToughConsumer<Throwable> failure) {
        try (final T t = supplier.get(failure)) {
            if (success != null) {
                success.acceptWithoutException(t);
            }
        } catch (IOException ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
        }
    }
    
    @Override
    public Future<T> submit() {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " needs to make sure it can close its supplied value");
    }
    
    @Override
    public Future<T> submitSingle() {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " needs to make sure it can close its supplied value");
    }
    
    @Override
    public void consume(ToughConsumer<T> consumer) throws Exception {
        try (final T t = supplier.get()) {
            consumer.accept(t);
        }
    }
    
}
