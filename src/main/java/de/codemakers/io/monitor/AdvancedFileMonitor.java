/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
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

package de.codemakers.io.monitor;

import de.codemakers.base.util.HashUtil;
import de.codemakers.base.util.interfaces.Hasher;
import de.codemakers.base.util.monitor.AbstractMonitor;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.IOUtil;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.listeners.AdvancedFileChangeListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedFileMonitor extends AbstractMonitor implements AdvancedFileChangeListener {
    
    protected static final ToughSupplier<Hasher> DEFAULT_HASHER_TOUGH_SUPPLIER = () -> HashUtil.createHasher64XX();
    protected static final Map<String, byte[]> HASHES = new ConcurrentHashMap<>();
    
    protected final Map<String, byte[]> hashes;
    protected final List<AdvancedFileChangeListener> advancedFileChangeListeners = new ArrayList<>();
    protected ToughSupplier<Hasher> hasherToughSupplier;
    protected AdvancedFile root;
    protected boolean recursive = true;
    
    public AdvancedFileMonitor(AdvancedFile root) {
        this(root, DEFAULT_HASHER_TOUGH_SUPPLIER);
    }
    
    public AdvancedFileMonitor(AdvancedFile root, ToughSupplier<Hasher> hasherToughSupplier) {
        this.hashes = (hasherToughSupplier == DEFAULT_HASHER_TOUGH_SUPPLIER) ? HASHES : new ConcurrentHashMap<>();
        this.root = Objects.requireNonNull(root, "root");
        this.hasherToughSupplier = Objects.requireNonNull(hasherToughSupplier, "hasherToughSupplier");
    }
    
    public Map<String, byte[]> getHashes() {
        return hashes;
    }
    
    public boolean addAdvancedFileChangeListeners(AdvancedFileChangeListener... advancedFileChangeListeners) {
        return this.advancedFileChangeListeners.addAll(Arrays.asList(advancedFileChangeListeners));
    }
    
    public boolean removeAdvancedFileChangeListeners(AdvancedFileChangeListener... advancedFileChangeListeners) {
        return this.advancedFileChangeListeners.removeAll(Arrays.asList(advancedFileChangeListeners));
    }
    
    public List<AdvancedFileChangeListener> getAdvancedFileChangeListeners() {
        return advancedFileChangeListeners;
    }
    
    public ToughSupplier<Hasher> getHasherToughSupplier() {
        return hasherToughSupplier;
    }
    
    public AdvancedFileMonitor setHasherToughSupplier(ToughSupplier<Hasher> hasherToughSupplier) {
        this.hasherToughSupplier = hasherToughSupplier;
        return this;
    }
    
    public AdvancedFile getRoot() {
        return root;
    }
    
    private AdvancedFileMonitor setRoot(AdvancedFile root) { //TODO Make it public accessible?
        this.root = root;
        return this;
    }
    
    public boolean isRecursive() {
        return recursive;
    }
    
    public AdvancedFileMonitor setRecursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }
    
    @Override
    public boolean start() throws Exception {
        return false;
    }
    
    @Override
    public boolean stop() throws Exception {
        return false;
    }
    
    @Override
    public void onFileCreated(AdvancedFile file) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onFileCreated(file));
    }
    
    @Override
    public void onFileModified(AdvancedFile file) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onFileModified(file));
    }
    
    @Override
    public void onFileDeleted(AdvancedFile file) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onFileDeleted(file));
    }
    
    @Override
    public void onFileRenamed(AdvancedFile file) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onFileRenamed(file));
    }
    
    @Override
    public void onDirectoryCreated(AdvancedFile directory) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onDirectoryCreated(directory));
    }
    
    @Override
    public void onDirectoryModified(AdvancedFile directory) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onDirectoryModified(directory));
    }
    
    @Override
    public void onDirectoryDeleted(AdvancedFile directory) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onDirectoryDeleted(directory));
    }
    
    @Override
    public void onDirectoryRenamed(AdvancedFile directory) {
        advancedFileChangeListeners.forEach((advancedFileChangeListener) -> advancedFileChangeListener.onDirectoryRenamed(directory));
    }
    
    @Override
    public String toString() {
        return "AdvancedFileMonitor{" + "period=" + period + '}';
    }
    
    public byte[] generateHashForAdvancedFile(AdvancedFile advancedFile) {
        return generateHashForAdvancedFile(advancedFile, recursive, hasherToughSupplier, hashes);
    }
    
    public static byte[] generateHashForAdvancedFile(AdvancedFile advancedFile, boolean recursive, ToughSupplier<Hasher> hasherToughSupplier, Map<String, byte[]> hashes) {
        if (advancedFile.isFile()) {
            return generateHashForFile(advancedFile, hasherToughSupplier, hashes);
        } else {
            return generateHashForDirectory(advancedFile, recursive, hasherToughSupplier, hashes);
        }
    }
    
    public static byte[] generateHashForFile(AdvancedFile file, ToughSupplier<Hasher> hasherToughSupplier, Map<String, byte[]> hashes) {
        final byte[] hash = file.hashWithoutException(hasherToughSupplier.getWithoutException());
        hashes.put(file.toExactString(), hash);
        return hash;
    }
    
    public static byte[] generateHashForDirectory(AdvancedFile directory, boolean recursive, ToughSupplier<Hasher> hasherToughSupplier, Map<String, byte[]> hashes) {
        final Hasher hasher = hasherToughSupplier.getWithoutException();
        final byte[] hash = new byte[hasher.getHashLength()];
        for (AdvancedFile advancedFile : directory.listFiles(false)) {
            if (advancedFile.isFile()) {
                IOUtil.loadInputStreamToHasher(advancedFile.createInputStreamWithoutException(), hasher);
            } else {
                hasher.updateWithoutException(advancedFile.getName().getBytes());
                if (recursive) {
                    final byte[] temp = generateHashForDirectory(advancedFile, true, hasherToughSupplier, hashes);
                    hasher.updateWithoutException(temp);
                }
            }
        }
        System.arraycopy(hasher.hashWithoutException(), 0, hash, 0, hash.length);
        hashes.put(directory.toExactString(), hash);
        return hash;
    }
    
}
