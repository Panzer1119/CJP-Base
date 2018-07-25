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

package de.codemakers.base.os.function;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BatteryInfo {
    
    private final String id;
    private final String name;
    private final double charge;
    private final BatteryState state;
    private final long remaining_time;
    private final TimeUnit remaining_timeUnit;
    private Object[] extra = null;
    
    public BatteryInfo(String id, String name, double charge, BatteryState state, long remaining_time, TimeUnit remaining_timeUnit, Object... extra) {
        this.id = id;
        this.name = name;
        this.charge = charge;
        this.state = state;
        this.remaining_time = remaining_time;
        this.remaining_timeUnit = remaining_timeUnit;
        this.extra = extra;
    }
    
    public final String getId() {
        return id;
    }
    
    public final String getName() {
        return name;
    }
    
    /**
     * Returns Battery Level in percentage
     * @return Battery Level (0.0 = Empty, 1.0 = Full, -1.0 = No information available)
     */
    public final double getCharge() {
        return charge;
    }
    
    public final BatteryState getState() {
        return state;
    }
    
    public final long getRemainingTime() {
        return remaining_time;
    }
    
    public final TimeUnit getRemainingTimeUnit() {
        return remaining_timeUnit;
    }
    
    public final Object[] getExtra() {
        return extra;
    }
    
    @Override
    public String toString() {
        return "BatteryInfo{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", charge=" + charge + ", state=" + state + ", remaining_time=" + remaining_time + ", remaining_timeUnit=" + remaining_timeUnit + ", extra=" + Arrays.toString(extra) + '}';
    }
    
}
