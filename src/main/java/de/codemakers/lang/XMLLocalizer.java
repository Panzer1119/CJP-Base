/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.lang;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.XMLUtil;
import de.codemakers.base.util.interfaces.Copyable;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughSupplier;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.io.file.exceptions.isnot.FileIsNotExistingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Objects;

public class XMLLocalizer extends FileLocalizer {
    
    public static final String KEY_LANGUAGE_TAG = "languageTag";
    public static final String KEY_LANGUAGE_NAME_ENGLISH = "languageNameEnglish";
    public static final String KEY_LANGUAGE_NAME_LOCAL = "languageNameLocal";
    
    public static final String TAG_NAME_INFO = "info";
    public static final String TAG_NAME_TRANSLATION = "translation";
    
    public static final String ATTRIBUTE_NAME_NAME = "name";
    
    private Document document;
    // Elements
    private transient Element rootElement = null;
    private transient Element infoElement = null;
    
    public XMLLocalizer(AdvancedFile advancedFile) {
        super(advancedFile);
        loadWithoutException();
    }
    
    public XMLLocalizer(Document document) {
        super();
        this.document = document;
    }
    
    public boolean loadFromFileWithoutException(AdvancedFile advancedFile) {
        return loadFromFile(advancedFile, null);
    }
    
    public boolean loadFromFile(AdvancedFile advancedFile, ToughConsumer<Throwable> failure) {
        try {
            return loadFromFile(advancedFile);
        } catch (Exception ex) {
            if (failure == null) {
                Logger.handleError(ex);
            } else {
                failure.acceptWithoutException(ex);
            }
            return false;
        }
    }
    
    public boolean loadFromFile(AdvancedFile advancedFile) throws Exception {
        Objects.requireNonNull(advancedFile, "advancedFile");
        if (!advancedFile.exists()) {
            throw new FileIsNotExistingException(advancedFile.getAbsolutePath());
        }
        setDocument(advancedFile.createInputStreamClosingAction().use(XMLUtil::parseToMemory));
        return true;
    }
    
    public boolean hasDocument() {
        return document != null;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public XMLLocalizer setDocument(Document document) {
        this.document = document;
        return this;
    }
    
    protected boolean loadRootElement() {
        if (!hasDocument()) {
            return false;
        }
        if (rootElement == null) {
            rootElement = document.getDocumentElement();
        }
        return true;
    }
    
    protected boolean loadInfoElement() {
        if (!hasDocument() || !loadRootElement()) {
            return false;
        }
        if (infoElement == null) {
            try {
                infoElement = (Element) rootElement.getElementsByTagName(TAG_NAME_INFO).item(0);
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String localizeWithArguments(String name, String defaultValue, Object... arguments) {
        if (!loadRootElement()) {
            return defaultValue;
        }
        final Element element = XMLUtil.stream(rootElement.getElementsByTagName(TAG_NAME_TRANSLATION)).filter((element_) -> element_.getAttribute(ATTRIBUTE_NAME_NAME).equals(name)).findFirst().orElse(null);
        if (element != null) {
            return element.getTextContent(); //TODO Use "arguments"!
        }
        return defaultValue;
    }
    
    @Override
    public String localizeWithArguments(String name, ToughSupplier<String> defaultValueSupplier, Object... arguments) {
        if (!loadRootElement()) {
            return defaultValueSupplier.getWithoutException();
        }
        final Element element = XMLUtil.stream(rootElement.getElementsByTagName(TAG_NAME_TRANSLATION)).filter((element_) -> element_.getAttribute(ATTRIBUTE_NAME_NAME).equals(name)).findFirst().orElse(null);
        if (element != null) {
            return element.getTextContent(); //TODO Use "arguments"!
        }
        return defaultValueSupplier.getWithoutException();
    }
    
    @Override
    public String getKeyLanguageTag() {
        return KEY_LANGUAGE_TAG;
    }
    
    @Override
    public String getKeyLanguageNameEnglish() {
        return KEY_LANGUAGE_NAME_ENGLISH;
    }
    
    @Override
    public String getKeyLanguageNameLocal() {
        return KEY_LANGUAGE_NAME_LOCAL;
    }
    
    @Override
    public String getLanguageTag() {
        if (!loadInfoElement()) {
            return null;
        }
        return infoElement.getAttribute(getKeyLanguageTag());
    }
    
    @Override
    public String getLanguageNameEnglish() {
        if (!loadInfoElement()) {
            return null;
        }
        if (!infoElement.hasAttribute(getKeyLanguageNameEnglish())) {
            return getLanguageTag();
        }
        return infoElement.getAttribute(getKeyLanguageNameEnglish());
    }
    
    @Override
    public String getLanguageNameLocal() {
        if (!loadInfoElement()) {
            return null;
        }
        if (!infoElement.hasAttribute(getKeyLanguageNameLocal())) {
            return getLanguageNameEnglish();
        }
        return infoElement.getAttribute(getKeyLanguageNameLocal());
    }
    
    @Override
    public boolean addLocalizer(Localizer localizer) {
        return false; //TODO
    }
    
    @Override
    public Copyable copy() {
        return null; //TODO
    }
    
    @Override
    public void set(Copyable copyable) {
        //TODO
    }
    
    @Override
    public boolean load() throws Exception {
        if (!hasAdvancedFile()) {
            return false;
        }
        return loadFromFile(getAdvancedFile());
    }
    
    @Override
    public boolean unload() throws Exception {
        if (!hasDocument()) {
            return false;
        }
        setDocument(null);
        return true;
    }
    
    @Override
    public String toString() {
        return "XMLLocalizer{" + "document=" + document + ", rootElement=" + rootElement + ", infoElement=" + infoElement + ", advancedFile=" + advancedFile + '}';
    }
    
}
