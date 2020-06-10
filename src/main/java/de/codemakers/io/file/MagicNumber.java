package de.codemakers.io.file;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.ConvertUtil;
import de.codemakers.base.util.tough.ToughSupplier;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MagicNumbers from https://filesignatures.net
 */
public class MagicNumber implements Predicate<InputStream> {
    
    /**
     * Jar archive
     */
    public static final MagicNumber JAR = ofHexString("50 4B 03 04").addExtensions("JAR");
    /**
     * JARCS compressed archive
     */
    public static final MagicNumber JARCS = ofHexString("5F 27 A8 89").addExtensions("JAR");
    /**
     * Java archive_1
     */
    public static final MagicNumber JAR1 = ofHexString("4A 41 52 43 53 00").addExtensions("JAR");
    /**
     * Java archive_2
     */
    public static final MagicNumber JAR2 = ofHexString("50 4B 03 04 14 00 08 00").addExtensions("JAR");
    /**
     * All JAR MagicNumbers
     */
    private static final Set<MagicNumber> ALL_JARS = generateMergingSet(JAR, JARCS, JAR1, JAR2);
    /**
     * PNG image
     */
    public static final MagicNumber PNG = ofHexString("89 50 4E 47 0D 0A 1A 0A").addExtensions("PNG");
    /**
     * All Image MagicNumbers
     */
    private static final Set<MagicNumber> ALL_IMAGES = generateMergingSet(PNG);
    /**
     * Tape Archive
     */
    public static final MagicNumber TAR = ofString("ustar", 257).addExtensions("TAR");
    /**
     * bzip2 compressed archive
     */
    public static final MagicNumber TAR_dot_BZ2 = ofString("BZh").addExtensions("TAR");
    /**
     * Compressed tape archive_1
     */
    public static final MagicNumber TAR_dot_Z1 = ofHexString("1F 9D 90").addExtensions("TAR");
    /**
     * Compressed tape archive_2
     */
    public static final MagicNumber TAR_dot_Z2 = ofHexString("1F A0").addExtensions("TAR");
    /**
     * All TAR MagicNumbers
     */
    private static final Set<MagicNumber> ALL_TARS = generateMergingSet(TAR, TAR_dot_BZ2, TAR_dot_Z1, TAR_dot_Z2);
    /**
     * PKZIP archive_1
     */
    public static final MagicNumber ZIP_PKZIP1 = ofHexString("50 4B 03 04").addExtensions("ZIP");
    /**
     * PKLITE archive
     */
    public static final MagicNumber ZIP_PKLITE = ofHexString("50 4B 4C 49 54 45").addExtensions("ZIP");
    /**
     * PKSFX self-extracting archive
     */
    public static final MagicNumber ZIP_PKSFX = ofHexString("50 4B 53 70 58").addExtensions("ZIP");
    /**
     * PKZIP archive_2
     */
    public static final MagicNumber ZIP_PKZIP2 = ofHexString("50 4B 05 06").addExtensions("ZIP");
    /**
     * PKZIP archive_3
     */
    public static final MagicNumber ZIP_PKZIP3 = ofHexString("50 4B 07 08").addExtensions("ZIP");
    /**
     * WinZip compressed archive
     */
    public static final MagicNumber ZIP_WIN_ZIP = ofHexString("57 69 6E 5A 69 70").addExtensions("ZIP");
    /**
     * ZLock Pro encrypted ZIP
     */
    public static final MagicNumber ZIP_ZLOCK_PRO_ENCRYPTED = ofHexString("50 4B 03 04 14 00 01 00").addExtensions("ZIP");
    /**
     * All ZIP MagicNumbers
     */
    private static final Set<MagicNumber> ALL_ZIPS = generateMergingSet(ZIP_PKZIP1, ZIP_PKLITE, ZIP_PKSFX, ZIP_PKZIP2, ZIP_PKZIP3, ZIP_WIN_ZIP, ZIP_ZLOCK_PRO_ENCRYPTED);
    /**
     * All MagicNumbers
     */
    private static final Set<MagicNumber> ALL = mergeSets(ALL_IMAGES, ALL_JARS, ALL_TARS, ALL_ZIPS);
    
    private final byte[] magicNumber;
    private final long byteOffset;
    private final String magicNumberString;
    //
    private final Set<String> extensions = new HashSet<>();
    
    public MagicNumber(String magicNumberString) {
        this(magicNumberString, 0);
    }
    
    public MagicNumber(String magicNumberString, long byteOffset) {
        this.magicNumberString = Objects.requireNonNull(magicNumberString, "magicNumberString may not be null");
        if (magicNumberString.isEmpty()) {
            throw new IllegalArgumentException("magicNumberString may not be empty");
        }
        this.magicNumber = magicNumberString.getBytes(Charset.defaultCharset());
        if (byteOffset < 0) {
            throw new IllegalArgumentException("byteOffset may not be negative");
        }
        this.byteOffset = byteOffset;
    }
    
    public MagicNumber(byte[] magicNumber) {
        this(magicNumber, 0);
    }
    
    public MagicNumber(byte[] magicNumber, long byteOffset) {
        this.magicNumber = Objects.requireNonNull(magicNumber, "magicNumber may not be null");
        if (magicNumber.length == 0) {
            throw new IllegalArgumentException("magicNumber may not be empty");
        }
        this.magicNumberString = new String(magicNumber, Charset.defaultCharset());
        if (byteOffset < 0) {
            throw new IllegalArgumentException("byteOffset may not be negative");
        }
        this.byteOffset = byteOffset;
    }
    
    public byte[] getMagicNumber() {
        return magicNumber;
    }
    
    public int getLength() {
        return magicNumber.length;
    }
    
    public long getByteOffset() {
        return byteOffset;
    }
    
    public String getMagicNumberString() {
        return magicNumberString;
    }
    
    public Set<String> getExtensions() {
        return extensions;
    }
    
    public String getFirstExtension() {
        return extensions.isEmpty() ? null : extensions.iterator().next();
    }
    
    public boolean hasExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }
        return extensions.contains(extension.toUpperCase());
    }
    
    public MagicNumber addExtensions(String... extensions) {
        return addExtensions(Stream.of(extensions));
    }
    
    public MagicNumber addExtensions(Collection<String> extensions) {
        return addExtensions(extensions.stream());
    }
    
    private MagicNumber addExtensions(Stream<String> extensions) {
        this.extensions.addAll(extensions.map(String::toUpperCase).collect(Collectors.toSet()));
        return this;
    }
    
    public boolean equals(byte[] bytes) {
        return Arrays.equals(magicNumber, bytes);
    }
    
    @Override
    public boolean test(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        try {
            final byte[] bytes = new byte[getLength()];
            if (inputStream.skip(getByteOffset()) != getByteOffset() || inputStream.read(bytes) != getLength()) {
                return false;
            }
            inputStream.close();
            return equals(bytes);
        } catch (Exception ex) {
            Standard.silentClose(inputStream);
            Logger.handleError(ex);
            return false;
        }
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final MagicNumber that = (MagicNumber) other;
        return byteOffset == that.byteOffset && Arrays.equals(magicNumber, that.magicNumber);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(byteOffset);
        result = 31 * result + Arrays.hashCode(magicNumber);
        return result;
    }
    
    @Override
    public String toString() {
        return "MagicNumber{" + "magicNumber=" + Arrays.toString(magicNumber) + " (" + ConvertUtil.byteArrayToHexString(magicNumber).toUpperCase() + "), byteOffset=" + byteOffset + ", magicNumberString='" + magicNumberString.replace("\t", "\\t").replace("\r", "\\r").replace("\n", "\\n") + '\'' + ", extensions=" + extensions + '}';
    }
    
    public static final MagicNumber ofHexString(String magicNumberHexString) {
        return ofHexString(magicNumberHexString, 0);
    }
    
    public static final MagicNumber ofHexString(String magicNumberHexString, long byteOffset) {
        if (magicNumberHexString != null) {
            magicNumberHexString = magicNumberHexString.replaceAll(" ", "");
        }
        return ofBytes(ConvertUtil.hexStringToByteArray(magicNumberHexString), byteOffset);
    }
    
    public static final MagicNumber ofString(String magicNumberString) {
        return ofString(magicNumberString, 0);
    }
    
    public static final MagicNumber ofString(String magicNumberString, long byteOffset) {
        return new MagicNumber(magicNumberString, byteOffset);
    }
    
    public static final MagicNumber ofBytes(byte[] magicNumber) {
        return ofBytes(magicNumber, 0);
    }
    
    public static final MagicNumber ofBytes(byte[] magicNumber, long byteOffset) {
        return new MagicNumber(magicNumber, byteOffset);
    }
    
    public static final Set<MagicNumber> resolveMagicNumbers(byte[] bytes) {
        return resolveMagicNumbers(bytes, getAll());
    }
    
    public static final Set<MagicNumber> resolveMagicNumbers(byte[] bytes, Set<MagicNumber> magicNumbers) {
        if (bytes == null || bytes.length == 0 || magicNumbers == null || magicNumbers.isEmpty()) {
            return null;
        }
        return magicNumbers.stream().filter((magicNumber_) -> magicNumber_.equals(bytes)).collect(Collectors.toSet());
    }
    
    public static final Set<MagicNumber> resolveMagicNumbers(ToughSupplier<InputStream> inputStreamSupplier) {
        return resolveMagicNumbers(inputStreamSupplier, getAll());
    }
    
    public static final Set<MagicNumber> resolveMagicNumbers(ToughSupplier<InputStream> inputStreamSupplier, Set<MagicNumber> magicNumbers) {
        if (inputStreamSupplier == null || magicNumbers == null || magicNumbers.isEmpty()) {
            return null;
        }
        return magicNumbers.stream().filter((magicNumber) -> magicNumber.test(inputStreamSupplier.getWithoutException())).collect(Collectors.toSet());
    }
    
    private static final Set<MagicNumber> mergeSets(Set<MagicNumber>... magicNumberSets) {
        final Set<MagicNumber> merged = generateMergingSet();
        for (Set<MagicNumber> magicNumberSet : magicNumberSets) {
            merged.addAll(magicNumberSet);
        }
        return merged;
    }
    
    private static final Set<MagicNumber> generateMergingSet() {
        return generateMergingSet((Collection<MagicNumber>) null);
    }
    
    private static final Set<MagicNumber> generateMergingSet(MagicNumber... magicNumbers) {
        return generateMergingSet(Arrays.asList(magicNumbers));
    }
    
    private static final Set<MagicNumber> generateMergingSet(Collection<MagicNumber> magicNumbers) {
        final Set<MagicNumber> magicNumberSet = new HashSet<MagicNumber>() {
            @Override
            public boolean add(MagicNumber magicNumber) {
                if (magicNumber == null) {
                    return false;
                }
                if (contains(magicNumber)) {
                    final Iterator<MagicNumber> iterator = iterator();
                    while (iterator.hasNext()) {
                        final MagicNumber magicNumber_ = iterator.next();
                        if (Objects.equals(magicNumber, magicNumber_)) {
                            iterator.remove();
                            return super.add(new MagicNumber(magicNumber.getMagicNumber(), magicNumber.getByteOffset()).addExtensions(magicNumber.getExtensions()).addExtensions(magicNumber_.getExtensions()));
                        }
                    }
                    return false;
                } else {
                    return super.add(magicNumber);
                }
            }
            
            @Override
            public boolean addAll(Collection<? extends MagicNumber> collection) {
                if (collection == null) {
                    return false;
                }
                if (collection.isEmpty()) {
                    return true;
                }
                return collection.stream().filter(Objects::nonNull).allMatch(this::add);
            }
        };
        if (magicNumbers != null) {
            magicNumberSet.addAll(magicNumbers);
        }
        return magicNumberSet;
    }
    
    public static Set<MagicNumber> getAllImages() {
        return Collections.unmodifiableSet(ALL_IMAGES);
    }
    
    public static Set<MagicNumber> getAllJars() {
        return Collections.unmodifiableSet(ALL_JARS);
    }
    
    public static Set<MagicNumber> getAllTars() {
        return Collections.unmodifiableSet(ALL_TARS);
    }
    
    public static Set<MagicNumber> getAllZips() {
        return Collections.unmodifiableSet(ALL_ZIPS);
    }
    
    public static Set<MagicNumber> getAll() {
        return Collections.unmodifiableSet(ALL);
    }
    
    public static boolean addImage(MagicNumber magicNumber) {
        if (magicNumber == null) {
            return false;
        }
        return ALL_IMAGES.add(magicNumber) & ALL.add(magicNumber);
    }
    
    public static boolean addJar(MagicNumber magicNumber) {
        if (magicNumber == null) {
            return false;
        }
        return ALL_JARS.add(magicNumber) & ALL.add(magicNumber);
    }
    
    public static boolean addTar(MagicNumber magicNumber) {
        if (magicNumber == null) {
            return false;
        }
        return ALL_TARS.add(magicNumber) & ALL.add(magicNumber);
    }
    
    public static boolean addZip(MagicNumber magicNumber) {
        if (magicNumber == null) {
            return false;
        }
        return ALL_ZIPS.add(magicNumber) & ALL.add(magicNumber);
    }
    
    public static boolean addAll(MagicNumber magicNumber) {
        if (magicNumber == null) {
            return false;
        }
        return ALL.add(magicNumber);
    }
    
}
