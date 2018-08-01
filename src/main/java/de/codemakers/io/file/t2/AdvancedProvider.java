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

import java.util.List;

public abstract class AdvancedProvider {
    
    public abstract List<TestAdvancedFile> listFiles(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath);
    
    public abstract byte[] readBytes(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath);
    
    public abstract byte[] readBytes(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath, byte[] data_parent);
    
    public abstract boolean writeBytes(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath, byte[] data);
    
    public abstract boolean createFile(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath);
    
    public abstract boolean deleteFile(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath);
    
    public abstract boolean mkdir(TestAdvancedFile parent, TestAdvancedFile advancedFile, String[] subPath);
    
    public abstract boolean accept(TestAdvancedFile parent, String name, String name_lower, String name_upper);
    
    @Override
    public String toString() {
        return "AdvancedProvider{}";
    }
    
}
