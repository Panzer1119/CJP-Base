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

package de.codemakers.math;

public class MathUtil {
    
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt((x * x) + 1));
    }
    
    public static double acosh(double x) {
        if (x < 1.0) {
            throw new IllegalArgumentException("x may not be less than 1.0 for acosh(x)");
        }
        return Math.log(x + Math.sqrt((x * x) + 1));
    }
    
    public static double atanh(double x) {
        final double abs = Math.abs(x);
        if (abs > 1.0) {
            return Math.log((x + 1.0) / (x - 1.0)) / 2.0;
        } else if (abs < 1.0) {
            return Math.log((1.0 + x) / (1.0 - x)) / 2.0;
        }
        return Double.NaN;
    }
    
}
