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
import de.codemakers.base.util.tough.ToughConsumer;

public class AdvancedLeveledSystemLogger extends AdvancedLeveledLogger {
    
    private ToughConsumer<LeveledLogEntry> preLogEntryToughConsumer = null; //TODO Writing LogEntries to Console?
    private ToughConsumer<LeveledLogEntry> postLogEntryToughConsumer = null; //TODO Writing LogEntries to AdvancedFile?
    
    public ToughConsumer<LeveledLogEntry> getPreLogEntryToughConsumer() {
        return preLogEntryToughConsumer;
    }
    
    public AdvancedLeveledSystemLogger setPreLogEntryToughConsumer(ToughConsumer<LeveledLogEntry> preLogEntryToughConsumer) {
        this.preLogEntryToughConsumer = preLogEntryToughConsumer;
        return this;
    }
    
    public ToughConsumer<LeveledLogEntry> getPostLogEntryToughConsumer() {
        return postLogEntryToughConsumer;
    }
    
    public AdvancedLeveledSystemLogger setPostLogEntryToughConsumer(ToughConsumer<LeveledLogEntry> postLogEntryToughConsumer) {
        this.postLogEntryToughConsumer = postLogEntryToughConsumer;
        return this;
    }
    
    @Override
    protected void preFinal(LogEntry logEntry) {
        if (preLogEntryToughConsumer != null) {
            preLogEntryToughConsumer.acceptWithoutException((LeveledLogEntry) logEntry);
        }
    }
    
    @Override
    protected void logFinal(LogEntry logEntry) { //Writing LogEntries to System.out/System.err
        if (logEntry == null) {
            return; //FIXME Remove this? Or is this not a performance problem?
        }
        if (logEntry.isBad()) {
            Standard.SYSTEM_ERROR_STREAM.println(logEntry.formatWithoutException(this));
            if (logEntry.hasError()) {
                logEntry.getThrowable().printStackTrace(Standard.SYSTEM_ERROR_STREAM);
            }
        } else {
            Standard.SYSTEM_OUTPUT_STREAM.println(logEntry.formatWithoutException(this));
        }
    }
    
    @Override
    protected void postFinal(LogEntry logEntry) {
        if (postLogEntryToughConsumer != null) {
            postLogEntryToughConsumer.acceptWithoutException((LeveledLogEntry) logEntry);
        }
    }
    
}
