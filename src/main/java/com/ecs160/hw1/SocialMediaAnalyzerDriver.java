package com.ecs160.hw1;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


// the functionality of the program is described in the file Homework-1.md
public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
        System.out.println("Good morning!");
        // read the arguments from the command line.
        // there may be one or two arguments.
        // weighted is a required argument either true or false.
        // the other argument, `file`, is optional. If file isn't provided, use the file from `src/resources/input.json`. otherwise, use the file provided by the file argument.

        // read the arguments from the command line using Apache Commons CLI

        Options options = new Options();
        options.addOption("weighted", true, "weighted");
        options.addOption("file", true, "file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        boolean weighted = cmd.hasOption("weighted") && cmd.getOptionValue("weighted").equals("true");
        String fileName = cmd.getOptionValue("file");
        if (!cmd.hasOption("file")) {
            fileName = "./src/main/resources/input.json";
        }


        // parse the file with google gson
        JsonElement element = JsonParser.parseReader(new InputStreamReader(JsonDeserializer.class.getClassLoader().getResourceAsStream("input.json")));

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            JsonArray feedArray = jsonObject.get("feed").getAsJsonArray();
            for (JsonElement feedObject: feedArray) {
                // Check if you have the thread key
                if (feedObject.getAsJsonObject().has("thread")) {
                    System.out.println("thread");
                    // parse the post and any replies (recursively)?
                }
            }
        }


    }
}
