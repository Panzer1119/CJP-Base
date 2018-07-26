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

import de.codemakers.base.os.functions.OSFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class MacOSHelper implements OSHelper {
    
    AtomicLong LAST_ID = new AtomicLong(-1);
    Map<Long, OSFunction> OS_FUNCTIONS = new ConcurrentHashMap<>();
    
    public static final String PATTERN_BATTERY_INFO_MAC_OS_STRING = "Now drawing from '(.+)' (.+) \\(id=(\\d+)\\)\t(\\d{1,3})%; (.+); (\\d+:\\d{1,2})(?: remaining present: (\\w+))?";
    public static final Pattern PATTERN_BATTERY_INFO_MAC_OS = Pattern.compile(PATTERN_BATTERY_INFO_MAC_OS_STRING);
    
    @Override
    public boolean isPathAbsolute(String path) {
        return OSUtil.LINUX_HELPER.isPathAbsolute(path);
    }
    
    @Override
    public String getFileSeparator() {
        return OSUtil.LINUX_HELPER.getFileSeparator();
    }
    
    @Override
    public String getPathSeparator() {
        return OSUtil.LINUX_HELPER.getPathSeparator();
    }
    
    @Override
    public String getLineSeparator() {
        return "\r";
    }
    
    @Override
    public AtomicLong getIDCounter() {
        return LAST_ID;
    }
    
    @Override
    public Map<Long, OSFunction> getOSFunctionsMap() {
        return OS_FUNCTIONS;
    }
    
    @Override
    public String toString() {
        return toStringIntern();
    }
    
}
