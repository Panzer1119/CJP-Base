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

package de.codemakers.io.file.t3;

import de.codemakers.base.os.OSUtil;
import de.codemakers.base.util.Copyable;
import de.codemakers.io.file.t3.exceptions.FileRuntimeException;
import de.codemakers.io.file.t3.exceptions.is.*;
import de.codemakers.io.file.t3.exceptions.isnot.*;
import de.codemakers.io.file.t3.providers.FileProvider;
import de.codemakers.io.file.t3.providers.InternProvider;
import de.codemakers.io.file.t3.providers.ZIPProvider;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedFile implements Copyable, IFile {
    
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
    
    public static final List<FileProvider<AdvancedFile>> FILE_PROVIDERS = new CopyOnWriteArrayList<>();
    public static final ZIPProvider ZIP_PROVIDER = new ZIPProvider();
    public static final InternProvider INTERN_PROVIDER = new InternProvider();
    
    static {
        FILE_PROVIDERS.add(ZIP_PROVIDER);
        FILE_PROVIDERS.add(INTERN_PROVIDER);
    }
    
    private String[] paths;
    private boolean windowsSeparator = true;
    private boolean extern = true;
    private boolean absolute = true;
    private AdvancedFile parent;
    private FileProvider fileProvider;
    //Only for relative intern files
    private Class<?> clazz; //TODO this is for the reference of a relative internal file
    //Temp
    private transient String path;
    private transient Path path_;
    private transient URI uri;
    private transient URL url;
    private transient File file;
    
    public AdvancedFile(String... paths) { //TODO Implement init method, that looks if some fileProviders are needed
        this.paths = paths;
    }
    
    public AdvancedFile(String name, String[] paths) { //TODO Implement init method, that looks if some fileProviders are needed
        this.paths = paths;
    }
    
    public AdvancedFile(AdvancedFile parent, String... paths) { //TODO Implement init method, that looks if some fileProviders are needed
        this.parent = parent;
        this.paths = paths;
    }
    
    protected AdvancedFile(AdvancedFile parent, FileProvider fileProvider, String[] paths) {
        this.parent = parent;
        this.fileProvider = fileProvider;
        this.paths = paths;
    }
    
    public String[] getPaths() {
        return paths;
    }
    
    public String getPathsCollected(String delimiter) {
        return Stream.of(paths).collect(Collectors.joining(delimiter));
    }
    
    public String getPathsCollected(String delimiter, String prefix, String suffix) {
        return Stream.of(paths).collect(Collectors.joining(delimiter, prefix, suffix));
    }
    
    final void reset() {
        resetPathString();
        resetPath();
        resetURI();
        resetURL();
        resetFile();
    }
    
    @Override
    public String getName() {
        if (paths.length == 0) {
            return "";
        }
        return paths[paths.length - 1];
    }
    
    @Override
    public String getPath() {
        if (path == null) {
            generatePath();
        }
        if (parent != null) {
            return parent.getPath() + getSeparatorChar() + path;
        }
        return path;
    }
    
    private final String generatePath() {
        path = Stream.of(paths).collect(Collectors.joining(getSeparator()));
        return path;
    }
    
    final void resetPathString() {
        path = null;
    }
    
    @Override
    public String getAbsolutePath() {
        if (isAbsolute()) { // Absolute path
            return getPath();
        } else {
            return getAbsoluteFile().getPath();
        }
    }
    
    @Override
    public AdvancedFile getAbsoluteFile() {
        if (isAbsolute()) { // Absolute file
            return copy();
        } else if (parent != null) {
            final AdvancedFile file_absolute = copy();
            final AdvancedFile parent_root = file_absolute.getRootParent();
            final AdvancedFile parent_penultimate = file_absolute.getPenultimateParent();
            parent_penultimate.parent = parent_root.getAbsoluteFile(); //TODO Maybe add some update/reset methods, so all children will update their "absolute/relative" state [DONE: or just change the methods "isAbsolute/isRelative", so they check their parent status if they have a parent]
            return file_absolute;
        } else if (isIntern()) { // Relative intern file
            checkAndErrorIfRelativeClassIsNull(true);
            final String[] paths_prefixes = clazz.getPackage().getName().split("\\.");
            final String[] paths_ = new String[paths_prefixes.length + paths.length];
            System.arraycopy(paths_prefixes, 0, paths_, 0, paths_prefixes.length);
            System.arraycopy(paths, 0, paths_, paths_prefixes.length, paths.length);
            return new AdvancedFile(parent, fileProvider, paths_);
        } else { // Relative extern file
            return new AdvancedFile(parent, fileProvider, toFile().getAbsolutePath().split(OSUtil.CURRENT_OS_HELPER.getFileSeparatorRegex()));
            
        }
    }
    
    @Override
    public AdvancedFile getParentFile() {
        if (paths.length <= 1) { //TODO What happened, when the paths.length == 0?
            return parent;
        }
        final String[] paths_ = new String[paths.length - 1];
        System.arraycopy(paths, 0, paths_, 0, paths_.length);
        return new AdvancedFile(parent, null, paths_);
    }
    
    protected AdvancedFile getParent() {
        return parent;
    }
    
    protected AdvancedFile getRootParent() {
        if (parent == null) {
            return this;
        }
        return parent.getRootParent();
    }
    
    protected AdvancedFile getPenultimateParent() {
        if (parent == null) {
            return this;
        } else if (parent.getParent() == null) {
            return this;
        }
        return parent.getPenultimateParent();
    }
    
    @Override
    public String getSeparator() {
        return windowsSeparator ? FILE_SEPARATOR_WINDOWS_STRING : FILE_SEPARATOR_DEFAULT_STRING;
    }
    
    @Override
    public char getSeparatorChar() {
        return windowsSeparator ? FILE_SEPARATOR_WINDOWS_CHAR : FILE_SEPARATOR_DEFAULT_CHAR;
    }
    
    @Override
    public boolean isFile() {
        return false; //TODO Implement
    }
    
    private boolean checkAndErrorIfFile(boolean throwException) {
        if (isFile()) {
            if (throwException) {
                throw new FileIsFileRuntimeException(getPath() + " is a file");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfNotFile(boolean throwException) {
        if (!isFile()) {
            if (throwException) {
                throw new FileIsNotFileRuntimeException(getPath() + " is not a file");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isDirectory() {
        return false; //TODO Implement
    }
    
    private boolean checkAndErrorIfDirectory(boolean throwException) {
        if (isDirectory()) {
            if (throwException) {
                throw new FileIsDirectoryRuntimeException(getPath() + " is a directory");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfNotDirectory(boolean throwException) {
        if (!isDirectory()) {
            if (throwException) {
                throw new FileIsNotDirectoryRuntimeException(getPath() + " is not a directory");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean exists() {
        return false; //TODO Implement
    }
    
    private boolean checkAndErrorIfExisting(boolean throwException) {
        if (exists()) {
            if (throwException) {
                throw new FileIsExistingRuntimeException(getPath() + " does exist");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfNotExisting(boolean throwException) {
        if (!exists()) {
            if (throwException) {
                throw new FileIsNotExistingRuntimeException(getPath() + " does not exist");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isAbsolute() {
        if (parent != null) {
            return parent.isAbsolute();
        }
        return absolute; //TODO Implement, but at Construction
    }
    
    private boolean checkAndErrorIfAbsolute(boolean throwException) {
        if (isAbsolute()) {
            if (throwException) {
                throw new FileIsAbsoluteRuntimeException(getPath() + " is absolute");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfNotAbsolute(boolean throwException) {
        if (!isAbsolute()) {
            if (throwException) {
                throw new FileIsNotAbsoluteRuntimeException(getPath() + " is not absolute");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isRelative() {
        if (parent != null) {
            return parent.isRelative();
        }
        return !absolute; //TODO Implement, but at Construction
    }
    
    private boolean checkAndErrorIfRelative(boolean throwException) {
        if (isRelative()) {
            if (throwException) {
                throw new FileIsRelativeRuntimeException(getPath() + " is relative");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfNotRelative(boolean throwException) {
        if (!isRelative()) {
            if (throwException) {
                throw new FileIsNotRelativeRuntimeException(getPath() + " is not relative");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isIntern() {
        if (parent != null) {
            return parent.isIntern();
        }
        return !extern; //TODO Implement, but at Construction
    }
    
    private boolean checkAndErrorIfIntern(boolean throwException) {
        if (isIntern()) {
            if (throwException) {
                throw new FileIsInternRuntimeException(getPath() + " is intern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfNotIntern(boolean throwException) {
        if (!isIntern()) {
            if (throwException) {
                throw new FileIsNotInternRuntimeException(getPath() + " is not intern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isExtern() {
        if (parent != null) {
            return parent.isExtern();
        }
        return extern; //TODO Implement, but at Construction
    }
    
    private boolean checkAndErrorIfExtern(boolean throwException) {
        if (isExtern()) {
            if (throwException) {
                throw new FileIsExternRuntimeException(getPath() + " is extern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfNotExtern(boolean throwException) {
        if (!isExtern()) {
            if (throwException) {
                throw new FileIsNotExternRuntimeException(getPath() + " is not extern");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public Path toPath() throws Exception {
        if (path_ == null) {
            path_ = toFile().toPath();
        }
        return path_;
    }
    
    final void resetPath() {
        path_ = null;
    }
    
    @Override
    public URI toURI() throws Exception {
        if (uri == null) {
            if (isIntern()) {
                uri = getNonNullClazz().getResource(getPath()).toURI();
            } else {
                uri = toFile().toURI();
            }
        }
        return uri;
    }
    
    final void resetURI() {
        uri = null;
    }
    
    @Override
    public URL toURL() throws Exception {
        if (url == null) {
            if (isIntern()) {
                url = getNonNullClazz().getResource(getPath());
            } else {
                url = toURI().toURL();
            }
        }
        return url;
    }
    
    final void resetURL() {
        url = null;
    }
    
    @Override
    public File toFile() throws FileRuntimeException {
        checkAndErrorIfIntern(true);
        if (file == null) {
            file = new File(getPath());
        }
        return file;
    }
    
    final void resetFile() {
        file = null;
        resetPath();
        if (isExtern()) {
            resetURI();
            resetURL();
        }
    }
    
    @Override
    public boolean mkdir() throws FileIsNotRuntimeException {
        checkAndErrorIfIntern(true);
        return false; //TODO Implement
    }
    
    @Override
    public boolean mkdirs() throws FileIsNotRuntimeException {
        checkAndErrorIfIntern(true);
        return false; //TODO Implement
    }
    
    @Override
    public boolean delete() throws FileRuntimeException {
        checkAndErrorIfIntern(true);
        //checkAndErrorIfNotExisting();
        return false; //TODO Implement
    }
    
    @Override
    public boolean createNewFile() throws FileIsNotRuntimeException {
        checkAndErrorIfIntern(true);
        checkAndErrorIfDirectory(checkAndErrorIfExisting(false));
        return false; //TODO Implement
    }
    
    @Override
    public byte[] readBytes() throws FileRuntimeException {
        checkAndErrorIfNotExisting(true);
        checkAndErrorIfNotFile(true);
        return new byte[0]; //TODO Implement
    }
    
    @Override
    public boolean writeBytes(byte[] data) throws FileRuntimeException {
        checkAndErrorIfIntern(true);
        checkAndErrorIfDirectory(checkAndErrorIfExisting(false));
        return false; //TODO Implement
    }
    
    @Override
    public List<IFile> listFiles(boolean recursive) throws FileRuntimeException {
        checkAndErrorIfNotExisting(true);
        checkAndErrorIfNotDirectory(true);
        return null; //TODO Implement
    }
    
    @Override
    public AdvancedFile copy() {
        return new AdvancedFile(parent, fileProvider, paths);
    }
    
    @Override
    public String toString() {
        return getPath();
    }
    
    public final Class<?> getClazz() {
        return clazz;
    }
    
    public final AdvancedFile setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }
    
    public final Class<?> getNonNullClazz() {
        if (clazz == null) {
            return getClass();
        }
        return clazz;
    }
    
    private boolean checkAndErrorIfRelativeClassIsNull(boolean throwException) {
        if (clazz == null) {
            if (throwException) {
                throw new RelativeClassIsNullException(getPath() + " has no relative class");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    private boolean checkAndErrorIfRelativeClassIsNotNull(boolean throwException) {
        if (clazz != null) {
            if (throwException) {
                throw new RelativeClassIsNotNullException(getPath() + " has an relative class");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
}