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

package de.codemakers.base.os.functions.impl;

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.os.OS;
import de.codemakers.base.os.WindowsHelper;
import de.codemakers.base.os.functions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@RegisterOSFunction(supported = {OS.WINDOWS})
public class WindowsSystemInfo extends SystemInfo {
    
    public static final String COMMAND_BATTERY_INFO = "WMIC path Win32_Battery";
    
    @Override
    public PowerInfo getPowerInfo() throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINEST);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(COMMAND_BATTERY_INFO).getInputStream()));
        final String line_1 = bufferedReader.readLine();
        bufferedReader.readLine();
        final String line_2 = bufferedReader.readLine();
        bufferedReader.close();
        if (line_1.isEmpty() || line_2.isEmpty()) {
            return null;
        }
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
        return new PowerInfo(properties.getProperty(WindowsHelper.DEVICE_ID, WindowsHelper.DEVICE_ID), properties.getProperty(WindowsHelper.NAME, WindowsHelper.NAME), Double.parseDouble(properties.getProperty(WindowsHelper.ESTIMATED_CHARGE_REMAINING, "0.0")) * 0.01, batteryState, Integer.parseInt(properties.getProperty(WindowsHelper.ESTIMATED_RUN_TIME, "0")), TimeUnit.MINUTES, properties.getProperty(WindowsHelper.TIME_TO_FULL_CHARGE, "").isEmpty() ? -1 : Integer.parseInt(properties.getProperty(WindowsHelper.TIME_TO_FULL_CHARGE, "0")), TimeUnit.MINUTES, PowerSupply.BATTERY, properties);
    }
    
}
