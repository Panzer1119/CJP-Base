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

package de.codemakers.base.entities;

import java.util.Arrays;

public class IncrementalDataTest3 {
    
    public static final void main(String[] args) {
        final String test_1 = "Das ist ein kleiner Test Satz; naturrlich ohne Fehler1";
        System.out.println("test_1=" + test_1);
        final byte[] test_1_bytes = test_1.getBytes();
        System.out.println("test_1_bytes=" + Arrays.toString(test_1_bytes));
        final IncrementalData incrementalData_1 = new IncrementalData(test_1_bytes);
        System.out.println("incrementalData_1=" + incrementalData_1);
        final String test_2 = "Das ist ein kleiner Test Satz, natürlich ohne Fehler!";
        System.out.println("test_2=" + test_2);
        final byte[] test_2_bytes = test_2.getBytes();
        System.out.println("test_2_bytes=" + Arrays.toString(test_2_bytes));
        final DataDelta dataDelta_1 = incrementalData_1.changeData(test_2_bytes);
        System.out.println("dataDelta_1=" + dataDelta_1);
        System.out.println("dataDelta_1=" + new String(dataDelta_1.getDataNew()));
        System.out.println("dataDelta_1.getBitSize()=" + dataDelta_1.getBitSize());
        //final DataDelta dataDelta_2 = new PreciseDataDelta(test_1_bytes, test_2_bytes);
        //System.out.println("dataDelta_2=" + dataDelta_2);
        //System.out.println("dataDelta_2.getBitSize()=" + dataDelta_2.getBitSize());
        final IncrementalData incrementalData_r_1 = new IncrementalData(test_1_bytes);
        System.out.println("incrementalData_r_1=" + incrementalData_r_1);
        System.out.println("incrementalData_r_1=" + new String(incrementalData_r_1.getData()));
        incrementalData_r_1.incrementData(dataDelta_1);
        System.out.println("incrementalData_r_1=" + incrementalData_r_1);
        System.out.println("incrementalData_r_1=" + new String(incrementalData_r_1.getData()));
        //final IncrementalData incrementalData_r_2 = new IncrementalData(test_1_bytes);
        //System.out.println("incrementalData_r_2=" + incrementalData_r_2);
        //System.out.println("incrementalData_r_2=" + new String(incrementalData_r_2.getData()));
        //incrementalData_r_2.incrementData(dataDelta_2);
        //System.out.println("incrementalData_r_2=" + incrementalData_r_2);
        //System.out.println("incrementalData_r_1=" + new String(incrementalData_r_2.getData()));
    }
    
}