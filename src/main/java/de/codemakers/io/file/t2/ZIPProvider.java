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

import de.codemakers.base.logger.Logger;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZIPProvider extends AdvancedProvider {
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath, List<AdvancedFile> advancedFiles, boolean recursive, byte... data_parent) {
        if (data_parent == null || data_parent.length == 0) {
            try {
                final ZipFile zipFile = new ZipFile(parent.getPathString());
                if (recursive) {
                    zipFile.stream().forEach((zipEntry) -> advancedFiles.add(new AdvancedFile(zipEntry.getName(), parent.getPaths())));
                } else {
                    zipFile.stream().filter((zipEntry) -> !zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR)).forEach((zipEntry) -> advancedFiles.add(new AdvancedFile(zipEntry.getName(), parent.getPaths())));
                }
                zipFile.close();
                return advancedFiles;
            } catch (Exception ex) {
                Logger.handleError(ex);
                return null;
            }
        } else {
            try {
                final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data_parent));
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (!recursive && zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR)) {
                        continue;
                    }
                    advancedFiles.add(new AdvancedFile(parent, zipEntry.getName()));
                }
                zipInputStream.close();
                return advancedFiles;
            } catch (Exception ex) {
                Logger.handleError(ex);
                return null;
            }
        }
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath, List<AdvancedFile> advancedFiles, AdvancedFileFilter fileFilter, boolean recursive, byte... data_parent) {
        if (fileFilter == null) {
            return listFiles(parent, advancedFile, subPath, advancedFiles, recursive, data_parent);
        }
        if (data_parent == null || data_parent.length == 0) {
            try {
                final ZipFile zipFile = new ZipFile(parent.getPathString());
                if (recursive) {
                    zipFile.stream().forEach((zipEntry) -> {
                        final AdvancedFile advancedFile_ = new AdvancedFile(zipEntry.getName(), parent.getPaths());
                        if (fileFilter.accept(advancedFile_)) {
                            advancedFiles.add(advancedFile_);
                        }
                    });
                } else {
                    zipFile.stream().filter((zipEntry) -> !zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR)).forEach((zipEntry) -> {
                        final AdvancedFile advancedFile_ = new AdvancedFile(zipEntry.getName(), parent.getPaths());
                        if (fileFilter.accept(advancedFile_)) {
                            advancedFiles.add(advancedFile_);
                        }
                    });
                }
                zipFile.close();
                return advancedFiles;
            } catch (Exception ex) {
                Logger.handleError(ex);
                return null;
            }
        } else {
            try {
                final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data_parent));
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (!recursive && zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR)) {
                        continue;
                    }
                    final AdvancedFile advancedFile_ = new AdvancedFile(parent, zipEntry.getName());
                    if (fileFilter.accept(advancedFile_)) {
                        advancedFiles.add(advancedFile_);
                    }
                }
                zipInputStream.close();
                return advancedFiles;
            } catch (Exception ex) {
                Logger.handleError(ex);
                return null;
            }
        }
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath, List<AdvancedFile> advancedFiles, AdvancedFilenameFilter filenameFilter, boolean recursive, byte... data_parent) {
        if (filenameFilter == null) {
            return listFiles(parent, advancedFile, subPath, advancedFiles, recursive, data_parent);
        }
        if (data_parent == null || data_parent.length == 0) {
            try {
                final ZipFile zipFile = new ZipFile(parent.getPathString());
                if (recursive) {
                    zipFile.stream().forEach((zipEntry) -> {
                        final AdvancedFile advancedFile_ = new AdvancedFile(zipEntry.getName(), parent.getPaths());
                        if (filenameFilter.accept(parent, zipEntry.getName())) {
                            advancedFiles.add(advancedFile_);
                        }
                    });
                } else {
                    zipFile.stream().filter((zipEntry) -> !zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR)).forEach((zipEntry) -> {
                        final AdvancedFile advancedFile_ = new AdvancedFile(zipEntry.getName(), parent.getPaths());
                        if (filenameFilter.accept(parent, zipEntry.getName())) {
                            advancedFiles.add(advancedFile_);
                        }
                    });
                }
                zipFile.close();
                return advancedFiles;
            } catch (Exception ex) {
                Logger.handleError(ex);
                return null;
            }
        } else {
            try {
                final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data_parent));
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (!recursive && zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR)) {
                        continue;
                    }
                    final AdvancedFile advancedFile_ = new AdvancedFile(parent, zipEntry.getName());
                    if (filenameFilter.accept(parent, zipEntry.getName())) {
                        advancedFiles.add(advancedFile_);
                    }
                }
                zipInputStream.close();
                return advancedFiles;
            } catch (Exception ex) {
                Logger.handleError(ex);
                return null;
            }
        }
    }
    
    @Override
    public byte[] readBytes(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath, byte... data_parent) {
        if (data_parent == null || data_parent.length == 0) {
            try {
                final ZipFile zipFile = new ZipFile(parent.getPathString());
                final byte[] data = IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry(Arrays.asList(subPath).stream().collect(Collectors.joining(File.separator)))));
                zipFile.close();
                return data;
            } catch (Exception ex) {
                Logger.handleError(ex);
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
                Logger.handleError(ex);
                return null;
            }
        }
    }
    
    @Override
    public boolean writeBytes(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath, byte[] data) {
        throw new UnsupportedOperationException("Coming soon TM");
    }
    
    @Override
    public boolean createFile(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath) {
        throw new UnsupportedOperationException("Coming soon TM");
    }
    
    @Override
    public boolean deleteFile(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath) {
        throw new UnsupportedOperationException("Coming soon TM");
    }
    
    @Override
    public boolean mkdir(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath) {
        throw new UnsupportedOperationException("Coming soon TM");
    }
    
    @Override
    public boolean mkdirs(AdvancedFile parent, AdvancedFile advancedFile, String[] subPath) {
        throw new UnsupportedOperationException("Coming soon TM");
    }
    
    @Override
    public boolean accept(AdvancedFile parent, String name) {
        return name.toLowerCase().endsWith(".zip");
    }
    
}
