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
import de.codemakers.base.os.OSUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdvancedFile implements IFile {
    
    public static final String FILE_SEPARATOR_WINDOWS_STRING = OSUtil.WINDOWS_HELPER.getFileSeparator();
    public static final String FILE_SEPARATOR_DEFAULT_STRING = OSUtil.DEFAULT_HELPER.getFileSeparator();
    public static final String FILE_SEPARATOR_CURRENT_STRING = OSUtil.CURRENT_OS_HELPER.getFileSeparator();
    public static final String FILE_SEPARATOR_NOT_CURRENT_STRING = FILE_SEPARATOR_CURRENT_STRING == FILE_SEPARATOR_WINDOWS_STRING ? FILE_SEPARATOR_DEFAULT_STRING : FILE_SEPARATOR_WINDOWS_STRING;
    public static final char FILE_SEPARATOR_WINDOWS_CHAR = OSUtil.WINDOWS_HELPER.getFileSeparatorChar();
    public static final char FILE_SEPARATOR_DEFAULT_CHAR = OSUtil.DEFAULT_HELPER.getFileSeparatorChar();
    public static final char FILE_SEPARATOR_CURRENT_CHAR = OSUtil.CURRENT_OS_HELPER.getFileSeparatorChar();
    public static final char FILE_SEPARATOR_NOT_CURRENT_CHAR = FILE_SEPARATOR_CURRENT_CHAR == FILE_SEPARATOR_WINDOWS_CHAR ? FILE_SEPARATOR_DEFAULT_CHAR : FILE_SEPARATOR_WINDOWS_CHAR;
    public static final String FILE_SEPARATOR_WINDOWS_REGEX = OSUtil.WINDOWS_HELPER.getFileSeparatorRegex();
    public static final String FILE_SEPARATOR_DEFAULT_REGEX = OSUtil.DEFAULT_HELPER.getFileSeparatorRegex();
    public static final String FILE_SEPARATOR_CURRENT_REGEX = (FILE_SEPARATOR_CURRENT_CHAR == FILE_SEPARATOR_WINDOWS_CHAR) ? FILE_SEPARATOR_WINDOWS_REGEX : FILE_SEPARATOR_DEFAULT_REGEX;
    public static final String FILE_SEPARATOR_NOT_CURRENT_REGEX = (FILE_SEPARATOR_CURRENT_CHAR != FILE_SEPARATOR_WINDOWS_CHAR) ? FILE_SEPARATOR_WINDOWS_REGEX : FILE_SEPARATOR_DEFAULT_REGEX;
    
    public static final String PREFIX_INTERN = "intern:";
    public static final String PREFIX_EXTERN = "extern:";
    
    public static final List<FileProvider> FILE_PROVIDERS = new ArrayList<>();
    public static final ZIPProvider ZIP_PROVIDER = new ZIPProvider();
    
    static {
        FILE_PROVIDERS.add(ZIP_PROVIDER);
    }
    
    private boolean init = false;
    private boolean windowsSeparator = false;
    private boolean extern = true;
    protected String[] paths = new String[0];
    private AdvancedFile parent = null;
    private FileProvider provider = null;
    private String path = null;
    
    public AdvancedFile(String... paths) {
        this((AdvancedFile) null, paths);
    }
    
    public AdvancedFile(String name, String[] paths) {
        this.paths = Arrays.copyOf(paths, paths.length + 1);
        this.paths[this.paths.length - 1] = name;
        checkIntern();
        init();
    }
    
    public AdvancedFile(AdvancedFile parent, String... paths) {
        this.parent = parent;
        this.provider = null;
        this.paths = paths;
        if (parent != null) {
            extern = parent.extern;
        } else {
            checkIntern();
        }
        init();
    }
    
    public AdvancedFile(AdvancedFile parent, FileProvider provider, String... paths) {
        this.parent = parent;
        this.provider = provider;
        this.paths = paths;
        if (parent != null) {
            windowsSeparator = parent.windowsSeparator;
            extern = parent.extern;
            init = true;
        } else {
            checkIntern();
        }
    }
    
    public static final FileProvider getProvider(AdvancedFile parent, String name) {
        Objects.requireNonNull(name);
        return FILE_PROVIDERS.stream().filter((provider) -> provider.accept(parent, name)).findFirst().orElse(null);
    }
    
    private final void init() {
        boolean done = false;
        final List<String> paths_ = new ArrayList<>();
        for (String p : paths) {
            done = false;
            if (!init) {
                if (p.contains(FILE_SEPARATOR_WINDOWS_STRING)) {
                    paths_.addAll(Arrays.asList(p.split(FILE_SEPARATOR_WINDOWS_REGEX)));
                    windowsSeparator = true;
                    init = true;
                    done = true;
                }
                if (p.contains(FILE_SEPARATOR_DEFAULT_STRING)) {
                    if (init) {
                        throw new RuntimeException(getClass().getSimpleName() + " already contains a Windows file separator");
                    }
                    paths_.addAll(Arrays.asList(p.split(FILE_SEPARATOR_DEFAULT_REGEX)));
                    windowsSeparator = false;
                    init = true;
                    done = true;
                }
                if (!done) {
                    paths_.add(p);
                }
            } else {
                if (p.contains(FILE_SEPARATOR_WINDOWS_STRING)) {
                    if (windowsSeparator) {
                        paths_.addAll(Arrays.asList(p.split(FILE_SEPARATOR_WINDOWS_REGEX)));
                    } else {
                        throw new RuntimeException(getClass().getSimpleName() + " already contains an UNIX file separator");
                    }
                    done = true;
                }
                if (p.contains(FILE_SEPARATOR_DEFAULT_STRING)) {
                    if (windowsSeparator) {
                        throw new RuntimeException(getClass().getSimpleName() + " already contains a Windows file separator");
                    } else {
                        paths_.addAll(Arrays.asList(p.split(FILE_SEPARATOR_DEFAULT_REGEX)));
                    }
                    done = true;
                }
                if (!done) {
                    paths_.add(p);
                }
            }
        }
        init = true;
        final List<String> temp = new ArrayList<>();
        for (String p : paths_) {
            temp.add(p);
            final FileProvider provider = getProvider(parent, p);
            if (provider != null) {
                parent = new AdvancedFile(parent, provider, temp.toArray(new String[0]));
                temp.clear();
            }
        }
        paths = temp.toArray(new String[0]);
        temp.clear();
        paths_.clear();
    }
    
    public final String[] getPaths() {
        return paths;
    }
    
    public final String getPathString() {
        if (path == null) {
            path = Arrays.stream(paths).collect(Collectors.joining(windowsSeparator ? FILE_SEPARATOR_WINDOWS_STRING : FILE_SEPARATOR_DEFAULT_STRING));
            if (parent != null) {
                path = parent.getPathString() + (windowsSeparator ? FILE_SEPARATOR_WINDOWS_CHAR : FILE_SEPARATOR_DEFAULT_CHAR) + path;
            }
        }
        return path;
    }
    
    private final void checkIntern() {
        if (paths != null && paths.length > 0) {
            if (paths[0].startsWith(PREFIX_INTERN)) {
                extern = false;
                paths[0] = paths[0].substring(PREFIX_INTERN.length());
            } else if (paths[0].startsWith(PREFIX_EXTERN)) {
                extern = true;
                paths[0] = paths[0].substring(PREFIX_EXTERN.length());
            }
            if (paths[0].isEmpty()) {
                paths = Arrays.asList(paths).stream().skip(1).toArray(String[]::new);
            }
        }
    }
    
    public final File toFile() {
        if (!extern) {
            throw new UnsupportedOperationException("Not yet implemented for jar intern files");
        }
        return new File(getPathString());
    }
    
    @Override
    public byte[] readBytes() {
        if (parent != null) {
            return parent.readBytes(this);
        } else {
            try {
                return Files.readAllBytes(new File(getPathString()).toPath());
            } catch (Exception ex) {
                Logger.handleError(ex);
                return null;
            }
        }
    }
    
    byte[] readBytes(AdvancedFile advancedFile) {
        if (parent != null) {
            return provider.readBytes(this, advancedFile, readBytes());
        } else {
            return provider.readBytes(this, advancedFile);
        }
    }
    
    @Override
    public boolean writeBytes(byte[] data) {
        if (parent != null) {
            return parent.writeBytes(this, data);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            try {
                Files.write(toFile().toPath(), data);
                return true;
            } catch (Exception ex) {
                Logger.handleError(ex);
                return false;
            }
        }
    }
    
    boolean writeBytes(AdvancedFile advancedFile, byte[] data) {
        if (parent != null) {
            //return provider.writeBytes(this, advancedFile, advancedFile.paths, data, readBytes());
            throw new UnsupportedOperationException("Not yet implemented for files in other files");
        } else {
            return provider.writeBytes(this, advancedFile, data);
        }
    }
    
    @Override
    public boolean mkdir() {
        if (parent != null) {
            return parent.mkdir(this);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            return toFile().mkdir();
        }
    }
    
    boolean mkdir(AdvancedFile advancedFile) {
        if (parent != null) {
            //return provider.mkdir(this, advancedFile, readBytes());
            throw new UnsupportedOperationException("Not yet implemented for files in other files");
        } else {
            return provider.mkdir(this, advancedFile);
        }
    }
    
    @Override
    public boolean mkdirs() {
        if (parent != null) {
            return parent.mkdirs(this);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            return toFile().mkdirs();
        }
    }
    
    boolean mkdirs(AdvancedFile advancedFile) {
        if (parent != null) {
            //return provider.mkdirs(this, advancedFile, readBytes());
            throw new UnsupportedOperationException("Not yet implemented for files in other files");
        } else {
            return provider.mkdirs(this, advancedFile);
        }
    }
    
    @Override
    public boolean createNewFile() {
        if (parent != null) {
            return parent.createNewFile(this);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            try {
                return toFile().createNewFile();
            } catch (Exception ex) {
                Logger.handleError(ex);
                return false;
            }
        }
    }
    
    boolean createNewFile(AdvancedFile advancedFile) {
        if (parent != null) {
            //return provider.createNewFile(this, advancedFile, readBytes());
            throw new UnsupportedOperationException("Not yet implemented for files in other files");
        } else {
            return provider.createNewFile(this, advancedFile);
        }
    }
    
    @Override
    public boolean delete() {
        if (parent != null) {
            return parent.delete(this);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            return toFile().delete();
        }
    }
    
    boolean delete(AdvancedFile advancedFile) {
        if (parent != null) {
            //return provider.delete(this, advancedFile, readBytes());
            throw new UnsupportedOperationException("Not yet implemented for files in other files");
        } else {
            return provider.delete(this, advancedFile);
        }
    }
    
    @Override
    public boolean isFile() {
        if (parent != null) {
            return parent.isFile(this);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            return toFile().isFile();
        }
    }
    
    boolean isFile(AdvancedFile advancedFile) {
        if (parent != null) {
            return provider.isFile(this, advancedFile, readBytes());
        } else {
            return provider.isFile(this, advancedFile);
        }
    }
    
    @Override
    public boolean isDirectory() {
        if (parent != null) {
            return parent.isDirectory(this);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            return toFile().isDirectory();
        }
    }
    
    boolean isDirectory(AdvancedFile advancedFile) {
        if (parent != null) {
            return provider.isDirectory(this, advancedFile, readBytes());
        } else {
            return provider.isDirectory(this, advancedFile);
        }
    }
    
    @Override
    public final List<AdvancedFile> listFiles(boolean recursive) {
        return listFiles(new ArrayList<>(), null, null, recursive);
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFileFilter advancedFileFilter, boolean recursive) {
        return listFiles(new ArrayList<>(), advancedFileFilter, null, recursive);
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFilenameFilter advancedFilenameFilter, boolean recursive) {
        return listFiles(new ArrayList<>(), null, advancedFilenameFilter, recursive);
    }
    
    final List<AdvancedFile> listFiles(List<AdvancedFile> advancedFiles, AdvancedFileFilter advancedFileFilter, AdvancedFilenameFilter advancedFilenameFilter, boolean recursive) {
        if (parent != null) {
            return parent.listFiles(advancedFiles, advancedFileFilter, advancedFilenameFilter, recursive, this);
        } else {
            if (!extern) {
                throw new UnsupportedOperationException("Not yet implemented for jar intern files");
            }
            final File directory = new File(getPathString());
            for (File file : directory.listFiles()) {
                if (advancedFilenameFilter != null) {
                    if (!advancedFilenameFilter.accept(this, file.getName())) {
                        continue;
                    }
                }
                final AdvancedFile advancedFile = new AdvancedFile(file.getName(), paths);
                if (advancedFileFilter != null) {
                    if (!advancedFileFilter.accept(advancedFile)) {
                        continue;
                    }
                }
                advancedFiles.add(advancedFile);
                if (recursive && file.isDirectory()) {
                    advancedFile.listFiles(advancedFiles, advancedFileFilter, advancedFilenameFilter, recursive);
                }
            }
            return advancedFiles;
        }
    }
    
    /* When "this" is a parent file, then this functions lists all files for a given file under "this" file*/
    final List<AdvancedFile> listFiles(List<AdvancedFile> advancedFiles, AdvancedFileFilter advancedFileFilter, AdvancedFilenameFilter advancedFilenameFilter, boolean recursive, AdvancedFile advancedFile) {
        Objects.requireNonNull(advancedFiles);
        Objects.requireNonNull(provider);
        Objects.requireNonNull(advancedFile);
        if (parent != null) {
            return provider.listFiles(this, advancedFile, advancedFiles, advancedFileFilter, advancedFilenameFilter, recursive, readBytes());
        } else {
            return provider.listFiles(this, advancedFile, advancedFiles, advancedFileFilter, advancedFilenameFilter, recursive);
        }
    }
    
    @Override
    public final String toString() {
        return getClass().getSimpleName() + "{" + "windowsSeparator=" + windowsSeparator + ", extern=" + extern + ", paths=" + Arrays.toString(paths) + ", parent=" + parent + ", provider=" + provider + ", path='" + path + '\'' + '}';
    }
    
}
