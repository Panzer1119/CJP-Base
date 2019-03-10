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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.InputStream;

public class Console extends JFrame {
    
    public static final String DEFAULT_ICON = "Farm-Fresh_application_xp_terminal.png";
    public static final AdvancedFile DEFAULT_ICON_FILE = new AdvancedFile(Standard.ICONS_FOLDER, DEFAULT_ICON);
    
    public Console() {
        this(DEFAULT_ICON_FILE);
    }
    
    public Console(AdvancedFile iconAdvancedFile) {
        super();
        init();
        initIconImage(iconAdvancedFile);
    }
    
    private void init() {
        //TODO
    }
    
    private void initIconImage(AdvancedFile advancedFile) {
        try (final InputStream inputStream = advancedFile.createInputStream()) {
            setIconImage(ImageIO.read(inputStream));
        } catch (Exception ex) {
            Logger.logError("Error while loading " + getClass().getSimpleName() + " icon", ex);
        }
    }
    
}
