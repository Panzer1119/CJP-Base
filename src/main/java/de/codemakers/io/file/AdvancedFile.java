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

import de.codemakers.base.logger.Logger;
import de.codemakers.base.os.OSUtil;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdvancedFile implements FileOperations, DirectoryOperations {
    
    public static final String INTERN_PREFIX = "jar:";
    public static final String UNIX_PATH_SEPARATOR = "/";
    
    private final String[] paths;
    private String path = null;
    private File file = null;
    private FileType type = FileType.UNKNOWN;
    
    public AdvancedFile(String... paths) {
        Objects.requireNonNull(paths);
        if (paths.length == 0) {
            paths = new String[] {""};
        }
        this.paths = paths;
        checkFile();
    }
    
    public AdvancedFile(String[] paths, String name) {
        Objects.requireNonNull(paths);
        Objects.requireNonNull(name);
        this.paths = new String[paths.length + 1];
        System.arraycopy(paths, 0, this.paths, 0, paths.length);
        this.paths[paths.length] = name;
        checkFile();
    }
    
    public AdvancedFile(File file) {
        this(file.getPath());
    }
    
    private void checkFile() {
        if (paths[0].startsWith(INTERN_PREFIX)) {
            type = FileType.FILE_INTERN;
            paths[0] = paths[0].substring(INTERN_PREFIX.length());
            if (paths[0].startsWith(UNIX_PATH_SEPARATOR)) {
                type = FileType.FILE_INTERN_ABSOLUTE;
            } else {
                type = FileType.FILE_INTERN_RELATIVE;
            }
        } else {
            type = FileType.FILE;
            if (toFile().isAbsolute()) {
                type = FileType.FILE_ABSOLUTE;
            } else {
                type = FileType.FILE_RELATIVE;
            }
        }
    }
    
    public String toPath() {
        if (path == null) {
            final String separator = type.isExtern() ? OSUtil.CURRENT_OS_HELPER.getFileSeparator() : UNIX_PATH_SEPARATOR;
            path = "";
            for (String g : paths) {
                path += g + separator;
            }
            if (!path.isEmpty()) {
                path = path.substring(0, path.length() - separator.length());
            }
        }
        return path;
    }
    
    public File toFile() {
        if (file == null) {
            file = new File(toPath());
        }
        return file;
    }
    
    private void checkForExtern() {
        if (!type.isExtern()) {
            throw new RuntimeException(toString() + " is not an external file");
        }
    }
    
    private void checkForFile() {
        if (!isFile()) {
            throw new RuntimeException(toString() + " is not a file");
        }
    }
    
    private void checkForDirectory() {
        if (!isFile()) {
            throw new RuntimeException(toString() + " is not a directory");
        }
    }
    
    private void checkForExistance() {
        if (!isFile()) {
            throw new RuntimeException(toString() + " does not exist");
        }
    }
    
    @Override
    public boolean mkdir() {
        checkForExtern();
        checkForDirectory();
        return toFile().mkdir();
    }
    
    @Override
    public boolean mkdirs() {
        checkForExtern();
        checkForDirectory();
        return toFile().mkdirs();
    }
    
    @Override
    public List<IFile> listFiles() {
        checkForDirectory();
        if (type.isExtern()) {
            final List<IFile> files = new ArrayList<>();
            for (String f : toFile().list()) {
                files.add(new AdvancedFile(paths, f));
            }
            return files;
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public IFile[] listFilesAsArray() {
        checkForDirectory();
        return (IFile[]) listFiles().toArray();
    }
    
    @Override
    public boolean createNewFile() {
        checkForFile();
        try {
            return toFile().createNewFile();
        } catch (Exception ex) {
            Logger.handleError(ex);
            return false;
        }
    }
    
    @Override
    public byte[] toBytes() {
        checkForExistance();
        checkForFile();
        try {
            return Files.readAllBytes(toFile().toPath());
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    @Override
    public IFile openAs(Class<IFile> type) {
        return null;
    }
    
    @Override
    public String getName() {
        return paths[paths.length - 1];
    }
    
    @Override
    public IFile getParent() {
        if (type.isExtern()) {
            return new AdvancedFile(toFile().getParentFile());
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public String getPath() {
        return toPath();
    }
    
    @Override
    public FileType getType() {
        return type;
    }
    
    @Override
    public IFile getAbsoluteFile() {
        if (type.isExtern()) {
            return new AdvancedFile(toFile().getAbsoluteFile());
        } else {
            final String[] paths_copy = Arrays.copyOf(paths, paths.length);
            paths_copy[0] = UNIX_PATH_SEPARATOR + paths_copy[0];
            return new AdvancedFile(paths_copy);
        }
    }
    
    @Override
    public URL toURL() {
        try {
            if (type.isExtern()) {
                return toFile().toURL();
            } else {
                return new URL(toPath());
            }
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    @Override
    public URI toURI() {
        try {
            if (type.isExtern()) {
                return toFile().toURI();
            } else {
                return new URI(toPath());
            }
        } catch (Exception ex) {
            Logger.handleError(ex);
            return null;
        }
    }
    
    @Override
    public boolean canRead() {
        if (type.isExtern()) {
            return toFile().canRead();
        } else {
            return exists();
        }
    }
    
    @Override
    public boolean canWrite() {
        if (type.isExtern()) {
            return toFile().canWrite();
        } else {
            return false;
        }
    }
    
    @Override
    public boolean exists() {
        if (type.isExtern()) {
            return toFile().exists();
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public boolean isDirectory() {
        if (type.isExtern()) {
            return toFile().isDirectory();
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public boolean isFile() {
        if (type.isExtern()) {
            return toFile().isFile();
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public boolean isHidden() {
        if (type.isExtern()) {
            return toFile().isHidden();
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public boolean isCustom() {
        return type.isCustom();
    }
    
    @Override
    public long created() {
        return lastModified(); //TODO Is this even possible???
    }
    
    @Override
    public long lastModified() {
        if (type.isExtern()) {
            return toFile().lastModified();
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public long length() {
        if (type.isExtern()) {
            return toFile().length();
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public boolean delete() {
        if (type.isExtern()) {
            return toFile().delete();
        } else {
            return false;
        }
    }
    
    @Override
    public boolean deleteOnExit() {
        if (type.isExtern()) {
            toFile().deleteOnExit();
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean renameTo(String name) {
        if (type.isExtern()) {
            return toFile().renameTo(new File(toPath() + OSUtil.CURRENT_OS_HELPER.getFileSeparator() + name));
        } else {
            return false;
        }
    }
    
    @Override
    public boolean setLastModified(long time) {
        if (type.isExtern()) {
            return toFile().setLastModified(time);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean setReadOnly() {
        if (type.isExtern()) {
            return toFile().setReadOnly();
        } else {
            return false;
        }
    }
    
    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {
        if (type.isExtern()) {
            return toFile().setWritable(writable, ownerOnly);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {
        if (type.isExtern()) {
            return toFile().setReadable(readable, ownerOnly);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        if (type.isExtern()) {
            return toFile().setExecutable(executable, ownerOnly);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean canExecute() {
        if (type.isExtern()) {
            return toFile().canExecute();
        } else {
            throw new UnsupportedOperationException();
            //return null; //TODO Implement the resource walker thing
        }
    }
    
    @Override
    public boolean copy(IFile destination) {
        return false; //TODO Implement this
    }
    
    @Override
    public boolean copyToDir(IFile destination) {
        return false; //TODO Implement this
    }
    
    @Override
    public boolean move(IFile destination) {
        return false; //TODO Implement this
    }
    
    @Override
    public boolean moveToDir(IFile destination) {
        return false; //TODO Implement this
    }
    
    @Override
    public String toString() {
        return "AdvancedFile{" + "path='" + toPath() + '\'' + ", file=" + toFile() + ", file(absolute)=" + toFile().getAbsoluteFile() + ", type=" + getType() + '}';
    }
    
}
