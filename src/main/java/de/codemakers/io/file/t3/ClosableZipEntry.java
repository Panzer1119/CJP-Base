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

import de.codemakers.base.exceptions.CJPRuntimeException;
import de.codemakers.base.logger.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ClosableZipEntry implements Closeable {
    
    private final ZipFile zipFile;
    private final ZipInputStream zipInputStream;
    private final ZipEntry zipEntry;
    private boolean closed = false;
    
    public ClosableZipEntry(ZipFile zipFile, ZipInputStream zipInputStream, ZipEntry zipEntry) {
        Objects.requireNonNull(zipEntry);
        this.zipFile = zipFile;
        this.zipInputStream = zipInputStream;
        this.zipEntry = zipEntry;
    }
    
    public ClosableZipEntry(ZipInputStream zipInputStream, ZipEntry zipEntry) {
        Objects.requireNonNull(zipEntry);
        this.zipFile = null;
        this.zipInputStream = zipInputStream;
        this.zipEntry = zipEntry;
    }
    
    public ClosableZipEntry(ZipFile zipFile, ZipEntry zipEntry) {
        Objects.requireNonNull(zipEntry);
        this.zipFile = zipFile;
        this.zipInputStream = null;
        this.zipEntry = zipEntry;
    }
    
    public final ZipFile getZipFile() {
        return zipFile;
    }
    
    public final ZipInputStream getZipInputStream() {
        return zipInputStream;
    }
    
    public final ZipEntry getZipEntry() {
        return zipEntry;
    }
    
    public final boolean isClosed() {
        return closed;
    }
    
    @Override
    public final void close() throws IOException {
        if (closed) {
            throw new CJPRuntimeException(getClass().getSimpleName() + " already closed");
        }
        if (zipFile != null) {
            zipFile.close();
        }
        if (zipInputStream != null) {
            if (zipEntry != null) {
                zipInputStream.closeEntry();
            }
            zipInputStream.close();
        }
        closed = true;
    }
    
    public final void close(Consumer<Throwable> failure) {
        try {
            close();
        } catch (Exception ex) {
            if (failure != null) {
                failure.accept(ex);
            } else {
                Logger.handleError(ex);
            }
        }
    }
    
    public final void closeWithoutException() {
        close(null);
    }
    
    public final <R> R close(Function<ZipEntry, R> function, Consumer<Throwable> failureClosing) throws Exception {
        R r = null;
        if (function != null) {
            r = function.apply(zipEntry);
        }
        close(failureClosing);
        return r;
    }
    
    public final <R> R close(Function<ZipEntry, R> function, Consumer<Throwable> failureFunction, Consumer<Throwable> failureClosing) {
        try {
            return close(function, failureClosing);
        } catch (Exception ex) {
            if (failureFunction != null) {
                failureFunction.accept(ex);
            } else {
                Logger.handleError(ex);
            }
            close(failureClosing);
            return null;
        }
    }
    
    public final <R> R closeWithoutException(Function<ZipEntry, R> function, Consumer<Throwable> failureFunction) {
        return close(function, failureFunction, null);
    }
    
    public final <R> R closeWithoutException(Function<ZipEntry, R> function) {
        return closeWithoutException(function, null);
    }
    
}
