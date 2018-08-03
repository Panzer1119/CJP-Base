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

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {
    
    public static final String ZIP_FILE_STRING = "test/1/2/3.zip";
    
    public static final void main(String[] args) throws Exception {
        listZipFile(new FileInputStream(ZIP_FILE_STRING), null);
    }
    
    public static final void listZipFile(InputStream inputStream, String prefix) throws Exception {
        Objects.requireNonNull(inputStream);
        if (prefix == null) {
            prefix = "";
        }
        final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            System.out.println(prefix + zipEntry);
            final String name = zipEntry.getName();
            if (name.toLowerCase().endsWith(".zip")) {
                final byte[] data = IOUtils.toByteArray(zipInputStream);
                zipInputStream.read(data);
                listZipFile(new ByteArrayInputStream(data), prefix + "\t");
            }
        }
        zipInputStream.close();
    }
    
}
