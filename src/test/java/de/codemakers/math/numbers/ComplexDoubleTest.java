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

public class ComplexDoubleTest {
    
    public static final void main(String[] args) {
        final ComplexDouble complexDouble_1 = new ComplexDouble(4.0, 3.0);
        System.out.println("complexDouble_1             =" + complexDouble_1);
        System.out.println("complexDouble_1.conjugate() =" + complexDouble_1.conjugate());
        System.out.println("complexDouble_1.inverse()   =" + complexDouble_1.inverse());
        System.out.println("complexDouble_1.absSquared()=" + complexDouble_1.absSquared());
        System.out.println("complexDouble_1.abs()       =" + complexDouble_1.abs());
        System.out.println("complexDouble_1.sqrt()      =" + complexDouble_1.sqrt());
        System.out.println("complexDouble_1.sgn()       =" + complexDouble_1.sgn());
        System.out.println("complexDouble_1.sgn().abs() =" + complexDouble_1.sgn().abs());
        System.out.println("complexDouble_1.ln()        =" + complexDouble_1.ln());
        System.out.println("complexDouble_1.log(2)      =" + complexDouble_1.log(2));
        System.out.println("complexDouble_1.log(2+0i)   =" + complexDouble_1.log(new ComplexDouble(2)));
        System.out.println("complexDouble_1.log(2-4i)   =" + complexDouble_1.log(new ComplexDouble(2, -4)));
        System.out.println("complexDouble_1.arg()       =" + complexDouble_1.arg());
        System.out.println("complexDouble_1.pow(3)      =" + complexDouble_1.pow(3));
        System.out.println("complexDouble_1.exp()       =" + complexDouble_1.exp());
        System.out.println("complexDouble_1.pow(2-4i)   =" + complexDouble_1.pow(new ComplexDouble(2, -4)));
    }
    
}
