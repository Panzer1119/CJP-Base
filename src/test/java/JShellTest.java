/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
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

import de.codemakers.base.CJP;
import de.codemakers.base.io.SerializationUtil;
import de.codemakers.base.scripting.JavaScriptEngine;
import de.codemakers.base.scripting.JavaScriptEngineBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JShellTest {

    public static boolean AUTO_SAVE_RESULTS = true;
    public static boolean AUTO_ADD_RETURN = true;
    public static int lastResult = 0;

    public static final void main(String[] args) {
        final JavaScriptEngineBuilder javaScriptEngineBuilder = new JavaScriptEngineBuilder();
        javaScriptEngineBuilder.addImports(SerializationUtil.class);
        final JavaScriptEngine javaScriptEngine = javaScriptEngineBuilder.build();
        final Thread thread = new Thread(() -> {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    if (line.startsWith("/")) {
                        line = line.substring(1);
                        boolean recognized_command = true;
                        if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("shutdown") || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                            System.out.println("Stopped the " + JavaScriptEngine.class.getSimpleName());
                            CJP.shutdown().complete();
                        } else if (line.equalsIgnoreCase("reset") || line.equalsIgnoreCase("reboot") || line.equalsIgnoreCase("restart")) {
                            javaScriptEngine.reset();
                            System.out.println("Resetted the " + JavaScriptEngine.class.getSimpleName());
                        } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                            System.out.println(String.format("Usage:\n" + "/exit - Exits the %s\n" + "/get VARIABLE - Returns the DATA for the VARIABLE\n" + "/help - Shows this help\n" + "/reset - Resets the %s\n" + "/set VARIABLE DATA - Sets the DATA to the VARIABLE\n" + "/set VARIABLE /eval CODE - Sets the evaluated CODE to the VARIABLE\n" + "/toggle auto_add_return - Toggles if a missing \"return\" will be added before each CODE (Value: %b)\n" + "/toggle auto_save_results - Toggles if a result from an evaluation should be saved in a VARIABLE (Value: %b)\n", JShellTest.class.getSimpleName(), JavaScriptEngine.class.getSimpleName(), AUTO_ADD_RETURN, AUTO_SAVE_RESULTS));
                        } else if (line.startsWith("toggle ")) {
                            line = line.substring("toggle ".length());
                            if (line.equalsIgnoreCase("auto_save_results") || line.equalsIgnoreCase("asr")) {
                                AUTO_SAVE_RESULTS = !AUTO_SAVE_RESULTS;
                                System.out.println("Toggled AUTO_SAVE_RESULTS to " + AUTO_SAVE_RESULTS);
                            } else if (line.equalsIgnoreCase("auto_add_return") || line.equalsIgnoreCase("aar")) {
                                AUTO_ADD_RETURN = !AUTO_ADD_RETURN;
                                System.out.println("Toggled AUTO_ADD_RETURN to " + AUTO_ADD_RETURN);
                            } else {
                                recognized_command = false;
                            }
                        } else if (line.startsWith("put ") || line.startsWith("set ")) {
                            line = line.substring("put ".length());
                            final String variable = line.substring(0, line.indexOf(" "));
                            Object data = line.substring(variable.length() + " ".length());
                            if (data.toString().startsWith("/eval return ")) {
                                data = data.toString().substring("/eval ".length());
                                if (AUTO_ADD_RETURN && !data.toString().startsWith("return ")) {
                                    data = "return " + data.toString();
                                }
                                data = javaScriptEngine.execute(data.toString());
                            }
                            javaScriptEngine.put(variable, data);
                            System.out.println(String.format("Setted \"%s\" to \"%s\"", variable, data));
                        } else if (line.startsWith("get ")) {
                            line = line.substring("get ".length());
                            System.out.println(String.format("\"%s\" is \"%s\"", line, javaScriptEngine.get(line)));
                        } else {
                            recognized_command = false;
                        }
                        if (!recognized_command) {
                            System.err.println(String.format("Unrecognized command: %s (Use /help for help)", line));
                        }
                    } else {
                        if (AUTO_ADD_RETURN && !line.startsWith("return ")) {
                            line = "return " + line;
                        }
                        javaScriptEngine.executeLarge(line).queue((result) -> {
                            if (AUTO_SAVE_RESULTS) {
                                lastResult++;
                                final String variable = String.format("$%d", lastResult);
                                javaScriptEngine.put(variable, result);
                                System.out.println(String.format("%s => %s", variable, result));
                            } else {
                                System.out.println("=> " + result);
                            }
                        }, System.err::println);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

}
