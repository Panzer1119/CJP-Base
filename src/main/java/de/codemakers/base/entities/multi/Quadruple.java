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

public class Quadruple<A, B, C, D> extends Triple<A, B, C> {
    
    protected D d;
    
    public Quadruple(A a, B b, C c, D d) {
        super(a, b, c);
        this.d = d;
    }
    
    public final D getD() {
        return d;
    }
    
    public final Quadruple setD(D d) {
        this.d = d;
        return this;
    }
    
    @Override
    public String toString() {
        return "Quadruple{" + "d=" + d + ", c=" + c + ", b=" + b + ", a=" + a + '}';
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
        final Quadruple<?, ?, ?, ?> that = (Quadruple<?, ?, ?, ?>) o;
        return Objects.equals(d, that.d);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), d);
    }
    
}
