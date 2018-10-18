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

public class Septuple<A, B, C, D, E, F, G> extends Sextuple<A, B, C, D, E, F> {
    
    protected G g;
    
    public Septuple(A a, B b, C c, D d, E e, F f, G g) {
        super(a, b, c, d, e, f);
        this.g = g;
    }
    
    public final G getG() {
        return g;
    }
    
    public final Septuple setG(G g) {
        this.g = g;
        return this;
    }
    
    @Override
    public String toString() {
        return "Septuple{" + "g=" + g + ", f=" + f + ", e=" + e + ", d=" + d + ", c=" + c + ", b=" + b + ", a=" + a + '}';
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
        final Septuple<?, ?, ?, ?, ?, ?, ?> septuple = (Septuple<?, ?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(g, septuple.g);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), g);
    }
    
}
