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

import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.io.file.t3.AdvancedFile;
import de.codemakers.io.file.t3.AdvancedFileFilter;
import de.codemakers.io.file.t3.AdvancedFilenameFilter;
import de.codemakers.io.file.t3.closeable.CloseableZipEntry;
import de.codemakers.io.file.t3.closeable.CloseableZipFileEntry;
import de.codemakers.io.file.t3.closeable.CloseableZipInputStreamEntry;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZIPProvider extends FileProvider<AdvancedFile> { //TODO Test this
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, InputStream inputStream) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        System.out.println("parent.getPath(): " + parent.getPath().length());
        System.out.println("parent.getPath(): " + parent.getPath());
        System.out.println("  file.getPath(): " + file.getPath());
        System.out.println("  file.getPath(): " + file.getPath().length());
        final String file_path = (file.getPath().length() > parent.getPath().length()) ? file.getPath().substring(parent.getPath().length() + 1) : (file.getPath().startsWith(AdvancedFile.PATH_SEPARATOR) ? file.getPath().substring(AdvancedFile.PATH_SEPARATOR.length()) : file.getPath());
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                if (recursive) {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).forEach((zipEntry) -> advancedFiles.add(new AdvancedFile(parent, true, zipEntry.getName())));
                } else {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> !zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING)).forEach((zipEntry) -> advancedFiles.add(new AdvancedFile(parent, true, zipEntry.getName())));
                }
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (!zipEntry.getName().startsWith(file_path)) { //FIXME Test this (and in the above ZipFile streams)
                        zipInputStream.closeEntry();
                        continue;
                    }
                    if (!recursive && zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR_CURRENT_STRING)) {
                        zipInputStream.closeEntry();
                        continue;
                    }
                    advancedFiles.add(new AdvancedFile(parent, true, zipEntry.getName()));
                    zipInputStream.closeEntry();
                }
                zipInputStream.close();
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
        }
        return advancedFiles;
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, AdvancedFileFilter advancedFileFilter, InputStream inputStream) throws Exception {
        if (advancedFileFilter == null) {
            return listFiles(parent, file, recursive, inputStream);
        } else if (advancedFileFilter instanceof AdvancedFilenameFilter) {
            return listFiles(parent, file, recursive, (AdvancedFilenameFilter) advancedFileFilter, inputStream);
        }
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String file_path = file.getPath().substring(parent.getPath().length() + 1);
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                if (recursive) {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).forEach((zipEntry) -> {
                        final AdvancedFile advancedFile = new AdvancedFile(parent, true, zipEntry.getName());
                        if (advancedFileFilter.test(advancedFile)) {
                            advancedFiles.add(advancedFile);
                        }
                    });
                } else {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> !zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING)).forEach((zipEntry) -> {
                        final AdvancedFile advancedFile = new AdvancedFile(parent, true, zipEntry.getName());
                        if (advancedFileFilter.test(advancedFile)) {
                            advancedFiles.add(advancedFile);
                        }
                    });
                }
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (!zipEntry.getName().startsWith(file_path)) { //FIXME Test this (and in the above ZipFile streams)
                        zipInputStream.closeEntry();
                        continue;
                    }
                    if (!recursive && zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR_CURRENT_STRING)) {
                        zipInputStream.closeEntry();
                        continue;
                    }
                    final AdvancedFile advancedFile = new AdvancedFile(parent, true, zipEntry.getName());
                    if (advancedFileFilter.test(advancedFile)) {
                        advancedFiles.add(advancedFile);
                    }
                    zipInputStream.closeEntry();
                }
                zipInputStream.close();
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
        }
        return advancedFiles;
    }
    
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, AdvancedFilenameFilter advancedFilenameFilter, InputStream inputStream) throws Exception {
        if (advancedFilenameFilter == null) {
            return listFiles(parent, file, recursive, inputStream);
        }
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String file_path = file.getPath().substring(parent.getPath().length() + 1);
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                if (recursive) {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).forEach((zipEntry) -> {
                        final AdvancedFile advancedFile = new AdvancedFile(parent, true, zipEntry.getName());
                        if (advancedFilenameFilter.test(advancedFile.getParentFile(), advancedFile.getName())) {
                            advancedFiles.add(advancedFile);
                        }
                    });
                } else {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> !zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING)).forEach((zipEntry) -> {
                        final AdvancedFile advancedFile = new AdvancedFile(parent, true, zipEntry.getName());
                        if (advancedFilenameFilter.test(advancedFile.getParentFile(), advancedFile.getName())) {
                            advancedFiles.add(advancedFile);
                        }
                    });
                }
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (!zipEntry.getName().startsWith(file_path)) { //FIXME Test this (and in the above ZipFile streams)
                        zipInputStream.closeEntry();
                        continue;
                    }
                    if (!recursive && zipEntry.getName().substring(0, zipEntry.getName().length() - 1).contains(AdvancedFile.FILE_SEPARATOR_CURRENT_STRING)) {
                        zipInputStream.closeEntry();
                        continue;
                    }
                    final AdvancedFile advancedFile = new AdvancedFile(parent, true, zipEntry.getName());
                    if (advancedFilenameFilter.test(advancedFile.getParentFile(), advancedFile.getName())) {
                        advancedFiles.add(advancedFile);
                    }
                    zipInputStream.closeEntry();
                }
                zipInputStream.close();
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
        }
        return advancedFiles;
    }
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        return !isDirectory(parent, file, inputStream);
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final CloseableZipEntry closableZipEntry = getClosableZipEntry(parent, file, inputStream);
        if (closableZipEntry == null) {
            return false;
        }
        //return closableZipEntry.closeWithoutException(ZipEntry::isDirectory); //FIXME why is this not working?
        return (boolean) closableZipEntry.closeWithoutException((object) -> ((ZipEntry) object).isDirectory()); //TODO Test this
    }
    
    @Override
    public boolean exists(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                final boolean exists = zipFile.getEntry(file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING)) != null;
                zipFile.close();
                return exists;
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final String path = file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            try {
                boolean found = false;
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.getName().equals(path)) {
                        found = true;
                        zipInputStream.closeEntry();
                        break;
                    }
                    zipInputStream.closeEntry();
                }
                zipInputStream.close();
                return found;
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
        }
    }
    
    @Override
    public InputStream createInputStream(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                //return new AdvancedCloseableInputStream(zipFile, zipFile.getInputStream(zipFile.getEntry(file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING))));
                return new BufferedInputStream(zipFile.getInputStream(zipFile.getEntry(file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING)))) {
                    @Override
                    public void close() throws IOException {
                        zipFile.close();
                        super.close();
                    }
                };
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final String path = file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream) {
                @Override
                public void close() throws IOException {
                    super.closeEntry();
                    super.close();
                }
            };
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.getName().equals(path)) {
                        break;
                    }
                    zipInputStream.closeEntry();
                }
                /*
                return new AdvancedCloseableInputStream(inputStream, zipInputStream) {
                    @Override
                    public void preClose(Closeable closeable, InputStream data) throws IOException {
                        if (data != null && data instanceof ZipInputStream) {
                            ((ZipInputStream) data).closeEntry();
                        }
                        super.preClose(closeable, data);
                    }
                };
                */
                return zipInputStream;
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
        }
    }
    
    @Override
    public byte[] readBytes(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            byte[] data = null;
            try {
                data = IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry(file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING))));
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
            zipFile.close();
            return data;
        } else {
            final String path = file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            byte[] data = null;
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.getName().equals(path)) {
                        break;
                    }
                    zipInputStream.closeEntry();
                }
                data = IOUtils.toByteArray(zipInputStream);
                if (zipEntry != null) {
                    zipInputStream.closeEntry();
                }
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
            zipInputStream.close();
            return data;
        }
    }
    
    public CloseableZipEntry getClosableZipEntry(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                return new CloseableZipFileEntry(zipFile, zipFile.getEntry(file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING)));
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final String path = file.getPathsCollected(AdvancedFile.FILE_SEPARATOR_DEFAULT_STRING);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.getName().equals(path)) {
                        break;
                    }
                    zipInputStream.closeEntry();
                }
                if (zipEntry == null) {
                    zipInputStream.close();
                    return null;
                }
                return new CloseableZipInputStreamEntry(zipInputStream, zipEntry);
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
        }
    }
    
    @Override
    public OutputStream createOutputStream(AdvancedFile parent, AdvancedFile file, boolean append) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean writeBytes(AdvancedFile parent, AdvancedFile file, byte[] data) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (data == null) {
            data = new byte[0];
        }
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean createNewFile(AdvancedFile parent, AdvancedFile file) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean delete(AdvancedFile parent, AdvancedFile file) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean mkdir(AdvancedFile parent, AdvancedFile file) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean mkdirs(AdvancedFile parent, AdvancedFile file) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean test(AdvancedFile parent, String name) {
        if (name == null || name.isEmpty() || !name.contains(".")) {
            return false;
        }
        final String name_lower = name.toLowerCase();
        return name_lower.endsWith(".zip") || name_lower.endsWith(".jar"); //TODO is a ".tar" also "unzipable"? Or do i have to create another FileProvider "TARProvider"?
    }
    
}
