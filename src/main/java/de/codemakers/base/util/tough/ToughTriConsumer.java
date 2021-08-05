/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@FunctionalInterface
public interface ToughTriConsumer<T, U, V> extends Tough<T, Void> {
    
    Logger logger = LogManager.getLogger();
    
    void accept(T t, U u, V v) throws Exception;
    
    default void accept(T t, U u, V v, ToughConsumer<Throwable> failure) {
        try {
            accept(t, u, v);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                logger.error(ex);
            }
        }
    }
    
    default void acceptWithoutException(T t, U u, V v) {
        accept(t, u, v, null);
    }
    
    @Override
    default Void action(T t) throws Exception {
        accept(t, null, null);
        return null;
    }
    
    @Override
    default boolean canConsume() {
        return true;
    }
    
    @Override
    default boolean canSupply() {
        return false;
    }
    
}
