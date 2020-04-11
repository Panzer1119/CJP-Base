/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

package de.codemakers.io.file.providers;

import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.file.AdvancedFileFilter;
import de.codemakers.io.file.AdvancedFilenameFilter;
import de.codemakers.io.file.closeable.CloseableZipEntry;
import de.codemakers.io.file.closeable.CloseableZipFileEntry;
import de.codemakers.io.file.closeable.CloseableZipInputStreamEntry;
import de.codemakers.io.file.exceptions.isnot.FileIsNotExistingException;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZIPProvider extends FileProvider<AdvancedFile> {
    
    public static final String ZIPSeparator = "/";
    
    protected static final String zipEntryToPath(ZipEntry zipEntry) {
        return zipEntryToPath(zipEntry, null);
    }
    
    protected static final String zipEntryToPath(ZipEntry zipEntry, String separator) {
        if (zipEntry == null) {
            return null;
        }
        final String name = zipEntry.isDirectory() ? zipEntry.getName().substring(0, zipEntry.getName().length() - ZIPSeparator.length()) : zipEntry.getName();
        if (separator == null) {
            return name;
        }
        return name.replaceAll(ZIPSeparator, Matcher.quoteReplacement(separator));
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, InputStream inputStream) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String file_path = (file.getPath().startsWith(parent.getPath()) ? file.getPath().substring(parent.getPath().length() + (parent.getPath().length() == file.getPath().length() ? 0 : 1)) : (file.getPath().startsWith(AdvancedFile.PATH_SEPARATOR) ? file.getPath().substring(AdvancedFile.PATH_SEPARATOR.length()) : file.getPath()));
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                if (recursive) {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> zipEntryToPath(zipEntry).length() > file_path.length()).forEach((zipEntry) -> advancedFiles.add(new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))));
                } else {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> {
                        final String path = zipEntryToPath(zipEntry);
                        if (path.length() <= file_path.length()) {
                            return false;
                        }
                        return !path.substring(file_path.length() + ZIPSeparator.length()).contains(ZIPSeparator);
                    }).forEach((zipEntry) -> advancedFiles.add(new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))));
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
                    final String path = zipEntryToPath(zipEntry);
                    if (!path.startsWith(file_path)) { //FIXME Test this (and in the above ZipFile streams)
                        zipInputStream.closeEntry();
                        continue;
                    }
                    if (!recursive && path.contains(ZIPSeparator)) {
                        zipInputStream.closeEntry();
                        continue;
                    }
                    advancedFiles.add(new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator())));
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
            //return listFiles(parent, file, recursive, (AdvancedFilenameFilter) advancedFileFilter, inputStream);
        }
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String file_path = (file.getPath().startsWith(parent.getPath()) ? file.getPath().substring(parent.getPath().length() + 1) : (file.getPath().startsWith(AdvancedFile.PATH_SEPARATOR) ? file.getPath().substring(AdvancedFile.PATH_SEPARATOR.length()) : file.getPath()));
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                if (recursive) {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> zipEntryToPath(zipEntry).length() > file_path.length()).map((zipEntry) -> new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))).filter(advancedFileFilter).forEach(advancedFiles::add);
                } else {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> {
                        final String path = zipEntryToPath(zipEntry);
                        if (path.length() <= file_path.length()) {
                            return false;
                        }
                        return !path.substring(file_path.length() + ZIPSeparator.length()).contains(ZIPSeparator);
                    }).map((zipEntry) -> new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))).filter(advancedFileFilter).forEach(advancedFiles::add);
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
                    final String path = zipEntryToPath(zipEntry);
                    if (!path.startsWith(file_path)) { //FIXME Test this (and in the above ZipFile streams)
                        zipInputStream.closeEntry();
                        continue;
                    }
                    if (!recursive && path.contains(ZIPSeparator)) {
                        zipInputStream.closeEntry();
                        continue;
                    }
                    final AdvancedFile advancedFile = new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()));
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
    
    @Deprecated
    private List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, AdvancedFilenameFilter advancedFilenameFilter, InputStream inputStream) throws Exception {
        if (advancedFilenameFilter == null) {
            return listFiles(parent, file, recursive, inputStream);
        }
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String file_path = (file.getPath().startsWith(parent.getPath()) ? file.getPath().substring(parent.getPath().length() + 1) : (file.getPath().startsWith(AdvancedFile.PATH_SEPARATOR) ? file.getPath().substring(AdvancedFile.PATH_SEPARATOR.length()) : file.getPath())) + ZIPSeparator;
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                if (recursive) {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> zipEntry.getName().length() > file_path.length()).map((zipEntry) -> new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))).filter(advancedFilenameFilter).forEach(advancedFiles::add);
                } else {
                    zipFile.stream().filter((zipEntry) -> zipEntry.getName().startsWith(file_path)).filter((zipEntry) -> {
                        final String path = zipEntryToPath(zipEntry);
                        if (path.length() <= file_path.length()) {
                            return false;
                        }
                        return !path.substring(file_path.length() + ZIPSeparator.length()).contains(ZIPSeparator);
                    }).map((zipEntry) -> new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))).filter(advancedFilenameFilter).forEach(advancedFiles::add);
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
                    final AdvancedFile advancedFile = new AdvancedFile(parent, false, zipEntry.getName());
                    if (advancedFilenameFilter.test(advancedFile)) {
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
        final CloseableZipEntry<?> closeableZipEntry = getCloseableZipEntry(parent, file, inputStream);
        if (closeableZipEntry == null) {
            return false;
        }
        return closeableZipEntry.closeWithoutException(ZipEntry::isDirectory);
    }
    
    @Override
    public boolean exists(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                final boolean exists = zipFile.getEntry(file.getPathsCollected(ZIPSeparator)) != null;
                zipFile.close();
                return exists;
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final String path = file.getPathsCollected(ZIPSeparator);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            try {
                boolean found = false;
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (Objects.equals(path, zipEntryToPath(zipEntry))) {
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
                final ZipEntry zipEntry = zipFile.getEntry(file.getPathsCollected(ZIPSeparator));
                if (zipEntry == null) {
                    throw new FileIsNotExistingException(file + " does not exist");
                }
                return new BufferedInputStream(zipFile.getInputStream(zipEntry)) {
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
            final String path = file.getPathsCollected(ZIPSeparator);
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
                    if (Objects.equals(path, zipEntryToPath(zipEntry))) {
                        break;
                    }
                    zipInputStream.closeEntry();
                }
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
                final ZipEntry zipEntry = zipFile.getEntry(file.getPathsCollected(ZIPSeparator));
                if (zipEntry == null) {
                    throw new FileIsNotExistingException(file + " does not exist");
                }
                data = IOUtils.toByteArray(zipFile.getInputStream(zipEntry));
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
            zipFile.close();
            return data;
        } else {
            final String path = file.getPathsCollected(ZIPSeparator);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            byte[] data = null;
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (Objects.equals(path, zipEntryToPath(zipEntry))) {
                        break;
                    }
                    zipInputStream.closeEntry();
                }
                data = IOUtils.toByteArray(zipInputStream);
                if (zipEntry != null) {
                    zipInputStream.closeEntry();
                } else {
                    throw new FileIsNotExistingException(file + " does not exist");
                }
            } catch (Exception ex) {
                zipInputStream.close();
                throw ex;
            }
            zipInputStream.close();
            return data;
        }
    }
    
    @Override
    public boolean canWrite(AdvancedFile parent, AdvancedFile file) {
        return false;
    }
    
    public CloseableZipEntry getCloseableZipEntry(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
        if (inputStream == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                final ZipEntry zipEntry = zipFile.getEntry(file.getPathsCollected(ZIPSeparator));
                if (zipEntry == null) {
                    throw new FileIsNotExistingException(file + " does not exist");
                }
                return new CloseableZipFileEntry(zipFile, zipEntry);
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final String path = file.getPathsCollected(ZIPSeparator);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            try {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (Objects.equals(path, zipEntryToPath(zipEntry))) {
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
