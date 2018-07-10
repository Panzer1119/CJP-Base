/*
 *     Copyright 2018 Paul Hagedorn (Panzer1119)
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

package de.codemakers.base.os;

import de.codemakers.base.util.ArrayUtil;

public enum OS {
    WINDOWS("Windows", new String[] {"Windows Server 2019", "Windows Server 2016", "Windows 10 Mobile", "Windows 10", "Windows Server 2012", "Windows RT", "Windows Phone 8", "Windows 8", "Windows Server 2008 R2", "Windows 7", "Windows Server 2008", "Windows Vista", "Windows XP Professional", "Windows Server 2003", "Windows 2003", "Windows XP", "Windows 2000", "Windows NT", "Windows Me", "Windows 98", "Windows 95"}), LINUX("Linux", new String[] {"Unix"}), MACOS("Mac OS", new String[] {"Mac OS X"}), SUNOS("SunOS", new String[0]), FREEBSD("FreeBSD", new String[0]), UNKNOWN("Unknown", new String[0]);
    
    private final String name;
    private final String[] versions;
    
    OS(String name, String[] versions) {
        this.name = name;
        this.versions = versions;
    }
    
    public final String getName() {
        return name;
    }
    
    public final String[] getVersions() {
        return versions;
    }
    
    public static final OS getOS(String name) {
        if (name == null || name.isEmpty()) {
            return UNKNOWN;
        }
        for (OS os : values()) {
            if (name.equalsIgnoreCase(os.name) || name.startsWith(os.name) || ArrayUtil.arrayContains(os.versions, name) || name.toLowerCase().startsWith(os.name.toLowerCase())) {
                return os;
            }
        }
        return UNKNOWN;
    }
    
}
