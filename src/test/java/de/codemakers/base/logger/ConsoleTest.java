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
import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import org.apache.commons.text.StringSubstitutor;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleTest {
    
    public static final void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINEST);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendTimestamp().appendLogLevel().appendText(": ").appendObject().appendText(" ").appendSource().appendThread().finishWithoutException();
        final Console console = new Console() {
            @Override
            public boolean reload() throws Exception {
                //Logger.log("Reload requested", LogLevel.WARNING);
                //Logger.log("getLogEntries()=" + getLogEntries(), LogLevel.DEBUG);
                //Logger.log("getLogEntriesFilteredByLogLevel()=" + getLogEntriesFilteredByLogLevel(), LogLevel.DEBUG);
                //TODO Testing adding LogEntry start
                final List<LogEntry> logEntries = getLogEntriesFilteredByLogLevel();
                final StyledDocument styledDocument = textPane_output.getStyledDocument();
                styledDocument.remove(0, styledDocument.getLength()); //TODO Good? Because when there are too many LogEntries, this could cause lag
                final Style style = styledDocument.addStyle("LogStyle", null);
                for (LogEntry logEntry : logEntries) {
                    StyleConstants.setBackground(style, logEntry.getLogLevel() == null ? Color.WHITE : logEntry.getLogLevel().getColorBackground());
                    StyleConstants.setForeground(style, logEntry.getLogLevel() == null ? Color.BLACK : logEntry.getLogLevel().getColorForeground());
                    styledDocument.insertString(styledDocument.getLength(), StringSubstitutor.replace(Logger.getDefaultAdvancedLeveledLogger().getLogFormat(), Logger.getDefaultAdvancedLeveledLogger().createValueMap(logEntry)) + "\n", style);
                }
                textPane_output.setCaretPosition(styledDocument.getLength());
                //TODO Testing adding LogEntry end
                return true;
            }
            
            @Override
            protected boolean handleInput(String input) throws Exception {
                if (input.isEmpty()) {
                    return false;
                }
                if (input.startsWith("/")) {
                    return runCommand(input);
                }
                //Logger.log(input, LogLevel.INPUT);
                write((input + "\n").getBytes());
                return true;
            }
            
            protected boolean runCommand(String command) throws Exception {
                Logger.log(command, LogLevel.COMMAND);
                throw new NotYetImplementedRuntimeException(); //TODO Implement Command stuff
            }
            
        };
        Standard.async(() -> {
            Thread.currentThread().setName("Console-InputStream-Reader");
            final InputStream inputStream = console.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Logger.log(line, LogLevel.INPUT);
            }
            bufferedReader.close();
        });
        //AdvancedLeveledLogger.LOG_ENTRY_CONSUMER = console.logEntries::add;
        AdvancedLeveledLogger.LOG_ENTRY_CONSUMER = (logEntry) -> {
            console.logEntries.add(logEntry);
            console.reloadWithoutException();
        };
        Logger.log("console=" + console);
        console.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
