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

package de.codemakers.io;

import de.codemakers.base.action.ReturningAction;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

import java.io.*;

public class SerializationUtil {
    
    public static byte[] objectToBytes(Serializable serializable) throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(serializable);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] objectToBytes(Serializable serializable, ToughConsumer<Throwable> failure) {
        try {
            return objectToBytes(serializable);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public static byte[] objectToBytesWithoutException(Serializable serializable) {
        return objectToBytes(serializable, null);
    }
    
    public static ReturningAction<byte[]> objectToBytesAction(Serializable serializable) {
        return new ReturningAction<>(() -> objectToBytes(serializable));
    }
    
    public static Serializable bytesToObject(byte[] data) throws Exception {
        final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        final Serializable serializable = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        return serializable;
    }
    
    public static Serializable bytesToObject(byte[] data, ToughConsumer<Throwable> failure) {
        try {
            return bytesToObject(data);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public static Serializable bytesToObjectWithoutException(byte[] data) {
        return bytesToObject(data, (ToughConsumer<Throwable>) null);
    }
    
    public static ReturningAction<Serializable> bytesToObjectAction(byte[] data) {
        return new ReturningAction<>(() -> bytesToObject(data));
    }
    
    public static <T> T bytesToObject(byte[] data, Class<T> clazz) throws Exception {
        return (T) bytesToObject(data);
    }
    
    public static <T> T bytesToObject(byte[] data, Class<T> clazz, ToughConsumer<Throwable> failure) {
        try {
            return bytesToObject(data, clazz);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public static <T> T bytesToObjectWithoutException(byte[] data, Class<T> clazz) {
        return bytesToObject(data, clazz, null);
    }
    
    public static <T> ReturningAction<T> bytesToObjectAction(byte[] data, Class<T> clazz) {
        return new ReturningAction<>(() -> bytesToObject(data, clazz));
    }
    
}
