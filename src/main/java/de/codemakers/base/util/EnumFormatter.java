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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class EnumFormatter<E extends Enum> {
    
    public static final char FORMAT_ESCAPE_CHARACTER = '\\';
    public static final char FORMAT_TAG_START_CHARACTER_1 = '$';
    public static final char FORMAT_TAG_START_CHARACTER_2 = '{';
    public static final char FORMAT_TAG_END_CHARACTER = '}';
    protected final List<Object> format = new CopyOnWriteArrayList<>();
    protected final Class<E> clazz;
    protected final Map<E, Object> values = null;
    protected String formatString;
    protected boolean replaceNotExistingTagsWithNames = true;
    
    public EnumFormatter(Class<E> clazz, String formatString) {
        this.clazz = clazz;
        //this.values = Collections.synchronizedMap(new EnumMap<E, Object>(clazz));
        this.formatString = formatString;
    }
    
    private void init() {
        format.clear();
        String temp = "";
        String last = null;
        final char[] chars = formatString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == FORMAT_ESCAPE_CHARACTER) {
                temp += chars[++i];
            } else if (last == null) {
                if (c == FORMAT_TAG_START_CHARACTER_1 && i < chars.length - 1) {
                    if (chars[++i] == FORMAT_TAG_START_CHARACTER_2) {
                        format.add(temp);
                        temp = "";
                        last = "";
                    } else {
                        temp += chars[--i];
                    }
                } else if (c == FORMAT_TAG_END_CHARACTER) {
                    throw new IllegalArgumentException("Tag not started");
                } else {
                    temp += chars[i];
                }
            } else if (last != null) {
                if (c == FORMAT_TAG_START_CHARACTER_1 && i < chars.length - 1 && chars[i + 1] == FORMAT_TAG_START_CHARACTER_2) {
                    throw new IllegalArgumentException("Tag already started");
                } else if (c == FORMAT_TAG_END_CHARACTER) {
                    //format.add(new Tag<E>(last));
                    last = null;
                } else {
                    last += chars[i];
                }
            }
        }
        if (last != null) {
            throw new IllegalArgumentException("Tag was not ended");
        } else if (!temp.isEmpty()) {
            format.add(temp);
        }
    }
    
    public boolean reset() {
        values.clear();
        return values.isEmpty();
    }
    
    @Override
    public String toString() {
        return format.stream().map(StringUtil::toString).collect(Collectors.joining());
    }
    
    public String toDebugString() {
        return format.stream().map((object) -> {
            if (object instanceof StringFormatter.Tag) {
                final StringFormatter.Tag tag = (StringFormatter.Tag) object;
                return tag.toFullyString();
            } else {
                return "" + object;
            }
        }).collect(Collectors.joining());
    }
    
    public String getFormatString() {
        return formatString;
    }
    
    public EnumFormatter<E> setFormatString(String formatString) {
        this.formatString = formatString;
        init();
        return this;
    }
    
    public List<Object> getFormat() {
        return format;
    }
    
    public List<Tag<E>> getTags() {
        return format.stream().filter(Objects::nonNull).filter((object) -> object instanceof Tag).map((object) -> (Tag<E>) object).collect(Collectors.toList());
    }
    
    public Map<E, Object> getValues() {
        return values;
    }
    
    public EnumFormatter<E> setValue(E e, Object value) {
        values.put(e, value);
        return this;
    }
    
    public Object getValue(E e) {
        return values.get(e);
    }
    
    public Object getOrDefault(E e, Object defaultValue) {
        return values.getOrDefault(e, defaultValue);
    }
    
    protected String getValueForTag(Tag<E> tag) {
        if (replaceNotExistingTagsWithNames) {
            return "" + values.getOrDefault(tag.getName(), tag.toFullyString());
        } else {
            return "" + values.getOrDefault(tag.getName(), "");
        }
    }
    
    public EnumFormatter<E> removeValue(E e) {
        values.remove(e);
        return this;
    }
    
    public boolean isReplaceNotExistingTagsWithNames() {
        return replaceNotExistingTagsWithNames;
    }
    
    public EnumFormatter<E> setReplaceNotExistingTagsWithNames(boolean replaceNotExistingTagsWithNames) {
        this.replaceNotExistingTagsWithNames = replaceNotExistingTagsWithNames;
        return this;
    }
    
    public class Tag<E extends Enum> {
        
        private final E e;
        
        public Tag(E e) {
            this.e = e;
        }
        
        public String getName() {
            return e.name();
        }
        
        public String toFullyString() {
            return "" + FORMAT_TAG_START_CHARACTER_1 + FORMAT_TAG_START_CHARACTER_2 + getName() + FORMAT_TAG_END_CHARACTER;
        }
        
        @Override
        public String toString() {
            //return EnumFormatter<E>.this.getValueForTag(this);
            return "";
        }
        
    }
    
}
