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

public class WindowsHelper implements OSHelper {
    
    public static final Pattern DRIVER_PATTERN = Pattern.compile("([A-Z]):\\\\(.*)");
    AtomicLong LAST_ID = new AtomicLong(-1);
    Map<Long, OSFunction> OS_FUNCTIONS = new ConcurrentHashMap<>();
    
    public static final String DESIGN_CAPACITY = "DesignCapacity";
    public static final String DESCRIPTION = "Description";
    public static final String SYSTEM_CREATION_CLASS_NAME = "SystemCreationClassName";
    public static final String CONFIG_MANAGER_USER_CONFIG = "ConfigManagerUserConfig";
    public static final String ERROR_CLEARED = "ErrorCleared";
    public static final String BATTERY_RECHARGE_TIME = "BatteryRechargeTime";
    public static final String ESTIMATED_CHARGE_REMAINING = "EstimatedChargeRemaining";
    public static final String NAME = "Name";
    public static final String SMART_BATTERY_VERSION = "SmartBatteryVersion";
    public static final String TIME_ON_BATTERY = "TimeOnBattery";
    public static final String P_N_P_DEVICE_I_D = "PNPDeviceID";
    public static final String DESIGN_VOLTAGE = "DesignVoltage";
    public static final String POWER_MANAGEMENT_SUPPORTED = "PowerManagementSupported";
    public static final String SYSTEM_NAME = "SystemName";
    public static final String AVAILABILITY = "Availability";
    public static final String ERROR_DESCRIPTION = "ErrorDescription";
    public static final String STATUS = "Status";
    public static final String MAX_RECHARGE_TIME = "MaxRechargeTime";
    public static final String DEVICE_I_D = "DeviceID";
    public static final String EXPECTED_LIFE = "ExpectedLife";
    public static final String EXPECTED_BATTERY_LIFE = "ExpectedBatteryLife";
    public static final String INSTALL_DATE = "InstallDate";
    public static final String TIME_TO_FULL_CHARGE = "TimeToFullCharge";
    public static final String CAPTION = "Caption";
    public static final String CHEMISTRY = "Chemistry";
    public static final String CONFIG_MANAGER_ERROR_CODE = "ConfigManagerErrorCode";
    public static final String POWER_MANAGEMENT_CAPABILITIES = "PowerManagementCapabilities";
    public static final String BATTERY_STATUS = "BatteryStatus";
    public static final String STATUS_INFO = "StatusInfo";
    public static final String CREATION_CLASS_NAME = "CreationClassName";
    public static final String FULL_CHARGE_CAPACITY = "FullChargeCapacity";
    public static final String LAST_ERROR_CODE = "LastErrorCode";
    public static final String ESTIMATED_RUN_TIME = "EstimatedRunTime";
    
    @Override
    public boolean isPathAbsolute(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        return DRIVER_PATTERN.matcher(path).matches() || path.startsWith(getFileSeparator() + getFileSeparator());
    }
    
    @Override
    public String getFileSeparator() {
        return "\\";
    }
    
    @Override
    public String getPathSeparator() {
        return ";";
    }
    
    @Override
    public String getLineSeparator() {
        return "\r\n";
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
