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

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class TestAdvancedFile {
    
    public static final List<AdvancedProvider> PROVIDERS = new ArrayList<>();
    
    static {
        PROVIDERS.add(new AdvancedProvider() {
            @Override
            public List<TestAdvancedFile> listFiles(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath) {
                throw new UnsupportedOperationException("Coming soon TM");
            }
            
            @Override
            public byte[] readBytes(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath, byte[] data_parent) {
                if (data_parent == null || data_parent.length == 0) {
                    try {
                        final ZipFile zipFile = new ZipFile(parent.getPathString());
                        final byte[] data = IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry(Arrays.asList(subPath).stream().collect(Collectors.joining(File.separator)))));
                        zipFile.close();
                        return data;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        final String subPath_ = Arrays.asList(subPath).stream().collect(Collectors.joining(File.separator));
                        final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data_parent));
                        ZipEntry zipEntry = null;
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                            if (zipEntry.getName().equals(subPath_)) {
                                break;
                            }
                        }
                        final byte[] data = IOUtils.toByteArray(zipInputStream);
                        zipInputStream.close();
                        return data;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }
            }
    
            @Override
            public boolean writeBytes(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath, byte[] data) {
                throw new UnsupportedOperationException("Coming soon TM");
            }
            
            @Override
            public boolean createFile(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath) {
                throw new UnsupportedOperationException("Coming soon TM");
            }
            
            @Override
            public boolean deleteFile(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath) {
                throw new UnsupportedOperationException("Coming soon TM");
            }
            
            @Override
            public boolean mkdir(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath) {
                throw new UnsupportedOperationException("Coming soon TM");
            }
            
            @Override
            public boolean accept(TestAdvancedFile parent, String name, String name_lower, String name_upper) {
                return name_lower.endsWith(".zip") || name_upper.endsWith(".ZIP");
            }
        });
    }
    
    private final String separator = "/"; //TODO Change this
    private String[] paths = new String[0];
    private TestAdvancedFile parent;
    private AdvancedProvider provider = null;
    private String path = null;
    
    public TestAdvancedFile(String... paths) {
        this(null, paths);
    }
    
    public TestAdvancedFile(TestAdvancedFile parent, String... paths) {
        this(parent, null, paths);
        init();
    }
    
    public TestAdvancedFile(TestAdvancedFile parent, AdvancedProvider provider, String... paths) {
        this.parent = parent;
        this.provider = provider;
        this.paths = paths;
    }
    
    public static final AdvancedProvider getProvider(TestAdvancedFile parent, String name) {
        Objects.requireNonNull(name);
        final String name_lower = name.toLowerCase();
        final String name_upper = name.toUpperCase();
        return PROVIDERS.stream().filter((advancedProvider) -> advancedProvider.accept(parent, name, name_lower, name_upper)).findFirst().orElse(null);
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
        //TestAdvancedFile advancedFile = null;
        //for (String p : paths) {
        
        final List<String> temp = new ArrayList<>();
        for (String p : paths_) {
            temp.add(p);
            final AdvancedProvider advancedProvider = getProvider(parent, p);
            System.out.println("p = " + p);
            if (advancedProvider != null) {
                System.out.println("Found provider: " + advancedProvider);
                parent = new TestAdvancedFile(parent, advancedProvider, temp.toArray(new String[0]));
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
                parent = new TestAdvancedFile(parent, advancedProvider, p);
                iterator.remove();
            }
            /*
            if (advancedProvider != null || advancedFile == null) {
                System.out.println("Found provider: " + advancedProvider);
                advancedFile = new TestAdvancedFile(advancedFile, advancedProvider, p);
            } else {
                advancedFile.paths = Arrays.copyOf(advancedFile.paths, advancedFile.paths.length + 1);
                advancedFile.paths[advancedFile.paths.length - 1] = p;
            }
            
        }
        paths = paths_.toArray(new String[0]);
        */
    }
    
    public final String getPathString() {
        if (path == null) {
            path = Arrays.asList(paths).stream().collect(Collectors.joining(separator));
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
    
    public final byte[] readBytes(TestAdvancedFile advancedFile) throws Exception {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(advancedFile);
        if (parent != null) {
            return provider.readBytes(this, advancedFile, advancedFile.paths, readBytes());
        } else {
            return provider.readBytes(this, advancedFile, advancedFile.paths);
        }
    }
    
    @Override
    public final String toString() {
        return "TestAdvancedFile{" + "paths=" + Arrays.toString(paths) + ", parent=" + parent + ", provider=" + provider + ", path='" + path + '\'' + '}';
    }
    
}
