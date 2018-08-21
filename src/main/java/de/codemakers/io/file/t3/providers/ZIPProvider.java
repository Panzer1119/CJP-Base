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

import de.codemakers.io.file.t3.AdvancedFile;

import java.util.List;
import java.util.Objects;

public class ZIPProvider implements FileProvider<AdvancedFile> { //TODO Implement this
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return null;
    }
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return false;
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return false;
    }
    
    @Override
    public byte[] readBytes(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return new byte[0];
    }
    
    @Override
    public boolean writeBytes(AdvancedFile parent, AdvancedFile file, byte[] data) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (data == null) {
            data = new byte[0];
        }
        return false;
    }
    
    @Override
    public boolean createNewFile(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return false;
    }
    
    @Override
    public boolean delete(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return false;
    }
    
    @Override
    public boolean mkdir(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return false;
    }
    
    @Override
    public boolean mkdirs(AdvancedFile parent, AdvancedFile file) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        return false;
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
