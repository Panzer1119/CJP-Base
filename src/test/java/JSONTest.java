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
