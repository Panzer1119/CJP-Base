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

package de.codemakers.io.sql;

import de.codemakers.base.action.ClosingAction;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

import java.sql.Connection;

public abstract class DatabaseManager {
    
    protected final String host;
    protected final String database;
    protected final String username;
    protected final String password;
    
    public DatabaseManager(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }
    
    public String getHost() {
        return host;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public String getUsername() {
        return username;
    }
    
    protected String getPassword() {
        return password;
    }
    
    public abstract Connection createConnection() throws Exception;
    
    public Connection createConnection(ToughConsumer<Throwable> failure) {
        try {
            return createConnection();
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public Connection createConnectionWithoutException() {
        return createConnection(null);
    }
    
    public ClosingAction<Connection> createAutoClosingConnection() {
        return new ClosingAction<>(this::createConnection);
    }
    
}
