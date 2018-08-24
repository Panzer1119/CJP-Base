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

package de.codemakers.io.file.t3;

import de.codemakers.base.logger.Logger;
import de.codemakers.io.file.t3.exceptions.FileRuntimeException;
import de.codemakers.io.file.t3.exceptions.is.*;
import de.codemakers.io.file.t3.exceptions.isnot.*;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IFile<T extends IFile, P extends Predicate<T>> extends Serializable {
    
    String getName();
    
    String getPath();
    
    String getAbsolutePath();
    
    T getAbsoluteFile();
    
    T getParentFile();
    
    String getSeparator();
    
    char getSeparatorChar();
    
    boolean isFile();
    
    boolean isDirectory();
    
    boolean exists();
    
    boolean isAbsolute();
    
    boolean isRelative();
    
    boolean isIntern();
    
    boolean isExtern();
    
    Path toPath() throws Exception;
    
    URI toURI() throws Exception;
    
    URL toURL() throws Exception;
    
    File toFile() throws FileRuntimeException;
    
    boolean mkdir() throws Exception;
    
    default boolean mkdir(Consumer<Throwable> failure) {
        try {
            return mkdir();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean mkdirWithoutException() {
        return mkdir(null);
    }
    
    boolean mkdirs() throws Exception;
    
    default boolean mkdirs(Consumer<Throwable> failure) {
        try {
            return mkdirs();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean mkdirsWithoutException() {
        return mkdirs(null);
    }
    
    boolean delete() throws Exception;
    
    default boolean delete(Consumer<Throwable> failure) {
        try {
            return delete();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean deleteWithoutException() {
        return delete(null);
    }
    
    boolean createNewFile() throws Exception;
    
    default boolean createNewFile(Consumer<Throwable> failure) {
        try {
            return createNewFile();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean createNewFileWithoutException() {
        return createNewFile(null);
    }
    
    InputStream createInputStream() throws Exception;
    
    default InputStream createInputStream(Consumer<Throwable> failure) {
        try {
            return createInputStream();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default InputStream createInputStreamWithoutException() {
        return createInputStream(null);
    }
    
    byte[] readBytes() throws Exception;
    
    default byte[] readBytes(Consumer<Throwable> failure) {
        try {
            return readBytes();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default byte[] readBytesWithoutException() {
        return readBytes(null);
    }
    
    OutputStream createOutputStream() throws Exception;
    
    default OutputStream createOutputStream(Consumer<Throwable> failure) {
        try {
            return createOutputStream();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default OutputStream createOutputStreamWithoutException() {
        return createOutputStream(null);
    }
    
    boolean writeBytes(byte[] data) throws Exception;
    
    default boolean writeBytes(byte[] data, Consumer<Throwable> failure) {
        try {
            return writeBytes(data);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean writeBytesWithoutException(byte[] data) {
        return writeBytes(data, null);
    }
    
    default <R> R use(Function<T, R> function) throws Exception {
        if (function == null) {
            return null;
        }
        return function.apply((T) this);
    }
    
    default <R> R use(Function<T, R> function, Consumer<Throwable> failure) {
        try {
            return use(function);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default <R> R useWithoutException(Function<T, R> function) {
        return use(function, null);
    }
    
    // listFiles BEGIN =================================================================================================
    
    default List<T> listFiles() throws FileRuntimeException {
        return listFiles(false);
    }
    
    List<T> listFiles(boolean recursive) throws FileRuntimeException;
    
    default List<T> listFiles(Consumer<Throwable> failure) {
        return listFiles(false, failure);
    }
    
    default List<T> listFiles(boolean recursive, Consumer<Throwable> failure) {
        try {
            return listFiles(recursive);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default List<T> listFilesWithoutException() {
        return listFilesWithoutException(false);
    }
    
    default List<T> listFilesWithoutException(boolean recursive) {
        return listFiles(recursive, (Consumer<Throwable>) null);
    }
    
    // listFiles MID ============================================================
    
    default List<T> listFiles(P fileFilter) throws FileRuntimeException {
        return listFiles(false, fileFilter);
    }
    
    List<T> listFiles(boolean recursive, P fileFilter) throws FileRuntimeException;
    
    default List<T> listFiles(P fileFilter, Consumer<Throwable> failure) {
        return listFiles(false, fileFilter, failure);
    }
    
    default List<T> listFiles(boolean recursive, P fileFilter, Consumer<Throwable> failure) {
        try {
            return listFiles(recursive, fileFilter);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default List<T> listFilesWithoutException(P fileFilter) {
        return listFilesWithoutException(false, fileFilter);
    }
    
    default List<T> listFilesWithoutException(boolean recursive, P fileFilter) {
        return listFiles(recursive, fileFilter, null);
    }
    
    // listFiles END ===================================================================================================
    
    default boolean forChildren(Consumer<T> consumer) throws Exception {
        return forChildren(consumer, false);
    }
    
    default boolean forChildren(Consumer<T> consumer, boolean recursive) throws Exception {
        if (consumer == null) {
            return false;
        }
        listFiles(recursive).forEach(consumer);
        return true;
    }
    
    default boolean forChildren(Consumer<T> consumer, Consumer<Throwable> failure) {
        return forChildren(consumer, false, failure);
    }
    
    default boolean forChildren(Consumer<T> consumer, boolean recursive, Consumer<Throwable> failure) {
        try {
            return forChildren(consumer, recursive);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean forChildrenWithoutException(Consumer<T> consumer) {
        return forChildren(consumer, null);
    }
    
    default boolean forChildrenWithoutException(Consumer<T> consumer, boolean recursive) {
        return forChildren(consumer, recursive, null);
    }
    
    default boolean forChildrenParallel(Consumer<T> consumer) throws Exception {
        return forChildrenParallel(consumer, false);
    }
    
    default boolean forChildrenParallel(Consumer<T> consumer, boolean recursive) throws Exception {
        if (consumer == null) {
            return false;
        }
        listFiles(recursive).stream().parallel().forEach(consumer);
        return true;
    }
    
    default boolean forChildrenParallel(Consumer<T> consumer, Consumer<Throwable> failure) {
        return forChildrenParallel(consumer, false, failure);
    }
    
    default boolean forChildrenParallel(Consumer<T> consumer, boolean recursive, Consumer<Throwable> failure) {
        try {
            return forChildrenParallel(consumer, recursive);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean forChildrenParallelWithoutException(Consumer<T> consumer) {
        return forChildrenParallelWithoutException(consumer, false);
    }
    
    default boolean forChildrenParallelWithoutException(Consumer<T> consumer, boolean recursive) {
        return forChildrenParallel(consumer, recursive, null);
    }
    
    default boolean checkAndErrorIfFile(boolean throwException) {
        if (isFile()) {
            if (throwException) {
                throw new FileIsFileRuntimeException(getPath() + " is a file");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfNotFile(boolean throwException) {
        if (!isFile()) {
            if (throwException) {
                throw new FileIsNotFileRuntimeException(getPath() + " is not a file");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfDirectory(boolean throwException) {
        if (isDirectory()) {
            if (throwException) {
                throw new FileIsDirectoryRuntimeException(getPath() + " is a directory");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfNotDirectory(boolean throwException) {
        if (!isDirectory()) {
            if (throwException) {
                throw new FileIsNotDirectoryRuntimeException(getPath() + " is not a directory");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfExisting(boolean throwException) {
        if (exists()) {
            if (throwException) {
                throw new FileIsExistingRuntimeException(getPath() + " does exist");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfNotExisting(boolean throwException) {
        if (!exists()) {
            if (throwException) {
                throw new FileIsNotExistingRuntimeException(getPath() + " does not exist");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfAbsolute(boolean throwException) {
        if (isAbsolute()) {
            if (throwException) {
                throw new FileIsAbsoluteRuntimeException(getPath() + " is absolute");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfNotAbsolute(boolean throwException) {
        if (!isAbsolute()) {
            if (throwException) {
                throw new FileIsNotAbsoluteRuntimeException(getPath() + " is not absolute");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfRelative(boolean throwException) {
        if (isRelative()) {
            if (throwException) {
                throw new FileIsRelativeRuntimeException(getPath() + " is relative");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfNotRelative(boolean throwException) {
        if (!isRelative()) {
            if (throwException) {
                throw new FileIsNotRelativeRuntimeException(getPath() + " is not relative");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfIntern(boolean throwException) {
        if (isIntern()) {
            if (throwException) {
                throw new FileIsInternRuntimeException(getPath() + " is intern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfNotIntern(boolean throwException) {
        if (!isIntern()) {
            if (throwException) {
                throw new FileIsNotInternRuntimeException(getPath() + " is not intern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfExtern(boolean throwException) {
        if (isExtern()) {
            if (throwException) {
                throw new FileIsExternRuntimeException(getPath() + " is extern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    default boolean checkAndErrorIfNotExtern(boolean throwException) {
        if (!isExtern()) {
            if (throwException) {
                throw new FileIsNotExternRuntimeException(getPath() + " is not extern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
}
