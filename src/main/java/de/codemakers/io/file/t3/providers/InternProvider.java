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
import de.codemakers.io.file.t3.exceptions.is.FileIsInternException;

import java.util.List;

public class InternProvider implements FileProvider<AdvancedFile> {
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        return null;
    }
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        return false;
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        return false;
    }
    
    @Override
    public byte[] readBytes(AdvancedFile parent, AdvancedFile file, byte... parentBytes) throws Exception {
        return new byte[0];
    }
    
    @Override
    public boolean writeBytes(AdvancedFile parent, AdvancedFile file, byte[] data) throws Exception {
        throw new FileIsInternException(file + " is intern");
    }
    
    @Override
    public boolean createNewFile(AdvancedFile parent, AdvancedFile file) throws Exception {
        throw new FileIsInternException(file + " is intern");
    }
    
    @Override
    public boolean delete(AdvancedFile parent, AdvancedFile file) throws Exception {
        throw new FileIsInternException(file + " is intern");
    }
    
    @Override
    public boolean mkdir(AdvancedFile parent, AdvancedFile file) throws Exception {
        throw new FileIsInternException(file + " is intern");
    }
    
    @Override
    public boolean mkdirs(AdvancedFile parent, AdvancedFile file) throws Exception {
        throw new FileIsInternException(file + " is intern");
    }
    
    @Override
    public boolean accept(AdvancedFile parent, String name) {
        if (parent == null) {
            return false;
        }
        return parent.isIntern();
    }
    
    @Override
    public boolean accept(AdvancedFile file) {
        if (file == null) {
            return false;
        }
        return file.isIntern();
    }
    
}
