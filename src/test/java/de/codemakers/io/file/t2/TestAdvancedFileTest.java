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

package de.codemakers.io.file.t2;

public class TestAdvancedFileTest {
    
    public static final void main(String[] args) throws Exception {
        System.out.println("test");
        //final AdvancedFile advancedFile = new AdvancedFile("test/1/2/win_2.zip/win.txt.zip/win.txt");
        final AdvancedFile advancedFile = new AdvancedFile("test/1/2/3.zip/3/win.txt.zip/win.txt");
        System.out.println(advancedFile);
        System.out.println("=======================================================================================================================================================");
        System.out.println(new String(advancedFile.readBytes()));
        System.out.println("=======================================================================================================================================================");
        System.out.println("=======================================================================================================================================================");
        System.out.println("=======================================================================================================================================================");
        final AdvancedFile advancedFile_1 = new AdvancedFile("test/1/2/3.zip");
        System.out.println(advancedFile_1);
        System.out.println("=======================================================================================================================================================");
        System.out.println("=======================================================================================================================================================");
        System.out.println("Recursive: ");
        advancedFile_1.listFiles(true).stream().map(AdvancedFile::getPathString).forEach(System.out::println);
        System.out.println("=======================================================================================================================================================");
        System.out.println("=======================================================================================================================================================");
        System.out.println("Not Recursive: ");
        advancedFile_1.listFiles(false).stream().map(AdvancedFile::getPathString).forEach(System.out::println);
        System.out.println("=======================================================================================================================================================");
        System.out.println("=======================================================================================================================================================");
        System.out.println("=======================================================================================================================================================");
        final AdvancedFile advancedFile_2 = new AdvancedFile("test/1/2/3.zip/3/win.txt.zip");
        System.out.println(advancedFile_2);
        System.out.println("=======================================================================================================================================================");
        advancedFile_2.listFiles(true).stream().map(AdvancedFile::getPathString).forEach(System.out::println);
        final AdvancedFile advancedFile__1 = new AdvancedFile("test/1/2");
        System.out.println("advancedFile__1 = " + advancedFile__1);
        final AdvancedFile advancedFile__2 = new AdvancedFile("test\\1\\2");
        System.out.println("advancedFile__2 = " + advancedFile__2);
        final AdvancedFile advancedFile__3 = new AdvancedFile("extern:test/1/2");
        System.out.println("advancedFile__3 = " + advancedFile__3);
        final AdvancedFile advancedFile__4 = new AdvancedFile("intern:test/1/2");
        System.out.println("advancedFile__4 = " + advancedFile__4);
        final AdvancedFile advancedFile__5 = new AdvancedFile("test/1\\2");
        System.out.println("advancedFile__5 = " + advancedFile__5);
    }
    
}
