/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
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

package de.codemakers.io.file;

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;

import java.util.stream.Collectors;

public class AdvancedFileInternTest {
    
    public static final void main(String[] args) {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINEST);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendThread().appendText(": ").appendObject().appendNewLine().appendLocation().appendNewLine().finishWithoutException();
        AdvancedFile.DEBUG = true;
        AdvancedFile.DEBUG_FILE_PROVIDER = false;
        AdvancedFile.DEBUG_TO_STRING = true;
        AdvancedFile.DEBUG_TO_STRING_BIG = false;
        Logger.logDebug(new AdvancedFile(""));
        Logger.logDebug(new AdvancedFile("").getPath());
        Logger.logDebug(new AdvancedFile("").getAbsoluteFile().getPath());
        Logger.logDebug(new AdvancedFile("").getAbsoluteFile().exists());
        Logger.logDebug(new AdvancedFile("").getAbsoluteFile().listFiles(false));
        /*
        //Not useful on Windows (Because windows has not THE root folder, it uses multiple drive letters)
        Logger.logDebug(new AdvancedFile("/"));
        Logger.logDebug(new AdvancedFile("/").getPath());
        Logger.logDebug(new AdvancedFile("/").exists());
        Logger.logDebug(new AdvancedFile("/").listFiles(false));
        */
        Logger.logDebug(new AdvancedFile("intern:/"));
        Logger.logDebug(new AdvancedFile("intern:/").getPath());
        Logger.logDebug(new AdvancedFile("intern:/").listFiles(false));
        Logger.logDebug(new AdvancedFile("intern:/").listFiles(false).stream().map(AdvancedFile::getPath).collect(Collectors.joining(", ", "[", "]")));
        Logger.logDebug(new AdvancedFile("intern:/de").listFiles(false));
        Logger.logDebug(new AdvancedFile("intern:/de").listFiles(false).stream().map(AdvancedFile::getPath).collect(Collectors.joining(", ", "[", "]")));
        Logger.logDebug(new AdvancedFile("intern:/de/codemakers").listFiles(false));
        Logger.logDebug(new AdvancedFile("intern:/de/codemakers").listFiles(false).stream().map(AdvancedFile::getPath).collect(Collectors.joining(", ", "[", "]")));
        final AdvancedFile advancedFile_file_1 = new AdvancedFile("intern:/de/codemakers/io/file/" + AdvancedFileInternTest.class.getSimpleName() + ".class");
        System.out.println("advancedFile_file_1: " + advancedFile_file_1);
        System.out.println("advancedFile_file_1 exists: " + advancedFile_file_1.exists());
        final byte[] data_file_1 = advancedFile_file_1.readBytesWithoutException();
        System.out.println("data_file_1: " + data_file_1);
        //System.out.println("data_file_1: " + Arrays.toString(data_file_1));
        //System.out.println("advancedFile_file_1 readBytes: " + new String(data_file_1));
        final AdvancedFile advancedFile_folder_1 = new AdvancedFile("intern:/de/codemakers/io/file");
        System.out.println("advancedFile_folder_1: " + advancedFile_folder_1);
        System.out.println("advancedFile_folder_1 exists: " + advancedFile_folder_1.exists());
        System.out.println("advancedFile_folder_1 listFiles(false): " + advancedFile_folder_1.listFiles(false));
        System.out.println("advancedFile_folder_1 listFiles(true): " + advancedFile_folder_1.listFiles(true));
        final AdvancedFile advancedFile_file_2 = new AdvancedFile("intern:" + AdvancedFileTest.class.getSimpleName() + ".class");
        System.out.println("advancedFile_file_2: " + advancedFile_file_2);
        System.out.println("advancedFile_file_2 absoluteFile: " + advancedFile_file_2.getAbsoluteFile());
        System.out.println("advancedFile_file_2 exists: " + advancedFile_file_2.exists());
        System.out.println("advancedFile_file_2 isFile: " + advancedFile_file_2.isFile());
        System.out.println("advancedFile_file_2 isDirectory: " + advancedFile_file_2.isDirectory());
        final AdvancedFile advancedFile_folder_2 = new AdvancedFile("test/test_1");
        System.out.println("advancedFile_folder_2: " + advancedFile_folder_2);
        advancedFile_folder_2.mkdirsWithoutException();
        System.out.println("advancedFile_folder_2 exists: " + advancedFile_folder_2.exists());
        System.out.println("advancedFile_folder_2 listFiles: " + advancedFile_folder_2.listFiles());
        advancedFile_folder_2.listFiles().stream().filter(AdvancedFile::isFile).forEach((advancedFile) -> System.out.println(new String(advancedFile.readBytesWithoutException())));
        System.out.println("########");
        final AdvancedFile advancedFile_folder_3 = new AdvancedFile("../CJP-Base/test/test_1");
        System.out.println("advancedFile_folder_3: " + advancedFile_folder_3);
        System.out.println("advancedFile_folder_3 exists: " + advancedFile_folder_3.exists());
        System.out.println("advancedFile_folder_3 listFiles: " + advancedFile_folder_3.listFiles());
        advancedFile_folder_3.listFiles().stream().filter(AdvancedFile::isFile).forEach((advancedFile) -> System.out.println(new String(advancedFile.readBytesWithoutException())));
        System.out.println("########");
        final AdvancedFile advancedFile_file_3 = new AdvancedFile(advancedFile_folder_2, "test.txt");
        advancedFile_file_3.writeBytesWithoutException(("Test " + Math.random()).getBytes());
        System.out.println(advancedFile_file_3);
    }
    
}
