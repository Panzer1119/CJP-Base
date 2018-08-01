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

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdvancedFile {
    
    public static final List<AdvancedProvider> PROVIDERS = new ArrayList<>();
    public static final ZIPProvider ZIP_PROVIDER = new ZIPProvider();
    
    static {
        PROVIDERS.add(ZIP_PROVIDER);
    }
    
    private final String separator = "/"; //TODO Change this
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
        this(parent, null, paths);
        init();
    }
    
    public AdvancedFile(AdvancedFile parent, AdvancedProvider provider, String... paths) {
        this.parent = parent;
        this.provider = provider;
        this.paths = paths;
    }
    
    public static final AdvancedProvider getProvider(AdvancedFile parent, String name) {
        Objects.requireNonNull(name);
        return PROVIDERS.stream().filter((advancedProvider) -> advancedProvider.accept(parent, name)).findFirst().orElse(null);
    }
    
    private final void init() {
        final List<String> paths_ = new ArrayList<>();
        for (String p : paths) {
            if (p.contains(separator)) {
                final String[] split = p.split(separator);
                for (String p_ : split) {
                    paths_.add(p_);
                }
            } else {
                paths_.add(p);
            }
        }
        //paths = paths_.toArray(new String[0]);
        //AdvancedFile advancedFile = null;
        //for (String p : paths) {
        
        final List<String> temp = new ArrayList<>();
        for (String p : paths_) {
            temp.add(p);
            final AdvancedProvider advancedProvider = getProvider(parent, p);
            //System.out.println("p = " + p);
            if (advancedProvider != null) {
                //System.out.println("Found provider: " + advancedProvider);
                parent = new AdvancedFile(parent, advancedProvider, temp.toArray(new String[0]));
                temp.clear();
            }
        }
        paths = temp.toArray(new String[0]);
        
        /*
        final Iterator<String> iterator = paths_.iterator();
        while (iterator.hasNext()) {
            final String p = iterator.next();
            final AdvancedProvider advancedProvider = getProvider(advancedFile, p);
            System.out.println("p = " + p);
            if (advancedProvider != null) {
                System.out.println("Found provider: " + advancedProvider);
                parent = new AdvancedFile(parent, advancedProvider, p);
                iterator.remove();
            }
            /*
            if (advancedProvider != null || advancedFile == null) {
                System.out.println("Found provider: " + advancedProvider);
                advancedFile = new AdvancedFile(advancedFile, advancedProvider, p);
            } else {
                advancedFile.paths = Arrays.copyOf(advancedFile.paths, advancedFile.paths.length + 1);
                advancedFile.paths[advancedFile.paths.length - 1] = p;
            }
            
        }
        paths = paths_.toArray(new String[0]);
        */
    }
    
    public final String[] getPaths() {
        return paths;
    }
    
    public final String getPathString() {
        if (path == null) {
            path = Arrays.asList(paths).stream().collect(Collectors.joining(separator));
        }
        if (parent != null) {
            path = parent.getPathString() + separator + path;
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
        return "AdvancedFile{" + "paths=" + Arrays.toString(paths) + ", parent=" + parent + ", provider=" + provider + ", path='" + path + '\'' + '}';
    }
    
}
