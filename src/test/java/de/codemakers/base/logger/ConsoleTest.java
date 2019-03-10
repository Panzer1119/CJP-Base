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

import javax.swing.*;

public class ConsoleTest {
    
    public static final void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINEST);
        final Console console = new Console();
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
