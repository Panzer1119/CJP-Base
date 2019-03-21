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
import de.codemakers.base.util.TimeUtil;
import de.codemakers.io.file.AdvancedFile;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;

public class DefaultConsoleTest {
    
    public static final AdvancedFile LOG_FOLDER = new AdvancedFile("test/logs");
    public static final AdvancedFile LOG_FILE;
    public static final BufferedWriter BUFFERED_WRITER_LOG_FILE;
    
    static {
        LOG_FOLDER.mkdirsWithoutException();
        LOG_FILE = new AdvancedFile(LOG_FOLDER, "log_" + ZonedDateTime.now().format(TimeUtil.ISO_OFFSET_DATE_TIME_FIXED_LENGTH_FOR_FILES) + ".txt");
        BUFFERED_WRITER_LOG_FILE = LOG_FILE.createBufferedWriterWithoutException(false);
        Standard.addShutdownHook(() -> {
            Logger.log("Closing BUFFERED_WRITER_LOG_FILE");
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
        final DefaultConsole defaultConsole = new DefaultConsole();
        Standard.async(() -> {
            Thread.currentThread().setName("Console-InputStream-Reader");
            final InputStream inputStream = defaultConsole.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Logger.log(line, LogLevel.INPUT);
            }
            bufferedReader.close();
        });
        //AdvancedLeveledLogger.LOG_ENTRY_CONSUMER = console.logEntries::add;
        //Logger.getDefaultAdvancedLeveledLogger().setPreLogEntryToughConsumer(console.logEntries::add); //TODO Do this in the Console constructor?
        Logger.getDefaultAdvancedLeveledLogger().setPreLogEntryToughConsumer((levelLogEntry) -> {
            defaultConsole.logEntries.add(levelLogEntry);
            defaultConsole.reloadWithoutException();
        }); //TODO Do this in the Console constructor?
        /*
        AdvancedLeveledLogger.LOG_ENTRY_CONSUMER = (logEntry) -> {
            console.logEntries.add(logEntry);
            console.reloadWithoutException();
        };
        */
        Logger.log("defaultConsole=" + defaultConsole);
        defaultConsole.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //FIXME Testing only
        defaultConsole.menuItem_exit.addActionListener((actionEvent) -> System.exit(1)); //FIXME Testing only
        defaultConsole.menuItem_settings.addActionListener((actionEvent) -> defaultConsole.consoleSettings.showAtConsole()); //FIXME Testing only
        //console.setPreferredSize(new Dimension(600, 300));
        defaultConsole.show();
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
