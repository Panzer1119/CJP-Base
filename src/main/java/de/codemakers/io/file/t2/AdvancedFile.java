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

import de.codemakers.base.os.OSUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdvancedFile {
    
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
    
    public static final List<AdvancedProvider> PROVIDERS = new ArrayList<>();
    public static final ZIPProvider ZIP_PROVIDER = new ZIPProvider();
    
    static {
        PROVIDERS.add(ZIP_PROVIDER);
    }
    
    private boolean init = false;
    private boolean windowsSeparator = false;
    private String[] paths = new String[0];
    private AdvancedFile parent = null;
    private AdvancedProvider provider = null;
    private String path = null;
    
    public AdvancedFile(String... paths) {
        this((AdvancedFile) null, paths);
    }
    
    public AdvancedFile(String name, String[] paths) {
        this.paths = Arrays.copyOf(paths, paths.length + 1);
        this.paths[this.paths.length - 1] = name;
        init();
    }
    
    public AdvancedFile(AdvancedFile parent, String... paths) {
        this.parent = parent;
        this.provider = null;
        this.paths = paths;
        init();
    }
    
    public AdvancedFile(AdvancedFile parent, AdvancedProvider provider, String... paths) {
        this.parent = parent;
        this.provider = provider;
        this.paths = paths;
        if (parent != null) {
            windowsSeparator = parent.windowsSeparator;
            init = true;
        }
    }
    
    public static final AdvancedProvider getProvider(AdvancedFile parent, String name) {
        Objects.requireNonNull(name);
        return PROVIDERS.stream().filter((advancedProvider) -> advancedProvider.accept(parent, name)).findFirst().orElse(null);
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
            init = true;
        }
        final List<String> temp = new ArrayList<>();
        for (String p : paths_) {
            temp.add(p);
            final AdvancedProvider advancedProvider = getProvider(parent, p);
            if (advancedProvider != null) {
                parent = new AdvancedFile(parent, advancedProvider, temp.toArray(new String[0]));
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
        }
        if (parent != null) {
            path = parent.getPathString() + (windowsSeparator ? FILE_SEPARATOR_WINDOWS_CHAR : FILE_SEPARATOR_DEFAULT_CHAR) + path;
        }
        return path;
    }
    
    public final byte[] readBytes() throws Exception {
        if (parent != null) {
            return parent.readBytes(this);
        } else {
            return Files.readAllBytes(new File(getPathString()).toPath());
        }
    }
    
    public final byte[] readBytes(AdvancedFile advancedFile) throws Exception {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(advancedFile);
        if (parent != null) {
            return provider.readBytes(this, advancedFile, advancedFile.paths, readBytes());
        } else {
            return provider.readBytes(this, advancedFile, advancedFile.paths);
        }
    }
    
    public final List<AdvancedFile> listFiles() throws Exception {
        return listFiles(false);
    }
    
    public final List<AdvancedFile> listFiles(boolean recursive) throws Exception {
        return listFiles(new ArrayList<>(), recursive);
    }
    
    public final List<AdvancedFile> listFiles(List<AdvancedFile> advancedFiles, boolean recursive) throws Exception {
        if (parent != null) {
            return parent.listFiles(advancedFiles, recursive, this);
        } else {
            final File directory = new File(getPathString());
            for (File file : directory.listFiles()) {
                final AdvancedFile advancedFile = new AdvancedFile(file.getName(), paths);
                advancedFiles.add(advancedFile);
                if (recursive && file.isDirectory()) {
                    advancedFile.listFiles(advancedFiles, recursive);
                }
            }
            return advancedFiles;
        }
    }
    
    public final List<AdvancedFile> listFiles(List<AdvancedFile> advancedFiles, boolean recursive, AdvancedFile advancedFile) throws Exception {
        Objects.requireNonNull(advancedFiles);
        Objects.requireNonNull(provider);
        Objects.requireNonNull(advancedFile);
        if (parent != null) {
            return provider.listFiles(this, advancedFile, advancedFile.paths, advancedFiles, recursive, readBytes());
        } else {
            return provider.listFiles(this, advancedFile, advancedFile.paths, advancedFiles, recursive);
        }
    }
    
    @Override
    public final String toString() {
        return "AdvancedFile{" + "init=" + init + ", windowsSeparator=" + windowsSeparator + ", paths=" + Arrays.toString(paths) + ", parent=" + parent + ", provider=" + provider + ", path='" + path + '\'' + '}';
    }
    
}
