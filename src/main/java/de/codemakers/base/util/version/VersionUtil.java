/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

package de.codemakers.base.util.version;

import de.codemakers.base.util.RegExUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VersionUtil {
    
    public static final String DEFAULT_DELIMITER = ".";
    
    public static Version parseToVersion(String text) {
        return parseToVersion(text, DEFAULT_DELIMITER);
    }
    
    public static Version parseToVersion(String text, String delimiter) {
        final String[] split = text.split(RegExUtil.escapeToRegEx(delimiter));
        final List<VersionPart> versionParts = new ArrayList<>();
        for (String versionPartString : split) {
            final String temp = StringUtils.getDigits(versionPartString);
            if (versionPartString.equals(temp)) {
                versionParts.add(new IntegerVersionPart(versionParts.size(), Integer.parseInt(versionPartString)));
            } else {
                versionParts.add(new StringVersionPart(versionParts.size(), versionPartString));
            }
        }
        return new Version(versionParts);
    }
    
    public static Version createVersion(Object... versionParts) {
        final List<VersionPart> versionParts_list = new ArrayList<>();
        for (Object versionPart : versionParts) {
            if (versionPart instanceof String) {
                final String versionPartString = (String) versionPart;
                final String temp = StringUtils.getDigits(versionPartString);
                if (versionPartString.equals(temp)) {
                    versionParts_list.add(new IntegerVersionPart(versionParts_list.size(), Integer.parseInt(versionPartString)));
                } else {
                    versionParts_list.add(new StringVersionPart(versionParts_list.size(), versionPartString));
                }
            } else if (versionPart instanceof Integer) {
                versionParts_list.add(new IntegerVersionPart(versionParts_list.size(), (Integer) versionPart));
            } else {
                throw new IllegalArgumentException(versionPart.getClass().getSimpleName() + " is not a valid " + VersionPart.class.getSimpleName() + " type");
            }
        }
        return new Version(versionParts_list);
    }
    
    public static int compareVersions(String version_1, String version_2) {
        return parseToVersion(version_1).compareTo(parseToVersion(version_2));
    }
    
}
