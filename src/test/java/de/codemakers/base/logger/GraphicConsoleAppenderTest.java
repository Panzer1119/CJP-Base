/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.base.logger;

import de.codemakers.base.Standard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class GraphicConsoleAppenderTest {
    
    private static final Logger logger = LogManager.getLogger(GraphicConsoleAppenderTest.class);
    
    @Test
    void test() throws InterruptedException {
        logger.info("This is a Message of Level INFO");
        final Console<?> console = Standard.getConsoleByName("default");
        logger.debug("console={}", console);
        final Thread thread = Standard.toughThread(() -> {
            Thread.currentThread().setName("Console-InputStream-Reader");
            final InputStream inputStream = console.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logger.info(line);
            }
            bufferedReader.close();
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                logger.info("This is a Test");
                logger.warn("This is a Warning");
            }
        }, 3000);
        console.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //FIXME Testing only
        console.menuItem_exit.addActionListener((actionEvent) -> System.exit(1)); //FIXME Testing only
        console.menuItem_settings.addActionListener((actionEvent) -> console.consoleSettings.showAtConsole()); //FIXME Testing only
        console.show();
        thread.start();
        thread.join();
    }
    
}
