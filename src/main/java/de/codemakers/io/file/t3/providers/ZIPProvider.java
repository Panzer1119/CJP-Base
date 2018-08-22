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

package de.codemakers.io.file.t3.providers;

import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.io.file.t3.AdvancedFile;
import de.codemakers.io.file.t3.AdvancedFileFilter;
import de.codemakers.io.file.t3.AdvancedFilenameFilter;
import de.codemakers.io.file.t3.ClosableZipEntry;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZIPProvider implements FileProvider<AdvancedFile> { //TODO Implement this
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        //TODO Implement
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, AdvancedFileFilter advancedFileFilter, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        //TODO Implement
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, AdvancedFilenameFilter advancedFilenameFilter, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        //TODO Implement
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        return !isDirectory(parent, file, parentBytes);
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final ClosableZipEntry closableZipEntry = getClosableZipEntry(parent, file, parentBytes);
        if (closableZipEntry == null) {
            return false;
        }
        return closableZipEntry.closeWithoutException(ZipEntry::isDirectory);
    }
    
    @Override
    public byte[] readBytes(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (parentBytes == null || parentBytes.length == 0) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            final byte[] data = IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry(file.getPathsCollected(File.separator))));
            zipFile.close();
            return data;
        } else {
            final String path = file.getPathsCollected(File.separator);
            final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(parentBytes));
            ZipEntry zipEntry = null;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().equals(path)) {
                    break;
                }
                zipInputStream.closeEntry();
            }
            final byte[] data = IOUtils.toByteArray(zipInputStream);
            if (zipEntry != null) {
                zipInputStream.closeEntry();
            }
            zipInputStream.close();
            return data;
        }
    }
    
    public ClosableZipEntry getClosableZipEntry(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        if (parentBytes == null || parentBytes.length == 0) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            return new ClosableZipEntry(zipFile, zipFile.getEntry(file.getPathsCollected(File.separator)));
        } else {
            final String path = file.getPathsCollected(File.separator);
            final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(parentBytes));
            ZipEntry zipEntry = null;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().equals(path)) {
                    break;
                }
                zipInputStream.closeEntry();
            }
            if (zipEntry == null) {
                zipInputStream.close();
                return null;
            }
            return new ClosableZipEntry(zipInputStream, zipEntry);
        }
    }
    
    @Override
    public boolean writeBytes(AdvancedFile parent, AdvancedFile file, byte[] data) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (data == null) {
            data = new byte[0];
        }
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean createNewFile(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean delete(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean mkdir(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean mkdirs(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean accept(AdvancedFile parent, String name) {
        if (parent == null || name == null || name.isEmpty() || !name.contains(".")) {
            return false;
        }
        final String name_lower = name.toLowerCase();
        return name_lower.endsWith(".zip") || name_lower.endsWith(".jar"); //TODO is a ".tar" also "unzipable"? Or do i have to create another FileProvider "TARProvider"?
    }
    
}
