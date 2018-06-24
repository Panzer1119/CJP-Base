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

@FunctionalInterface
public interface ToughFunction<T, R> extends Tough<T, R> {

    R apply(T t) throws Exception;

    default R apply(T t, ToughConsumer<Throwable> failure) {
        try {
            return apply(t);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                ex.printStackTrace();
            }
            return null;
        }
    }

    default R applyWithoutException(T t) {
        return apply(t, null);
    }

    @Override
    default R action(T t) throws Exception {
        return apply(t);
    }

    @Override
    default boolean canConsume() {
        return true;
    }

    @Override
    default boolean canSupply() {
        return true;
    }

}
