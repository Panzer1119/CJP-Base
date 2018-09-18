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

package de.codemakers.io.file.closeable;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CloseableZipInputStreamEntry extends CloseableZipEntry<ZipInputStream> {
    
    public CloseableZipInputStreamEntry(ZipInputStream zipInputStream, ZipEntry zipEntry) {
        super(zipInputStream, zipEntry);
    }
    
    public final ZipInputStream getZipInputStream() {
        return getCloseable();
    }
    
    @Override
    public void preClose(ZipInputStream closeable, ZipEntry data) throws IOException {
        if (closeable != null) {
            if (data != null) {
                closeable.closeEntry();
            }
        }
    }
    
}
