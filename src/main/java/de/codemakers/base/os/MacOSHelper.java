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

import de.codemakers.base.logger.Logger;
import de.codemakers.base.os.function.BatteryInfo;
import de.codemakers.base.os.function.BatteryState;
import de.codemakers.base.os.function.OSFunction;
import de.codemakers.base.os.function.SystemInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MacOSHelper implements OSHelper {
    
    public static final String PATTERN_STRING = "Now drawing from '(.+)' (.+) \\(id=(\\d+)\\)\t(\\d{1,3})%; (.+); (\\d+:\\d{1,2})(?: remaining present: (\\w+))?";
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);
    
    private static final AtomicLong LAST_ID = new AtomicLong(-1);
    private static final Map<Long, OSFunction> OS_FUNCTIONS = new ConcurrentHashMap<>();
    
    static {
        OS_FUNCTIONS.put(LAST_ID.incrementAndGet(), new SystemInfo() {
            @Override
            public BatteryInfo getBatteryInfo() {
                try {
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("pmset -g ps").getInputStream()));
                    final Matcher matcher = PATTERN.matcher(bufferedReader.lines().collect(Collectors.joining()));
                    if (matcher.matches()) {
                        bufferedReader.close();
                        return new BatteryInfo(matcher.group(3), matcher.group(2), Double.parseDouble(matcher.group(4)) / 100.0, BatteryState.of(matcher.group(5)), (long) (Double.parseDouble(matcher.group(6).replace(':', '.')) * 60.0), TimeUnit.MINUTES);
                    }
                    bufferedReader.close();
                    return null;
                } catch (Exception ex) {
                    Logger.handleError(ex);
                    return null;
                }
            }
        });
    }
    
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
    public List<OSFunction> getOSFunctions() {
        return new ArrayList<>(OS_FUNCTIONS.values());
    }
    
    @Override
    public <T extends OSFunction> T getOSFunction(long id) {
        return (T) OS_FUNCTIONS.get(id);
    }
    
    @Override
    public long addOSFunction(OSFunction osFunction) {
        Objects.requireNonNull(osFunction);
        final long id = LAST_ID.incrementAndGet();
        OS_FUNCTIONS.put(id, osFunction);
        return id;
    }
    
    @Override
    public boolean removeOSFunction(long id) {
        return OS_FUNCTIONS.remove(id) != null;
    }
    
    @Override
    public String toString() {
        return toStringIntern();
    }
    
}
