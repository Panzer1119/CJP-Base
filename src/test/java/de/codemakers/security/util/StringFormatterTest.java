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

package de.codemakers.security.util;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.StringFormatter;
import de.codemakers.base.util.StringUtil;
import org.apache.commons.text.StringSubstitutor;

import java.time.LocalDate;
import java.time.LocalTime;

public class StringFormatterTest {
    
    public static final String FORMAT_1 = "Dies ist ein Test am $${datum}$ um ${uhrzeit}. Wir sind in ${land}.";
    
    public static final void main(String[] args) throws Exception {
        //Logger.DEFAULT_ADVANCED_LEVELED_LOGGER.setLogFormat("%1$s");
        //Logger.DEFAULT_ADVANCED_LEVELED_LOGGER.getLogFormatter().setFormatString("[${thread}][${loglevel}][${location}]: ${object}");
        Logger.log("FORMAT_1=" + FORMAT_1);
        final StringFormatter stringFormatter_1 = new StringFormatter(FORMAT_1);
        Logger.log("stringFormatter_1.getTags()=" + stringFormatter_1.getTags());
        Logger.log("stringFormatter_1.toDebugString()=" + stringFormatter_1.toDebugString());
        Logger.log("stringFormatter_1.toString()=" + stringFormatter_1.toString());
        Logger.log("stringFormatter_1=" + stringFormatter_1);
        stringFormatter_1.setValue("datum", LocalDate.now());
        stringFormatter_1.setValue("uhrzeit", LocalTime.now());
        stringFormatter_1.setValue("land", null);
        Logger.log("stringFormatter_1.toDebugString()=" + stringFormatter_1.toDebugString());
        Logger.log("stringFormatter_1.toString()=" + stringFormatter_1.toString());
        Logger.log("stringFormatter_1=" + stringFormatter_1);
        Logger.log("stringFormatter_1.getValues()=" + stringFormatter_1.getValues());
        final StringUtil.StringMapLookup stringMapLookup_1 = new StringUtil.StringMapLookup();
        System.out.println("stringMapLookup_1=" + stringMapLookup_1);
        stringMapLookup_1.put("test_1", "Test 1");
        StringSubstitutor stringSubstitutor = new StringSubstitutor(stringMapLookup_1);
        System.out.println(stringSubstitutor.replace("Test 1: \"${test_1}\", Test2: \"${test_2}\""));
        stringMapLookup_1.put("test_2", "Test 2");
        System.out.println(stringSubstitutor.replace("Test 1: \"${test_1}\", Test2: \"${test_2}\""));
    }
    
}
