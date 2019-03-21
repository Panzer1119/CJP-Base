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

package de.codemakers.base.entities;

import de.codemakers.base.util.interfaces.Resettable;
import de.codemakers.base.util.tough.ToughSupplier;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class History<T> implements Resettable {
    
    protected final Deque<T> stack_main = new ConcurrentLinkedDeque<>();
    protected final Deque<T> stack_temp = new ConcurrentLinkedDeque<>();
    
    public T previous() {
        return previous(() -> null);
    }
    
    public T previous(ToughSupplier<T> defaultValueSupplier) {
        if (stack_main.isEmpty() && defaultValueSupplier != null) {
            return defaultValueSupplier.getWithoutException();
        }
        stack_temp.add(stack_main.remove());
        return current(defaultValueSupplier);
    }
    
    public T current() {
        return current(() -> null);
    }
    
    public T current(ToughSupplier<T> defaultValueSupplier) {
        if (stack_main.isEmpty() && defaultValueSupplier != null) {
            return defaultValueSupplier.getWithoutException();
        }
        return stack_main.element();
    }
    
    public T next() {
        return next(() -> null);
    }
    
    public T next(ToughSupplier<T> defaultValueSupplier) {
        if (stack_temp.isEmpty() && defaultValueSupplier != null) {
            return defaultValueSupplier.getWithoutException();
        }
        stack_main.add(stack_temp.remove());
        return current(defaultValueSupplier);
    }
    
    public boolean isNewest() {
        return getDepth() == 0;
    }
    
    public boolean isOldest() {
        return getDepth() == getSize();
    }
    
    public History<T> add(T t) {
        stack_main.add(t);
        stack_temp.clear();
        return this;
    }
    
    public int getDepth() {
        return stack_temp.size();
    }
    
    public int getMainSize() {
        return stack_main.size();
    }
    
    public int getSize() {
        return stack_main.size() + stack_temp.size();
    }
    
    @Override
    public boolean reset() throws Exception {
        stack_temp.clear();
        return true;
    }
    
}
