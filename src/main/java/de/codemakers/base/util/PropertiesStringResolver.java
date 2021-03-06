package de.codemakers.base.util;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.interfaces.StringResolver;
import de.codemakers.io.file.AdvancedFile;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesStringResolver implements StringResolver {
    
    public static final String DEFAULT_TAG_START = ":";
    public static final String DEFAULT_TAG_END = ":";
    
    protected Properties properties = null;
    private String tagStart = DEFAULT_TAG_START;
    private String tagEnd = DEFAULT_TAG_END;
    private AdvancedFile advancedFile = null;
    
    public PropertiesStringResolver(AdvancedFile advancedFile) {
        this.advancedFile = advancedFile;
        loadFromFile();
    }
    
    public PropertiesStringResolver() {
        this(new Properties());
    }
    
    public PropertiesStringResolver(Properties properties) {
        setProperties(properties);
    }
    
    private void loadFromFile() {
        final AdvancedFile advancedFile = getAdvancedFile();
        if (advancedFile == null) {
            return;
        }
        Properties properties = getProperties();
        if (properties == null) {
            properties = new Properties();
            setProperties(properties);
        }
        advancedFile.createInputStreamClosingAction().consumeWithoutException(properties::load);
    }
    
    public Properties getProperties() {
        return properties;
    }
    
    public StringResolver setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }
    
    public AdvancedFile getAdvancedFile() {
        return advancedFile;
    }
    
    public StringResolver setAdvancedFile(AdvancedFile advancedFile) {
        this.advancedFile = advancedFile;
        return this;
    }
    
    public String getTagStart() {
        return tagStart;
    }
    
    public StringResolver setTagStart(String tagStart) {
        this.tagStart = tagStart;
        return this;
    }
    
    public String getTagEnd() {
        return tagEnd;
    }
    
    public StringResolver setTagEnd(String tagEnd) {
        this.tagEnd = tagEnd;
        return this;
    }
    
    @Override
    public String resolve(String input) {
        String output = input;
        final Properties properties = getProperties();
        if (properties != null) {
            final String tagStart = getTagStart();
            final String tagEnd = getTagEnd();
            for (String key : properties.stringPropertyNames()) {
                final String toReplace = Pattern.quote(tagStart + key + tagEnd);
                final String replacement = Matcher.quoteReplacement(properties.getProperty(key, ""));
                output = output.replaceAll(toReplace, replacement);
            }
            final Matcher matcher = Pattern.compile(Pattern.quote(tagStart) + ".*" + Pattern.quote(tagEnd)).matcher(output);
            if (matcher.find()) {
                Logger.logWarning(String.format("Could not fully resolve the String \"%s\", only resolved it to \"%s\"", input, output)); //DEBUG
                return null;
            }
        }
        return output;
    }
    
    @Override
    public String toString() {
        return "PropertiesStringResolver{" + "properties=" + properties + ", tagStart='" + tagStart + '\'' + ", tagEnd='" + tagEnd + '\'' + ", advancedFile=" + advancedFile + '}';
    }
    
}
