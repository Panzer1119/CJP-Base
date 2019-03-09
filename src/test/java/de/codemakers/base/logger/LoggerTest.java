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

package de.codemakers.base.logger;

import de.codemakers.base.Standard;
import de.codemakers.base.util.StringUtil;
import org.apache.commons.text.StringSubstitutor;

public class LoggerTest {
    
    public static final void main(String[] args) {
        Logger.getDefaultAdvancedLeveledLogger().setLogFormat(Logger.LOG_FORMAT_VAR_TIMESTAMP + Logger.LOG_FORMAT_VAR_LOG_LEVEL + ": " + Logger.LOG_FORMAT_VAR_OBJECT + "\n" + Logger.LOG_FORMAT_VAR_THREAD + " " + Logger.LOG_FORMAT_VAR_SOURCE);
        /*
        Logger.getDefaultAdvancedLeveledLogger().setLocationFormatter(((stackTraceElement, advancedLogger) -> {
            if (stackTraceElement == null) {
                return "";
            }
            return StringSubstitutor.replace(advancedLogger.locationFormat, advancedLogger.createValueMap(stackTraceElement));
        }));
        */
        Logger.getDefaultAdvancedLeveledLogger().setSourceFormatter(((stackTraceElement, advancedLogger) -> {
            if (stackTraceElement == null) {
                return "";
            }
            return StringSubstitutor.replace(advancedLogger.sourceFormat, advancedLogger.createValueMap(stackTraceElement));
        }));
        Logger.log("Test");
        for (int i = 0; i < 100; i++) {
            Logger.log("i=" + i);
        }
        for (LogLevel logLevel : LogLevel.values()) {
            Logger.log("logLevel=" + logLevel);
            Logger.log("logLevel.getNameLeft() =" + logLevel.getNameLeft());
            Logger.log("logLevel.getNameRight()=" + logLevel.getNameRight());
            Logger.log("logLevel.getNameMid()  =" + logLevel.getNameMid());
            Logger.log("#####################");
        }
        Logger.log("LogLevel.MINIMUM_NAME_LENGTH=" + LogLevel.MINIMUM_NAME_LENGTH);
        Logger.log("LogLevel.MAXIMUM_NAME_LENGTH=" + LogLevel.MAXIMUM_NAME_LENGTH);
        Logger.log("LogLevel.MINIMUM_LEVEL=" + LogLevel.MINIMUM_LEVEL);
        Logger.log("LogLevel.MAXIMUM_LEVEL=" + LogLevel.MAXIMUM_LEVEL);
        final LogFormatBuilder logFormatBuilder = new LogFormatBuilder();
        Logger.log("logFormatBuilder=" + logFormatBuilder);
        logFormatBuilder.appendTimestamp().appendLogLevel().appendText(": ").appendObject().appendNewLine().appendThread().appendLogLevel().appendText(" ").appendSource();
        Logger.log("logFormatBuilder=" + logFormatBuilder);
        //final LocationFormatBuilder locationFormatBuilder = new LocationFormatBuilder();
        final SourceFormatBuilder sourceFormatBuilder = new SourceFormatBuilder();
        //Logger.log("locationFormatBuilder=" + locationFormatBuilder);
        Logger.log("sourceFormatBuilder=" + sourceFormatBuilder);
        //locationFormatBuilder.appendClassName().appendText(".").appendMethodName().appendText("(").appendFileName().appendText(":").appendLineNumber().appendText(")");
        sourceFormatBuilder.appendClassName().appendText(".").appendMethodName().appendText("(").appendFileName().appendText(":").appendLineNumber().appendText(")");
        //Logger.log("locationFormatBuilder=" + locationFormatBuilder);
        Logger.log("sourceFormatBuilder=" + sourceFormatBuilder);
        Logger.getDefaultAdvancedLeveledLogger().setLogFormat(logFormatBuilder.toFormat());
        //Logger.getDefaultAdvancedLeveledLogger().setLocationFormat(locationFormatBuilder.toFormat());
        Logger.getDefaultAdvancedLeveledLogger().setSourceFormat(sourceFormatBuilder.toFormat());
        Logger.log("Test 2");
        Logger.log("logFormatBuilder.example()=" + logFormatBuilder.example());
        //Logger.log("locationFormatBuilder.example()=" + locationFormatBuilder.example());
        Logger.log("sourceFormatBuilder.example()=" + sourceFormatBuilder.example());
        //Logger.log("logFormatBuilder.example(locationFormatBuilder)=" + logFormatBuilder.example(locationFormatBuilder));
        Logger.log("logFormatBuilder.example(sourceFormatBuilder)=" + logFormatBuilder.example(sourceFormatBuilder));
        Standard.silentError(() -> {
            Thread.currentThread().setName("Anonymous-Test-Thread${loglevel}");
            Logger.log("Anonymous Test");
            final StackTraceElement stackTraceElement = AdvancedLogger.cutStackTrace(new Exception().getStackTrace());
            System.out.println(stackTraceElement);
            System.out.println(stackTraceElement.getClassName());
            System.out.println(stackTraceElement.getMethodName());
            System.out.println(stackTraceElement.getFileName());
            System.out.println(stackTraceElement.getLineNumber());
        });
        Logger.log("StringUtil.STRING_SUBSTITUTOR_TO_REPLACE =" + StringUtil.STRING_SUBSTITUTOR_TO_REPLACE);
        Logger.log("StringUtil.STRING_SUBSTITUTOR_REPLACEMENT=" + StringUtil.STRING_SUBSTITUTOR_REPLACEMENT);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendText("NEW LOG FORMAT___").appendTimestamp().appendText(": ").appendObject().finishWithoutException();
        Logger.log("Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder() Test");
        Logger.log("Test if escape chars are causing problems $$ <- these are 2 dollar signs, this is just one -> $ and this are 3 -> $$$ (chars$around$signs) Test end");
    }
    
}
