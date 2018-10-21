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

import de.codemakers.base.exceptions.NotImplementedRuntimeException;
import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.os.functions.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class OSUtil {
    
    public static final String[] STANDARD_OS_NAMES = new String[] {"Windows", "Linux", "Mac OS", "SunOS", "FreeBSD"};
    
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final OS OS = de.codemakers.base.os.OS.getOS(OS_NAME);
    
    public static final WindowsHelper WINDOWS_HELPER = new WindowsHelper();
    public static final LinuxHelper LINUX_HELPER = new LinuxHelper();
    public static final MacOSHelper MAC_OS_HELPER = new MacOSHelper();
    public static final OSHelper DEFAULT_HELPER = LINUX_HELPER;
    public static final CurrentOSHelper CURRENT_OS_HELPER = new CurrentOSHelper();
    
    // SystemInfo
    protected static final SystemInfo OSFUNCTION_SYSTEM_INFO_WINDOWS;
    protected static final SystemInfo OSFUNCTION_SYSTEM_INFO_LINUX;
    protected static final SystemInfo OSFUNCTION_SYSTEM_INFO_MAC_OS;
    protected static final SystemInfo OSFUNCTION_SYSTEM_INFO_CURRENT;
    // SystemFunctions
    protected static final SystemFunctions OSFUNCTION_SYSTEM_FUNCTIONS_WINDOWS;
    protected static final SystemFunctions OSFUNCTION_SYSTEM_FUNCTIONS_LINUX;
    protected static final SystemFunctions OSFUNCTION_SYSTEM_FUNCTIONS_MAC_OS;
    protected static final SystemFunctions OSFUNCTION_SYSTEM_FUNCTIONS_CURRENT;
    // ==
    
    static {
        // Windows
        OSFUNCTION_SYSTEM_INFO_WINDOWS = WINDOWS_HELPER.putOSFunction(SystemInfo.class, new SystemInfo() {
            @Override
            public PowerInfo getBatteryInfo() {
                try {
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("WMIC path Win32_Battery").getInputStream()));
                    final String line_1 = bufferedReader.readLine();
                    bufferedReader.readLine();
                    final String line_2 = bufferedReader.readLine();
                    bufferedReader.close();
                    final Properties properties = new Properties();
                    WindowsHelper.process(line_1, line_2, properties);
                    final int batteryState_int = Integer.parseInt(properties.getProperty(WindowsHelper.BATTERY_STATUS));
                    BatteryState batteryState = null;
                    switch (batteryState_int) {
                        case 1:
                            batteryState = BatteryState.ERROR;
                            break;
                        case 3:
                            batteryState = BatteryState.FULL;
                            break;
                        case 4:
                            batteryState = BatteryState.DISCHARGING_LOW;
                            break;
                        case 5:
                            batteryState = BatteryState.DISCHARGING_CRITICAL;
                            break;
                        case 6:
                            batteryState = BatteryState.CHARGING;
                            break;
                        case 7:
                            batteryState = BatteryState.CHARGING_HIGH;
                            break;
                        case 8:
                            batteryState = BatteryState.CHARGING_LOW;
                            break;
                        case 9:
                            batteryState = BatteryState.CHARGING_CRITICAL;
                            break;
                        case 11:
                            batteryState = BatteryState.PARTIALLY_CHARGED;
                            break;
                        case 2:
                        case 10:
                        default:
                            batteryState = BatteryState.UNKNOWN;
                            break;
                    }
                    return new PowerInfo(properties.getProperty(WindowsHelper.DEVICE_ID), properties.getProperty(WindowsHelper.NAME), Double.parseDouble(properties.getProperty(WindowsHelper.ESTIMATED_CHARGE_REMAINING)) * 0.01, batteryState, Integer.parseInt(properties.getProperty(WindowsHelper.ESTIMATED_RUN_TIME)), TimeUnit.MINUTES, properties.getProperty(WindowsHelper.TIME_TO_FULL_CHARGE).isEmpty() ? -1 : Integer.parseInt(properties.getProperty(WindowsHelper.TIME_TO_FULL_CHARGE)), TimeUnit.MINUTES, PowerSupply.BATTERY, properties);
                } catch (Exception ex) {
                    Logger.handleError(ex);
                    return null;
                }
            }
        });
        OSFUNCTION_SYSTEM_FUNCTIONS_WINDOWS = WINDOWS_HELPER.putOSFunction(SystemFunctions.class, new SystemFunctions() {
            @Override
            public boolean lockMonitor(long delay, boolean force) throws Exception { //TODO Test this
                return Runtime.getRuntime().exec("rundll32.exe user32.dll, LockWorkStation").waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean logout(long delay, boolean force) throws Exception { //TODO Test this
                return Runtime.getRuntime().exec("shutdown –l " + (force ? "-f " : "") + "-t " + TimeUnit.MILLISECONDS.toSeconds(Math.max(0, delay))).waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean shutdown(long delay, boolean force) throws Exception { //TODO Test this
                return Runtime.getRuntime().exec("shutdown -s " + (force ? "-f " : "") + "-t " + TimeUnit.MILLISECONDS.toSeconds(Math.max(0, delay))).waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean reboot(long delay, boolean force) throws Exception { //TODO Test this
                return Runtime.getRuntime().exec("shutdown -r " + (force ? "-f " : "") + "-t " + TimeUnit.MILLISECONDS.toSeconds(Math.max(0, delay))).waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean lock(long delay, boolean force) {
                throw new NotImplementedException();
            }
            
            @Override
            public SystemInfo getSystemInfo() {
                return OSFUNCTION_SYSTEM_INFO_WINDOWS;
            }
        });
        // Linux
        OSFUNCTION_SYSTEM_INFO_LINUX = LINUX_HELPER.putOSFunction(SystemInfo.class, new SystemInfo() {
            @Override
            public PowerInfo getBatteryInfo() {
                try {
                    for (File file : LinuxHelper.FOLDER_AC.listFiles(pathname -> {
                        if (!pathname.isDirectory()) {
                            return false;
                        }
                        for (File f : pathname.listFiles()) {
                            if (f.getName().equalsIgnoreCase(LinuxHelper.FILE_UEVENT_NAME)) {
                                return true;
                            }
                        }
                        return false;
                    })) {
                        final File file_uevent = new File(file.getAbsolutePath() + File.separator + LinuxHelper.FILE_UEVENT_NAME);
                        if (file_uevent.exists()) {
                            final PowerSupply powerSupply = PowerSupply.of(file.getName());
                            if (powerSupply != PowerSupply.BATTERY) {
                                continue;
                            }
                            final Properties properties = new Properties();
                            properties.load(new FileReader(file_uevent));
                            return new PowerInfo(properties.getProperty(LinuxHelper.POWER_SUPPLY_SERIAL_NUMBER), properties.getProperty(LinuxHelper.POWER_SUPPLY_NAME), (Long.parseLong(properties.getProperty(LinuxHelper.POWER_SUPPLY_ENERGY_NOW)) * 1.0 / (Long.parseLong(properties.getProperty(LinuxHelper.POWER_SUPPLY_ENERGY_FULL)) * 1.0)), BatteryState.of(properties.getProperty(LinuxHelper.POWER_SUPPLY_STATUS)), -1, null, -1, null, powerSupply, properties);
                        }
                    }
                } catch (Exception ex) {
                    Logger.handleError(ex);
                }
                return null;
            }
        });
        OSFUNCTION_SYSTEM_FUNCTIONS_LINUX = LINUX_HELPER.putOSFunction(SystemFunctions.class, new SystemFunctions() {
            @Override
            public boolean lockMonitor(long delay, boolean force) throws Exception { //TODO Test this
                if (!Runtime.getRuntime().exec("gnome-screensaver-command --lock").waitFor(5, TimeUnit.SECONDS)) {
                    return Runtime.getRuntime().exec("dbus-send --type=method_call --dest=org.gnome.ScreenSaver /org/gnome/ScreenSaver org.gnome.ScreenSaver.Lock").waitFor(5, TimeUnit.SECONDS);
                }
                return true;
            }
            
            @Override
            public boolean logout(long delay, boolean force) {
                throw new NotYetImplementedRuntimeException(); //TODO Implement
            }
            
            @Override
            public boolean shutdown(long delay, boolean force) throws Exception { //TODO Test this (especially the missing "sudo")
                //TODO Test the delay implementation
                return Runtime.getRuntime().exec("shutdown " + (delay <= 0 ? "now" : TimeUnit.MILLISECONDS.toMinutes(delay))).waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean reboot(long delay, boolean force) throws Exception { //TODO Test this (especially the missing "sudo")
                //TODO Test the delay implementation
                return Runtime.getRuntime().exec("reboot " + (delay <= 0 ? "now" : TimeUnit.MILLISECONDS.toMinutes(delay))).waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean lock(long delay, boolean force) {
                throw new NotImplementedRuntimeException();
            }
            
            @Override
            public SystemInfo getSystemInfo() {
                return OSFUNCTION_SYSTEM_INFO_LINUX;
            }
        });
        // Max OS
        OSFUNCTION_SYSTEM_INFO_MAC_OS = MAC_OS_HELPER.putOSFunction(SystemInfo.class, new SystemInfo() {
            @Override
            public PowerInfo getBatteryInfo() {
                try {
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("pmset -g ps").getInputStream()));
                    final Matcher matcher = MacOSHelper.PATTERN_BATTERY_INFO_MAC_OS.matcher(bufferedReader.lines().collect(Collectors.joining()));
                    if (matcher.matches()) {
                        bufferedReader.close();
                        return new PowerInfo(matcher.group(3), matcher.group(2), Double.parseDouble(matcher.group(4)) / 100.0, BatteryState.of(matcher.group(5)), -1, null, (long) (Double.parseDouble(matcher.group(6).replace(':', '.')) * 60.0), TimeUnit.MINUTES, PowerSupply.of(matcher.group(1)));
                    }
                    bufferedReader.close();
                    return null;
                } catch (Exception ex) {
                    Logger.handleError(ex);
                    return null;
                }
            }
        });
        OSFUNCTION_SYSTEM_FUNCTIONS_MAC_OS = MAC_OS_HELPER.putOSFunction(SystemFunctions.class, new SystemFunctions() {
            @Override
            public boolean lockMonitor(long delay, boolean force) throws Exception {
                return Runtime.getRuntime().exec("pmset displaysleepnow").waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean logout(long delay, boolean force) throws Exception { //TODO Test this (especially the missing "sudo")
                return Runtime.getRuntime().exec("kill WindowServer").waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean shutdown(long delay, boolean force) throws Exception { //TODO Test this (especially the missing "sudo")
                return Runtime.getRuntime().exec("shutdown -h " + (delay <= 0 ? "now" : TimeUnit.MILLISECONDS.toMinutes(delay))).waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean reboot(long delay, boolean force) throws Exception { //TODO Test this (especially the missing "sudo")
                return Runtime.getRuntime().exec("shutdown -r " + (delay <= 0 ? "now" : TimeUnit.MILLISECONDS.toMinutes(delay))).waitFor(1, TimeUnit.SECONDS);
            }
            
            @Override
            public boolean lock(long delay, boolean force) {
                throw new NotImplementedException();
            }
            
            @Override
            public SystemInfo getSystemInfo() {
                return OSFUNCTION_SYSTEM_INFO_MAC_OS;
            }
        });
        // Current
        switch (OS) {
            case WINDOWS:
                OSFUNCTION_SYSTEM_INFO_CURRENT = CURRENT_OS_HELPER.putOSFunction(SystemInfo.class, OSFUNCTION_SYSTEM_INFO_WINDOWS);
                OSFUNCTION_SYSTEM_FUNCTIONS_CURRENT = CURRENT_OS_HELPER.putOSFunction(SystemFunctions.class, OSFUNCTION_SYSTEM_FUNCTIONS_WINDOWS);
                break;
            case MACOS:
                OSFUNCTION_SYSTEM_INFO_CURRENT = CURRENT_OS_HELPER.putOSFunction(SystemInfo.class, OSFUNCTION_SYSTEM_INFO_MAC_OS);
                OSFUNCTION_SYSTEM_FUNCTIONS_CURRENT = CURRENT_OS_HELPER.putOSFunction(SystemFunctions.class, OSFUNCTION_SYSTEM_FUNCTIONS_MAC_OS);
                break;
            case LINUX:
            case FREEBSD:
            case SUNOS:
            case UNKNOWN:
                OSFUNCTION_SYSTEM_INFO_CURRENT = CURRENT_OS_HELPER.putOSFunction(SystemInfo.class, OSFUNCTION_SYSTEM_INFO_LINUX);
                OSFUNCTION_SYSTEM_FUNCTIONS_CURRENT = CURRENT_OS_HELPER.putOSFunction(SystemFunctions.class, OSFUNCTION_SYSTEM_FUNCTIONS_LINUX);
                break;
            default:
                OSFUNCTION_SYSTEM_INFO_CURRENT = null;
                OSFUNCTION_SYSTEM_FUNCTIONS_CURRENT = null;
        }
    }
    
    public static final <T extends OSFunction> T getOSFunction(Class<T> clazz) {
        switch (OS) {
            case WINDOWS:
                return WINDOWS_HELPER.getOSFunction(clazz);
            case MACOS:
                return MAC_OS_HELPER.getOSFunction(clazz);
            case LINUX:
                return LINUX_HELPER.getOSFunction(clazz);
            case FREEBSD:
            case SUNOS:
            case UNKNOWN:
                return DEFAULT_HELPER.getOSFunction(clazz);
            default:
                return null;
        }
    }
    
    public static final <T extends OSFunction> T getCurrentOSFunction(Class<T> clazz) {
        return CURRENT_OS_HELPER.getOSFunction(clazz);
    }
    
}
