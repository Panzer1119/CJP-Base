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
import de.codemakers.base.entities.History;
import de.codemakers.base.util.interfaces.Closeable;
import de.codemakers.base.util.interfaces.Finishable;
import de.codemakers.base.util.interfaces.Reloadable;
import de.codemakers.base.util.interfaces.Resettable;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.streams.BufferedPipedOutputStream;
import de.codemakers.io.streams.PipedInputStream;
import de.codemakers.io.streams.PipedOutputStream;
import de.codemakers.lang.LanguageReloadable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Console implements Closeable, LanguageReloadable, Reloadable {
    
    private static final Logger logger = LogManager.getLogger();
    
    public static final String DEFAULT_ICON = "application_xp_terminal.png";
    public static final String DEFAULT_ICON_SETTINGS = "gear_in.png";
    public static final AdvancedFile DEFAULT_ICON_FILE = new AdvancedFile(Standard.ICONS_FAT_COW_32x32_FOLDER, DEFAULT_ICON);
    public static final AdvancedFile DEFAULT_ICON_SETTINGS_FILE = new AdvancedFile(Standard.ICONS_FAT_COW_32x32_FOLDER, DEFAULT_ICON_SETTINGS);
    //Language key constants
    public static final String LANGUAGE_KEY_CONSOLE = "console";
    public static final String LANGUAGE_KEY_FILE = "file";
    public static final String LANGUAGE_KEY_RELOAD = "reload";
    public static final String LANGUAGE_KEY_SETTINGS = "settings";
    public static final String LANGUAGE_KEY_SAVE_AS = "save_as";
    public static final String LANGUAGE_KEY_RESTART = "restart";
    public static final String LANGUAGE_KEY_EXIT = "exit";
    public static final String LANGUAGE_KEY_VIEW = "view";
    public static final String LANGUAGE_KEY_DISPLAYED_LOG_LEVELS = "console_displayed_log_levels";
    public static final String LANGUAGE_KEY_DISPLAY = "console_display";
    public static final String LANGUAGE_KEY_TIMESTAMP = "console_timestamp";
    public static final String LANGUAGE_KEY_THREAD = "console_thread";
    public static final String LANGUAGE_KEY_SOURCE = "console_source";
    public static final String LANGUAGE_KEY_LOG_LEVEL = "console_log_level";
    public static final String LANGUAGE_KEY_ENTER = "button_enter";
    public static final String LANGUAGE_KEY_OUTPUT = "console_output";
    public static final String LANGUAGE_KEY_INPUT = "console_input";
    
    public static final String DEFAULT_FONT_NAME = "Courier New";
    
    protected final List<LeveledLogEntry> logEntries = new CopyOnWriteArrayList<>(); //FIXME Save them here?
    protected final Map<Level, Boolean> logLevelDisplayStatus = new ConcurrentHashMap<>();
    protected final Set<Level> displayedLogLevels = new CopyOnWriteArraySet<>();
    
    protected final JFrame frame = new JFrame(Standard.localize(LANGUAGE_KEY_CONSOLE)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuBar menuBar = new JMenuBar();
    protected final JMenu menu_file = new JMenu(Standard.localize(LANGUAGE_KEY_FILE)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuItem menuItem_reload = new JMenuItem(Standard.localize(LANGUAGE_KEY_RELOAD)); //FIXME Language/Localization stuff?! //Reloading Language??
    //JSeparator
    protected final JMenuItem menuItem_settings = new JMenuItem(Standard.localize(LANGUAGE_KEY_SETTINGS));
    //JSeparator
    protected final JMenuItem menuItem_saveAs = new JMenuItem(Standard.localize(LANGUAGE_KEY_SAVE_AS)); //FIXME Language/Localization stuff?! //Reloading Language??
    //JSeparator
    protected final JMenuItem menuItem_restart = new JMenuItem(Standard.localize(LANGUAGE_KEY_RESTART)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuItem menuItem_exit = new JMenuItem(Standard.localize(LANGUAGE_KEY_EXIT)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenu menu_view = new JMenu(Standard.localize(LANGUAGE_KEY_VIEW));
    //Output
    protected final JLabel label_displayedLogLevels = new JLabel(Standard.localize(LANGUAGE_KEY_DISPLAYED_LOG_LEVELS)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JCheckBoxMenuItem[] checkBoxMenuItems_logLevels = Stream.of(LogLevel.LEVELS).map((level) -> {
        final LogLevelStyle logLevelStyle = LogLevelStyle.ofLevel(level);
        final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(Standard.localize(logLevelStyle.getLangKey())); //FIXME Language/Localization stuff?! //Reloading Language??
        //checkBoxMenuItem.setSelected(Logger.getDefaultAdvancedLeveledLogger().getMinimumLogLevel().isThisLevelLessImportantOrEqual(level));
        //TODO Where to set/determine from what level should be shown?
        logLevelDisplayStatus.put(level, checkBoxMenuItem.isSelected());
        if (checkBoxMenuItem.isSelected()) {
            displayedLogLevels.add(level);
        } else {
            displayedLogLevels.remove(level);
        }
        checkBoxMenuItem.addActionListener((actionEvent) -> {
            logLevelDisplayStatus.put(level, checkBoxMenuItem.isSelected());
            if (checkBoxMenuItem.isSelected()) {
                displayedLogLevels.add(level);
            } else {
                displayedLogLevels.remove(level);
            }
            reloadWithoutException();
        });
        return checkBoxMenuItem;
    }).toArray(JCheckBoxMenuItem[]::new);
    protected final JLabel label_display = new JLabel(Standard.localize(LANGUAGE_KEY_DISPLAY)); //FIXME Rename this? //FIXME Language/Localization stuff?! //Reloading Language??
    //TODO Maybe make this customizable in a separate (settings) window? (Like switching positioning of the elements e.g. with another LogFormatBuilder)
    protected final JCheckBoxMenuItem checkBoxMenuItem_displayTimestamp = new JCheckBoxMenuItem(Standard.localize(LANGUAGE_KEY_TIMESTAMP)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JCheckBoxMenuItem checkBoxMenuItem_displayThread = new JCheckBoxMenuItem(Standard.localize(LANGUAGE_KEY_THREAD)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JCheckBoxMenuItem checkBoxMenuItem_displaySource = new JCheckBoxMenuItem(Standard.localize(LANGUAGE_KEY_SOURCE)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JCheckBoxMenuItem checkBoxMenuItem_displayLogLevel = new JCheckBoxMenuItem(Standard.localize(LANGUAGE_KEY_LOG_LEVEL)); //FIXME Language/Localization stuff?! //Reloading Language??
    //TODO What was the "Debug Mode"?
    protected final JTextPane textPane_output = new JTextPane();
    protected final JScrollPane scrollPane_output = new JScrollPane(textPane_output);
    //Input
    protected final JPanel panel_input = new JPanel();
    protected final JTextField textField_input = new JTextField();
    protected final JButton button_input = new JButton(Standard.localize(LANGUAGE_KEY_ENTER)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final BufferedPipedOutputStream pipedOutputStream = new BufferedPipedOutputStream();
    protected final PipedInputStream pipedInputStream = new PipedInputStream();
    protected final int shutdownHookId = Standard.addShutdownHook(this::closeIntern);
    
    protected final ConsoleSettings consoleSettings;
    
    protected final History<String> inputHistory = new History<>();
    
    public Console() {
        this(DEFAULT_ICON_FILE, DEFAULT_ICON_SETTINGS_FILE);
    }
    
    public Console(AdvancedFile iconAdvancedFile, AdvancedFile iconSettingsAdvancedFile) {
        init();
        initIconImage(iconAdvancedFile);
        initListeners();
        initStreams();
        consoleSettings = createConsoleSettings(iconSettingsAdvancedFile);
        setPreferredSize(new Dimension(1200, 600)); //TODO Testing only
        reloadWithoutException(); //TODO Good?
    }
    
    protected void write(int b) throws IOException {
        pipedOutputStream.write(b);
    }
    
    protected void write(byte[] data) throws IOException {
        pipedOutputStream.write(data);
    }
    
    protected void write(byte[] data, int off, int len) throws IOException {
        pipedOutputStream.write(data, off, len);
    }
    
    protected void flush() throws IOException {
        pipedOutputStream.flush();
    }
    
    protected abstract ConsoleSettings createConsoleSettings(AdvancedFile iconAdvancedFile);
    
    /**
     * Handles input
     *
     * @param input Input
     *
     * @return If the {@link #textField_input} should be cleared
     *
     * @throws Exception
     */
    protected abstract boolean handleInput(String input) throws Exception; //FIXME Where is determined if this is an LogLevel.INPUT or an LogLevel.COMMAND?
    
    protected boolean handleInputWithoutException(String input) {
        try {
            return handleInput(input);
        } catch (Exception ex) {
            logger.error("Error while handling input \"" + input + "\"", ex);
            return true;
        }
    }
    
    @Override
    public boolean reloadLanguage() throws Exception {
        frame.setTitle(Standard.localize(LANGUAGE_KEY_CONSOLE));
        menu_file.setText(Standard.localize(LANGUAGE_KEY_FILE));
        menuItem_reload.setText(Standard.localize(LANGUAGE_KEY_RELOAD));
        menuItem_settings.setText(Standard.localize(LANGUAGE_KEY_SETTINGS));
        menuItem_saveAs.setText(Standard.localize(LANGUAGE_KEY_SAVE_AS));
        menuItem_restart.setText(Standard.localize(LANGUAGE_KEY_RESTART));
        menuItem_exit.setText(Standard.localize(LANGUAGE_KEY_EXIT));
        menu_view.setText(Standard.localize(LANGUAGE_KEY_VIEW));
        label_displayedLogLevels.setText(Standard.localize(LANGUAGE_KEY_DISPLAYED_LOG_LEVELS));
        final LogLevelStyle[] logLevelStyles = LogLevelStyle.values();
        for (int i = 0; i < logLevelStyles.length; i++) {
            checkBoxMenuItems_logLevels[i].setText(Standard.localize(logLevelStyles[i].getLangKey()));
        }
        label_display.setText(Standard.localize(LANGUAGE_KEY_DISPLAY));
        checkBoxMenuItem_displayTimestamp.setText(Standard.localize(LANGUAGE_KEY_TIMESTAMP));
        checkBoxMenuItem_displayThread.setText(Standard.localize(LANGUAGE_KEY_THREAD));
        checkBoxMenuItem_displaySource.setText(Standard.localize(LANGUAGE_KEY_SOURCE));
        checkBoxMenuItem_displayLogLevel.setText(Standard.localize(LANGUAGE_KEY_LOG_LEVEL));
        button_input.setText(Standard.localize(LANGUAGE_KEY_ENTER));
        ((TitledBorder) scrollPane_output.getBorder()).setTitle(Standard.localize(LANGUAGE_KEY_OUTPUT));
        ((TitledBorder) panel_input.getBorder()).setTitle(Standard.localize(LANGUAGE_KEY_INPUT));
        frame.invalidate();
        frame.repaint();
        return consoleSettings.reloadLanguage();
    }
    
    @Override
    public boolean unloadLanguage() throws Exception {
        return consoleSettings.unloadLanguage();
    }
    
    private void initStreams() {
        Standard.silentError(() -> pipedOutputStream.connect(pipedInputStream));
        pipedOutputStream.setThreadFunction((thread) -> {
            thread.setName(Console.class.getSimpleName() + "-" + PipedOutputStream.class.getSimpleName() + "-" + Thread.class.getSimpleName());
            return thread;
        });
    }
    
    private void initListeners() {
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                hide();
            }
            
            @Override
            public void windowClosed(WindowEvent e) {
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            
            @Override
            public void windowActivated(WindowEvent e) {
            }
            
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        //TODO Add ActionListeners and KeyInputListeners for the (CheckBox)MenuItems and the TextField/Button
        //Output
        final ActionListener actionListener_reload = actionEvent -> reloadWithoutException();
        menuItem_reload.addActionListener(actionListener_reload);
        checkBoxMenuItem_displayTimestamp.addActionListener(actionListener_reload);
        checkBoxMenuItem_displayThread.addActionListener(actionListener_reload);
        checkBoxMenuItem_displaySource.addActionListener(actionListener_reload);
        checkBoxMenuItem_displayLogLevel.addActionListener(actionListener_reload);
        //Input
        textField_input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }
            
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    setInputTextField(inputHistory.previous());
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    setInputTextField(inputHistory.next());
                }
            }
            
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    button_input.doClick();
                }
            }
        });
        button_input.addActionListener((actionEvent) -> {
            final String input = textField_input.getText();
            if (handleInputWithoutException(input)) { //FIXME What do, when the last input is the exact same as this? Ignore the duplicate? A Set would not be useful, because maybe there is an input between 2 same inputs and then they should be both in the history? So make a toggle to enable a function that not saves the new input, if the exact last one is exact the same?
                inputHistory.add(input);
                textField_input.setText("");
            }
        });
    }
    
    private void init() {
        Standard.getDefaultLanguageReloader().addLanguageReloadable(this);
        menu_file.add(menuItem_settings);
        menu_file.add(new JSeparator());
        menu_file.add(menuItem_reload);
        menu_file.add(menuItem_saveAs);
        menu_file.add(new JSeparator());
        menu_file.add(menuItem_restart);
        menu_file.add(menuItem_exit);
        menuBar.add(menu_file);
        menu_view.add(label_displayedLogLevels);
        for (JCheckBoxMenuItem checkBoxMenuItem : checkBoxMenuItems_logLevels) {
            menu_view.add(checkBoxMenuItem); //TODO ActionListener or something similar?
        }
        menu_view.add(new JSeparator());
        menu_view.add(label_display);
        menu_view.add(checkBoxMenuItem_displayTimestamp);
        menu_view.add(checkBoxMenuItem_displayThread);
        menu_view.add(checkBoxMenuItem_displaySource);
        menu_view.add(checkBoxMenuItem_displayLogLevel);
        menuBar.add(menu_view);
        frame.setJMenuBar(menuBar);
        frame.setLayout(new BorderLayout());
        textPane_output.setEditable(false);
        final Font font = textPane_output.getFont();
        textPane_output.setFont(new Font(DEFAULT_FONT_NAME, font.getStyle(), font.getSize()));
        scrollPane_output.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Standard.localize(LANGUAGE_KEY_OUTPUT))); //TODO Is this looking good? //FIXME Language/Localization stuff?! //Reloading Language??
        frame.add(scrollPane_output, BorderLayout.CENTER);
        panel_input.setLayout(new BoxLayout(panel_input, BoxLayout.X_AXIS));
        panel_input.add(textField_input);
        panel_input.add(button_input);
        panel_input.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Standard.localize(LANGUAGE_KEY_INPUT))); //TODO Is this looking good? //FIXME Language/Localization stuff?! //Reloading Language??
        frame.add(panel_input, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    private void initIconImage(AdvancedFile advancedFile) {
        try (final InputStream inputStream = advancedFile.createInputStream()) {
            frame.setIconImage(ImageIO.read(inputStream));
        } catch (Exception ex) {
            logger.error("Error while loading icon for frame", ex);
        }
    }
    
    protected void setInputTextField(String input) {
        textField_input.setText(input == null ? "" : input);
    }
    
    protected List<LeveledLogEntry> getLogEntries() {
        return logEntries;
    }
    
    protected List<LeveledLogEntry> getLogEntriesFilteredByLogLevel() {
        return logEntries.stream().filter((entry) -> entry.getLevel() == null || displayedLogLevels.contains(entry.getLevel())).collect(Collectors.toList());
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
    
    @Override
    public void closeIntern() throws Exception {
        pipedInputStream.close();
        pipedOutputStream.close(); //TODO Is the order important? Close OutputStream before InputStream or vice versa?
        Standard.getDefaultLanguageReloader().removeLanguageReloadable(this);
    }
    
    /*
    //Do not use this, but if we need that?
    @Override
    protected void finalize() throws Throwable {
        Standard.useWhenNotNull(Standard.removeShutdownHook(shutdownHookId), ToughRunnable::runWithoutException);
        super.finalize();
    }
    */
    
    public InputStream getInputStream() {
        return pipedInputStream;
    }
    
    public abstract class ConsoleSettings<C extends ConsoleSettings> implements Finishable<Boolean>, LanguageReloadable, Resettable {
        
        protected abstract void showing();
        
        protected abstract void closing();
        
        public abstract C showAtConsole();
        
        public abstract C show(Component component);
        
        public abstract C hide();
        
        protected abstract void onAction();
        
        protected abstract boolean isEdited();
        
        protected abstract boolean isNotEdited();
        
    }
    
}
