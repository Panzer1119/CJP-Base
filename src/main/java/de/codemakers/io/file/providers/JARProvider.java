package de.codemakers.io.file.providers;

import de.codemakers.base.Standard;
import de.codemakers.base.multiplets.Doublet;
import de.codemakers.base.util.ArrayUtil;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.file.closeable.CloseableZipEntry;
import de.codemakers.io.file.closeable.CloseableZipFileEntry;
import de.codemakers.io.file.closeable.CloseableZipInputStreamEntry;
import de.codemakers.io.file.exceptions.isnot.FileIsNotExistingException;
import de.codemakers.io.file.filters.AdvancedFileFilter;
import de.codemakers.io.file.filters.MagicNumberFileFilter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class JARProvider extends ZIPProvider {
    
    @Override
    protected List<AdvancedFile> listFiles(AdvancedFile parent, boolean recursive, AdvancedFileFilter advancedFileFilter, String path, int pathLength) throws Exception {
        final List<AdvancedFile> advancedFiles = new ArrayList<>();
        final ZipFile zipFile = new ZipFile(parent.getPath());
        try {
            if (recursive) {
                streamFilesRecursive(zipFile, parent, advancedFileFilter, path, pathLength).forEach(advancedFiles::add);
            } else {
                streamFilesNonRecursive(zipFile, parent, advancedFileFilter, path, pathLength).forEach(advancedFiles::add);
            }
            zipFile.close();
        } catch (Exception ex) {
            zipFile.close();
            throw ex;
        }
        return advancedFiles;
    }
    
    private Stream<AdvancedFile> streamFilesRecursive(ZipFile zipFile, AdvancedFile parent, AdvancedFileFilter advancedFileFilter, String path, int pathLength) {
        final Stream<AdvancedFile> stream = zipFile.stream().filter((zipEntry) -> {
            final String zipEntryPath = zipEntryToPath(zipEntry);
            return zipEntryPath.length() > pathLength && zipEntryPath.startsWith(path);
        }).map((zipEntry) -> new AdvancedFile(parent, false, zipEntryToPath(zipEntry, parent.getSeparator())));
        if (advancedFileFilter != null) {
            return stream.filter(advancedFileFilter);
        }
        return stream;
    }
    
    private Stream<AdvancedFile> streamFilesNonRecursive(ZipFile zipFile, AdvancedFile parent, AdvancedFileFilter advancedFileFilter, String path, int pathLength) {
        final Stream<AdvancedFile> stream = zipFile.stream().filter((zipEntry) -> {
            final String zipEntryPath = zipEntryToPath(zipEntry);
            return zipEntryPath.length() > pathLength && zipEntryPath.startsWith(path);
        }).map((zipEntry) -> {
            final String zipEntryPath = zipEntryToPath(zipEntry);
            final String zipEntryPathSubString = zipEntryPath.substring(pathLength + ZIP_SEPARATOR_LENGTH);
            final String temp = zipEntryPathSubString.contains(ZIP_SEPARATOR) ? zipEntryPath.substring(0, zipEntryPathSubString.indexOf(ZIP_SEPARATOR)) : zipEntryPath;
            return new AdvancedFile(parent, false, temp.replaceAll(Pattern.quote(ZIP_SEPARATOR), Matcher.quoteReplacement(parent.getSeparator())));
        }).distinct();
        if (advancedFileFilter != null) {
            return stream.filter(advancedFileFilter);
        }
        return stream;
    }
    
    @Override
    public boolean isFile(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        final Doublet<Boolean, Boolean> doublet = existsIntern(parent, file, inputStreamSupplier);
        if (!doublet.getA() || doublet.getB() != null) {
            return !doublet.getB();
        }
        final CloseableZipEntry<?> closeableZipEntry = getCloseableZipEntry(parent, file, inputStreamSupplier);
        if (closeableZipEntry == null) {
            return false;
        }
        return closeableZipEntry.closeWithoutException((zipEntry) -> !zipEntry.isDirectory());
    }
    
    @Override
    public boolean isDirectory(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        final Doublet<Boolean, Boolean> doublet = existsIntern(parent, file, inputStreamSupplier);
        if (!doublet.getA() || doublet.getB() != null) {
            return doublet.getB();
        }
        final CloseableZipEntry<?> closeableZipEntry = getCloseableZipEntry(parent, file, inputStreamSupplier);
        if (closeableZipEntry == null) {
            return false;
        }
        return closeableZipEntry.closeWithoutException(ZipEntry::isDirectory);
    }
    
    private CloseableZipEntry<?> getCloseableZipEntry(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        if (inputStreamSupplier == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                final ZipEntry zipEntry = zipFile.getEntry(file.getPathsCollected(ZIP_SEPARATOR));
                if (zipEntry == null) {
                    throw new FileIsNotExistingException(file + " does not exist");
                }
                return new CloseableZipFileEntry(zipFile, zipEntry);
            } catch (Exception ex) {
                zipFile.close();
                throw ex;
            }
        } else {
            final String path = file.getPathsCollected(ZIP_SEPARATOR);
            final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get());
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
    public boolean exists(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        return existsIntern(parent, file, inputStreamSupplier).getA();
    }
    
    private Doublet<Boolean, Boolean> existsIntern(AdvancedFile parent, AdvancedFile file, ToughSupplier<InputStream> inputStreamSupplier) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(file);
        if (inputStreamSupplier == null) {
            final ZipFile zipFile = new ZipFile(parent.getPath());
            try {
                final boolean exists = zipFile.getEntry(file.getPathsCollected(ZIP_SEPARATOR)) != null;
                zipFile.close();
                if (exists) {
                    return new Doublet<>(true, null);
                }
                inputStreamSupplier = parent::createInputStream;
            } catch (Exception ex) {
                Standard.silentClose(zipFile);
                throw ex;
            }
        }
        final String path = file.getPathsCollected(ZIP_SEPARATOR);
        final String[] paths = file.getPaths();
        final ZipInputStream zipInputStream = new ZipInputStream(inputStreamSupplier.get());
        try {
            boolean found = false;
            boolean foundDirectory = false;
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
    public int getPriority(AdvancedFile parent, String name) {
        return super.getPriority(parent, name) + 1;
    }
    
    @Override
    public boolean test(AdvancedFile parent, String name, AdvancedFile file, boolean exists) {
        if (exists) {
            return MagicNumberFileFilter.ALL_JARS.test(file);
        }
        if (name == null || name.isEmpty() || !name.contains(".")) {
            return false;
        }
        return name.toLowerCase().endsWith(".jar");
    }
    
}
