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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class StringFormatter {
    
    public static final char FORMAT_ESCAPE_CHARACTER = '\\';
    public static final char FORMAT_TAG_START_CHARACTER = '[';
    public static final char FORMAT_TAG_END_CHARACTER = ']';
    
    protected final String formatString;
    protected final List<Object> format = new CopyOnWriteArrayList<>();
    protected final Map<String, Object> values = new ConcurrentHashMap<>();
    protected boolean replaceNotExistingTagsWithNames = true;
    
    public StringFormatter(String formatString) {
        this.formatString = formatString;
        init();
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
                if (c == FORMAT_TAG_START_CHARACTER) {
                    format.add(temp);
                    temp = "";
                    last = "";
                } else if (c == FORMAT_TAG_END_CHARACTER) {
                    throw new IllegalArgumentException("Tag not started");
                } else {
                    temp += chars[i];
                }
            } else if (last != null) {
                if (c == FORMAT_TAG_START_CHARACTER) {
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
                return FORMAT_TAG_START_CHARACTER + tag.getName() + FORMAT_TAG_END_CHARACTER;
            } else {
                return "" + object;
            }
        }).collect(Collectors.joining());
    }
    
    public String getFormatString() {
        return formatString;
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
            return "" + values.getOrDefault(tag.getName(), FORMAT_TAG_START_CHARACTER + tag.getName() + FORMAT_TAG_END_CHARACTER);
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
        
        @Override
        public String toString() {
            return StringFormatter.this.getValueForTag(this);
        }
        
    }
    
}
