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
import de.codemakers.base.util.tough.Tough;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughRunnable;
import de.codemakers.base.util.tough.ToughSupplier;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Inspired by the RestAction from Austin Keener &amp; Michael Ritter &amp; Florian Spieß
 *
 * @param <T> Type Input
 * @param <R> Type Output
 */
public abstract class Action<T extends Tough, R> {

    final CJP cjp;

    Action() {
        this(CJP.getInstance());
    }

    Action(CJP cjp) {
        Objects.requireNonNull(cjp, "CJP may not be null!");
        this.cjp = cjp;
    }

    public static final <T> ReturningAction<T> ofSupplier(Supplier<T> supplier) {
        return ofToughSupplier(() -> supplier.get());
    }

    public static final <T> ReturningAction<T> ofToughSupplier(ToughSupplier<T> supplier) {
        return new ReturningAction<>(supplier);
    }

    public static final RunningAction ofRunnable(Runnable runnable) {
        return ofToughRunnable(() -> runnable.run());
    }

    public static final RunningAction ofToughRunnable(ToughRunnable runnable) {
        return new RunningAction(runnable);
    }

    public final CJP getCJP() {
        return cjp;
    }

    public final void queue() {
        queue(null, null);
    }

    public final void queue(T success) {
        queue(success, null);
    }

    public abstract void queue(T success, ToughConsumer<Throwable> failure);

    public final void queueSingle() {
        queueSingle(null, null);
    }

    public final void queueSingle(T success) {
        queueSingle(success, null);
    }

    public abstract void queueSingle(T success, ToughConsumer<Throwable> failure);

    public final void queueAfter(long delay, TimeUnit unit) {
        queueAfter(delay, unit, null, null);
    }

    public final void queueAfter(long delay, TimeUnit unit, T success) {
        queueAfter(delay, unit, success, null);
    }

    public final void queueAfter(long delay, TimeUnit unit, T success, ToughConsumer<Throwable> failure) {
        cjp.getScheduledExecutorService().schedule(() -> queue(success, failure), delay, unit);
    }

    public final void queueSingleAfter(long delay, TimeUnit unit) {
        queueSingleAfter(delay, unit, null, null);
    }

    public final void queueSingleAfter(long delay, TimeUnit unit, T success) {
        queueSingleAfter(delay, unit, success, null);
    }

    public final void queueSingleAfter(long delay, TimeUnit unit, T success, ToughConsumer<Throwable> failure) {
        cjp.getScheduledExecutorService().schedule(() -> queueSingle(success, failure), delay, unit);
    }

    public abstract Future<R> submit();

    public abstract Future<R> submitSingle();

    public final R complete() {
        return complete(null);
    }

    public final R complete(ToughConsumer<Throwable> failure) {
        try {
            return submit().get();
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
                return null;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

}
