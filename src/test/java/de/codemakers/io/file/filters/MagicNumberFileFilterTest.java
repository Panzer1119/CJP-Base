package de.codemakers.io.file.filters;

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.ConvertUtil;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.file.MagicNumber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MagicNumberFileFilterTest {
    
    @BeforeAll
    public void setup() {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINER);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendLogLevel().appendText(": ").appendObject().appendNewLine().appendSource().finishWithoutException();
        //AdvancedFile.DEBUG = true;
        //AdvancedFile.DEBUG_TO_STRING = true;
    }
    
    @Disabled
    @Test
    public void test1() {
        final String hexString1 = "57696E5A6970".toLowerCase();
        final byte[] bytes1 = new byte[] {87, 105, 110, 90, 105, 112};
        assertArrayEquals(bytes1, ConvertUtil.hexStringToByteArray(hexString1));
        assertEquals(hexString1, ConvertUtil.byteArrayToHexString(bytes1));
        final String hexString2 = "504B030414000100".toLowerCase();
        final byte[] bytes2 = new byte[] {80, 75, 3, 4, 20, 0, 1, 0};
        assertArrayEquals(bytes2, ConvertUtil.hexStringToByteArray(hexString2));
        assertEquals(hexString2, ConvertUtil.byteArrayToHexString(bytes2));
        assertEquals("ustar", MagicNumber.ofHexString("75 73 74 61 72").getMagicNumberString());
        assertEquals("ustar", MagicNumber.ofHexString("7573746172").getMagicNumberString());
        assertEquals("PKLITE", MagicNumber.ofHexString("50 4B 4C 49 54 45").getMagicNumberString());
    }
    
    @Disabled
    @Test
    public void test2() {
        final AdvancedFile jarFile = new AdvancedFile("test", "DummyMod.jar");
        Logger.logDebug("jarFile='" + jarFile + "'");
        final Set<MagicNumber> magicNumbers1 = MagicNumber.resolveMagicNumbers(jarFile::createInputStream);
        assertNotNull(magicNumbers1);
        Logger.logDebug("magicNumbers1=" + magicNumbers1.stream().map(Objects::toString).collect(Collectors.joining("\n", "\n", "\n")));
        final AdvancedFile png_in_jarFile = new AdvancedFile(jarFile, "res", "planets_1.png");
        final Set<MagicNumber> magicNumbers2 = MagicNumber.resolveMagicNumbers(png_in_jarFile::createInputStream);
        assertNotNull(magicNumbers2);
        Logger.logDebug("magicNumbers2=" + magicNumbers2.stream().map(Objects::toString).collect(Collectors.joining("\n", "\n", "\n")));
    }
    
    @Disabled
    @Test
    public void test3() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        final AdvancedFile jarFile = new AdvancedFile("test", "DummyMod.jar");
        Logger.logDebug("jarFile='" + jarFile + "'");
        assertTrue(jarFile.exists());
        assertTrue(jarFile.isFile());
        Logger.logDebug("MagicNumberFileFilter.ALL_JARS=" + MagicNumberFileFilter.ALL_JARS);
        assertTrue(MagicNumberFileFilter.ALL_JARS.test(jarFile));
        System.out.println();
        System.out.println();
        Logger.logDebug("jarFile.listFiles(true)=" + jarFile.listFiles(true));
        System.out.println();
        System.out.println();
        Logger.logDebug("jarFile.listFiles(false)=" + jarFile.listFiles(false));
    }
    
    @Disabled
    @Test
    public void test4() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        final AdvancedFile jarFile = new AdvancedFile("test", "DummyModWithoutDotJar");
        Logger.logDebug("jarFile='" + jarFile + "'");
        assertTrue(jarFile.exists());
        assertTrue(jarFile.isFile());
        Logger.logDebug("MagicNumberFileFilter.ALL_JARS=" + MagicNumberFileFilter.ALL_JARS);
        assertTrue(MagicNumberFileFilter.ALL_JARS.test(jarFile));
        System.out.println();
        System.out.println();
        Logger.logDebug("jarFile.listFiles(true)=" + jarFile.listFiles(true));
        System.out.println();
        System.out.println();
        Logger.logDebug("jarFile.listFiles(false)=" + jarFile.listFiles(false));
    }
    
}
