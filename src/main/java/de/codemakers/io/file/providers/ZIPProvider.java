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

import de.codemakers.base.Standard;
import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.base.multiplets.Doublet;
import de.codemakers.base.util.ArrayUtil;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.file.AdvancedFileFilter;
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
import java.util.regex.Pattern;
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
        return name.replaceAll(Pattern.quote(ZIPSeparator), Matcher.quoteReplacement(separator));
    }
    
    protected static final String[] zipEntryToPaths(ZipEntry zipEntry) {
        if (zipEntry == null) {
            return null;
        }
        final String path = zipEntryToPath(zipEntry);
        return path.split(Pattern.quote(ZIPSeparator));
    }
    
    @Override
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String file_path = file.getPath().startsWith(parent.getPath()) ? file.getPath().substring(parent.getPath().length() + (parent.getPath().length() == file.getPath().length() ? 0 : 1)) : file.getPath().startsWith(AdvancedFile.PATH_SEPARATOR) ? file.getPath().substring(AdvancedFile.PATH_SEPARATOR.length()) : file.getPath();
        if (inputStreamSupplier == null) {
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
            final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get());
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
    public List<AdvancedFile> listFiles(AdvancedFile parent, AdvancedFile file, boolean recursive, ToughSupplier<InputStream> inputStreamSupplier, AdvancedFileFilter advancedFileFilter) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String path = file.getPathsCollected(ZIPSeparator);
        final int pathLength = path.length();
        if (inputStreamSupplier == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                if (advancedFileFilter == null) {
                    zipFile.stream().filter((zipEntry) -> {
                        final String zipEntryPath = zipEntryToPath(zipEntry);
                        return zipEntryPath.length() > pathLength && (recursive || !zipEntryPath.contains(ZIPSeparator)) && zipEntryPath.startsWith(path);
                    }).map((zipEntry) -> new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))).forEach(advancedFiles::add);
                } else {
                    zipFile.stream().filter((zipEntry) -> {
                        final String zipEntryPath = zipEntryToPath(zipEntry);
                        return zipEntryPath.length() > pathLength && (recursive || !zipEntryPath.contains(ZIPSeparator)) && zipEntryPath.startsWith(path);
                    }).map((zipEntry) -> new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()))).filter(advancedFileFilter).forEach(advancedFiles::add);
                }
                zipFile.close();
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get());
            try {
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    final String zipEntryPath = zipEntryToPath(zipEntry);
                    if (zipEntryPath.length() <= pathLength || (!recursive && zipEntryPath.contains(ZIPSeparator) || !zipEntryPath.startsWith(path))) {
                        zipInputStream.closeEntry();
                        continue;
                    }
                    final AdvancedFile advancedFile = new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator()));
                    if (advancedFileFilter == null || advancedFileFilter.test(advancedFile)) {
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
        
        
        
        
        
        
        
        
        
        
        
        /*
        @Deprecated
        if (advancedFileFilter == null) {
            return listFiles(parent, file, recursive, inputStreamSupplier);
        } else if (advancedFileFilter instanceof AdvancedFilenameFilter) {
            //return listFiles(parent, file, recursive, (AdvancedFilenameFilter) advancedFileFilter, inputStream);
        }
        */
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final String file_path = (file.getPath().startsWith(parent.getPath()) ? file.getPath().substring(parent.getPath().length() + 1) : (file.getPath().startsWith(AdvancedFile.PATH_SEPARATOR) ? file.getPath().substring(AdvancedFile.PATH_SEPARATOR.length()) : file.getPath()));
        if (inputStreamSupplier == null) {
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
                zipFile.close();
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get());
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
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        final Doublet<Boolean, Boolean> doublet = existsIntern(parent, file, inputStreamSupplier);
        if (!doublet.getA()) {
            return false;
        }
        final CloseableZipEntry<?> closeableZipEntry = getCloseableZipEntry(parent, file, inputStreamSupplier.get());
        if (closeableZipEntry == null) {
            return false;
        }
        return closeableZipEntry.closeWithoutException((zipEntry) -> !zipEntry.isDirectory());
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        final Doublet<Boolean, Boolean> doublet = existsIntern(parent, file, inputStreamSupplier);
        if (!doublet.getA()) {
            return doublet.getB();
        }
        final CloseableZipEntry<?> closeableZipEntry = getCloseableZipEntry(parent, file, inputStreamSupplier.get());
        if (closeableZipEntry == null) {
            return false;
        }
        return closeableZipEntry.closeWithoutException(ZipEntry::isDirectory);
    }
    
    @Override
    public boolean exists(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        return existsIntern(parent, file, inputStreamSupplier).getA();
    }
    
    private Doublet<Boolean, Boolean> existsIntern(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStreamSupplier == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                final boolean exists = zipFile.getEntry(file.getPathsCollected(ZIPSeparator)) != null;
                zipFile.close();
                if (exists) {
                    return new Doublet<>(true, null);
                }
                inputStreamSupplier = parent::createInputStream;
            } catch (Exception ex) {
                Standard.silentError(zipFile::close);
                throw ex;
            }
        }
        final String path = file.getPathsCollected(ZIPSeparator);
        final String[] paths = file.getPaths();
        final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get());
        try {
            boolean found = false;
            boolean foundDirectory = true;
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                final String zipEntryPath = zipEntryToPath(zipEntry);
                if (Objects.equals(path, zipEntryPath)) {
                    found = true;
                    zipInputStream.closeEntry();
                    break;
                }
                final String[] zipEntryPaths = zipEntryToPaths(zipEntry);
                if (ArrayUtil.arrayStartsWith(zipEntryPaths, paths)) {
                    found = true;
                    foundDirectory = true;
                    zipInputStream.closeEntry();
                    break;
                }
                zipInputStream.closeEntry();
            }
            zipInputStream.close();
            return new Doublet<>(found, foundDirectory);
        } catch (Exception ex) {
            zipInputStream.close();
            throw ex;
        }
    }
    
    @Override
    public InputStream createInputStream(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStreamSupplier == null) {
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
            final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get()) {
                @Override
                public void close() throws IOException {
                    super.closeEntry();
                    super.close();
                }
            };
            try {
                ZipEntry zipEntry;
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
    public byte[] readBytes(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStreamSupplier == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            byte[] data;
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
            final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get());
            byte[] data;
            try {
                ZipEntry zipEntry;
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
    
    private CloseableZipEntry<?> getCloseableZipEntry(AdvancedFile parent, AdvancedFile file, InputStream inputStream) throws Exception {
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
                ZipEntry zipEntry;
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
