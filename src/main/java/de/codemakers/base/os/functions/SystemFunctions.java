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

package de.codemakers.base.os.functions;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

public abstract class SystemFunctions extends OSFunction {
    
    public abstract boolean lockMonitor(long delay, boolean force) throws Exception;
    
    public boolean lockMonitor(long delay, boolean force, ToughConsumer<Throwable> failure) {
        try {
            return lockMonitor(delay, force);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    public boolean lockMonitorWithoutException(long delay, boolean force) {
        return lockMonitor(delay, force, null);
    }
    
    public abstract boolean logout(long delay, boolean force) throws Exception;
    
    public boolean logout(long delay, boolean force, ToughConsumer<Throwable> failure) {
        try {
            return logout(delay, force);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    public boolean logoutWithoutException(long delay, boolean force) {
        return logout(delay, force, null);
    }
    
    public abstract boolean shutdown(long delay, boolean force) throws Exception;
    
    public boolean shutdown(long delay, boolean force, ToughConsumer<Throwable> failure) {
        try {
            return shutdown(delay, force);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    public boolean shutdownWithoutException(long delay, boolean force) {
        return shutdown(delay, force, null);
    }
    
    public abstract boolean reboot(long delay, boolean force) throws Exception;
    
    public boolean reboot(long delay, boolean force, ToughConsumer<Throwable> failure) {
        try {
            return reboot(delay, force);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    public boolean rebootWithoutException(long delay, boolean force) {
        return reboot(delay, force, null);
    }
    
    public abstract boolean lock(long delay, boolean force) throws Exception;
    
    public boolean lock(long delay, boolean force, ToughConsumer<Throwable> failure) {
        try {
            return lock(delay, force);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    public boolean lockWithoutException(long delay, boolean force) {
        return lock(delay, force, null);
    }
    
    public abstract SystemInfo getSystemInfo();
    
}
