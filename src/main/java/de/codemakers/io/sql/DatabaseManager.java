/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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
import de.codemakers.base.util.tough.ToughFunction;

import java.sql.Connection;
import java.util.Objects;

public abstract class DatabaseManager {
    
    protected final String host;
    protected final int port;
    protected final String database;
    protected final String username;
    protected final String password;
    
    public DatabaseManager(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }
    
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
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
    
    public <R> R useConnectionAndClose(ToughFunction<Connection, R> toughFunction) throws Exception {
        try (final Connection connection = createConnection()) {
            return toughFunction.apply(connection);
        }
    }
    
    public <R> R useConnectionAndClose(ToughFunction<Connection, R> toughFunction, ToughConsumer<Throwable> failure) {
        try {
            return useConnectionAndClose(toughFunction);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public <R> R useConnectionAndCloseWithoutException(ToughFunction<Connection, R> toughFunction) {
        return useConnectionAndClose(toughFunction, null);
    }
    
    public void useConnectionAndClose(ToughConsumer<Connection> toughConsumer) throws Exception {
        try (final Connection connection = createConnection()) {
            toughConsumer.accept(connection);
        }
    }
    
    public void useConnectionAndClose(ToughConsumer<Connection> toughConsumer, ToughConsumer<Throwable> failure) {
        try {
            useConnectionAndClose(toughConsumer);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
        }
    }
    
    public void useConnectionAndCloseWithoutException(ToughConsumer<Connection> toughConsumer) {
        useConnectionAndClose(toughConsumer, null);
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final DatabaseManager that = (DatabaseManager) other;
        return port == that.port && Objects.equals(host, that.host) && Objects.equals(database, that.database) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(host, port, database, username, password);
    }
    
    @Override
    public String toString() {
        return "DatabaseManager{" + "host='" + host + '\'' + ", port=" + port + ", database='" + database + '\'' + ", username='" + username + '\'' + ", password.length()='" + password.length() + '\'' + '}';
    }
    
}
