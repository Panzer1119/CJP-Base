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

package de.codemakers.swing.frame;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.StringUtil;
import de.codemakers.io.file.AdvancedFile;
import org.apache.commons.text.StringSubstitutor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Queue;
import java.util.*;
import java.util.stream.Collectors;

//FIXME This is crap?! Maybe i need to create a completely new JFrameManager class...
public class JFrameManager extends JFrame {
    
    public static final String TITLE_FORMAT_NAME = "name";
    public static final String TITLE_FORMAT_VAR_NAME = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_NAME + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_VERSION = "version";
    public static final String TITLE_FORMAT_VAR_VERSION = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_VERSION + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_IDE = "ide"; //TODO Rename this to "ide_state" or something similar?
    public static final String TITLE_FORMAT_VAR_IDE = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_IDE + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_PREFIX = "prefix";
    public static final String TITLE_FORMAT_VAR_PREFIX = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_PREFIX + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_SUFFIX = "suffix";
    public static final String TITLE_FORMAT_VAR_SUFFIX = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_SUFFIX + StringSubstitutor.DEFAULT_VAR_END;
    /**
     * Value = "{@link #TITLE_FORMAT_VAR_PREFIX}{@link #TITLE_FORMAT_VAR_NAME}{@link #TITLE_FORMAT_VAR_VERSION}{@link #TITLE_FORMAT_VAR_IDE}{@link #TITLE_FORMAT_VAR_SUFFIX}"
     */
    public static final String DEFAULT_TITLE_FORMAT = TITLE_FORMAT_VAR_PREFIX + TITLE_FORMAT_VAR_NAME + " V" + TITLE_FORMAT_VAR_VERSION + TITLE_FORMAT_VAR_IDE + TITLE_FORMAT_VAR_SUFFIX;
    public static final String DEFAULT_PREFIX_OR_SUFFIX_DELIMITER = " - ";
    
    protected String name;
    protected String version;
    protected String titleFormat = DEFAULT_TITLE_FORMAT;
    protected final Map<String, Object> titleFormatValues = new HashMap<>();
    protected final Queue<Object> queue_prefix = new LinkedList<>();
    protected final Queue<Object> queue_suffix = new LinkedList<>();
    
    public JFrameManager() {
        this("");
    }
    
    public JFrameManager(String name) {
        this(name, "");
    }
    
    public JFrameManager(String name, String version) {
        super();
        titleFormatValues.put(TITLE_FORMAT_IDE, Standard.RUNNING_JAR_IS_JAR ? "" : " IDE");
        updateTitle();
        setName(name);
        setVersion(version);
    }
    
    public void setDefaultSettings() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateTitle();
    }
    
    public void show(Component c) {
        updateTitle();
        pack();
        setVisible(true);
        setLocationRelativeTo(c);
    }
    
    public void updateTitle() {
        titleFormatValues.put(TITLE_FORMAT_PREFIX, queue_prefix.stream().map(StringUtil::toString).collect(Collectors.joining(DEFAULT_PREFIX_OR_SUFFIX_DELIMITER, "", " ")));
        titleFormatValues.put(TITLE_FORMAT_SUFFIX, queue_suffix.stream().map(StringUtil::toString).collect(Collectors.joining(DEFAULT_PREFIX_OR_SUFFIX_DELIMITER, " ", "")));
        setTitle(StringSubstitutor.replace(titleFormat, titleFormatValues));
    }
    
    public void setIconImage(String internPath) {
        setIconImage(new AdvancedFile(AdvancedFile.PREFIX_INTERN + internPath));
    }
    
    public void setIconImage(AdvancedFile advancedFile) {
        try (final InputStream inputStream = advancedFile.createInputStream()) {
            setIconImage(ImageIO.read(inputStream));
        } catch (Exception ex) {
            Logger.logError("Error while loading icon", ex);
        }
    }
    
    public JFrameTitleFormatBuilder createJFrameTitleFormatBuilder() {
        return new JFrameTitleFormatBuilder() {
            @Override
            public String finish() throws Exception {
                final String temp = toFormat();
                setTitleFormat(temp);
                return temp;
            }
        };
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
        titleFormatValues.put(TITLE_FORMAT_NAME, name);
        updateTitle();
    }
    
    public String getVersion() {
        return version;
    }
    
    public JFrameManager setVersion(String version) {
        this.version = version;
        titleFormatValues.put(TITLE_FORMAT_VERSION, version);
        updateTitle();
        return this;
    }
    
    public String getTitleFormat() {
        return titleFormat;
    }
    
    public JFrameManager setTitleFormat(String titleFormat) {
        this.titleFormat = Objects.requireNonNull(titleFormat, "titleFormat");
        return this;
    }
    
    public Queue<Object> getQueuePrefix() {
        return queue_prefix;
    }
    
    public JFrameManager addPrefix(Object object) {
        queue_prefix.add(object);
        updateTitle();
        return this;
    }
    
    public boolean removePrefix(Object object) {
        queue_prefix.remove(object);
        updateTitle();
        return queue_prefix.contains(object);
    }
    
    public Queue<Object> getQueueSuffix() {
        return queue_suffix;
    }
    
    public JFrameManager addSuffix(Object object) {
        queue_suffix.add(object);
        updateTitle();
        return this;
    }
    
    public boolean removeSuffix(Object object) {
        queue_suffix.remove(object);
        updateTitle();
        return queue_suffix.contains(object);
    }
    
    @Override
    public String toString() {
        return "JFrameManager{" + "name='" + name + '\'' + ", version='" + version + '\'' + ", titleFormat='" + titleFormat + '\'' + ", titleFormatValues=" + titleFormatValues + '}';
    }
    
}
