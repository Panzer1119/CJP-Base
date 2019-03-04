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

package de.codemakers.math;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.scripting.JavaScriptEngine;
import de.codemakers.base.scripting.JavaScriptEngineBuilder;

import java.util.Arrays;

public class HecTocTest {
    
    public static final String NUMBERS_STRING = "0123456789";
    public static final char[] NUMBERS = NUMBERS_STRING.toCharArray();
    public static final String OPERATORS_STRING = " +-*/^";
    public static final char[] OPERATORS = OPERATORS_STRING.toCharArray();
    public static final String PRES_STRING = " -";
    public static final char[] PRES = PRES_STRING.toCharArray();
    
    public static final void main(String[] args) throws Exception {
        final JavaScriptEngineBuilder javaScriptEngineBuilder = new JavaScriptEngineBuilder();
        Logger.log("javaScriptEngineBuilder=" + javaScriptEngineBuilder);
        final JavaScriptEngine javaScriptEngine = javaScriptEngineBuilder.build();
        Logger.log("javaScriptEngine=" + javaScriptEngine);
        //final char[] numbers = new char[6];
        final char[] numbers = "523954".toCharArray();
        Logger.log("numbers=" + Arrays.toString(numbers));
        final boolean found = test(javaScriptEngine, numbers);
        Logger.log("found=" + found);
    }
    
    public static boolean test(JavaScriptEngine javaScriptEngine, char[] numbers) throws Exception {
        boolean found = false;
        for (char o : PRES) {
            if (test(javaScriptEngine, numbers, o)) {
                found = true;
            }
        }
        return found;
    }
    
    public static boolean test(JavaScriptEngine javaScriptEngine, char[] numbers, char o_1) throws Exception {
        boolean found = false;
        for (char o : OPERATORS) {
            if (test(javaScriptEngine, numbers, o_1, o)) {
                found = true;
            }
        }
        return found;
    }
    
    public static boolean test(JavaScriptEngine javaScriptEngine, char[] numbers, char o_1, char o_2) throws Exception {
        boolean found = false;
        for (char o : OPERATORS) {
            if (test(javaScriptEngine, numbers, o_1, o_2, o)) {
                found = true;
            }
        }
        return found;
    }
    
    public static boolean test(JavaScriptEngine javaScriptEngine, char[] numbers, char o_1, char o_2, char o_3) throws Exception {
        boolean found = false;
        for (char o : OPERATORS) {
            if (test(javaScriptEngine, numbers, o_1, o_2, o_3, o)) {
                found = true;
            }
        }
        return found;
    }
    
    public static boolean test(JavaScriptEngine javaScriptEngine, char[] numbers, char o_1, char o_2, char o_3, char o_4) throws Exception {
        boolean found = false;
        for (char o : OPERATORS) {
            if (test(javaScriptEngine, numbers, o_1, o_2, o_3, o_4, o)) {
                found = true;
            }
        }
        return found;
    }
    
    public static boolean test(JavaScriptEngine javaScriptEngine, char[] numbers, char o_1, char o_2, char o_3, char o_4, char o_5) throws Exception {
        boolean found = false;
        for (char o : OPERATORS) {
            if (test(javaScriptEngine, numbers, o_1, o_2, o_3, o_4, o_5, o)) {
                found = true;
            }
        }
        return found;
    }
    
    public static boolean test(JavaScriptEngine javaScriptEngine, char[] numbers, char o_1, char o_2, char o_3, char o_4, char o_5, char o_6) throws Exception {
        String last = "";
        String code = "return ";
        if (o_1 == '-') {
            code += o_1;
        }
        if (o_2 == '^') {
            code += "Math.pow(" + numbers[0] + ", " + numbers[1] + ")";
        } else if (o_2 == ' ') {
            //code += numbers[0]; //TODO ?
            last += numbers[0];
        } else {
            code += numbers[0];
            code += o_2;
        }
        if (o_3 == '^') {
            code += "Math.pow(" + last + numbers[1] + ", " + numbers[2] + ")";
            last = "";
        } else if (o_3 == ' ') {
            //code += numbers[1]; //TODO ?
            last += numbers[1];
        } else {
            code += last + numbers[1];
            last = "";
            code += o_3;
        }
        if (o_4 == '^') {
            code += "Math.pow(" + last + numbers[2] + ", " + numbers[3] + ")";
            last = "";
        } else if (o_4 == ' ') {
            //code += numbers[2]; //TODO ?
            last += numbers[2];
        } else {
            code += last + numbers[2];
            last = "";
            code += o_4;
        }
        if (o_5 == '^') {
            code += "Math.pow(" + last + numbers[3] + ", " + numbers[4] + ")";
            last = "";
        } else if (o_5 == ' ') {
            //code += numbers[3]; //TODO ?
            last += numbers[3];
        } else {
            code += last + numbers[3];
            last = "";
            code += o_5;
        }
        if (o_6 == '^') {
            code += "Math.pow(" + last + numbers[4] + ", " + numbers[5] + ")";
            last = "";
        } else if (o_6 == ' ') {
            //code += numbers[4]; //TODO ?
            last += numbers[4];
        } else {
            code += last + numbers[4];
            last = "";
            code += o_6;
            code += numbers[5];
        }
        code += last;
        last = "";
        code += " == 100";
        try {
            final Object result = javaScriptEngine.execute(code);
            if (result == null) {
                return false;
            }
            final boolean found = (Boolean) result;
            if (found) {
                Logger.log("code=" + code);
                Logger.log("result=" + result);
                return true;
            }
            return false;
        } catch (Exception ex) {
            //Logger.logError(ex.getMessage());
            return false;
        }
        /*
        if (result instanceof Integer) {
            if (((Integer) result) == 100) {
                return true;
            }
            return false;
        }
        return ((double) result) == 100.0;
        */
    }
}
