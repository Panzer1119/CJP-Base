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
import de.codemakers.io.file.t3.exceptions.FileException;
import de.codemakers.io.file.t3.exceptions.isnot.*;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    protected String[] paths;
    private boolean windowsSeparator = true;
    private boolean extern = true;
    private boolean absolute = true;
    private AdvancedFile parent;
    private FileProvider fileProvider;
    private String path;
    
    public AdvancedFile(String... paths) {
        this.paths = paths;
    }
    
    protected AdvancedFile(AdvancedFile parent, FileProvider fileProvider, String[] paths) {
        this.parent = parent;
        this.fileProvider = fileProvider;
        this.paths = paths;
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
    
    @Override
    public String getAbsolutePath() {
        return null;
    }
    
    @Override
    public AdvancedFile getAbsoluteFile() {
        return null;
    }
    
    @Override
    public AdvancedFile getParentFile() {
        return null;
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
        return false;
    }
    
    private void checkForFile() {
        if (!isFile()) {
            throw new FileIsNotAFileException(getPath() + " is not a file");
        }
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
    
    private void checkForDirectory() {
        if (!isDirectory()) {
            throw new FileIsNotADirectoryException(getPath() + " is not a directory");
        }
    }
    
    @Override
    public boolean exists() {
        return false;
    }
    
    private void checkForExistance() {
        if (!exists()) {
            throw new FileIsNotExistingException(getPath() + " does not exist");
        }
    }
    
    @Override
    public boolean isAbsolute() {
        return absolute;
    }
    
    private void checkForAbsolute() {
        if (!isAbsolute()) {
            throw new FileIsNotAbsoluteException(getPath() + " is not absolute");
        }
    }
    
    @Override
    public boolean isRelative() {
        return !absolute;
    }
    
    private void checkForRelative() {
        if (!isRelative()) {
            throw new FileIsNotRelativeException(getPath() + " is not relative");
        }
    }
    
    @Override
    public boolean isIntern() {
        return !extern;
    }
    
    private void checkForIntern() {
        if (!isIntern()) {
            throw new FileIsNotInternException(getPath() + " is not intern");
        }
    }
    
    @Override
    public boolean isExtern() {
        return extern;
    }
    
    private void checkForExtern() {
        if (!isExtern()) {
            throw new FileIsNotExternException(getPath() + " is not extern");
        }
    }
    
    @Override
    public Path toPath() throws Exception {
        return null;
    }
    
    @Override
    public URI toURI() throws Exception {
        return null;
    }
    
    @Override
    public URL toURL() throws Exception {
        return null;
    }
    
    @Override
    public boolean mkdir() throws FileIsNotException {
        return false;
    }
    
    @Override
    public boolean mkdirs() throws FileIsNotException {
        return false;
    }
    
    @Override
    public boolean delete() throws FileException {
        return false;
    }
    
    @Override
    public boolean createNewFile() throws FileIsNotException {
        return false;
    }
    
    @Override
    public byte[] readBytes() throws FileException {
        checkForExistance();
        checkForFile();
        return new byte[0];
    }
    
    @Override
    public boolean writeBytes(byte[] data) throws FileException {
        return false;
    }
    
    @Override
    public List<IFile> listFiles(boolean recursive) throws FileException {
        checkForExistance();
        checkForDirectory();
        return null;
    }
    
}
