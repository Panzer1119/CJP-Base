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
import de.codemakers.base.exceptions.NotImplementedRuntimeException;
import de.codemakers.base.util.TimeUtil;
import de.codemakers.io.file.AdvancedFile;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.List;

public class ConsoleTest {
    
    private static final org.apache.logging.log4j.Logger loggerTODO = LogManager.getLogger();
    
    public static final AdvancedFile LOG_FOLDER = new AdvancedFile("test/logs");
    public static final AdvancedFile LOG_FILE;
    public static final BufferedWriter BUFFERED_WRITER_LOG_FILE;
    
    static {
        LOG_FOLDER.mkdirsWithoutException();
        LOG_FILE = new AdvancedFile(LOG_FOLDER, "log_" + ZonedDateTime.now().format(TimeUtil.ISO_OFFSET_DATE_TIME_FIXED_LENGTH_FOR_FILES) + ".txt");
        BUFFERED_WRITER_LOG_FILE = LOG_FILE.createBufferedWriterWithoutException(false);
        Standard.addShutdownHook(() -> {
            loggerTODO.info("Closing BUFFERED_WRITER_LOG_FILE");
            BUFFERED_WRITER_LOG_FILE.flush();
            BUFFERED_WRITER_LOG_FILE.close();
        });
    }
    
    public static final void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINEST);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendTimestamp().appendLogLevel().appendText(": ").appendObject().appendText(" ").appendSource().appendThread().finishWithoutException();
        Logger.getDefaultAdvancedLeveledLogger().setPostLogEntryToughConsumer((leveledLogEntry) -> {
            BUFFERED_WRITER_LOG_FILE.write(leveledLogEntry.formatWithoutException(Logger.getDefaultAdvancedLeveledLogger()));
            BUFFERED_WRITER_LOG_FILE.newLine();
            BUFFERED_WRITER_LOG_FILE.flush();
        });
        final Console<AdvancedLeveledLogger> console = new Console<AdvancedLeveledLogger>(Logger.getDefaultAdvancedLeveledLogger()) {
            @Override
            public boolean reload() throws Exception {
                //loggerTODO.info(LogLevel.WARNING, "Reload requested");
                //loggerTODO.info(LogLevel.DEBUG, "getLogEntries()=" + getLogEntries());
                //loggerTODO.info(LogLevel.DEBUG, "getLogEntriesFilteredByLogLevel()=" + getLogEntriesFilteredByLogLevel());
                //TODO Testing adding LogEntry start
                final List<LeveledLogEntry> logEntries = getLogEntriesFilteredByLogLevel();
                final StyledDocument styledDocument = textPane_output.getStyledDocument();
                styledDocument.remove(0, styledDocument.getLength()); //TODO Good? Because when there are too many LogEntries, this could cause lag
                final Style style = styledDocument.addStyle("LogStyle", null);
                for (LeveledLogEntry logEntry : logEntries) {
                    StyleConstants.setBackground(style, logEntry.getLogLevel() == null ? Color.WHITE : logEntry.getLogLevel().getColorBackground());
                    StyleConstants.setForeground(style, logEntry.getLogLevel() == null ? Color.BLACK : logEntry.getLogLevel().getColorForeground());
                    styledDocument.insertString(styledDocument.getLength(), StringSubstitutor.replace(Logger.getDefaultAdvancedLeveledLogger().getLogFormat(), Logger.getDefaultAdvancedLeveledLogger().createValueMap(logEntry)) + "\n", style);
                }
                textPane_output.setCaretPosition(styledDocument.getLength());
                //TODO Testing adding LogEntry end
                return true;
            }
    
            @Override
            protected ConsoleSettings createConsoleSettings(AdvancedFile iconAdvancedFile) {
                throw new NotImplementedRuntimeException();
            }
    
            @Override
            protected boolean handleInput(String input) throws Exception {
                if (input.isEmpty()) {
                    return false;
                }
                if (input.startsWith("/")) {
                    return runCommand(input);
                }
                //loggerTODO.info(LogLevel.INPUT, input);
                write((input + "\n").getBytes());
                return true;
            }
            
            protected boolean runCommand(final String command) throws Exception {
                loggerTODO.log(LogLevel.COMMAND, command);
                String temp = command.substring("/".length()).trim();
                if (temp.startsWith("lang")) {
                    temp = temp.substring("lang".length()).trim();
                    if (temp.equalsIgnoreCase("english")) {
                        Standard.setLocalizer(Standard.getLocalizerEnglishUs());
                        loggerTODO.debug("Setted localizer to english us");
                        return true;
                    } else if (temp.equalsIgnoreCase("default")) {
                        Standard.setLocalizer(Standard.getLocalizerDefault());
                        loggerTODO.debug("Setted localizer to default");
                        return true;
                    }
                } else if (temp.startsWith("test")) {
                    temp = temp.substring("test".length()).trim();
                    if (temp.equalsIgnoreCase("1")) {
                        frame.setTitle("" + Math.random());
                        loggerTODO.debug("COMMAND: \"Test 1\"");
                        return true;
                    }
                }
                //throw new NotYetImplementedRuntimeException(); //TODO Implement Command stuff
                loggerTODO.warn(String.format("Command \"%s\" does not exist", command), ex);
                return false;
            }
            
        };
        Standard.async(() -> {
            Thread.currentThread().setName("Console-InputStream-Reader");
            final InputStream inputStream = console.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                loggerTODO.info(LogLevel.INPUT, line);
            }
            bufferedReader.close();
        });
        //AdvancedLeveledLogger.LOG_ENTRY_CONSUMER = console.logEntries::add;
        //Logger.getDefaultAdvancedLeveledLogger().setPreLogEntryToughConsumer(console.logEntries::add); //TODO Do this in the Console constructor?
        Logger.getDefaultAdvancedLeveledLogger().setPreLogEntryToughConsumer((levelLogEntry) -> {
            console.logEntries.add(levelLogEntry);
            console.reloadWithoutException();
        }); //TODO Do this in the Console constructor?
        /*
        AdvancedLeveledLogger.LOG_ENTRY_CONSUMER = (logEntry) -> {
            console.logEntries.add(logEntry);
            console.reloadWithoutException();
        };
        */
        loggerTODO.info("console=" + console);
        console.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //FIXME Testing only
        console.menuItem_exit.addActionListener((actionEvent) -> System.exit(1)); //FIXME Testing only
        console.menuItem_settings.addActionListener((actionEvent) -> console.consoleSettings.showAtConsole()); //FIXME Testing only
        //console.setPreferredSize(new Dimension(600, 300));
        console.show();
        /*
        Standard.async(() -> {
            Thread.sleep(1000);
            console.hide();
            Thread.sleep(1000);
            console.show();

        });
        */
    }
    
}
