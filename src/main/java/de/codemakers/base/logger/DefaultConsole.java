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
import de.codemakers.io.file.AdvancedFile;
import org.apache.commons.text.StringSubstitutor;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;

public class DefaultConsole extends Console<AdvancedLeveledLogger> {
    
    public DefaultConsole() {
        this(Logger.getDefaultAdvancedLeveledLogger());
    }
    
    public DefaultConsole(AdvancedLeveledLogger logger) {
        super(logger);
    }
    
    public DefaultConsole(AdvancedFile iconAdvancedFile, AdvancedFile iconSettingsAdvancedFile) {
        this(Logger.getDefaultAdvancedLeveledLogger(), iconAdvancedFile, iconSettingsAdvancedFile);
    }
    
    public DefaultConsole(AdvancedLeveledLogger logger, AdvancedFile iconAdvancedFile, AdvancedFile iconSettingsAdvancedFile) {
        super(logger, iconAdvancedFile, iconSettingsAdvancedFile);
    }
    
    protected String getLogFormat() {
        return logger.getLogFormat();
    }
    
    protected String getSourceFormat() {
        return logger.getSourceFormat();
    }
    
    @Override
    public boolean reload() throws Exception {
        final List<LeveledLogEntry> logEntries = getLogEntriesFilteredByLogLevel();
        final StyledDocument styledDocument = textPane_output.getStyledDocument();
        styledDocument.remove(0, styledDocument.getLength()); //TODO Good? Because when there are too many LogEntries, this could cause lag
        final Style style = styledDocument.addStyle("LogEntryStyle", null);
        for (LeveledLogEntry logEntry : logEntries) {
            StyleConstants.setBackground(style, logEntry.getLogLevel() == null ? Color.WHITE : logEntry.getLogLevel().getColorBackground());
            StyleConstants.setForeground(style, logEntry.getLogLevel() == null ? Color.BLACK : logEntry.getLogLevel().getColorForeground());
            styledDocument.insertString(styledDocument.getLength(), StringSubstitutor.replace(logger.getLogFormat(), logger.createValueMap(logEntry)) + "\n", style);
        }
        textPane_output.setCaretPosition(styledDocument.getLength());
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
    
    protected boolean runCommand(final String command) throws Exception {
        Logger.log(command, LogLevel.COMMAND);
        String temp = command.substring("/".length()).trim();
        if (temp.startsWith("lang")) {
            temp = temp.substring("lang".length()).trim();
            if (temp.equalsIgnoreCase("english")) {
                Standard.setLocalizer(Standard.getEnglishLocalizer());
                Logger.logDebug("Setted localizer to english");
                return true;
            } else if (temp.equalsIgnoreCase("default")) {
                Standard.setLocalizer(Standard.getDefaultLocalizer());
                Logger.logDebug("Setted localizer to default");
                return true;
            }
        } else if (temp.startsWith("test")) {
            temp = temp.substring("test".length()).trim();
            if (temp.equalsIgnoreCase("1")) {
                frame.setTitle("" + Math.random());
                Logger.logDebug("COMMAND: \"Test 1\"");
                return true;
            }
        }
        //throw new NotYetImplementedRuntimeException(); //TODO Implement Command stuff
        Logger.logWarning(String.format("Command \"%s\" does not exist", command));
        return false;
    }
    
}
