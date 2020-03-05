/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

package de.codemakers.base.util;

import de.codemakers.base.logger.Logger;

public class RegExUtilTest {
    
    public static void main(String[] args) {
        final String text = "This is a text s$archFor.this! haha";
        Logger.log("text=" + text);
        final String textToSearchFor = "s$archFor.this!";
        Logger.log("textToSearchFor=" + textToSearchFor);
        final String replacement = "I replace the old Text (hehe)!";
        Logger.log("replacement=" + replacement);
        final String result_1 = text.replaceAll(textToSearchFor, replacement);
        Logger.log("result_1=" + result_1);
        final String regEx = RegExUtil.escapeToRegEx(textToSearchFor);
        Logger.log("regEx=" + regEx);
        final String result_2 = text.replaceAll(regEx, replacement);
        Logger.log("result_2=" + result_2);
    }
    
}
