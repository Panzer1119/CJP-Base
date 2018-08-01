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

import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZIPProvider extends FileProvider {
    
    private PathEntry parent;
    
    public ZIPProvider(PathEntry parent) {
        this.parent = parent;
    }
    
    @Override
    public List<TestAdvancedFile> listFiles() {
        return null;
    }
    
    @Override
    public byte[] readFile(String path) {
        //return new byte[0];
        try {
            final double r = Math.random();
            System.out.println(r + " parent: " + parent);
            System.out.println(r + " path: " + path);
            final ZipFile zipFile = new ZipFile(parent.toPathString("/"));
            System.out.println(r + " zipFile: " + zipFile);
            final ZipEntry zipEntry = zipFile.getEntry(path);
            System.out.println(r + " zipEntry: " + zipEntry);
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
            System.out.println(r + " bufferedInputStream: " + bufferedInputStream);
            byte[] data = new byte[0];
            final byte[] buffer = new byte[64];
            int read = -1;
            while ((read = bufferedInputStream.read(buffer)) != -1) {
                data = Arrays.copyOf(data, data.length + read);
                System.arraycopy(buffer, 0, data, data.length - read, read);
            }
            bufferedInputStream.close();
            zipFile.close();
            return data;
            //return Files.readAllBytes(new File(parent.toPathString("/") + "/" + path).toPath());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean writeFile(String path, byte[] data) {
        return false;
    }
    
    @Override
    public boolean createFile(String path) {
        return false;
    }
    
    @Override
    public boolean deleteFile(String path) {
        return false;
    }
    
}
