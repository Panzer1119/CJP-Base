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

import de.codemakers.base.Standard;
import de.codemakers.base.logger.Logger;

import java.util.Arrays;
import java.util.Scanner;

public class ArgumentUtilTest {
    
    public static final void main(String[] args) throws Exception {
        Standard.async(() -> {
            final Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("/")) {
                    line = line.substring("/".length());
                    if (line.equals("quit") || line.equals("exit")) {
                        break;
                    }
                } else {
                    final String[] arguments = ArgumentUtil.parseArguments(line);
                    Logger.log("arguments=" + Arrays.toString(arguments));
                }
            }
            scanner.close();
            System.exit(0);
        });
    }
    
}
