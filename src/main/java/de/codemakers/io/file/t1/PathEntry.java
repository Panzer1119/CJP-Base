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

public class PathEntry {
    
    public final String name;
    public final boolean file;
    public PathEntry parent;
    public FileProvider provider;
    
    public PathEntry(String name, boolean file) {
        this(null, name, file);
    }
    
    public PathEntry(PathEntry parent, String name, boolean file) {
        this.parent = parent;
        this.name = name;
        this.file = file;
        if (name.toLowerCase().endsWith(".zip")) {
            provider = new ZIPProvider(this);
        }
    }
    
    public final PathEntry getParent() {
        return parent;
    }
    
    public final String getName() {
        return name;
    }
    
    public final boolean isFile() {
        return file;
    }
    
    public final FileProvider getProvider() {
        return provider;
    }
    
    public final PathEntry setProvider(FileProvider provider) {
        this.provider = provider;
        return this;
    }
    
    public final String toPathString(String separator) {
        if (parent == null) {
            return name;
        }
        return parent.toPathString(separator) + separator + name;
    }
    
    public final PathEntry subtract(PathEntry pathEntry) {
        final PathEntry this_ = copy();
        PathEntry temp = this_;
        System.out.println("pathEntry: " + pathEntry.toPathString("/"));
        System.out.println("this     : " + this.toPathString("/"));
        System.out.println("this_    : " + this_.toPathString("/"));
        System.out.println("temp     : " + temp.toPathString("/"));
        while (temp.parent != null && !temp.parent.name.equals(pathEntry.name)) {
            System.out.println("VOR  temp = " + temp);
            temp = temp.parent;
            System.out.println("NACH temp = " + temp);
        }
        temp.parent = null;
        return this_;
    }
    
    public final PathEntry copy() {
        if (parent == null) {
            return new PathEntry(name, file);
        } else {
            return new PathEntry(parent.copy(), name, file);
        }
    }
    
    @Override
    public final String toString() {
        return "PathEntry{" + "parent=" + parent + ", name='" + name + '\'' + ", file=" + file + '}';
    }
    
}
