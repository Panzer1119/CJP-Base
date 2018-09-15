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

package de.codemakers.base.util;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

import java.lang.reflect.Field;
import java.util.Objects;

public class ReflectionUtil {
    
    public static Object getValue(String fieldName, Object object) throws IllegalAccessException, NoSuchFieldException {
        return getValue(null, fieldName, object);
    }
    
    public static Object getValue(String fieldName, Object object, ToughConsumer<Throwable> failure) {
        try {
            return getValue(fieldName, object);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public static Object getValueWithoutExeption(String fieldName, Object object) {
        return getValue(fieldName, object, null);
    }
    
    public static <T> T getValue(Class<T> clazz, String fieldName, Object object) throws IllegalAccessException, NoSuchFieldException {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(object);
        final Field field = object.getClass().getField(fieldName);
        if (field == null) {
            return null;
        }
        final boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        final Object value = field.get(object);
        if (!accessible) {
            field.setAccessible(false);
        }
        return (T) value;
    }
    
    public static <T> T getValue(Class<T> clazz, String fieldName, Object object, ToughConsumer<Throwable> failure) {
        try {
            return getValue(clazz, fieldName, object);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public static <T> T getValueWithoutExeption(Class<T> clazz, String fieldName, Object object) {
        return getValue(clazz, fieldName, object, null);
    }
    
    public static Object getStaticValue(String fieldName, Class<?> clazz) throws IllegalAccessException, NoSuchFieldException {
        return getStaticValue(null, fieldName, clazz);
    }
    
    public static Object getStaticValue(String fieldName, Class<?> clazz_, ToughConsumer<Throwable> failure) {
        try {
            return getStaticValue(fieldName, clazz_);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public static Object getStaticValueWithoutExeption(String fieldName, Class<?> clazz_) {
        return getStaticValue(fieldName, clazz_, null);
    }
    
    public static <T> T getStaticValue(Class<T> clazz, String fieldName, Class<?> clazz_) throws IllegalAccessException, NoSuchFieldException {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(clazz_);
        final Field field = clazz_.getField(fieldName);
        if (field == null) {
            return null;
        }
        final boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        final Object value = field.get(null);
        if (!accessible) {
            field.setAccessible(false);
        }
        return (T) value;
    }
    
    public static <T> T getStaticValue(Class<T> clazz, String fieldName, Class<?> clazz_, ToughConsumer<Throwable> failure) {
        try {
            return getStaticValue(clazz, fieldName, clazz_);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public static <T> T getStaticValueWithoutExeption(Class<T> clazz, String fieldName, Class<?> clazz_) {
        return getStaticValue(clazz, fieldName, clazz_, null);
    }
    
}
