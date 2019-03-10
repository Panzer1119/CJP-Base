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
import java.awt.*;
import java.io.InputStream;
import java.util.stream.Stream;

public class Console {
    
    public static final String DEFAULT_ICON = "Farm-Fresh_application_xp_terminal.png";
    public static final AdvancedFile DEFAULT_ICON_FILE = new AdvancedFile(Standard.ICONS_FOLDER, DEFAULT_ICON);
    
    protected final JFrame frame = new JFrame("Console"); //FIXME Language/Localization stuff?!
    protected final JMenuBar menuBar = new JMenuBar();
    protected final JMenu menu_file = new JMenu("File"); //FIXME Language/Localization stuff?!
    protected final JMenuItem menuItem_reload = new JMenuItem("Reload"); //FIXME Language/Localization stuff?!
    protected final JMenuItem menuItem_saveAs = new JMenuItem("Save As"); //FIXME Language/Localization stuff?!
    protected final JMenuItem menuItem_restart = new JMenuItem("Restart"); //FIXME Language/Localization stuff?!
    protected final JMenuItem menuItem_exit = new JMenuItem("Exit"); //FIXME Language/Localization stuff?!
    protected final JMenu menu_options = new JMenu("Options"); //FIXME Language/Localization stuff?!
    protected final JLabel label_displayedLogLevels = new JLabel("Displayed Log Levels"); //FIXME Language/Localization stuff?!
    protected final JCheckBoxMenuItem[] checkBoxMenuItems_logLevels = Stream.of(LogLevel.values()).map((logLevel) -> {
        //FIXME Language/Localization stuff?!
        return new JCheckBoxMenuItem(logLevel.toText());
    }).toArray(JCheckBoxMenuItem[]::new);
    protected final JLabel label_display = new JLabel("Display"); //FIXME Rename this? //FIXME Language/Localization stuff?!
    protected final JCheckBoxMenuItem checkBoxMenuItem_displayTimestamp = new JCheckBoxMenuItem("Timestamp"); //FIXME Language/Localization stuff?!
    protected final JCheckBoxMenuItem checkBoxMenuItem_displayThread = new JCheckBoxMenuItem("Thread"); //FIXME Language/Localization stuff?!
    protected final JCheckBoxMenuItem checkBoxMenuItem_displaySource = new JCheckBoxMenuItem("Source"); //FIXME Language/Localization stuff?!
    protected final JCheckBoxMenuItem checkBoxMenuItem_displayLogLevel = new JCheckBoxMenuItem("Log Level"); //FIXME Language/Localization stuff?!
    //TODO What was the "Debug Mode"?
    protected final JTextPane textPane_output = new JTextPane();
    protected final JScrollPane scrollPane_output = new JScrollPane(textPane_output);
    protected final JPanel panel_input = new JPanel();
    protected final JTextField textField_input = new JTextField();
    protected final JButton button_input = new JButton("Enter"); //FIXME Language/Localization stuff?!
    
    public Console() {
        this(DEFAULT_ICON_FILE);
    }
    
    public Console(AdvancedFile iconAdvancedFile) {
        super();
        init();
        initIconImage(iconAdvancedFile);
        setPreferredSize(new Dimension(1200, 600)); //TODO Testing only
    }
    
    private void init() {
        menu_file.add(menuItem_reload);
        menu_file.add(new JSeparator());
        menu_file.add(menuItem_saveAs);
        menu_file.add(new JSeparator());
        menu_file.add(menuItem_restart);
        menu_file.add(menuItem_exit);
        menuBar.add(menu_file);
        menu_options.add(label_displayedLogLevels);
        for (JCheckBoxMenuItem checkBoxMenuItem : checkBoxMenuItems_logLevels) {
            menu_options.add(checkBoxMenuItem); //TODO ActionListener or something similar?
        }
        menu_options.add(new JSeparator());
        menu_options.add(label_display);
        menu_options.add(checkBoxMenuItem_displayTimestamp);
        menu_options.add(checkBoxMenuItem_displayThread);
        menu_options.add(checkBoxMenuItem_displaySource);
        menu_options.add(checkBoxMenuItem_displayLogLevel);
        menuBar.add(menu_options);
        frame.setJMenuBar(menuBar);
        frame.setLayout(new BorderLayout());
        scrollPane_output.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Output")); //TODO Is this looking good? //FIXME Language/Localization stuff?!
        frame.add(scrollPane_output, BorderLayout.CENTER);
        panel_input.setLayout(new BoxLayout(panel_input, BoxLayout.X_AXIS));
        panel_input.add(textField_input);
        panel_input.add(button_input);
        panel_input.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Input")); //TODO Is this looking good? //FIXME Language/Localization stuff?!
        frame.add(panel_input, BorderLayout.SOUTH);
        //TODO Add ActionListeners and KeyInputListeners for the (CheckBox)MenuItems and the TextField/Button
    }
    
    private void initIconImage(AdvancedFile advancedFile) {
        try (final InputStream inputStream = advancedFile.createInputStream()) {
            frame.setIconImage(ImageIO.read(inputStream));
        } catch (Exception ex) {
            Logger.logError("Error while loading " + getClass().getSimpleName() + " icon", ex);
        }
    }
    
    protected JFrame getFrame() {
        return frame;
    }
    
    public Console show() {
        return show(null);
    }
    
    public Console show(Component component) {
        frame.pack();
        frame.setLocationRelativeTo(component);
        frame.setVisible(true);
        return this;
    }
    
    public Console hide() {
        frame.setVisible(false);
        return this;
    }
    
    public Console setPreferredSize(Dimension dimension) {
        frame.setPreferredSize(dimension);
        return this;
    }
    
}
