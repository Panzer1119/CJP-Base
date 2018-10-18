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

package de.codemakers.base.entities.multi;

import java.util.Objects;

public class Quintuple<A, B, C, D, E> extends Quadruple<A, B, C, D> {
    
    protected E e;
    
    public Quintuple(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        this.e = e;
    }
    
    public final E getE() {
        return e;
    }
    
    public final Quintuple setE(E e) {
        this.e = e;
        return this;
    }
    
    @Override
    public String toString() {
        return "Quintuple{" + "e=" + e + ", d=" + d + ", c=" + c + ", b=" + b + ", a=" + a + '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().isAssignableFrom(o.getClass())) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Quintuple<?, ?, ?, ?, ?> that = (Quintuple<?, ?, ?, ?, ?>) o;
        return Objects.equals(e, that.e);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), e);
    }
    
}
