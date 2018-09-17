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

import java.io.File;

public class ZIPProviderTest {
    
    public static final void main(String[] args) {
        AdvancedFile.DEBUG = true;
        AdvancedFile.DEBUG_TO_STRING = true;
        AdvancedFile.DEBUG_FILE_PROVIDER = false;
        final File file_1 = new File("test/1/2/3.zip");
        final AdvancedFile advancedFile_file_1 = new AdvancedFile(new AdvancedFile(file_1), "3/win.txt.zip", "win.txt");
        System.out.println("advancedFile_file_1: " + advancedFile_file_1);
        System.out.println("advancedFile_file_1 exists: " + advancedFile_file_1.exists());
        System.out.println("advancedFile_file_1 readBytes: " + new String(advancedFile_file_1.readBytesWithoutException()));
    }
    
}
