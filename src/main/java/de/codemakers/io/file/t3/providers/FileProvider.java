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

import de.codemakers.io.file.t3.AdvancedFileFilter;
import de.codemakers.io.file.t3.AdvancedFilenameFilter;
import de.codemakers.io.file.t3.IFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileProvider<T extends IFile> extends AdvancedFilenameFilter { //TODO Make some "RARProvider", which can at least read/list .rar files, and maybe autodetect WinRaR installations for optionally writing/editing .rar files (Make some OS function for that, so that this framework has some useful features (finding WinRaR installations platform independent))
    
    List<T> listFiles(T parent, T file, boolean recursive, InputStream inputStream) throws Exception;
    
    List<T> listFiles(T parent, T file, AdvancedFileFilter advancedFileFilter, boolean recursive, InputStream inputStream) throws Exception;
    
    boolean isFile(T parent, T file, InputStream inputStream) throws Exception;
    
    boolean isDirectory(T parent, T file, InputStream inputStream) throws Exception;
    
    InputStream createInputStream(T parent, T file, InputStream inputStream) throws Exception;
    
    byte[] readBytes(T parent, T file, InputStream inputStream) throws Exception;
    
    OutputStream createOutputStream(T parent, T file) throws Exception;
    
    boolean writeBytes(T parent, T file, byte[] data) throws Exception;
    
    boolean createNewFile(T parent, T file) throws Exception;
    
    boolean delete(T parent, T file) throws Exception;
    
    boolean mkdir(T parent, T file) throws Exception;
    
    boolean mkdirs(T parent, T file) throws Exception;
    
}
