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

package de.codemakers.base.env;

public class SystemProperties {
    
    public static final String USER_NAME = "user.name";
    public static final String USER_DIR = "user.dir";
    public static final String USER_HOME = "user.home";
    public static final String OS_NAME = "os.name";
    public static final String OS_ARCH = "os.arch";
    
    public static final String getProperty(String key) {
        return System.getProperty(key);
    }
    
    public static final String getProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
    
    public static String getUserName() {
        return getProperty(USER_NAME);
    }
    
    public static String getUserDir() {
        return getProperty(USER_DIR);
    }
    
    public static String getUserHome() {
        return getProperty(USER_HOME);
    }
    
    public static String getOsName() {
        return getProperty(OS_NAME);
    }
    
    public static String getOSArch() {
        return getProperty(OS_ARCH);
    }
    
}
