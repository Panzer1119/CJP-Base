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

import de.codemakers.base.os.function.OSFunction;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface OSHelper {
    
    boolean isPathAbsolute(String path);
    
    String getFileSeparator();
    
    String getPathSeparator();
    
    String getLineSeparator();
    
    default String toStringIntern() {
        String newLine = "";
        for (char c : getLineSeparator().toCharArray()) {
            newLine += ((int) c);
        }
        return getClass().getSimpleName() + ": fileSep = " + getFileSeparator() + ", pathSep = " + getPathSeparator() + ", newLine = " + newLine;
    }
    
    List<OSFunction> getOSFunctions();
    
    default <T extends OSFunction> List<T> getOSFunctions(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        final List<OSFunction> osFunctions = getOSFunctions();
        if (osFunctions == null || osFunctions.isEmpty()) {
            return null;
        }
        return osFunctions.stream().filter((osFunction) -> (clazz.isAssignableFrom(osFunction.getClass()))).map((osFunction) -> (T) osFunction).collect(Collectors.toList());
    }
    
    default <T extends OSFunction> T getOSFunction(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        final List<OSFunction> osFunctions = getOSFunctions();
        if (osFunctions == null || osFunctions.isEmpty()) {
            return null;
        }
        return (T) osFunctions.stream().filter((osFunction) -> (clazz.isAssignableFrom(osFunction.getClass()))).findFirst().orElse(null);
    }
    
    default List<OSFunction> getOSFunctions(String name) {
        Objects.requireNonNull(name);
        final List<OSFunction> osFunctions = getOSFunctions();
        if (osFunctions == null || osFunctions.isEmpty()) {
            return null;
        }
        return osFunctions.stream().filter((osFunction) -> (name.equals(osFunction.getName()))).collect(Collectors.toList());
    }
    
    <T extends OSFunction> T getOSFunction(long id);
    
    long addOSFunction(OSFunction osFunction);
    
    boolean removeOSFunction(long id);
    
}
