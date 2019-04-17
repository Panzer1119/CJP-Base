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
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.listeners.AdvancedFileChangeListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedFileMonitor extends AbstractMonitor implements AdvancedFileChangeListener {
    
    protected static final Map<String, byte[]> HASHES = new ConcurrentHashMap<>();
    
    protected final List<AdvancedFileChangeListener> advancedFileChangeListeners = new ArrayList<>();
    protected Hasher hasher;
    protected AdvancedFile root;
    protected boolean recursive = true;
    
    public AdvancedFileMonitor(AdvancedFile root) {
        this(root, HashUtil.createHasher64XX());
    }
    
    public AdvancedFileMonitor(AdvancedFile root, Hasher hasher) {
        this.root = Objects.requireNonNull(root, "root");
        this.hasher = Objects.requireNonNull(hasher, "hasher");
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
    
    public Hasher getHasher() {
        return hasher;
    }
    
    public AdvancedFileMonitor setHasher(Hasher hasher) {
        this.hasher = hasher;
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
    
}
