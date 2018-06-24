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
import de.codemakers.base.util.tough.ToughRunnable;

import java.util.Objects;
import java.util.concurrent.Future;

/**
 * Inspired by the RestAction from Austin Keener &amp; Michael Ritter &amp; Florian Spieß
 */
public class RunningAction extends Action<ToughRunnable, Void> {

    private final ToughRunnable runnable;

    public RunningAction(ToughRunnable runnable) {
        super();
        Objects.requireNonNull(runnable, "runnable may not be null!");
        this.runnable = runnable;
    }

    public RunningAction(CJP cjp, ToughRunnable runnable) {
        super(cjp);
        Objects.requireNonNull(runnable, "runnable may not be null!");
        this.runnable = runnable;
    }

    public final ToughRunnable getRunnable() {
        return runnable;
    }

    public final void direct(ToughConsumer<Throwable> failure) {
        runnable.run(failure);
    }

    public final void direct() {
        runnable.runWithoutException();
    }

    @Override
    public final void queue(ToughRunnable success, ToughConsumer<Throwable> failure) {
        cjp.getFixedExecutorService().submit(() -> run(success, failure));
    }

    @Override
    public final void queueSingle(ToughRunnable success, ToughConsumer<Throwable> failure) {
        cjp.getSingleExecutorService().submit(() -> run(success, failure));
    }

    private void run(ToughRunnable success, ToughConsumer<Throwable> failure) {
        try {
            runnable.run();
            if (success != null) {
                success.actionWithoutException(null);
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
    public final Future<Void> submit() {
        return cjp.getFixedExecutorService().submit(() -> {
            runnable.runWithoutException();
            return null;
        });
    }

    @Override
    public final Future<Void> submitSingle() {
        return cjp.getSingleExecutorService().submit(() -> {
            runnable.runWithoutException();
            return null;
        });
    }

}
