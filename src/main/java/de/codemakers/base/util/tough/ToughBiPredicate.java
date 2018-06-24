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
public interface ToughBiPredicate<T, U> extends Tough<T, Boolean> {

    Boolean test(T t, U u) throws Exception;

    default ToughBiPredicate<T, U> negate() {
        return (t, u) -> !test(t, u);
    }

    default Boolean test(T t, U u, ToughConsumer<Throwable> failure) {
        return test(t, u, false, failure);
    }

    default Boolean test(T t, U u, boolean onError, ToughConsumer<Throwable> failure) {
        try {
            return test(t, u);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                ex.printStackTrace();
            }
            return onError;
        }
    }

    default Boolean testWithoutException(T t, U u) {
        return test(t, u, null);
    }

    default Boolean testWithoutException(T t, U u, boolean onError) {
        return test(t, u, onError, null);
    }

    @Override
    default Boolean action(T t) throws Exception {
        return test(t, null);
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
