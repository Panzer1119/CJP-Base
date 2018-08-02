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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FolderToFileTest {
    
    public static final void main(String[] args) {
        Objects.requireNonNull(args.length == 0 ? null : "");
        final File folder = new File(args[0]);
        File output = new File(folder.getParentFile().getAbsolutePath() + File.separator + folder.getName() + ".txt");
        if (args.length >= 2) {
            output = new File(args[1]);
        }
        final List<Byte> buffer = new ArrayList<>();
        listFolder(folder, buffer);
    }
    
    public static final void listFolder(File folder, List<Byte> buffer) {
    
    }
    
    public static final byte[] folderToBytes(File folder) {
        return null;
    }
    
    public static final byte[] fileToBytes(File file) {
        return null;
    }
    
}
