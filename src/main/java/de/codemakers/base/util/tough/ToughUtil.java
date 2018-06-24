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

package de.codemakers.base.util.tough;

import java.util.function.Consumer;

public class ToughUtil {

    public static final <T, R> void useTough(Tough<T, R> tough, Consumer<ToughConsumer<T>> toughConsumer, Consumer<ToughSupplier<R>> toughSupplier, Consumer<ToughFunction<T, R>> toughFunction, Consumer<ToughRunnable> toughRunnable) {
        if (tough == null) {
            return;
        }
        if (tough.canConsume()) {
            if (tough.canSupply()) {
                if (toughFunction != null) {
                    toughFunction.accept((ToughFunction<T, R>) tough);
                }
            } else {
                if (toughConsumer != null) {
                    toughConsumer.accept((ToughConsumer<T>) tough);
                }
            }
        } else {
            if (tough.canSupply()) {
                if (toughSupplier != null) {
                    toughSupplier.accept((ToughSupplier<R>) tough);
                }
            } else {
                if (toughRunnable != null) {
                    toughRunnable.accept((ToughRunnable) tough);
                }
            }
        }
    }

}
