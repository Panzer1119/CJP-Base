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

package de.codemakers.base.entities.heap;

import java.util.Arrays;

public class DynamicBinaryMaxHeap<T extends Comparable<T>> extends BinaryMaxHeap<T> {
    
    public DynamicBinaryMaxHeap(Class<T> clazz) {
        this(clazz, 10);
    }
    
    public DynamicBinaryMaxHeap(Class<T> clazz, int capacity) {
        super(clazz, capacity);
    }
    
    @Override
    public boolean add(T t) {
        if (isFull()) {
            heap = Arrays.copyOf(heap, heap.length + 1);
        }
        return super.add(t);
    }
    
    @Override
    public T remove(int i) {
        final T removed = super.remove(i);
        if (heap.length >= 1) {
            heap = Arrays.copyOf(heap, heap.length - 1);
        }
        return removed;
    }
    
    @Override
    public String toString() {
        return "DynamicBinaryMaxHeap{" + "heap=" + Arrays.toString(heap) + ", heapSize=" + heapSize + '}';
    }
    
}
