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

package de.codemakers.io.monitor;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.listeners.AdvancedFileChangeListener;

public class AdvancedFileMonitorTest {
    
    public static final void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINER);
        final AdvancedFile root = new AdvancedFile(args[0]);
        Logger.log("root=" + root);
        final AdvancedFileMonitor advancedFileMonitor = new AdvancedFileMonitor(root);
        Logger.log("advancedFileMonitor=" + advancedFileMonitor);
        advancedFileMonitor.addAdvancedFileChangeListeners(new AdvancedFileChangeListener() {
            @Override
            public void onFileCreated(AdvancedFile file) {
                Logger.logDebug("onFileCreated:" + file);
            }
    
            @Override
            public void onFileModified(AdvancedFile file) {
                Logger.logDebug("onFileModified:" + file);
            }
    
            @Override
            public void onFileDeleted(AdvancedFile file) {
                Logger.logDebug("onFileDeleted:" + file);
            }
    
            @Override
            public void onFileRenamed(AdvancedFile file) {
                Logger.logDebug("onFileRenamed:" + file);
            }
    
            @Override
            public void onDirectoryCreated(AdvancedFile directory) {
                Logger.logDebug("onDirectoryCreated:" + directory);
            }
    
            @Override
            public void onDirectoryModified(AdvancedFile directory) {
                Logger.logDebug("onDirectoryModified:" + directory);
            }
    
            @Override
            public void onDirectoryDeleted(AdvancedFile directory) {
                Logger.logDebug("onDirectoryDeleted:" + directory);
            }
    
            @Override
            public void onDirectoryRenamed(AdvancedFile directory) {
                Logger.logDebug("onDirectoryRenamed:" + directory);
            }
        });
        Logger.log("advancedFileMonitor=" + advancedFileMonitor);
        advancedFileMonitor.start();
        Logger.log("advancedFileMonitor=" + advancedFileMonitor);
        Standard.addShutdownHook(advancedFileMonitor::stop);
        Standard.async(() -> {
            Thread.sleep(20000);
            Logger.log("Exiting");
            System.exit(0);
        });
    }
    
}
