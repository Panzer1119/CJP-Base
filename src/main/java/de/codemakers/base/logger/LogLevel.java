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

import java.awt.*;

public enum LogLevel {
    
    FINEST(false, 7, Color.WHITE, Color.LIGHT_GRAY),
    FINER(false, 6, Color.WHITE, Color.GRAY),
    FINE(false, 5, Color.WHITE, Color.DARK_GRAY),
    DEBUG(false, 4, Color.WHITE, Color.DARK_GRAY),
    INFO(false, 3, Color.WHITE, Color.BLACK),
    COMMAND(false, 2, Color.WHITE, Color.MAGENTA),
    INPUT(false, 1, Color.WHITE, Color.BLUE),
    WARNING(true, 0, Color.WHITE, Color.ORANGE),
    ERROR(true, -1, Color.WHITE, Color.RED);
    
    private final boolean isBad;
    /**
     * The higher the level the less important is this LogLevel
     */
    private final int level;
    private Color colorBackground;
    private Color colorForeground;
    
    LogLevel(boolean isBad, int level, Color colorBackground, Color colorForeground) {
        this.isBad = isBad;
        this.level = level;
        this.colorBackground = colorBackground;
        this.colorForeground = colorForeground;
    }
    
    public boolean isBad() {
        return isBad;
    }
    
    public int getLevel() {
        return level;
    }
    
    public Color getColorBackground() {
        return colorBackground;
    }
    
    public LogLevel setColorBackground(Color colorBackground) {
        this.colorBackground = colorBackground;
        return this;
    }
    
    public Color getColorForeground() {
        return colorForeground;
    }
    
    public LogLevel setColorForeground(Color colorForeground) {
        this.colorForeground = colorForeground;
        return this;
    }
    
    public String toText() {
        return name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1);
    }
    
}
