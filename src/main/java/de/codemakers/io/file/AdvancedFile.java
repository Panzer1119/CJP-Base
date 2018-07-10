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
import java.util.Objects;

public class AdvancedFile implements FileOperations, DirectoryOperations {
    
    public static final String INTERN_PREFIX = "jar:";
    
    private final String[] path;
    private FileType type = FileType.UNKNOWN;
    
    public AdvancedFile(String... path) {
        Objects.requireNonNull(path);
        if (path.length == 0) {
            path = new String[] {""};
        }
        this.path = path;
        checkFile();
    }
    
    private void checkFile() {
        if (path[0].startsWith(INTERN_PREFIX)) {
            path[0] = path[0].substring(INTERN_PREFIX.length());
        } else {
            type = FileType.FILE_ABSOLUTE;
        }
    }
    
    @Override
    public boolean mkdir() {
        return false;
    }
    
    @Override
    public boolean mkdirs() {
        return false;
    }
    
    @Override
    public IFile listFiles() {
        return null;
    }
    
    @Override
    public boolean createNewFile() {
        return false;
    }
    
    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
    
    @Override
    public IFile openAs(Class<IFile> type) {
        return null;
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public IFile getParent() {
        return null;
    }
    
    @Override
    public String getPath() {
        return null;
    }
    
    @Override
    public FileType getType() {
        return null;
    }
    
    @Override
    public IFile getAbsoluteFile() {
        return null;
    }
    
    @Override
    public URL toURL() {
        return null;
    }
    
    @Override
    public URI toURI() {
        return null;
    }
    
    @Override
    public boolean canRead() {
        return false;
    }
    
    @Override
    public boolean canWrite() {
        return false;
    }
    
    @Override
    public boolean exists() {
        return false;
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
    
    @Override
    public boolean isFile() {
        return false;
    }
    
    @Override
    public boolean isHidden() {
        return false;
    }
    
    @Override
    public boolean isCustom() {
        return false;
    }
    
    @Override
    public long created() {
        return 0;
    }
    
    @Override
    public long lastModified() {
        return 0;
    }
    
    @Override
    public long length() {
        return 0;
    }
    
    @Override
    public boolean delete() {
        return false;
    }
    
    @Override
    public boolean deleteOnExit() {
        return false;
    }
    
    @Override
    public boolean renameTo(String name) {
        return false;
    }
    
    @Override
    public boolean setLastModified(long time) {
        return false;
    }
    
    @Override
    public boolean setReadOnly() {
        return false;
    }
    
    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {
        return false;
    }
    
    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {
        return false;
    }
    
    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        return false;
    }
    
    @Override
    public boolean canExecute() {
        return false;
    }
    
    @Override
    public boolean copy(IFile destination) {
        return false;
    }
    
    @Override
    public boolean copyToDir(IFile destination) {
        return false;
    }
    
    @Override
    public boolean move(IFile destination) {
        return false;
    }
    
    @Override
    public boolean moveToDir(IFile destination) {
        return false;
    }
    
}
