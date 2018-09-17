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

import de.codemakers.base.Standard;
import de.codemakers.io.file.t3.AdvancedFile;
import de.codemakers.io.file.t3.AdvancedFileFilter;
import de.codemakers.io.file.t3.exceptions.is.FileIsExternRuntimeException;
import de.codemakers.io.file.t3.exceptions.is.FileIsInternRuntimeException;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class InternProvider extends FileProvider<AdvancedFile> {
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, InputStream inputStream) throws Exception {
        if (Standard.RUNNING_JAR_IS_JAR) {
            parent = Standard.RUNNING_JAR_ADVANCED_FILE;
            return AdvancedFile.ZIP_PROVIDER.listFiles(parent, file, recursive, inputStream);
        } else {
            throw new FileIsExternRuntimeException(file + " is extern");
        }
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, AdvancedFileFilter advancedFileFilter, InputStream inputStream) throws Exception {
        if (Standard.RUNNING_JAR_IS_JAR) {
            parent = Standard.RUNNING_JAR_ADVANCED_FILE;
            return AdvancedFile.ZIP_PROVIDER.listFiles(parent, file, recursive, advancedFileFilter, inputStream);
        } else {
            throw new FileIsExternRuntimeException(file + " is extern");
        }
    }
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        if (Standard.RUNNING_JAR_IS_JAR) {
            parent = Standard.RUNNING_JAR_ADVANCED_FILE;
            return AdvancedFile.ZIP_PROVIDER.isFile(parent, file, inputStream);
        } else {
            throw new FileIsExternRuntimeException(file + " is extern");
        }
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        if (Standard.RUNNING_JAR_IS_JAR) {
            parent = Standard.RUNNING_JAR_ADVANCED_FILE;
            return AdvancedFile.ZIP_PROVIDER.isDirectory(parent, file, inputStream);
        } else {
            throw new FileIsExternRuntimeException(file + " is extern");
        }
    }
    
    @Override
    public boolean exists(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        if (Standard.RUNNING_JAR_IS_JAR) {
            parent = Standard.RUNNING_JAR_ADVANCED_FILE;
            return AdvancedFile.ZIP_PROVIDER.exists(parent, file, inputStream);
        } else {
            throw new FileIsExternRuntimeException(file + " is extern");
        }
    }
    
    @Override
    public InputStream createInputStream(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        if (Standard.RUNNING_JAR_IS_JAR) {
            parent = Standard.RUNNING_JAR_ADVANCED_FILE;
            return AdvancedFile.ZIP_PROVIDER.createInputStream(parent, file, inputStream);
        } else {
            throw new FileIsExternRuntimeException(file + " is extern");
        }
    }
    
    @Override
    public byte[] readBytes(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        return IOUtils.toByteArray(file.getNonNullClazz().getResourceAsStream(file.getPath()));
    }
    
    @Override
    public boolean canWrite(AdvancedFile parent, AdvancedFile file) {
        return false;
    }
    
    @Override
    public OutputStream createOutputStream(AdvancedFile parent, AdvancedFile file, boolean append) {
        throw new FileIsInternRuntimeException(file + " is intern");
    }
    
    @Override
    public boolean writeBytes(AdvancedFile parent, AdvancedFile file, byte[] data) {
        throw new FileIsInternRuntimeException(file + " is intern");
    }
    
    @Override
    public boolean createNewFile(AdvancedFile parent, AdvancedFile file) {
        throw new FileIsInternRuntimeException(file + " is intern");
    }
    
    @Override
    public boolean delete(AdvancedFile parent, AdvancedFile file) {
        throw new FileIsInternRuntimeException(file + " is intern");
    }
    
    @Override
    public boolean mkdir(AdvancedFile parent, AdvancedFile file) {
        throw new FileIsInternRuntimeException(file + " is intern");
    }
    
    @Override
    public boolean mkdirs(AdvancedFile parent, AdvancedFile file) {
        throw new FileIsInternRuntimeException(file + " is intern");
    }
    
    @Override
    public boolean test(AdvancedFile parent, String name) {
        if (parent == null) {
            return false;
        }
        return parent.isIntern();
    }
    
    @Override
    public boolean test(AdvancedFile file) {
        if (file == null) {
            return false;
        }
        return file.isIntern();
    }
    
}
