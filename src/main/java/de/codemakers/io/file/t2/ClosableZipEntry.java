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

package de.codemakers.io.file.t2;

import de.codemakers.base.logger.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
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
            throw new RuntimeException(getClass().getSimpleName() + " already closed");
        }
        if (zipFile != null) {
            zipFile.close();
        }
        if (zipInputStream != null) {
            zipInputStream.closeEntry();
            zipInputStream.close();
        }
        closed = true;
    }
    
    public final boolean closeWithOutException() {
        try {
            close();
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        return closed;
    }
    
    public final <R> R close(Function<ZipEntry, R> function) {
        R r = null;
        if (function != null) {
            try {
                r = function.apply(zipEntry);
            } catch (Exception ex) {
                Logger.handleError(ex);
            }
        }
        closeWithOutException();
        return r;
    }
    
}
