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

import java.util.List;

public abstract class FileProvider implements AdvancedFilenameFilter {
    
    public abstract List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile advancedFile, List<AdvancedFile> advancedFiles, AdvancedFileFilter advancedFileFilter, AdvancedFilenameFilter advancedFilenameFilter, boolean recursive, byte... data_parent);
    
    public abstract byte[] readBytes(AdvancedFile parent, AdvancedFile advancedFile, byte... data_parent);
    
    public abstract boolean writeBytes(AdvancedFile parent, AdvancedFile advancedFile, byte[] data);
    
    public abstract boolean createNewFile(AdvancedFile parent, AdvancedFile advancedFile);
    
    public abstract boolean delete(AdvancedFile parent, AdvancedFile advancedFile);
    
    public abstract boolean mkdir(AdvancedFile parent, AdvancedFile advancedFile);
    
    public abstract boolean mkdirs(AdvancedFile parent, AdvancedFile advancedFile);
    
    public abstract boolean isFile(AdvancedFile parent, AdvancedFile advancedFile, byte... data_parent);
    
    public abstract boolean isDirectory(AdvancedFile parent, AdvancedFile advancedFile, byte... data_parent);
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{}";
    }
    
}
