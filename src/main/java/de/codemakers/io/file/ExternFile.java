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

package de.codemakers.io.file;

import de.codemakers.base.util.interfaces.Convertable;
import de.codemakers.security.interfaces.Cryptor;
import de.codemakers.security.interfaces.Signer;
import de.codemakers.security.interfaces.Verifier;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExternFile extends IFile<ExternFile, ExternFileFilter> implements Convertable<AdvancedFile> {
    
    private File file;
    
    public ExternFile(File file) {
        if (file == null) {
            file = new File("");
        }
        this.file = file;
    }
    
    public ExternFile(String path) {
        this(new File(path));
    }
    
    static void listFilesRecursive(File folder, ExternFileFilter externFileFilter, List<ExternFile> externFiles) {
        if (externFileFilter != null) {
            Stream.of(folder.listFiles()).map((file) -> {
                if (file.isDirectory()) {
                    listFilesRecursive(file, externFileFilter, externFiles);
                }
                return file;
            }).map(ExternFile::new).filter(externFileFilter).forEach(externFiles::add);
        } else {
            Stream.of(folder.listFiles()).map((file) -> {
                if (file.isDirectory()) {
                    listFilesRecursive(file, externFileFilter, externFiles);
                }
                return file;
            }).map(ExternFile::new).forEach(externFiles::add);
        }
    }
    
    @Override
    public String getName() {
        return file.getName();
    }
    
    @Override
    public String getPath() {
        return file.getPath();
    }
    
    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }
    
    @Override
    public ExternFile getAbsoluteFile() {
        return new ExternFile(file.getAbsoluteFile());
    }
    
    @Override
    public ExternFile getParentFile() {
        return new ExternFile(file.getParentFile());
    }
    
    @Override
    public ExternFile getRoot() {
        String path_root = getAbsolutePath();
        final int index = path_root.indexOf(getSeparator());
        if (index >= 0) {
            path_root = path_root.substring(0, index);
        }
        return new ExternFile(path_root);
    }
    
    @Override
    public String getSeparator() {
        return File.separator;
    }
    
    @Override
    public char getSeparatorChar() {
        return File.separatorChar;
    }
    
    @Override
    public boolean isFile() {
        return file.isFile();
    }
    
    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }
    
    @Override
    public boolean exists() {
        return file.exists();
    }
    
    @Override
    public boolean isAbsolute() {
        return file.isAbsolute();
    }
    
    @Override
    public boolean isRelative() {
        return !isAbsolute();
    }
    
    @Override
    public final boolean isIntern() {
        return false;
    }
    
    @Override
    public final boolean isExtern() {
        return true;
    }
    
    @Override
    public Path toPath() {
        return file.toPath();
    }
    
    @Override
    public URI toURI() {
        return file.toURI();
    }
    
    @Override
    public URL toURL() throws Exception {
        //return file.toURL();
        return toURI().toURL();
    }
    
    @Override
    public File toFile() {
        return file;
    }
    
    @Override
    public boolean mkdir() {
        checkAndErrorIfFile(checkAndErrorIfExisting(false));
        return file.mkdir();
    }
    
    @Override
    public boolean mkdirs() {
        checkAndErrorIfFile(checkAndErrorIfExisting(false));
        return file.mkdirs();
    }
    
    @Override
    public boolean delete() {
        return file.delete();
    }
    
    @Override
    public boolean createNewFile() throws Exception {
        checkAndErrorIfDirectory(checkAndErrorIfExisting(false));
        return file.createNewFile();
    }
    
    @Override
    public InputStream createInputStream() throws Exception {
        checkAndErrorIfNotExisting(true);
        checkAndErrorIfNotFile(true);
        return new FileInputStream(file);
    }
    
    @Override
    public byte[] readBytes() throws Exception {
        checkAndErrorIfNotExisting(true);
        checkAndErrorIfNotFile(true);
        return Files.readAllBytes(toPath());
    }
    
    @Override
    public OutputStream createOutputStream(boolean append) throws Exception {
        checkAndErrorIfDirectory(checkAndErrorIfExisting(false));
        return new FileOutputStream(file, append);
    }
    
    @Override
    public boolean writeBytes(byte[] data) throws Exception {
        checkAndErrorIfDirectory(checkAndErrorIfExisting(false));
        Files.write(toPath(), data);
        return true;
    }
    
    @Override
    public List<ExternFile> listFiles(boolean recursive) {
        checkAndErrorIfNotExisting(true);
        checkAndErrorIfNotDirectory(true);
        if (recursive) {
            final List<ExternFile> externFiles = new ArrayList<>();
            listFilesRecursive(file, null, externFiles);
            return externFiles;
        } else {
            return Stream.of(file.listFiles()).map(ExternFile::new).collect(Collectors.toList());
        }
    }
    
    @Override
    public List<ExternFile> listFiles(boolean recursive, ExternFileFilter externFileFilter) {
        if (externFileFilter == null) {
            return listFiles(recursive);
        }
        checkAndErrorIfNotExisting(true);
        checkAndErrorIfNotDirectory(true);
        if (recursive) {
            final List<ExternFile> externFiles = new ArrayList<>();
            listFilesRecursive(file, externFileFilter, externFiles);
            return externFiles;
        } else {
            return Stream.of(file.listFiles()).map(ExternFile::new).filter(externFileFilter).collect(Collectors.toList());
        }
    }
    
    @Override
    public AdvancedFile convert(Class<AdvancedFile> clazz) {
        return new AdvancedFile(file);
    }
    
    @Override
    public byte[] crypt(Cryptor cryptor) throws Exception {
        Objects.requireNonNull(cryptor);
        return cryptor.crypt(readBytes());
    }
    
    @Override
    public byte[] sign(Signer signer) throws Exception {
        Objects.requireNonNull(signer);
        return signer.sign(readBytes());
    }
    
    @Override
    public boolean verify(Verifier verifier, byte[] data_signature) throws Exception {
        Objects.requireNonNull(verifier);
        Objects.requireNonNull(data_signature);
        return verifier.verify(readBytes(), data_signature);
    }
    
}
