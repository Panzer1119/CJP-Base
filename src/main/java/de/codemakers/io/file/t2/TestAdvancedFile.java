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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestAdvancedFile {
    
    public static final List<AdvancedProvider> PROVIDERS = new ArrayList<>();
    
    static {
        PROVIDERS.add(null);
    }
    
    private String[] paths;
    private TestAdvancedFile parent;
    private AdvancedProvider provider = null;
    private String path = null;
    
    public TestAdvancedFile(String... paths) {
        this(null, paths);
    }
    
    public TestAdvancedFile(TestAdvancedFile parent, String... paths) {
        this(parent, null, paths);
        init();
    }
    
    public TestAdvancedFile(TestAdvancedFile parent, AdvancedProvider provider, String... paths) {
        this.parent = parent;
        this.provider = provider;
        this.paths = paths;
    }
    
    public static final AdvancedProvider getProvider(TestAdvancedFile parent, String name) {
        return PROVIDERS.stream().filter((advancedProvider) -> advancedProvider.accept(parent, name)).findFirst().orElse(null);
    }
    
    private final void init() {
        TestAdvancedFile advancedFile = null;
        for (String p : paths) {
            final AdvancedProvider advancedProvider = getProvider(advancedFile, p);
            if (advancedProvider != null) {
                advancedFile = new TestAdvancedFile(advancedFile, advancedProvider, p);
            } else {
                advancedFile.paths = Arrays.copyOf(advancedFile.paths, advancedFile.paths.length + 1);
                advancedFile.paths[advancedFile.paths.length - 1] = p;
            }
        }
    }
    
    @Override
    public final String toString() {
        return "TestAdvancedFile{" + "paths=" + Arrays.toString(paths) + ", parent=" + parent + ", provider=" + provider + ", path='" + path + '\'' + '}';
    }
    
}
