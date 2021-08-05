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

import org.apache.logging.log4j.Level;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

public enum LogLevelStyle {
    DEFAULT(Color.WHITE, Color.BLACK),
    OFF(Color.WHITE, Color.WHITE, LogLevel.OFF),
    FATAL(Color.ORANGE, Color.RED, LogLevel.FATAL),
    ERROR(Color.WHITE, Color.RED, LogLevel.ERROR),
    WARNING(Color.WHITE, Color.ORANGE, LogLevel.WARN),
    COMMAND(Color.WHITE, Color.MAGENTA, LogLevel.COMMAND),
    INPUT(Color.WHITE, Color.BLUE, LogLevel.INPUT),
    INFO(Color.WHITE, Color.BLACK, LogLevel.INFO),
    DEBUG(Color.WHITE, Color.DARK_GRAY, LogLevel.DEBUG),
    FINE(Color.WHITE, Color.DARK_GRAY, LogLevel.FINE),
    FINER(Color.WHITE, Color.GRAY, LogLevel.FINER),
    FINEST(Color.WHITE, Color.LIGHT_GRAY, LogLevel.FINEST),
    TRACE(Color.WHITE, Color.LIGHT_GRAY, LogLevel.TRACE),
    ALL(Color.WHITE, Color.BLACK, LogLevel.ALL);
    
    public static final int MINIMUM_NAME_LENGTH = Stream.of(LogLevel.LEVELS).map(Level::name).map(String::length).min(Integer::compareTo).orElse(Integer.MAX_VALUE);
    public static final int MAXIMUM_NAME_LENGTH = Stream.of(LogLevel.LEVELS).map(Level::name).map(String::length).max(Integer::compareTo).orElse(Integer.MIN_VALUE);
    public static final String LANGUAGE_KEY_PREFIX = "de.codemakers.base.logging.loglevel.";
    
    private Color colorBackground;
    private Color colorForeground;
    private Level[] levels;
    //
    private final String nameLowerCase = name().toLowerCase();
    private final String langKey = LANGUAGE_KEY_PREFIX + nameLowerCase;
    private transient String nameLeft = null;
    private transient String nameCenter = null;
    private transient String nameRight = null;
    
    LogLevelStyle(Color colorBackground, Color colorForeground, Level... levels) {
        this.colorBackground = colorBackground;
        this.colorForeground = colorForeground;
        this.levels = levels;
    }
    
    public Color getColorBackground() {
        return colorBackground;
    }
    
    public LogLevelStyle setColorBackground(Color colorBackground) {
        this.colorBackground = colorBackground;
        return this;
    }
    
    public Color getColorForeground() {
        return colorForeground;
    }
    
    public LogLevelStyle setColorForeground(Color colorForeground) {
        this.colorForeground = colorForeground;
        return this;
    }
    
    public Level[] getLevels() {
        return levels;
    }
    
    public LogLevelStyle setLevels(Level[] levels) {
        this.levels = levels;
        return this;
    }
    
    public String getNameLowerCase() {
        return nameLowerCase;
    }
    
    public String getLangKey() {
        return langKey;
    }
    
    public String toText() {
        return name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
    }
    
    public String getNameLeft() {
        if (nameLeft == null) {
            nameLeft = name();
            while (nameLeft.length() < MAXIMUM_NAME_LENGTH) {
                nameLeft += " ";
            }
        }
        return nameLeft;
    }
    
    public String getNameCenter() {
        if (nameCenter == null) {
            nameCenter = name();
            while (nameCenter.length() < MAXIMUM_NAME_LENGTH) {
                nameCenter += " ";
                if (nameCenter.length() < MAXIMUM_NAME_LENGTH) {
                    nameCenter = " " + nameCenter;
                }
            }
        }
        return nameCenter;
    }
    
    public String getNameRight() {
        if (nameRight == null) {
            nameRight = name();
            while (nameRight.length() < MAXIMUM_NAME_LENGTH) {
                nameRight = " " + nameRight;
            }
        }
        return nameRight;
    }
    
    public static LogLevelStyle ofLevel(Level level) {
        if (level == null) {
            return DEFAULT;
        }
        for (LogLevelStyle logLevelStyle : values()) {
            if (Arrays.asList(logLevelStyle.levels).contains(level)) {
                return logLevelStyle;
            }
        }
        return DEFAULT;
    }
    
}
