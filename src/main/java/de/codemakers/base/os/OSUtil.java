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

package de.codemakers.base.os;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.os.functions.*;
import de.codemakers.base.reflection.ReflectionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class OSUtil {
    
    public static final WindowsHelper WINDOWS_HELPER = new WindowsHelper();
    public static final LinuxHelper LINUX_HELPER = new LinuxHelper();
    public static final MacOSHelper MAC_OS_HELPER = new MacOSHelper();
    public static final OSHelper DEFAULT_HELPER = LINUX_HELPER;
    public static final CurrentOSHelper CURRENT_OS_HELPER = new CurrentOSHelper();
    
    public static final String[] STANDARD_OS_NAMES = new String[] {"Windows", "Linux", "Mac OS", "SunOS", "FreeBSD"};
    
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final OS CURRENT_OS = OS.byName(OS_NAME);
    
    public static final long OSFUNCTION_ID_SYSTEM_INFO_WINDOWS;
    public static final long OSFUNCTION_ID_SYSTEM_INFO_LINUX;
    public static final long OSFUNCTION_ID_SYSTEM_INFO_MAC_OS;
    public static final long OSFUNCTION_ID_SYSTEM_INFO_CURRENT;
    
    static {
        OSFUNCTION_ID_SYSTEM_INFO_WINDOWS = WINDOWS_HELPER.addOSFunction(new SystemInfo() {
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
        OSFUNCTION_ID_SYSTEM_INFO_LINUX = LINUX_HELPER.addOSFunction(new SystemInfo() {
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
        OSFUNCTION_ID_SYSTEM_INFO_MAC_OS = MAC_OS_HELPER.addOSFunction(new SystemInfo() {
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
        switch (CURRENT_OS) {
            case WINDOWS:
                OSFUNCTION_ID_SYSTEM_INFO_CURRENT = CURRENT_OS_HELPER.addOSFunction(WINDOWS_HELPER.getOSFunction(OSFUNCTION_ID_SYSTEM_INFO_WINDOWS));
                break;
            case MACOS:
                OSFUNCTION_ID_SYSTEM_INFO_CURRENT = CURRENT_OS_HELPER.addOSFunction(LINUX_HELPER.getOSFunction(OSFUNCTION_ID_SYSTEM_INFO_LINUX));
                break;
            case LINUX:
                OSFUNCTION_ID_SYSTEM_INFO_CURRENT = CURRENT_OS_HELPER.addOSFunction(MAC_OS_HELPER.getOSFunction(OSFUNCTION_ID_SYSTEM_INFO_MAC_OS));
                break;
            case FREEBSD:
            case SUNOS:
            case UNKNOWN:
                OSFUNCTION_ID_SYSTEM_INFO_CURRENT = CURRENT_OS_HELPER.addOSFunction(DEFAULT_HELPER.getOSFunction(OSFUNCTION_ID_SYSTEM_INFO_LINUX));
                break;
            default:
                OSFUNCTION_ID_SYSTEM_INFO_CURRENT = -1;
        }
        init();
    }
    
    private static final void init() {
        final Set<Class<?>> classes = ReflectionUtil.getTypesAnnotatedWith(RegisterOSFunction.class);
        for (Class<?> clazz : classes) {
            registerClassAsOSFunction(clazz);
        }
    }
    
    private static final boolean registerClassAsOSFunction(Class<?> clazz) {
        try {
            final RegisterOSFunction registerOSFunction = clazz.getAnnotation(RegisterOSFunction.class);
            Logger.logDebug("registerOSFunction=" + registerOSFunction);
            final OSFunction osFunction = (OSFunction) clazz.newInstance();
            Logger.logDebug("osFunction=" + osFunction);
            for (OS os : registerOSFunction.supported()) {
                Logger.logDebug("os=" + os);
                Logger.logDebug("os.getHelper()=" + os.getHelper());
                os.getHelper().addOSFunction(osFunction);
            }
            return true;
        } catch (Exception ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    public static final OS getCurrentOs() {
        return CURRENT_OS;
    }
    
    public static final <T extends OSFunction> T getFunction(Class<T> clazz) {
        switch (CURRENT_OS) {
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
    
    public static final <T extends OSFunction> T getCurrentFunction(Class<T> clazz) {
        return CURRENT_OS_HELPER.getOSFunction(clazz);
    }
    
}
