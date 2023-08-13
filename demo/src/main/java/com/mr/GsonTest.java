package com.mr;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;

public class GsonTest {

    public static void main(String[] args) {
        String filePath = "repo_details.json";

        try (FileReader reader = new FileReader(filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
                if (jsonObj.has("Repository") && jsonObj.has("Language")) {
                    String repository = jsonObj.get("Repository").getAsString();
                    String language = jsonObj.get("Language").getAsString();
                    System.out.println("Repository: " + repository + ", Language: " + language);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
