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
import de.codemakers.io.file.t3.exceptions.is.*;
import de.codemakers.io.file.t3.exceptions.isnot.*;
import de.codemakers.security.interfaces.Cryptable;
import de.codemakers.security.interfaces.Signable;
import de.codemakers.security.interfaces.Verifiable;

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

public abstract class IFile<T extends IFile, P extends Predicate<T>> implements Cryptable, Serializable, Signable, Verifiable {
    
    public abstract String getName();
    
    public abstract String getPath();
    
    public abstract String getAbsolutePath();
    
    public abstract T getAbsoluteFile();
    
    public abstract T getParentFile();
    
    public abstract T getRoot();
    
    public boolean isRoot() {
        return equals(getRoot());
    }
    
    public abstract String getSeparator();
    
    public abstract char getSeparatorChar();
    
    public abstract boolean isFile();
    
    public abstract boolean isDirectory();
    
    public abstract boolean exists();
    
    public abstract boolean isAbsolute();
    
    public abstract boolean isRelative();
    
    public abstract boolean isIntern();
    
    public abstract boolean isExtern();
    
    public abstract Path toPath() throws Exception;
    
    public abstract URI toURI() throws Exception;
    
    public abstract URL toURL() throws Exception;
    
    public abstract File toFile();
    
    public abstract boolean mkdir() throws Exception;
    
    public boolean mkdir(Consumer<Throwable> failure) {
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
    
    public boolean mkdirWithoutException() {
        return mkdir(null);
    }
    
    public abstract boolean mkdirs() throws Exception;
    
    public boolean mkdirs(Consumer<Throwable> failure) {
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
    
    public boolean mkdirsWithoutException() {
        return mkdirs(null);
    }
    
    public abstract boolean delete() throws Exception;
    
    public boolean delete(Consumer<Throwable> failure) {
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
    
    public boolean deleteWithoutException() {
        return delete(null);
    }
    
    public abstract boolean createNewFile() throws Exception;
    
    public boolean createNewFile(Consumer<Throwable> failure) {
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
    
    public boolean createNewFileWithoutException() {
        return createNewFile(null);
    }
    
    public abstract InputStream createInputStream() throws Exception;
    
    public InputStream createInputStream(Consumer<Throwable> failure) {
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
    
    public InputStream createInputStreamWithoutException() {
        return createInputStream(null);
    }
    
    public abstract byte[] readBytes() throws Exception;
    
    public byte[] readBytes(Consumer<Throwable> failure) {
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
    
    public byte[] readBytesWithoutException() {
        return readBytes(null);
    }
    
    public OutputStream createOutputStream() throws Exception {
        return createOutputStream(false);
    }
    
    public abstract OutputStream createOutputStream(boolean append) throws Exception;
    
    public OutputStream createOutputStream(Consumer<Throwable> failure) {
        return createOutputStream(false, failure);
    }
    
    public OutputStream createOutputStream(boolean append, Consumer<Throwable> failure) {
        try {
            return createOutputStream(append);
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            return null;
        }
    }
    
    public OutputStream createOutputStreamWithoutException() {
        return createOutputStreamWithoutException(false);
    }
    
    public OutputStream createOutputStreamWithoutException(boolean append) {
        return createOutputStream(append, null);
    }
    
    public abstract boolean writeBytes(byte[] data) throws Exception;
    
    public boolean writeBytes(byte[] data, Consumer<Throwable> failure) {
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
    
    public boolean writeBytesWithoutException(byte[] data) {
        return writeBytes(data, null);
    }
    
    public <R> R use(Function<T, R> function) throws Exception {
        if (function == null) {
            return null;
        }
        return function.apply((T) this);
    }
    
    public <R> R use(Function<T, R> function, Consumer<Throwable> failure) {
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
    
    public <R> R useWithoutException(Function<T, R> function) {
        return use(function, null);
    }
    
    // listFiles BEGIN =================================================================================================
    
    public List<T> listFiles() {
        return listFiles(false);
    }
    
    public abstract List<T> listFiles(boolean recursive);
    
    public List<T> listFiles(Consumer<Throwable> failure) {
        return listFiles(false, failure);
    }
    
    public List<T> listFiles(boolean recursive, Consumer<Throwable> failure) {
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
    
    public List<T> listFilesWithoutException() {
        return listFilesWithoutException(false);
    }
    
    public List<T> listFilesWithoutException(boolean recursive) {
        return listFiles(recursive, (Consumer<Throwable>) null);
    }
    
    // listFiles MID ============================================================
    
    public List<T> listFiles(P fileFilter) {
        return listFiles(false, fileFilter);
    }
    
    public abstract List<T> listFiles(boolean recursive, P fileFilter);
    
    public List<T> listFiles(P fileFilter, Consumer<Throwable> failure) {
        return listFiles(false, fileFilter, failure);
    }
    
    public List<T> listFiles(boolean recursive, P fileFilter, Consumer<Throwable> failure) {
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
    
    public List<T> listFilesWithoutException(P fileFilter) {
        return listFilesWithoutException(false, fileFilter);
    }
    
    public List<T> listFilesWithoutException(boolean recursive, P fileFilter) {
        return listFiles(recursive, fileFilter, null);
    }
    
    // listFiles END ===================================================================================================
    
    public boolean forChildren(Consumer<T> consumer) throws Exception {
        return forChildren(consumer, false);
    }
    
    public boolean forChildren(Consumer<T> consumer, boolean recursive) throws Exception {
        if (consumer == null) {
            return false;
        }
        listFiles(recursive).forEach(consumer);
        return true;
    }
    
    public boolean forChildren(Consumer<T> consumer, Consumer<Throwable> failure) {
        return forChildren(consumer, false, failure);
    }
    
    public boolean forChildren(Consumer<T> consumer, boolean recursive, Consumer<Throwable> failure) {
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
    
    public boolean forChildrenWithoutException(Consumer<T> consumer) {
        return forChildren(consumer, null);
    }
    
    public boolean forChildrenWithoutException(Consumer<T> consumer, boolean recursive) {
        return forChildren(consumer, recursive, null);
    }
    
    public boolean forChildrenParallel(Consumer<T> consumer) throws Exception {
        return forChildrenParallel(consumer, false);
    }
    
    public boolean forChildrenParallel(Consumer<T> consumer, boolean recursive) throws Exception {
        if (consumer == null) {
            return false;
        }
        listFiles(recursive).stream().parallel().forEach(consumer);
        return true;
    }
    
    public boolean forChildrenParallel(Consumer<T> consumer, Consumer<Throwable> failure) {
        return forChildrenParallel(consumer, false, failure);
    }
    
    public boolean forChildrenParallel(Consumer<T> consumer, boolean recursive, Consumer<Throwable> failure) {
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
    
    public boolean forChildrenParallelWithoutException(Consumer<T> consumer) {
        return forChildrenParallelWithoutException(consumer, false);
    }
    
    public boolean forChildrenParallelWithoutException(Consumer<T> consumer, boolean recursive) {
        return forChildrenParallel(consumer, recursive, null);
    }
    
    protected boolean checkAndErrorIfFile(boolean throwException) {
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
    
    protected boolean checkAndErrorIfNotFile(boolean throwException) {
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
    
    protected boolean checkAndErrorIfDirectory(boolean throwException) {
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
    
    protected boolean checkAndErrorIfNotDirectory(boolean throwException) {
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
    
    protected boolean checkAndErrorIfExisting(boolean throwException) {
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
    
    protected boolean checkAndErrorIfNotExisting(boolean throwException) {
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
    
    protected boolean checkAndErrorIfAbsolute(boolean throwException) {
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
    
    protected boolean checkAndErrorIfNotAbsolute(boolean throwException) {
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
    
    protected boolean checkAndErrorIfRelative(boolean throwException) {
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
    
    protected boolean checkAndErrorIfNotRelative(boolean throwException) {
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
    
    protected boolean checkAndErrorIfIntern(boolean throwException) {
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
    
    protected boolean checkAndErrorIfNotIntern(boolean throwException) {
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
    
    protected boolean checkAndErrorIfExtern(boolean throwException) {
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
    
    protected boolean checkAndErrorIfNotExtern(boolean throwException) {
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
