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
public interface ToughSupplier<T> extends Tough<Void, T> {

    T get() throws Exception;

    default T get(ToughConsumer<Throwable> failure) {
        try {
            return get();
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                ex.printStackTrace();
            }
            return null;
        }
    }

    default T getWithoutException() {
        return get(null);
    }

    @Override
    default T action(Void v) throws Exception {
        return get();
    }

    @Override
    default boolean canConsume() {
        return false;
    }

    @Override
    default boolean canSupply() {
        return true;
    }

}
