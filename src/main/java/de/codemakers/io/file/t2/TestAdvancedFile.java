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
            public List<TestAdvancedFile> listFiles(TestAdvancedFile parent, List<TestAdvancedFile> advancedFiles, TestAdvancedFile advancedFile, String[] subPath, boolean recursive, byte... data_parent) {
                if (data_parent == null || data_parent.length == 0) {
                    try {
                        final ZipFile zipFile = new ZipFile(parent.getPathString());
                        zipFile.stream().forEach((zipEntry) -> {
                            final TestAdvancedFile advancedFile_ = new TestAdvancedFile(zipEntry.getName(), parent.paths);
                            System.out.println("===> " + zipEntry);
                            System.out.println("#==> " + advancedFile_);
                            advancedFiles.add(advancedFile_);
                        });
                        zipFile.close();
                        //
                        /*
                        final ZipFile zipFile1 = new ZipFile(parent.getPathString());
                        final Enumeration<? extends ZipEntry> zipEntryEnumeration = zipFile1.entries();
                        while (zipEntryEnumeration.hasMoreElements()) {
                            final ZipEntry zipEntry = zipEntryEnumeration.nextElement();
                            System.out.println("======> " + zipEntry + " == " + zipEntry.getName());
                        }
                        zipFile1.close();
                        */
                        //
                        /*
                        final ZipFile zipFile2 = new ZipFile(parent.getPathString());
                        zipFile2.stream().forEach((zipEntry) -> System.out.println("#=====> " + zipEntry + " == " + zipEntry.getName()));
                        zipFile2.close();
                        */
                        return advancedFiles;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data_parent));
                        ZipEntry zipEntry = null;
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                            final TestAdvancedFile advancedFile_ = new TestAdvancedFile(parent, zipEntry.getName());
                            System.out.println("*===> " + zipEntry);
                            System.out.println("*#==> " + advancedFile_);
                            advancedFiles.add(advancedFile_);
                        }
                        zipInputStream.close();
                        return advancedFiles;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }
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
    private TestAdvancedFile parent = null;
    private AdvancedProvider provider = null;
    private String path = null;
    
    public TestAdvancedFile(String... paths) {
        this((TestAdvancedFile) null, paths);
    }
    
    public TestAdvancedFile(String name, String[] paths) {
        this.paths = Arrays.copyOf(paths, paths.length + 1);
        this.paths[this.paths.length - 1] = name;
        init();
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
            //System.out.println("p = " + p);
            if (advancedProvider != null) {
                //System.out.println("Found provider: " + advancedProvider);
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
    
    public final byte[] readBytes(TestAdvancedFile advancedFile) throws Exception {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(advancedFile);
        if (parent != null) {
            return provider.readBytes(this, advancedFile, advancedFile.paths, readBytes());
        } else {
            return provider.readBytes(this, advancedFile, advancedFile.paths);
        }
    }
    
    public final List<TestAdvancedFile> listFiles() throws Exception {
        return listFiles(false);
    }
    
    public final List<TestAdvancedFile> listFiles(boolean recursive) throws Exception {
        return listFiles(new ArrayList<>(), recursive);
    }
    
    public final List<TestAdvancedFile> listFiles(List<TestAdvancedFile> advancedFiles, boolean recursive) throws Exception {
        if (parent != null) {
            return parent.listFiles(advancedFiles, recursive, this);
        } else {
            final File directory = new File(getPathString());
            for (File file : directory.listFiles()) {
                final TestAdvancedFile advancedFile = new TestAdvancedFile(file.getName(), paths);
                advancedFiles.add(advancedFile);
                if (recursive && file.isDirectory()) {
                    advancedFile.listFiles(advancedFiles, recursive);
                }
            }
            return advancedFiles;
        }
    }
    
    public final List<TestAdvancedFile> listFiles(List<TestAdvancedFile> advancedFiles, boolean recursive, TestAdvancedFile advancedFile) throws Exception {
        Objects.requireNonNull(advancedFiles);
        Objects.requireNonNull(provider);
        Objects.requireNonNull(advancedFile);
        if (parent != null) {
            return provider.listFiles(this, advancedFiles, advancedFile, advancedFile.paths, recursive, readBytes());
        } else {
            return provider.listFiles(this, advancedFiles, advancedFile, advancedFile.paths, recursive);
        }
    }
    
    @Override
    public final String toString() {
        return "TestAdvancedFile{" + "paths=" + Arrays.toString(paths) + ", parent=" + parent + ", provider=" + provider + ", path='" + path + '\'' + '}';
    }
    
}
