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

package de.codemakers.io.file;

public class AdvancedFileInternTest {
    
    public static final void main(String[] args) {
        AdvancedFile.DEBUG = true;
        AdvancedFile.DEBUG_FILE_PROVIDER = true;
        AdvancedFile.DEBUG_TO_STRING = true;
        AdvancedFile.DEBUG_TO_STRING_BIG = false;
        final AdvancedFile advancedFile_file_1 = new AdvancedFile("intern:/de/codemakers/io/file/t3/" + AdvancedFileInternTest.class.getSimpleName() + ".class");
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
