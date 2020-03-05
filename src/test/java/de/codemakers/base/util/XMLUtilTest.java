/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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


import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.lang.LanguageUtil;
import de.codemakers.lang.XMLLocalizer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.StringReader;

public class XMLUtilTest {
    
    public static void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINE);
        if (false) {
            Logger.logDebug("XMLUtil.DOCUMENT_BUILDER=" + XMLUtil.DOCUMENT_BUILDER);
            Logger.log("args[0]=" + args[0]);
            final Document document = XMLUtil.parseToMemory(new InputSource(new StringReader(args[0])));
            Logger.log("document=" + document);
            final Element root = document.getDocumentElement();
            Logger.log("root=" + root);
            final String test_tagName = root.getTagName();
            Logger.log("test_tagName=" + test_tagName);
            final String test_attributeName = root.getAttribute("name");
            Logger.log("test_attributeName=" + test_attributeName);
            final String test_textContent = root.getTextContent();
            Logger.log("test_textContent=" + test_textContent);
        }
        final AdvancedFile langFile = new AdvancedFile(LanguageUtil.LANG_FOLDER, "de-DE.xml");
        Logger.log("langFile=" + langFile);
        Logger.log("langFile.exists()=" + langFile.exists());
        final XMLLocalizer xmlLocalizer = new XMLLocalizer(langFile);
        Logger.log("xmlLocalizer=" + xmlLocalizer);
        Logger.log("xmlLocalizer.getLanguageTag()=" + xmlLocalizer.getLanguageTag());
        Logger.log("xmlLocalizer.getLanguageNameEnglish()=" + xmlLocalizer.getLanguageNameEnglish());
        Logger.log("xmlLocalizer.getLanguageNameLocal()=" + xmlLocalizer.getLanguageNameLocal());
        Logger.log("xmlLocalizer=" + xmlLocalizer);
        Logger.log("xmlLocalizer.localize(\"test_sentence\")=" + xmlLocalizer.localize("test_sentence"));
    }
    
}
