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
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

@Plugin(name = GraphicConsoleAppender.PLUGIN_NAME, category = "Core", elementType = Appender.ELEMENT_TYPE, printObject = true)
public class GraphicConsoleAppender extends AbstractAppender {
    
    public static final String PLUGIN_NAME = "GraphicConsole";
    
    /**
     * Builds ConsoleAppender instances.
     *
     * @param <B> The type to build
     */
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<GraphicConsoleAppender> {
        
        @PluginBuilderAttribute
        private String consoleName = "default";
        
        @Override
        public GraphicConsoleAppender build() {
            final String consoleName = getConsoleName();
            final Console<?> console = Standard.getConsoleByName(consoleName);
            if (console == null) {
                //TODO Throw error?
            }
            return new GraphicConsoleAppender(getName(), getFilter(), getLayout(), isIgnoreExceptions(), getPropertyArray(), console);
        }
        
        public String getConsoleName() {
            return consoleName;
        }
        
        public B setConsoleName(String consoleName) {
            this.consoleName = consoleName;
            return asBuilder();
        }
        
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private final Console<?> console;
    
    public GraphicConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, Console<?> console) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.console = Objects.requireNonNull(console);
    }
    
    @Override
    public void append(LogEvent logEvent) {
        final Instant timestamp = Instant.ofEpochMilli(logEvent.getInstant().getEpochMillisecond());
        int levelTemp = (logEvent.getLevel().intLevel() / 100) - 3;
        if (levelTemp == 1) {
            levelTemp++;
        }
        if (levelTemp == 2) {
            levelTemp++;
        }
        levelTemp = Math.min(levelTemp, 7);
        levelTemp = Math.max(levelTemp, -1);
        final int level = levelTemp;
        final LogLevel logLevel = Arrays.stream(LogLevel.values()).filter(temp -> temp.getLevel() == level).findFirst().orElse(null);
        final long threadId = logEvent.getThreadId();
        final Thread thread = getThreadById(threadId);
        final boolean bad = logLevel != null && logLevel.isBad();
        console.logEntries.add(new LeveledLogEntry(logEvent.getMessage(), timestamp, thread, logEvent.getSource(), logEvent.getThrown(), bad, logLevel));
        console.reloadWithoutException();
    }
    
    public static Thread getThreadById(long id) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getId() == id) {
                return thread;
            }
        }
        return null;
    }
    
}
