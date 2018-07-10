/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.io.file;

import java.net.URI;
import java.net.URL;

public interface IFile {
    
    String getName();
    
    IFile getParent();
    
    String getPath();
    
    FileType getType();
    
    IFile getAbsoluteFile();
    
    URL toURL();
    
    URI toURI();
    
    boolean canRead();
    
    boolean canWrite();
    
    boolean exists();
    
    boolean isDirectory();
    
    boolean isFile();
    
    boolean isHidden();
    
    boolean isCustom();
    
    long created();
    
    long lastModified();
    
    long length();
    
    boolean delete();
    
    boolean deleteOnExit();
    
    boolean renameTo(String name);
    
    boolean setLastModified(long time);
    
    boolean setReadOnly();
    
    boolean setWritable(boolean writable, boolean ownerOnly);
    
    default boolean setWritable(boolean writable) {
        return setWritable(writable, true);
    }
    
    boolean setReadable(boolean readable, boolean ownerOnly);
    
    default boolean setReadable(boolean readable) {
        return setReadable(readable, true);
    }
    
    boolean setExecutable(boolean executable, boolean ownerOnly);
    
    default boolean setExecutable(boolean executable) {
        return setExecutable(executable, true);
    }
    
    boolean canExecute();
    
    boolean copy(IFile destination);
    
    boolean copyToDir(IFile destination);
    
    boolean move(IFile destination);
    
    boolean moveToDir(IFile destination);
    
}
