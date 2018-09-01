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

import de.codemakers.base.exceptions.CJPRuntimeException;
import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.base.util.Returner;
import de.codemakers.io.file.t3.AdvancedFile;
import de.codemakers.io.file.t3.AdvancedFileFilter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoteProvider extends FileProvider<AdvancedFile> {
    
    public static final String PREFIX_REMOTE = "remote:";
    public static final String REMOTE_REGEX = PREFIX_REMOTE + "(?:(\\w+)@)?([A-Za-z0-9_\\-\\.]+)(?::(\\d{1,5}))?:(.*)";
    public static final Pattern REMOTE_PATTERN = Pattern.compile(REMOTE_REGEX);
    
    //TODO Is this possible? Because just sending an FileProvider Object over the internet does not manage the Input/OutputStreams which would be necessary to send (encrypted!) data between the remote and this client
    //TODO Maybe move this class into de.codemakers.io.file.providers package, but in the CJP-Net library
    private FileProvider<AdvancedFile> fileFileProvider_remote = null;
    private String remote = null;
    private int port = -1;
    private String username = null;
    private byte[] password = null;
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, InputStream inputStream) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.listFiles(parent, file, recursive, inputStream);
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, AdvancedFileFilter advancedFileFilter, InputStream inputStream) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.listFiles(parent, file, recursive, advancedFileFilter, inputStream);
    }
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.isFile(parent, file, inputStream);
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.isDirectory(parent, file, inputStream);
    }
    
    @Override
    public boolean exists(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.exists(parent, file, inputStream);
    }
    
    @Override
    public InputStream createInputStream(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.createInputStream(parent, file, inputStream);
    }
    
    @Override
    public byte[] readBytes(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.readBytes(parent, file, inputStream);
    }
    
    @Override
    public OutputStream createOutputStream(AdvancedFile parent, AdvancedFile file, boolean append) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.createOutputStream(parent, file, append);
    }
    
    @Override
    public boolean writeBytes(AdvancedFile parent, AdvancedFile file, byte[] data) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.writeBytes(parent, file, data);
    }
    
    @Override
    public boolean createNewFile(AdvancedFile parent, AdvancedFile file) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.createNewFile(parent, file);
    }
    
    @Override
    public boolean delete(AdvancedFile parent, AdvancedFile file) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.delete(parent, file);
    }
    
    @Override
    public boolean mkdir(AdvancedFile parent, AdvancedFile file) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.mkdir(parent, file);
    }
    
    @Override
    public boolean mkdirs(AdvancedFile parent, AdvancedFile file) throws Exception {
        checkAndErrorIfNotConnected();
        return fileFileProvider_remote.mkdirs(parent, file);
    }
    
    @Override
    public boolean test(AdvancedFile parent, String name) {
        return parent == null && (name != null && name.startsWith(PREFIX_REMOTE));
    }
    
    @Override
    public void processPaths(AdvancedFile parent, String path, List<String> paths) {
        if (paths.size() >= 1) {
            final Matcher matcher = REMOTE_PATTERN.matcher(paths.get(0));
            if (matcher.matches()) {
                paths.set(0, processRemote(matcher));
            }
        }
    }
    
    public final String processRemote(Matcher matcher) {
        if (matcher.group(1) != null) {
            setUsername(matcher.group(1));
        }
        if (matcher.group(2) != null) {
            setRemote(matcher.group(2));
        }
        if (matcher.group(3) != null) {
            setPort(Integer.parseInt(matcher.group(3)));
        }
        return Returner.of(matcher.group(4)).or("");
    }
    
    public final void checkAndErrorIfNotConnected() {
        if (!isConnected()) {
            throw new CJPRuntimeException(this + " is not connected to a remote");
        }
    }
    
    public final boolean isConnected() {
        if (fileFileProvider_remote == null) {
            return false;
        }
        //TODO Implement
        throw new NotYetImplementedRuntimeException();
    }
    
    public final String getRemote() {
        return remote;
    }
    
    public final RemoteProvider setRemote(String remote) {
        this.remote = remote;
        return this;
    }
    
    public final int getPort() {
        return port;
    }
    
    public final RemoteProvider setPort(int port) {
        this.port = port;
        return this;
    }
    
    public final String getUsername() {
        return username;
    }
    
    public final RemoteProvider setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public final byte[] getPassword() {
        return password;
    }
    
    public final RemoteProvider setPassword(byte[] password) {
        this.password = password;
        return this;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "fileFileProvider_remote=" + fileFileProvider_remote + ", remote='" + remote + '\'' + ", port=" + port + ", username='" + username + '\'' + ", password=" + Arrays.toString(password) + '}';
    }
    
}
