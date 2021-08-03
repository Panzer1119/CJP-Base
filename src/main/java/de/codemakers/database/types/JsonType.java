/*
 *    Copyright 2021 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.database.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class JsonType<T extends Serializable> implements UserType<T> {
    
    private final Class<T> type;
    
    public JsonType(Class<T> type) {
        this.type = type;
    }
    
    public Class<T> getType() {
        return type;
    }
    
    @Override
    public int[] sqlTypes() {
        return new int[] {Types.JAVA_OBJECT};
    }
    
    @Override
    public Class<T> returnedClass() {
        return getType();
    }
    
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }
    
    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }
    
    @Override
    public T nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        final String cellContent = resultSet.getString(position);
        if (cellContent == null) {
            return null;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cellContent.getBytes(StandardCharsets.UTF_8), returnedClass());
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to convert String to %s: %s", returnedClass().getName(), e.getMessage()), e);
        }
    }
    
    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, T value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            preparedStatement.setNull(index, Types.OTHER);
            return;
        }
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, value);
            stringWriter.flush();
            preparedStatement.setObject(index, stringWriter.toString(), Types.OTHER);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to convert %s to String: %s", returnedClass().getName(), e.getMessage()), e);
        }
    }
    
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            objectOutputStream.close();
            byteArrayOutputStream.close();
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return new ObjectInputStream(byteArrayInputStream).readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new HibernateException(e);
        }
    }
    
    @Override
    public boolean isMutable() {
        return true;
    }
    
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }
    
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }
    
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
