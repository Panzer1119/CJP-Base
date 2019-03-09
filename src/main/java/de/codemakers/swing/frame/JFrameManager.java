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

import org.apache.commons.text.StringSubstitutor;

import javax.swing.*;

public class JFrameManager extends JFrame {

    public static final String TITLE_FORMAT_TITLE = "title";
    public static final String TITLE_FORMAT_VAR_TITLE = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_TITLE + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_VERSION = "version";
    public static final String TITLE_FORMAT_VAR_VERSION = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_VERSION + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_IDE = "ide"; //TODO Rename this to "ide_state" or something similar?
    public static final String TITLE_FORMAT_VAR_IDE = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_IDE + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_PREFIX = "prefix";
    public static final String TITLE_FORMAT_VAR_PREFIX = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_PREFIX + StringSubstitutor.DEFAULT_VAR_END;
    public static final String TITLE_FORMAT_SUFFIX = "suffix";
    public static final String TITLE_FORMAT_VAR_SUFFIX = StringSubstitutor.DEFAULT_VAR_START + TITLE_FORMAT_SUFFIX + StringSubstitutor.DEFAULT_VAR_END;
    /**
     * Value = "{@link #TITLE_FORMAT_VAR_PREFIX}{@link #TITLE_FORMAT_VAR_TITLE}{@link #TITLE_FORMAT_VAR_VERSION}{@link #TITLE_FORMAT_VAR_IDE}{@link #TITLE_FORMAT_VAR_SUFFIX}"
     */
    public static final String DEFAULT_TITLE_FORMAT = TITLE_FORMAT_VAR_PREFIX + TITLE_FORMAT_VAR_TITLE + TITLE_FORMAT_VAR_VERSION + TITLE_FORMAT_VAR_IDE + TITLE_FORMAT_VAR_SUFFIX;

}
