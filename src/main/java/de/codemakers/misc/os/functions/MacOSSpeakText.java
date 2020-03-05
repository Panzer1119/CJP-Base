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

package de.codemakers.misc.os.functions;

import de.codemakers.base.logger.Logger;

import java.util.concurrent.TimeUnit;

//@RegisterOSFunction(supported = {OS.MACOS}) //FIXME This does not work
public class MacOSSpeakText extends SpeakText {
    
    protected static final String FORMAT = "osascript -e 'say \"%s\"'";
    
    @Override
    public boolean speak(String text) throws Exception {
        final String temp = String.format(FORMAT, text);
        Logger.logDebug("temp=" + temp);
        final Process process = Runtime.getRuntime().exec(temp);
        Logger.logDebug("process=" + process);
        return process.waitFor((int) (text.length() * 0.5 + 5), TimeUnit.SECONDS);
    }
    
}
