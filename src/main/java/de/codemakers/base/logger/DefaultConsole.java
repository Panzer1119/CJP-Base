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
import de.codemakers.base.entities.UpdatableBoundResettableVariable;
import de.codemakers.io.file.AdvancedFile;
import org.apache.commons.text.StringSubstitutor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultConsole extends Console<AdvancedLeveledLogger> {
    
    public DefaultConsole() {
        this(Logger.getDefaultAdvancedLeveledLogger());
    }
    
    public DefaultConsole(AdvancedLeveledLogger logger) {
        super(logger);
    }
    
    public DefaultConsole(AdvancedFile iconAdvancedFile, AdvancedFile iconSettingsAdvancedFile) {
        this(Logger.getDefaultAdvancedLeveledLogger(), iconAdvancedFile, iconSettingsAdvancedFile);
    }
    
    public DefaultConsole(AdvancedLeveledLogger logger, AdvancedFile iconAdvancedFile, AdvancedFile iconSettingsAdvancedFile) {
        super(logger, iconAdvancedFile, iconSettingsAdvancedFile);
    }
    
    @Override
    protected ConsoleSettings createConsoleSettings(AdvancedFile iconAdvancedFile) {
        return new DefaultConsoleSettings(iconAdvancedFile);
    }
    
    protected String getLogFormat() {
        return logger.getLogFormat();
    }
    
    protected DefaultConsole setLogFormat(String logFormat) {
        logger.setLogFormat(logFormat);
        return this;
    }
    
    protected String getSourceFormat() {
        return logger.getSourceFormat();
    }
    
    protected DefaultConsole setSourceFormat(String sourceFormat) {
        logger.setSourceFormat(sourceFormat);
        return this;
    }
    
    @Override
    public boolean reload() throws Exception {
        final List<LeveledLogEntry> logEntries = getLogEntriesFilteredByLogLevel();
        final StyledDocument styledDocument = textPane_output.getStyledDocument();
        styledDocument.remove(0, styledDocument.getLength()); //TODO Good? Because when there are too many LogEntries, this could cause lag
        final Style style = styledDocument.addStyle("LogEntryStyle", null);
        for (LeveledLogEntry logEntry : logEntries) {
            StyleConstants.setBackground(style, logEntry.getLogLevel() == null ? Color.WHITE : logEntry.getLogLevel().getColorBackground());
            StyleConstants.setForeground(style, logEntry.getLogLevel() == null ? Color.BLACK : logEntry.getLogLevel().getColorForeground());
            styledDocument.insertString(styledDocument.getLength(), StringSubstitutor.replace(logger.getLogFormat(), logger.createValueMap(logEntry)) + "\n", style);
        }
        textPane_output.setCaretPosition(styledDocument.getLength());
        return true;
    }
    
    @Override
    protected boolean handleInput(String input) throws Exception {
        if (input.isEmpty()) {
            return false;
        }
        if (input.startsWith("/")) {
            return runCommand(input);
        }
        //Logger.log(input, LogLevel.INPUT);
        write((input + "\n").getBytes());
        return true;
    }
    
    protected boolean runCommand(final String command) throws Exception {
        Logger.log(command, LogLevel.COMMAND);
        String temp = command.substring("/".length()).trim();
        if (temp.startsWith("lang")) {
            temp = temp.substring("lang".length()).trim();
            if (temp.equalsIgnoreCase("english")) {
                Standard.setLocalizer(Standard.getLocalizerEnglishUs());
                Logger.logDebug("Setted localizer to english us");
                return true;
            } else if (temp.equalsIgnoreCase("default")) {
                Standard.setLocalizer(Standard.getLocalizerDefault());
                Logger.logDebug("Setted localizer to default");
                return true;
            }
        } else if (temp.startsWith("test")) {
            temp = temp.substring("test".length()).trim();
            if (temp.equalsIgnoreCase("1")) {
                frame.setTitle("" + Math.random());
                Logger.logDebug("COMMAND: \"Test 1\"");
                return true;
            }
        }
        //throw new NotYetImplementedRuntimeException(); //TODO Implement Command stuff
        Logger.logWarning(String.format("Command \"%s\" does not exist", command));
        return false;
    }
    
    public class DefaultConsoleSettings extends ConsoleSettings {
        
        public static final String LANGUAGE_KEY_SETTINGS = "settings";
        public static final String LANGUAGE_KEY_TAB_GENERAL = "general";
        public static final String LANGUAGE_KEY_TAB_VIEW = "view";
        public static final String LANGUAGE_KEY_BUTTON_OK = "button_ok";
        public static final String LANGUAGE_KEY_BUTTON_CANCEL = "button_cancel";
        public static final String LANGUAGE_KEY_BUTTON_RESET = "button_reset";
        public static final String LANGUAGE_KEY_BUTTON_APPLY = "button_apply";
        
        protected final JDialog dialog = new JDialog(frame, true);
        
        // Bottom Buttons
        protected final JButton button_ok = new JButton(Standard.localize(LANGUAGE_KEY_BUTTON_OK));
        protected final JTabbedPane tabbedPane = new JTabbedPane();
        protected final JPanel panel_tab_general = new JPanel();
        protected final JPanel panel_tab_view = new JPanel();
        protected final JButton button_cancel = new JButton(Standard.localize(LANGUAGE_KEY_BUTTON_CANCEL));
        protected final JButton button_reset = new JButton(Standard.localize(LANGUAGE_KEY_BUTTON_RESET));
        protected final JButton button_apply = new JButton(Standard.localize(LANGUAGE_KEY_BUTTON_APPLY));
        
        protected final UpdatableBoundResettableVariable<String> titleBound = new UpdatableBoundResettableVariable<>(frame::getTitle, frame::setTitle); //FIXME Testing only //This is working great!
        protected final UpdatableBoundResettableVariable<String> logFormatBound = new UpdatableBoundResettableVariable<>(DefaultConsole.this::getLogFormat, DefaultConsole.this::setLogFormat); //FIXME Testing only
        protected final UpdatableBoundResettableVariable<String> sourceFormatBound = new UpdatableBoundResettableVariable<>(DefaultConsole.this::getSourceFormat, DefaultConsole.this::setSourceFormat); //FIXME Testing only
        
        protected final boolean livePreview = true; //FIXME Testing only?
        
        public DefaultConsoleSettings(AdvancedFile iconAdvancedFile) {
            init();
            initIconImage(iconAdvancedFile);
            initListeners();
            dialog.setPreferredSize(new Dimension(600, 800)); //TODO Testing only
            reloadLanguageWithoutException();
            test(); //FIXME Testing only
        }
        
        private void test() { //FIXME Testing only
            final JTextArea textArea = new JTextArea();
            textArea.setText(titleBound.getCurrent());
            titleBound.setToughConsumer((title) -> {
                frame.setTitle(title);
                textArea.setText(title);
            });
            final JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                }
                
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                }
                
                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    titleBound.setTemp(textArea.getText());
                    if (livePreview) {
                        titleBound.testWithoutException();
                    }
                    onAction();
                }
            });
            //dialog.add(scrollPane, BorderLayout.CENTER);
            tabbedPane.addTab(Standard.localize("test"), scrollPane); //TODO What if language reloads?
            final JPanel panel = new JPanel();
            button_ok.addActionListener((actionEvent) -> {
                finishWithoutException();
                hide();
            });
            button_cancel.addActionListener((actionEvent) -> {
                resetWithoutException();
                hide();
            });
            button_reset.addActionListener((actionEvent) -> {
                resetWithoutException();
                onAction();
            });
            button_apply.addActionListener((actionEvent) -> {
                finishWithoutException();
                onAction();
            });
            panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
            panel.add(button_ok);
            panel.add(button_cancel);
            panel.add(button_reset);
            panel.add(button_apply);
            dialog.add(panel, BorderLayout.SOUTH);
        }
        
        private void init() {
            dialog.setResizable(false);
            dialog.setLayout(new BorderLayout());
            tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            tabbedPane.addTab(Standard.localize(LANGUAGE_KEY_TAB_GENERAL), panel_tab_general); //TODO What if language reloads?
            //TODO Test start
            panel_tab_view.setLayout(new GridLayout(2, 2));
            panel_tab_view.add(new JLabel("Log Format")); //FIXME Language reloading!!!
            final JTextPane textPane_logFormat = new JTextPane();
            final CustomDocumentFilter customDocumentFilter_1 = new CustomDocumentFilter(textPane_logFormat, Arrays.asList(Logger.LOG_FORMAT_TIMESTAMP, Logger.LOG_FORMAT_THREAD, Logger.LOG_FORMAT_SOURCE, Logger.LOG_FORMAT_LOG_LEVEL, Logger.LOG_FORMAT_OBJECT));
            textPane_logFormat.setText(getLogFormat());
            textPane_logFormat.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                }
                
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                }
                
                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    logFormatBound.setTemp(textPane_logFormat.getText());
                    if (livePreview) {
                        logFormatBound.testWithoutException();
                    }
                    onAction();
                }
            });
            logFormatBound.setToughConsumer((logFormat) -> {
                DefaultConsole.this.setLogFormat(logFormat);
                if (!textPane_logFormat.getText().equals(logFormat)) {
                    textPane_logFormat.setText(logFormat);
                }
                DefaultConsole.this.reloadWithoutException(); //FIXME Or should this be executed in the finish method?
            });
            //panel_tab_view.add(textArea_logFormat);
            panel_tab_view.add(new JScrollPane(textPane_logFormat));
            panel_tab_view.add(new JLabel("Source Format")); //FIXME Language reloading!!!
            final JTextPane textPane_sourceFormat = new JTextPane();
            final CustomDocumentFilter customDocumentFilter_2 = new CustomDocumentFilter(textPane_sourceFormat, Arrays.asList("test", Logger.SOURCE_FORMAT_CLASS, Logger.SOURCE_FORMAT_METHOD, Logger.SOURCE_FORMAT_FILE, Logger.SOURCE_FORMAT_LINE));
            textPane_sourceFormat.setText(getSourceFormat());
            textPane_sourceFormat.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                }
                
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                }
                
                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    sourceFormatBound.setTemp(textPane_sourceFormat.getText());
                    if (livePreview) {
                        sourceFormatBound.testWithoutException();
                    }
                    onAction();
                }
            });
            sourceFormatBound.setToughConsumer((sourceFormat) -> {
                DefaultConsole.this.setSourceFormat(sourceFormat);
                if (!textPane_sourceFormat.getText().equals(sourceFormat)) {
                    textPane_sourceFormat.setText(sourceFormat);
                }
                DefaultConsole.this.reloadWithoutException(); //FIXME Or should this be executed in the finish method?
            });
            //panel_tab_view.add(textArea_sourceFormat);
            panel_tab_view.add(new JScrollPane(textPane_sourceFormat));
            //TODO Test end
            tabbedPane.addTab(Standard.localize(LANGUAGE_KEY_TAB_VIEW), panel_tab_view); //TODO What if language reloads?
            dialog.add(tabbedPane, BorderLayout.CENTER);
        }
        
        private void initIconImage(AdvancedFile advancedFile) {
            try (final InputStream inputStream = advancedFile.createInputStream()) {
                dialog.setIconImage(ImageIO.read(inputStream));
            } catch (Exception ex) {
                Logger.logError("Error while loading icon for dialog", ex);
            }
        }
        
        private void initListeners() {
            dialog.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }
                
                @Override
                public void windowClosing(WindowEvent e) {
                    closing();
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
        }
        
        @Override
        protected void showing() {
            titleBound.updateWithoutException();
            logFormatBound.updateWithoutException();
            sourceFormatBound.updateWithoutException();
            onAction();
        }
        
        @Override
        protected void closing() {
            resetWithoutException();
        }
        
        @Override
        public ConsoleSettings showAtConsole() {
            return show(frame);
        }
        
        @Override
        public ConsoleSettings show(Component component) {
            dialog.pack();
            dialog.setLocationRelativeTo(component);
            showing();
            dialog.setVisible(true);
            return this;
        }
        
        @Override
        public ConsoleSettings hide() {
            dialog.setVisible(false);
            return this;
        }
        
        @Override
        protected void onAction() {
            final boolean edited = isEdited();
            button_reset.setEnabled(edited);
            button_apply.setEnabled(edited);
        }
        
        @Override
        protected boolean isEdited() {
            return titleBound.isDifferent() || logFormatBound.isDifferent() || sourceFormatBound.isDifferent();
        }
        
        @Override
        protected boolean isNotEdited() {
            return titleBound.isSame() && logFormatBound.isSame() && sourceFormatBound.isSame();
        }
        
        @Override
        public boolean reloadLanguage() throws Exception {
            dialog.setTitle(Standard.localize(LANGUAGE_KEY_SETTINGS));
            button_ok.setText(Standard.localize(LANGUAGE_KEY_BUTTON_OK));
            button_cancel.setText(Standard.localize(LANGUAGE_KEY_BUTTON_CANCEL));
            button_reset.setText(new String(Standard.localize(LANGUAGE_KEY_BUTTON_RESET).getBytes(), "UTF-8")); //FIXME Why is this not working?
            button_apply.setText(Standard.localize(LANGUAGE_KEY_BUTTON_APPLY));
            dialog.invalidate();
            dialog.repaint();
            return true;
        }
        
        @Override
        public boolean unloadLanguage() throws Exception {
            return true;
        }
        
        @Override
        public Boolean finish() throws Exception {
            titleBound.finish();
            logFormatBound.finish();
            sourceFormatBound.finish();
            return true;
        }
        
        @Override
        public boolean reset() throws Exception {
            boolean good = true;
            if (!titleBound.reset()) {
                good = false;
            }
            if (!logFormatBound.reset()) {
                good = false;
            }
            if (!sourceFormatBound.reset()) {
                good = false;
            }
            return good;
        }
        
        public class CustomDocumentFilter extends DocumentFilter {
            
            private final JTextPane textPane;
            private final StyledDocument styledDocument;
            private final StyleContext styleContext = StyleContext.getDefaultStyleContext();
            private final AttributeSet attributeSetOrangeFont = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
            private final AttributeSet attributeSetRedFont = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.RED);
            private final AttributeSet attributeSetBlackFont = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
            private final List<String> tokens = new ArrayList<>();
            
            private final Pattern pattern_keyWords;
            private final Pattern pattern_invalidWords = Pattern.compile("\\$\\{.*?\\}");
            
            public CustomDocumentFilter(JTextPane textPane, List<String> tokens) {
                this.textPane = textPane;
                styledDocument = textPane.getStyledDocument();
                ((AbstractDocument) textPane.getDocument()).setDocumentFilter(this);
                this.tokens.addAll(tokens);
                pattern_keyWords = buildPatternKeyWords();
            }
            
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
                handleTextChanged();
            }
            
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                super.insertString(fb, offset, string, attr);
                handleTextChanged();
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                super.replace(fb, offset, length, text, attrs);
                handleTextChanged();
            }
            
            private void handleTextChanged() {
                SwingUtilities.invokeLater(this::updateTextStyles);
            }
            
            private Pattern buildPatternKeyWords() {
                final StringBuilder stringBuilder = new StringBuilder();
                for (String token : tokens) {
                    Logger.logDebug("token=\"" + token + "\"");
                    stringBuilder.append("\\b");
                    stringBuilder.append("\\$\\{");
                    stringBuilder.append(token);
                    stringBuilder.append("\\}");
                    stringBuilder.append("\\b|");
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                Logger.logDebug("stringBuilder=" + stringBuilder);
                return Pattern.compile(stringBuilder.toString());
            }
            
            private void updateTextStyles() {
                styledDocument.setCharacterAttributes(0, textPane.getText().length(), attributeSetBlackFont, true);
                replacePattern(pattern_invalidWords, attributeSetRedFont);
                //replacePattern(pattern_keyWords, attributeSetOrangeFont);
                for (String token : tokens) {
                    replacePattern(Pattern.compile("\\$\\{" + token + "\\}"), attributeSetOrangeFont); //FIXME Improve this
                }
            }
            
            private void replacePattern(Pattern pattern, AttributeSet attributeSet) {
                final Matcher matcher = pattern.matcher(textPane.getText());
                while (matcher.find()) {
                    styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), attributeSet, false);
                }
            }
            
        }
        
    }
    
}
