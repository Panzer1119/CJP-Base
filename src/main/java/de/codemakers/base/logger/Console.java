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
import de.codemakers.base.util.interfaces.Closeable;
import de.codemakers.base.util.interfaces.Reloadable;
import de.codemakers.base.util.tough.ToughRunnable;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.streams.BufferedPipedOutputStream;
import de.codemakers.io.streams.PipedInputStream;
import de.codemakers.io.streams.PipedOutputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Console implements Closeable, Reloadable {
    
    public static final String DEFAULT_ICON = "Farm-Fresh_application_xp_terminal.png";
    public static final AdvancedFile DEFAULT_ICON_FILE = new AdvancedFile(Standard.ICONS_FOLDER, DEFAULT_ICON);
    //Language key constants
    public static final String LANGUAGE_KEY_CONSOLE = "console";
    public static final String LANGUAGE_KEY_FILE = "file";
    public static final String LANGUAGE_KEY_RELOAD = "reload";
    public static final String LANGUAGE_KEY_SAVE_AS = "save_as";
    public static final String LANGUAGE_KEY_RESTART = "restart";
    public static final String LANGUAGE_KEY_EXIT = "exit";
    public static final String LANGUAGE_KEY_OPTIONS = "options";
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
    
    protected final List<LogEntry> logEntries = new CopyOnWriteArrayList<>();
    protected final Map<LogLevel, Boolean> logLevelDisplayStatus = new ConcurrentHashMap<>();
    protected final Set<LogLevel> displayedLogLevels = new CopyOnWriteArraySet<>();
    
    protected final JFrame frame = new JFrame(Standard.localize(LANGUAGE_KEY_CONSOLE)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuBar menuBar = new JMenuBar();
    protected final JMenu menu_file = new JMenu(Standard.localize(LANGUAGE_KEY_FILE)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuItem menuItem_reload = new JMenuItem(Standard.localize(LANGUAGE_KEY_RELOAD)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuItem menuItem_saveAs = new JMenuItem(Standard.localize(LANGUAGE_KEY_SAVE_AS)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuItem menuItem_restart = new JMenuItem(Standard.localize(LANGUAGE_KEY_RESTART)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenuItem menuItem_exit = new JMenuItem(Standard.localize(LANGUAGE_KEY_EXIT)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JMenu menu_options = new JMenu(Standard.localize(LANGUAGE_KEY_OPTIONS)); //FIXME Language/Localization stuff?! //Reloading Language??
    //Output
    protected final JLabel label_displayedLogLevels = new JLabel(Standard.localize(LANGUAGE_KEY_DISPLAYED_LOG_LEVELS)); //FIXME Language/Localization stuff?! //Reloading Language??
    protected final JCheckBoxMenuItem[] checkBoxMenuItems_logLevels = Stream.of(LogLevel.values()).map((logLevel) -> {
        final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(Standard.localize(logLevel.getUnlocalizedName())); //FIXME Language/Localization stuff?! //Reloading Language??
        checkBoxMenuItem.setSelected(Logger.getDefaultAdvancedLeveledLogger().getMinimumLogLevel().isThisLevelLessImportantOrEqual(logLevel));
        logLevelDisplayStatus.put(logLevel, checkBoxMenuItem.isSelected());
        if (checkBoxMenuItem.isSelected()) {
            displayedLogLevels.add(logLevel);
        } else {
            displayedLogLevels.remove(logLevel);
        }
        checkBoxMenuItem.addActionListener((actionEvent) -> {
            logLevelDisplayStatus.put(logLevel, checkBoxMenuItem.isSelected());
            if (checkBoxMenuItem.isSelected()) {
                displayedLogLevels.add(logLevel);
            } else {
                displayedLogLevels.remove(logLevel);
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
    
    protected final int shutdownHookId = Standard.addShutdownHook(this::closeIntern);
    protected final BufferedPipedOutputStream pipedOutputStream = new BufferedPipedOutputStream();
    protected final PipedInputStream pipedInputStream = new PipedInputStream();
    
    public Console() {
        this(DEFAULT_ICON_FILE);
    }
    
    public Console(AdvancedFile iconAdvancedFile) {
        super();
        init();
        initIconImage(iconAdvancedFile);
        initListeners();
        initStreams();
        setPreferredSize(new Dimension(1200, 600)); //TODO Testing only
        reloadWithoutException(); //TODO Good?
    }
    
    protected abstract boolean runCommand(String command) throws Exception;
    
    protected boolean runCommandWithoutException(String command) {
        try {
            return runCommand(command);
        } catch (Exception ex) {
            Logger.logError("Error while handling command \"" + command + "\"", ex);
            return false;
        }
    }
    
    /**
     * Handles input
     *
     * @param input Input
     *
     * @return If this input was handled (e.g. it was determined to be a command)
     *
     * @throws Exception
     */
    protected abstract InputType handleInput(String input) throws Exception; //FIXME Where is determined if this is an LogLevel.INPUT or an LogLevel.COMMAND?
    
    protected InputType handleInputWithoutException(String input) {
        try {
            return Objects.requireNonNull(handleInput(input), "handleInput(input)");
        } catch (Exception ex) {
            Logger.logError("Error while handling input \"" + input + "\"", ex);
            return InputType.ERRORED;
        }
    }
    
    private void initStreams() {
        Standard.silentError(() -> pipedOutputStream.connect(pipedInputStream));
        pipedOutputStream.setThreadFunction((thread) -> {
            thread.setName(Console.class.getSimpleName() + "-" + PipedOutputStream.class.getSimpleName() + "-" + Thread.class.getSimpleName());
            return thread;
        });
    }
    
    private void initListeners() {
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
                //TODO Implement this thing, where you can switch between old inputs with Arrow Keys Up and Down
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
            final InputType inputType = handleInputWithoutException(input);
            if (inputType.canInputBeCleared()) {
                textField_input.setText("");
            }
            if (!inputType.isHandled()) {
                Standard.silentError(() -> pipedOutputStream.write(input.getBytes()));
            }
            if (inputType == InputType.COMMAND) {
                runCommandWithoutException(input);
            }
        });
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
    }
    
    private void initIconImage(AdvancedFile advancedFile) {
        try (final InputStream inputStream = advancedFile.createInputStream()) {
            frame.setIconImage(ImageIO.read(inputStream));
        } catch (Exception ex) {
            Logger.logError("Error while loading " + getClass().getSimpleName() + " icon", ex);
        }
    }
    
    protected List<LogEntry> getLogEntries() {
        return logEntries;
    }
    
    protected List<LogEntry> getLogEntriesFilteredByLogLevel() {
        return logEntries.stream().filter((logEntry) -> logEntry.getLogLevel() == null || displayedLogLevels.contains(logEntry.getLogLevel())).collect(Collectors.toList());
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
    }
    
    @Override
    protected void finalize() throws Throwable {
        Standard.useWhenNotNull(Standard.removeShutdownHook(shutdownHookId), ToughRunnable::runWithoutException);
        super.finalize();
    }
    
    public enum InputTypeOLD {
        /**
         * The input has been handled and should not be treated further
         */
        HANDLED(true, true, false),
        /**
         * The input has not been handled and should be treated further
         */
        UNHANDLED(true, false, true),
        /**
         * The input has been handled, but should be treated further
         */
        CONTINUE(true, true, true),
        /**
         * The input has been recognized as a command and should not be treated further
         */
        COMMAND(true, true, false),
        /**
         * The input has been determined illegal and should not be treated further
         */
        ILLEGAL(false, false, false),
        /**
         * The input caused some error and should not be treated further
         */
        ERRORED(false, false, false),
        /**
         * The input is unknown and should be treated further
         */
        UNKNOWN(true, false, true);
        
        private final boolean clear;
        private final boolean handled;
        private final boolean write;
        
        InputTypeOLD(boolean clear, boolean handled, boolean write) {
            this.clear = clear;
            this.handled = handled;
            this.write = write;
        }
        
        public boolean isClear() {
            return clear;
        }
        
        public boolean isHandled() {
            return handled;
        }
        
        public boolean isWrite() {
            return write;
        }
        
    }
    
    public static class InputType2 { //FIXME Make a class that is returned to determine what should happen with an input? (13.03.2019 00:33)
    
    }
    
    public enum InputType {
        /**
         * The input should not be cleared, has not been handled and should not be written
         */
        ILLEGAL_UNHANDLED_STOP(false, false, false),
        /**
         * The input should not be cleared, has not been handled and should be written
         */
        ILLEGAL_UNHANDLED_CONTINUE(false, false, true),
        /**
         * The input should not be cleared, has been handled and should not be written
         */
        ILLEGAL_HANDLED_STOP(false, true, false),
        /**
         * The input should not be cleared, has been handled and should be written
         */
        ILLEGAL_HANDLED_CONTINUE(false, true, true),
        /**
         * The input should be cleared, has not been handled and should not be written
         */
        UNHANDLED_STOP(true, false, false),
        /**
         * The input should be cleared, has not been handled and should be written
         */
        UNHANDLED_CONTINUE(true, false, true),
        /**
         * The input should be cleared, has been handled and should not be written
         */
        STOP(true, true, false),
        /**
         * The input should be cleared, has been handled and should be written
         */
        CONTINUE(true, true, true);
        
        private final boolean clear;
        private final boolean handled;
        private final boolean write;
        
        InputType(boolean clear, boolean handled, boolean write) {
            this.clear = clear;
            this.handled = handled;
            this.write = write;
        }
        
        public boolean isClear() {
            return clear;
        }
        
        public boolean isHandled() {
            return handled;
        }
        
        public boolean isWrite() {
            return write;
        }
        
    }
    
}
