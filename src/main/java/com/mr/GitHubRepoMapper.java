package com.mr;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GitHubRepoMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text language = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        JsonObject jsonObj = JsonParser.parseString(line).getAsJsonObject();

        if (jsonObj.has("Language")) {
            String lang = jsonObj.get("Language").getAsString();
            if (!lang.isEmpty() && !lang.equals("Unknown")) {
                language.set(lang);
                context.write(language, one);
            }
        }
    }
}




