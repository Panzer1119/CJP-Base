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

package de.codemakers.base.logger;

import de.codemakers.base.Standard;
import de.codemakers.base.util.StringUtil;
import org.apache.logging.log4j.LogManager;

public class LoggerTest {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    
    public static final void main(String[] args) {
        logger.info("Test");
        for (int i = 0; i < 100; i++) {
            logger.info("i=" + i);
        }
        for (LogLevel logLevel : LogLevel.values()) {
            logger.info("logLevel=" + logLevel);
            logger.info("logLevel.getNameLeft() =" + logLevel.getNameLeft());
            logger.info("logLevel.getNameRight()=" + logLevel.getNameRight());
            logger.info("logLevel.getNameMid()  =" + logLevel.getNameMid());
            logger.info("#####################");
        }
        logger.info("LogLevel.MINIMUM_NAME_LENGTH=" + LogLevel.MINIMUM_NAME_LENGTH);
        logger.info("LogLevel.MAXIMUM_NAME_LENGTH=" + LogLevel.MAXIMUM_NAME_LENGTH);
        logger.info("LogLevel.MINIMUM_LEVEL=" + LogLevel.MINIMUM_LEVEL);
        logger.info("LogLevel.MAXIMUM_LEVEL=" + LogLevel.MAXIMUM_LEVEL);
        final LogFormatBuilder logFormatBuilder = new LogFormatBuilder();
        logger.info("logFormatBuilder=" + logFormatBuilder);
        logFormatBuilder.appendTimestamp()
                .appendLogLevel()
                .appendText(": ")
                .appendObject()
                .appendNewLine()
                .appendThread()
                .appendLogLevel()
                .appendText(" ")
                .appendSource();
        logger.info("logFormatBuilder=" + logFormatBuilder);
        final SourceFormatBuilder sourceFormatBuilder = new SourceFormatBuilder();
        logger.info("sourceFormatBuilder=" + sourceFormatBuilder);
        sourceFormatBuilder.appendClassName()
                .appendText(".")
                .appendMethodName()
                .appendText("(")
                .appendFileName()
                .appendText(":")
                .appendLineNumber()
                .appendText(")");
        logger.info("sourceFormatBuilder=" + sourceFormatBuilder);
        Logger.getDefaultAdvancedLeveledLogger().setLogFormat(logFormatBuilder.toFormat());
        Logger.getDefaultAdvancedLeveledLogger().setSourceFormat(sourceFormatBuilder.toFormat());
        logger.info("Test 2");
        logger.info("logFormatBuilder.example()=" + logFormatBuilder.example());
        logger.info("sourceFormatBuilder.example()=" + sourceFormatBuilder.example());
        logger.info("logFormatBuilder.example(sourceFormatBuilder)=" + logFormatBuilder.example(sourceFormatBuilder));
        Standard.silentError(() -> {
            Thread.currentThread().setName("Anonymous-Test-Thread${loglevel}");
            logger.info("Anonymous Test");
            final StackTraceElement stackTraceElement = AdvancedLogger.cutStackTrace(new Exception().getStackTrace());
            System.out.println(stackTraceElement);
            System.out.println(stackTraceElement.getClassName());
            System.out.println(stackTraceElement.getMethodName());
            System.out.println(stackTraceElement.getFileName());
            System.out.println(stackTraceElement.getLineNumber());
        });
        logger.info("StringUtil.STRING_SUBSTITUTOR_TO_REPLACE =" + StringUtil.STRING_SUBSTITUTOR_TO_REPLACE);
        logger.info("StringUtil.STRING_SUBSTITUTOR_REPLACEMENT=" + StringUtil.STRING_SUBSTITUTOR_REPLACEMENT);
        Logger.getDefaultAdvancedLeveledLogger()
                .createLogFormatBuilder()
                .appendText("NEW LOG FORMAT___")
                .appendTimestamp()
                .appendText(": ")
                .appendObject()
                .finishWithoutException();
        logger.info("Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder() Test");
        logger.info("Test if escape chars are causing problems $$ <- these are 2 dollar signs, this is just one -> $ and this are 3 -> $$$ (chars$around$signs) Test end");
    }
    
}
