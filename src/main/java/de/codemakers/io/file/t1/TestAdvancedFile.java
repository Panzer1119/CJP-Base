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

public class TestAdvancedFile {

    public static final String FILE_SEPARATOR_WINDOWS = "\\";
    public static final String FILE_SEPARATOR_UNIX = "/";
    
    private PathEntry path = null;
    private final String separator;
    
    public TestAdvancedFile(String path) {
        boolean isUnix = path.contains(FILE_SEPARATOR_UNIX);
        if (isUnix && !path.contains(FILE_SEPARATOR_WINDOWS)) {
            separator = FILE_SEPARATOR_UNIX;
        } else if (!isUnix && path.contains(FILE_SEPARATOR_WINDOWS)) {
            separator = FILE_SEPARATOR_WINDOWS;
        } else {
            throw new RuntimeException("Path may not contain Unix and Windows file separators at the same time");
        }
        final String[] split = path.split(isUnix ? separator : separator + separator);
        for (String p : split) {
            if (this.path == null) {
                this.path = new PathEntry(p, false);
            } else {
                this.path = new PathEntry(this.path, p, p.contains("."));
            }
        }
    }
    
    public final PathEntry getPath() {
        return path;
    }
    
    public final String getSeparator() {
        return separator;
    }
    
    public final String getPathString() {
        return path.toPathString(getSeparator());
    }
    
    @Override
    public String toString() {
        return "TestAdvancedFile{" + "path=" + path + ", separator='" + separator + '\'' + '}';
    }
    
}
