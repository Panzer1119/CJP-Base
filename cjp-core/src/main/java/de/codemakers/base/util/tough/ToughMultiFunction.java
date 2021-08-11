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

public interface ToughMultiFunction<T, R> extends Tough<T, R> {
    
    Logger logger = LogManager.getLogger();
    
    R apply(T... ts) throws Exception;
    
    default R apply(ToughConsumer<Throwable> failure, T... ts) {
        try {
            return apply(ts);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                logger.error(ex);
            }
            return null;
        }
    }
    
    default R applyWithoutException(T... ts) {
        return apply(null, ts);
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
