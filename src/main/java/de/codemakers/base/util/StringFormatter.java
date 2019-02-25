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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class StringFormatter {
    
    public static final char FORMAT_ESCAPE_CHARACTER = '\\';
    public static final char FORMAT_TAG_START_CHARACTER_1 = '$';
    public static final char FORMAT_TAG_START_CHARACTER_2 = '{';
    public static final char FORMAT_TAG_END_CHARACTER = '}';
    protected final List<Object> format = new CopyOnWriteArrayList<>();
    protected final Map<String, Object> values = new HashMap<>();
    protected String formatString;
    protected boolean replaceNotExistingTagsWithNames = true;
    
    public StringFormatter(String formatString) {
        setFormatString(formatString);
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
                    format.add(new Tag(last));
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
            if (object instanceof Tag) {
                final Tag tag = (Tag) object;
                return tag.toFullyString();
            } else {
                return "" + object;
            }
        }).collect(Collectors.joining());
    }
    
    public String getFormatString() {
        return formatString;
    }
    
    public StringFormatter setFormatString(String formatString) {
        this.formatString = formatString;
        init();
        return this;
    }
    
    public List<Object> getFormat() {
        return format;
    }
    
    public List<Tag> getTags() {
        return format.stream().filter((object) -> object instanceof Tag).map((object) -> (Tag) object).collect(Collectors.toList());
    }
    
    public Map<String, Object> getValues() {
        return values;
    }
    
    public StringFormatter setValue(String name, Object value) {
        values.put(name, value);
        return this;
    }
    
    public Object getValue(String name) {
        return values.get(name);
    }
    
    public Object getOrDefault(String name, Object defaultValue) {
        return values.getOrDefault(name, defaultValue);
    }
    
    protected String getValueForTag(Tag tag) {
        if (replaceNotExistingTagsWithNames) {
            return "" + values.getOrDefault(tag.getName(), tag.toFullyString());
        } else {
            return "" + values.getOrDefault(tag.getName(), "");
        }
    }
    
    public StringFormatter removeValue(String name) {
        values.remove(name);
        return this;
    }
    
    public boolean isReplaceNotExistingTagsWithNames() {
        return replaceNotExistingTagsWithNames;
    }
    
    public StringFormatter setReplaceNotExistingTagsWithNames(boolean replaceNotExistingTagsWithNames) {
        this.replaceNotExistingTagsWithNames = replaceNotExistingTagsWithNames;
        return this;
    }
    
    public class Tag {
        
        private final String name;
        
        public Tag(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public String toFullyString() {
            return "" + FORMAT_TAG_START_CHARACTER_1 + FORMAT_TAG_START_CHARACTER_2 + name + FORMAT_TAG_END_CHARACTER;
        }
        
        @Override
        public String toString() {
            return StringFormatter.this.getValueForTag(this);
        }
        
    }
    
}
