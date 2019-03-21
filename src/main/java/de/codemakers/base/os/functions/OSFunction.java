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

package de.codemakers.base.os.functions;

import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughFunction;

public abstract class OSFunction<T, R> implements ToughFunction<T, R> {
    
    private final String name;
    
    public OSFunction() {
        this(null);
    }
    
    public OSFunction(String name) {
        if (name == null) {
            name = getClass().getName();
        }
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public ReturningAction<R> applyAction(T t) {
        return new ReturningAction<>(() -> apply(t));
    }
    
    public R apply() throws Exception {
        return apply((T) null);
    }
    
    public R apply(ToughConsumer<Throwable> failure) {
        return apply(null, failure);
    }
    
    public R applyWithoutException() {
        return applyWithoutException(null);
    }
    
    public ReturningAction<R> applyAction() {
        return applyAction(null);
    }
    
}
