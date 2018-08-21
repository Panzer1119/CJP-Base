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
import de.codemakers.io.file.t3.exceptions.FileException;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IFile extends Serializable {
    
    String getName();
    
    String getPath();
    
    String getAbsolutePath();
    
    IFile getAbsoluteFile();
    
    IFile getParentFile();
    
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
    
    File toFile() throws FileException;
    
    boolean mkdir() throws FileException;
    
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
    
    boolean mkdirs() throws FileException;
    
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
    
    boolean delete() throws FileException;
    
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
    
    boolean createNewFile() throws FileException;
    
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
    
    byte[] readBytes() throws FileException;
    
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
    
    boolean writeBytes(byte[] data) throws FileException;
    
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
    
    default <R> R use(Function<IFile, R> function) throws Exception {
        if (function == null) {
            return null;
        }
        return function.apply(this);
    }
    
    default <R> R use(Function<IFile, R> function, Consumer<Throwable> failure) {
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
    
    default <R> R useWithoutException(Function<IFile, R> function) {
        return use(function, null);
    }
    
    default List<IFile> listFiles() throws FileException {
        return listFiles(false);
    }
    
    List<IFile> listFiles(boolean recursive) throws FileException;
    
    default List<IFile> listFiles(Consumer<Throwable> failure) {
        return listFiles(false, failure);
    }
    
    default List<IFile> listFiles(boolean recursive, Consumer<Throwable> failure) {
        try {
            return listFiles();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    default List<IFile> listFilesWithoutException() {
        return listFilesWithoutException(false);
    }
    
    default List<IFile> listFilesWithoutException(boolean recursive) {
        return listFiles(recursive, null);
    }
    
    default boolean forChildren(Consumer<IFile> consumer) throws Exception {
        return forChildren(consumer, false);
    }
    
    default boolean forChildren(Consumer<IFile> consumer, boolean recursive) throws Exception {
        if (consumer == null) {
            return false;
        }
        listFiles(recursive).forEach(consumer);
        return true;
    }
    
    default boolean forChildren(Consumer<IFile> consumer, Consumer<Throwable> failure) {
        return forChildren(consumer, false, failure);
    }
    
    default boolean forChildren(Consumer<IFile> consumer, boolean recursive, Consumer<Throwable> failure) {
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
    
    default boolean forChildrenWithoutException(Consumer<IFile> consumer) {
        return forChildren(consumer, null);
    }
    
    default boolean forChildrenWithoutException(Consumer<IFile> consumer, boolean recursive) {
        return forChildren(consumer, recursive, null);
    }
    
    default boolean forChildrenParallel(Consumer<IFile> consumer) throws Exception {
        return forChildrenParallel(consumer, false);
    }
    
    default boolean forChildrenParallel(Consumer<IFile> consumer, boolean recursive) throws Exception {
        if (consumer == null) {
            return false;
        }
        listFiles(recursive).stream().parallel().forEach(consumer);
        return true;
    }
    
    default boolean forChildrenParallel(Consumer<IFile> consumer, Consumer<Throwable> failure) {
        return forChildrenParallel(consumer, false, failure);
    }
    
    default boolean forChildrenParallel(Consumer<IFile> consumer, boolean recursive, Consumer<Throwable> failure) {
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
    
    default boolean forChildrenParallelWithoutException(Consumer<IFile> consumer) {
        return forChildrenParallelWithoutException(consumer, false);
    }
    
    default boolean forChildrenParallelWithoutException(Consumer<IFile> consumer, boolean recursive) {
        return forChildrenParallel(consumer, recursive, null);
    }
    
}
