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

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import de.codemakers.base.os.functions.OSFunction;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class OSHelper {
    
    protected final ClassToInstanceMap<OSFunction> osFunctions = MutableClassToInstanceMap.create();
    
    public abstract boolean isPathAbsolute(String path);
    
    public abstract String getFileSeparator();
    
    public abstract char getFileSeparatorChar();
    
    public abstract String getFileSeparatorRegex();
    
    public abstract String getPathSeparator();
    
    public abstract char getPathSeparatorChar();
    
    public abstract String getPathSeparatorRegex();
    
    public abstract String getLineSeparator();
    
    public String toStringIntern() {
        String newLine = "";
        for (char c : getLineSeparator().toCharArray()) {
            newLine += ((int) c);
        }
        return getClass().getSimpleName() + ": fileSep = " + getFileSeparator() + ", pathSep = " + getPathSeparator() + ", newLine = " + newLine;
    }
    
    public ClassToInstanceMap<OSFunction> getOSFunctions() {
        return osFunctions;
    }
    
    public <T extends OSFunction> List<T> getOSFunctions(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        if (osFunctions.isEmpty()) {
            return null;
        }
        return osFunctions.values().stream().filter((osFunction) -> (clazz.isAssignableFrom(osFunction.getClass()))).map((osFunction) -> (T) osFunction).collect(Collectors.toList());
    }
    
    public <T extends OSFunction> T getOSFunction(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        if (osFunctions.isEmpty()) {
            return null;
        }
        return osFunctions.getInstance(clazz);
    }
    
    public List<OSFunction> getOSFunctions(String name) {
        return osFunctions.values().stream().filter((osFunction) -> Objects.equals(name, osFunction.getName())).collect(Collectors.toList());
    }
    
    public <T extends OSFunction> T putOSFunction(T osFunction) {
        Objects.requireNonNull(osFunction);
        return putOSFunction(osFunction.getClass(), osFunction);
    }
    
    public <T extends OSFunction> T putOSFunction(Class<? extends OSFunction> clazz, T osFunction) {
        Objects.requireNonNull(osFunction);
        osFunctions.put(clazz, osFunction);
        return osFunction;
    }
    
    public boolean removeOSFunction(OSFunction osFunction) {
        return osFunctions.remove(osFunction) != null;
    }
    
}
