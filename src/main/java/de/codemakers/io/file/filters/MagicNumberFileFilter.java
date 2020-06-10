package de.codemakers.io.file.filters;

import de.codemakers.io.file.IFile;
import de.codemakers.io.file.MagicNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class MagicNumberFileFilter implements Predicate<IFile<?, ?>> {
    
    public static final MagicNumberFileFilter ALL_JARS = new MagicNumberFileFilter(MagicNumber.getAllJars());
    public static final MagicNumberFileFilter ALL_TARS = new MagicNumberFileFilter(MagicNumber.getAllTars());
    public static final MagicNumberFileFilter ALL_ZIPS = new MagicNumberFileFilter(MagicNumber.getAllZips());
    
    private final List<MagicNumber> magicNumbers = new ArrayList<>();
    
    public MagicNumberFileFilter(MagicNumber... magicNumbers) {
        this(Arrays.asList(magicNumbers));
    }
    
    public MagicNumberFileFilter(Collection<MagicNumber> magicNumbers) {
        this.magicNumbers.addAll(magicNumbers);
    }
    
    @Override
    public boolean test(IFile<?, ?> iFile) {
        if (iFile == null || !iFile.exists() || !iFile.isFile()) {
            return false;
        }
        return magicNumbers.stream().anyMatch((magicNumber) -> magicNumber.test(iFile.createInputStreamWithoutException()));
    }
    
    @Override
    public String toString() {
        return "MagicNumberFileFilter{" + "magicNumbers=" + magicNumbers + '}';
    }
    
}
