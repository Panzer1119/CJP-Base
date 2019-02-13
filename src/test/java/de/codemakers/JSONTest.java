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

package de.codemakers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

public class JSONTest {
    
    public static final void main(String[] args) throws Exception {
        //JsonObject jsonObject = new JsonParser().parse("{\"name\": \"John\"}").getAsJsonObject();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/paul/Documents/people.json")));
        final String json_string = bufferedReader.lines().collect(Collectors.joining());
        bufferedReader.close();
        JsonArray jsonArray = new JsonParser().parse(json_string).getAsJsonArray();
        System.out.println(jsonArray.get(0).getAsJsonObject());
        System.out.println(jsonArray.get(0).getAsJsonObject().get("firstName").getAsString()); //John
    }
    
}
