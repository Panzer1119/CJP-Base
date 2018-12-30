/*
 *     Copyright 2018 Paul Hagedorn (Panzer1119)
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

import java.lang.reflect.Array;
import java.util.*;

public class BinaryMaxHeap<T extends Comparable<T>> implements Collection<T> {
    
    public static final int DIMENSION = 2;
    public static boolean CLEAR_REMOVED_INDICES = false;
    
    protected T[] heap;
    protected int heapSize = 0;
    
    public BinaryMaxHeap(Class<T> clazz, int capacity) {
        heap = (T[]) Array.newInstance(clazz, capacity);
    }
    
    @Override
    public int size() {
        return heapSize;
    }
    
    @Override
    public boolean isEmpty() {
        return heapSize == 0;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new ArrayList<>(Arrays.asList(heap)).iterator();
    }
    
    @Override
    public Object[] toArray() {
        return heap;
    }
    
    @Override
    public <T1> T1[] toArray(T1[] array) {
        if (array.length < heapSize) {
            return (T1[]) Arrays.copyOf(heap, heapSize, array.getClass());
        }
        System.arraycopy(heap, 0, array, 0, heapSize);
        if (array.length > heapSize) {
            array[heapSize] = null;
        }
        return array;
    }
    
    public boolean isFull() {
        return heapSize >= heap.length;
    }
    
    protected static int parent(int i) {
        return (i - 1) / DIMENSION;
    }
    
    protected static int kthChild(int i, int k) {
        return DIMENSION * i + k;
    }
    
    public int indexOf(Object object) {
        if (object == null) {
            for (int i = 0; i < heapSize; i++) {
                if (heap[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < heapSize; i++) {
                if (object.equals(heap[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public int lastIndexOf(Object object) {
        if (object == null) {
            for (int i = heapSize - 1; i >= 0; i--) {
                if (heap[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = heapSize - 1; i >= 0; i--) {
                if (object.equals(heap[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public boolean contains(Object object) {
        return indexOf(object) >= 0;
    }
    
    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object object : collection)
            if (!contains(object)) {
                return false;
            }
        return true;
    }
    
    @Override
    public boolean add(T t) {
        if (isFull()) {
            throw new IndexOutOfBoundsException("Heap is full");
        }
        heap[heapSize] = t;
        heapifyUp(heapSize);
        heapSize++;
        return true;
    }
    
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean modified = false;
        for (T t : collection) {
            if (add(t)) {
                modified = true;
            }
        }
        return modified;
    }
    
    public T remove(int i) {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        final T t = heap[i];
        heapSize--;
        heap[i] = heap[heapSize];
        if (CLEAR_REMOVED_INDICES) {
            heap[heapSize] = null;
        }
        heapifyDown(i);
        return t;
    }
    
    @Override
    public boolean remove(Object object) {
        final int index = indexOf(object);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        for (Object object : collection) {
            if (remove(object)) {
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        for (T t : heap) {
            if (!collection.contains(t) && remove(t)) {
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public void clear() {
        heapSize = 0;
    }
    
    protected void heapifyUp(int i) {
        T t = heap[i];
        while (i > 0 && t.compareTo(heap[parent(i)]) > 0) {
            heap[i] = heap[parent(i)];
            i = parent(i);
        }
        heap[i] = t;
    }
    
    protected void heapifyDown(int i) {
        int child;
        T t = heap[i];
        while (kthChild(i, 1) < heapSize) {
            child = maxChild(i);
            if (t.compareTo(heap[child]) < 0) {
                heap[i] = heap[child];
            } else {
                break;
            }
            i = child;
        }
        heap[i] = t;
    }
    
    protected int maxChild(int i) {
        final int leftChild = kthChild(i, 1);
        final int rightChild = kthChild(i, 2);
        return heap[leftChild].compareTo(heap[rightChild]) > 0 ? leftChild : rightChild;
    }
    
    public T getMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap[0];
    }
    
    public T removeMax() {
        return remove(0);
    }
    
    @Override
    public String toString() {
        return "BinaryMaxHeap{" + "heap=" + Arrays.toString(heap) + ", heapSize=" + heapSize + '}';
    }
    
}
