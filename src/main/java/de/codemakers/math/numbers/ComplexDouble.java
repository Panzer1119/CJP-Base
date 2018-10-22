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

package de.codemakers.math.numbers;

import de.codemakers.base.util.Require;
import de.codemakers.base.util.interfaces.ByteSerializable;
import de.codemakers.base.util.interfaces.Copyable;

import java.nio.ByteBuffer;
import java.util.Objects;

public class ComplexDouble implements ByteSerializable, Copyable {
    
    protected double re;
    protected double im;
    
    public ComplexDouble() {
        this(0.0);
    }
    
    public ComplexDouble(double re) {
        this(re, 0.0);
    }
    
    public ComplexDouble(double re, double im) {
        this.re = re;
        this.im = im;
    }
    
    public final double getReal() {
        return re;
    }
    
    public final double getImaginary() {
        return im;
    }
    
    public final ComplexDouble add(ComplexDouble complexDouble) {
        return new ComplexDouble(re + complexDouble.re, im + complexDouble.im);
    }
    
    public final ComplexDouble subtract(ComplexDouble complexDouble) {
        return new ComplexDouble(re - complexDouble.re, im - complexDouble.im);
    }
    
    public final ComplexDouble multiply(ComplexDouble complexDouble) {
        return new ComplexDouble((re * complexDouble.re) - (im * complexDouble.im), (im * complexDouble.re) + (re * complexDouble.im));
    }
    
    public final ComplexDouble divide(ComplexDouble complexDouble) {
        final double temp = complexDouble.absSquared();
        return new ComplexDouble(((re * complexDouble.re) + (im * complexDouble.im)) / temp, ((im * complexDouble.re) - (re * complexDouble.im)) / temp);
    }
    
    public final ComplexDouble negateReal() {
        return new ComplexDouble(-re, im);
    }
    
    public final ComplexDouble negateImaginary() {
        return new ComplexDouble(re, -im);
    }
    
    public final ComplexDouble conjugate() {
        return negateImaginary();
    }
    
    public final ComplexDouble negateBoth() {
        return new ComplexDouble(-re, -im);
    }
    
    public final double absSquared() {
        return (re * re) + (im * im);
    }
    
    public final ComplexDouble absSquaredAsComplex() {
        return new ComplexDouble(absSquared());
    }
    
    public final double abs() {
        return Math.sqrt(absSquared());
    }
    
    public final ComplexDouble absAsComplex() {
        return new ComplexDouble(abs());
    }
    
    public final double arg() {
        return Math.atan2(im, re);
    }
    
    public final ComplexDouble argAsComplex() {
        return new ComplexDouble(arg());
    }
    
    public final ComplexDouble inverseReal() {
        return new ComplexDouble(1.0 / re, im);
    }
    
    public final ComplexDouble inverseImaginary() {
        return new ComplexDouble(re, 1.0 / im);
    }
    
    public final ComplexDouble inverse() {
        final double temp = absSquared();
        return new ComplexDouble(re / temp, -im / temp);
    }
    
    public final ComplexDouble sgn() {
        return divide(absAsComplex());
    }
    
    public final ComplexDouble sqrt() {
        final double temp = abs();
        return new ComplexDouble(Math.sqrt((re + temp) / 2.0), Math.signum(im) * Math.sqrt((temp - re) / 2.0));
    }
    
    public final ComplexDouble ln() {
        return new ComplexDouble(Math.log(abs()), arg());
    }
    
    public final ComplexDouble log(double base) {
        return ln().divide(new ComplexDouble(Math.log(base)));
    }
    
    public final ComplexDouble log(ComplexDouble base) {
        return ln().divide(base.ln());
    }
    
    public final ComplexDouble exp() {
        final double exp_re = Math.exp(re);
        return new ComplexDouble(exp_re * Math.cos(im), exp_re * Math.sin(im));
    }
    
    public final ComplexDouble pow(int n) {
        final double rTon = Math.pow(abs(), n);
        final double nPhi = n * arg();
        return new ComplexDouble(rTon * Math.cos(nPhi), rTon * Math.sin(nPhi));
    }
    
    public final ComplexDouble pow(ComplexDouble complexDouble) {
        return complexDouble.multiply(ln()).exp();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().isAssignableFrom(o.getClass())) {
            return false;
        }
        final ComplexDouble that = (ComplexDouble) o;
        return Double.compare(that.re, re) == 0 && Double.compare(that.im, im) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }
    
    @Override
    public String toString() {
        return "ComplexDouble{" + "re=" + re + ", im=" + im + '}';
    }
    
    @Override
    public Copyable copy() {
        return new ComplexDouble(re, im);
    }
    
    @Override
    public void set(Copyable copyable) {
        final ComplexDouble complexDouble = Require.clazz(copyable, ComplexDouble.class);
        if (complexDouble != null) {
            this.re = complexDouble.re;
            this.im = complexDouble.im;
        }
    }
    
    @Override
    public byte[] toBytes() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES * 2);
        byteBuffer.putDouble(re);
        byteBuffer.putDouble(im);
        return byteBuffer.array();
    }
    
    @Override
    public boolean fromBytes(byte[] bytes) {
        if (bytes == null || bytes.length != Double.BYTES * 2) {
            return false;
        }
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        this.re = byteBuffer.getDouble();
        this.im = byteBuffer.getDouble();
        return true;
    }
    
}
