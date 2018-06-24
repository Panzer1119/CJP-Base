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

package de.codemakers.base.action;

import de.codemakers.base.CJP;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughSupplier;

import java.util.Objects;
import java.util.concurrent.Future;

/**
 * Inspired by the RestAction from Austin Keener &amp; Michael Ritter &amp; Florian Spieß
 *
 * @param <T> Type input
 */
public class ReturningAction<T> extends Action<ToughConsumer<T>, T> {

    private final ToughSupplier<T> supplier;

    public ReturningAction(ToughSupplier<T> supplier) {
        super();
        Objects.requireNonNull(supplier, "supplier may not be null!");
        this.supplier = supplier;
    }

    public ReturningAction(CJP cjp, ToughSupplier<T> supplier) {
        super(cjp);
        Objects.requireNonNull(supplier, "supplier may not be null!");
        this.supplier = supplier;
    }

    public final ToughSupplier<T> getSupplier() {
        return supplier;
    }

    public final T direct(ToughConsumer<Throwable> failure) {
        return supplier.get(failure);
    }

    public final T direct() {
        return supplier.getWithoutException();
    }

    @Override
    public final void queue(ToughConsumer<T> success, ToughConsumer<Throwable> failure) {
        cjp.getFixedExecutorService().submit(() -> run(success, failure));
    }

    @Override
    public final void queueSingle(ToughConsumer<T> success, ToughConsumer<Throwable> failure) {
        cjp.getSingleExecutorService().submit(() -> run(success, failure));
    }

    private final void run(ToughConsumer<T> success, ToughConsumer<Throwable> failure) {
        try {
            final T t = supplier.get();
            if (success != null) {
                success.acceptWithoutException(t);
            }
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public final Future<T> submit() {
        return cjp.getFixedExecutorService().submit(supplier::getWithoutException);
    }

    @Override
    public final Future<T> submitSingle() {
        return cjp.getSingleExecutorService().submit(supplier::getWithoutException);
    }

}
