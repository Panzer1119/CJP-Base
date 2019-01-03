/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
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

package de.codemakers.base.util;

import de.codemakers.base.exceptions.CJPRuntimeException;

public class Require {
    
    public static final <T> T clazz(Object object, Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        if (object == null) {
            return null;
        }
        final boolean match = clazz.isAssignableFrom(object.getClass());
        if (!match) {
            throw new CJPRuntimeException(object + " does not match " + clazz);
        }
        return (T) object;
    }
    
}
