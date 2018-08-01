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

package de.codemakers.io.file.t1;

import java.io.File;

public class TestAdvancedFileTest {
    
    public static final void main(String[] args) throws Exception {
        final TestAdvancedFile testAdvancedFile = new TestAdvancedFile(new File("test.txt").getAbsolutePath());
        System.out.println(testAdvancedFile);
        System.out.println(testAdvancedFile.getPath().toPathString(testAdvancedFile.getSeparator()));
        System.out.println(testAdvancedFile.getPathString());
        
        final PathEntry pathEntry_1 = new PathEntry(new PathEntry(new PathEntry("test_1", false), "test_2", false), "test.txt", true);
        final PathEntry pathEntry_2 = new PathEntry("test_1", false);
        System.out.println("pathEntry_1 = " + pathEntry_1);
        System.out.println("pathEntry_2 = " + pathEntry_2);
        System.out.println(pathEntry_1.subtract(pathEntry_2));
        
        final TestAdvancedFile testAdvancedFile_1 = new TestAdvancedFile(new File("test/1/2/win.txt.zip").getAbsolutePath());
        System.out.println(testAdvancedFile_1);
        System.out.println(new String(testAdvancedFile_1.toBytes()));
        
        System.out.println(testAdvancedFile_1.getPath());
    }
    
}
