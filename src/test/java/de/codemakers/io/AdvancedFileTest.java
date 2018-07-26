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

package de.codemakers.io;

import de.codemakers.io.file.AdvancedFile;

public class AdvancedFileTest {
    
    public static final void main(String[] args) throws Exception {
        final AdvancedFile file = new AdvancedFile();
        System.out.println(file);
        final AdvancedFile file_1 = new AdvancedFile("");
        System.out.println(file_1);
        final AdvancedFile file_2 = new AdvancedFile("test");
        System.out.println(file_2);
        final AdvancedFile file_3 = new AdvancedFile("test.txt");
        System.out.println(file_3);
        final AdvancedFile file_4 = new AdvancedFile("E:\\Daten");
        System.out.println(file_4);
        final AdvancedFile file_9 = new AdvancedFile("E:\\Daten", "IntelliJ");
        System.out.println(file_9);
        final AdvancedFile file_5 = new AdvancedFile("jar:temp.txt");
        System.out.println(file_5);
        final AdvancedFile file_6 = new AdvancedFile("jar:resources/temp.txt");
        System.out.println(file_6);
        final AdvancedFile file_7 = new AdvancedFile("jar:/resources/temp.txt");
        System.out.println(file_7);
        final AdvancedFile file_8 = new AdvancedFile("jar:/resources", "temp.txt");
        System.out.println(file_8);
    }
    
}
