package com.ecs160.hw1;

import com.google.gson.*;
import java.sql.Timestamp;
import java.time.Instant;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Json_file_parser {
    public List<Post> json_parser(String filePath) throws Exception {
        List<Post> posts = new ArrayList<>();

        //loading the file and then checking if it was null or not.
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new NullPointerException("File not found in classpath: " + filePath);
        }

        // once open we have to parse the file and then ensure that it is an object.
        Reader reader = new InputStreamReader(inputStream);
        JsonElement element = JsonParser.parseReader(reader);

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray feedArray = jsonObject.get("feed").getAsJsonArray();

            // we will start getting each thread in the feed and parse them
            for (JsonElement feedObject : feedArray) {
                if (feedObject.getAsJsonObject().has("thread")) {
                    JsonObject threadObject = feedObject.getAsJsonObject().getAsJsonObject("thread");
                    Post post = parsePost(threadObject.getAsJsonObject("post"));

                    if (threadObject.has("replies")) {
                        parseReplies(threadObject.getAsJsonArray("replies"), post);
                    }
                    //add them all into posts and we are done.
                    posts.add(post);
                }
            }
        } else {
            //We get errors bc of the object, thus this will be good check for future assignments aswell.
            throw new IllegalArgumentException("Invalid JSON structure: Expected a JSON object.");
        }
        return posts;
    }

    private Post parsePost(JsonObject post_object) {
        int post_Id = post_object.get("uri").getAsString().hashCode();
        String content = post_object.getAsJsonObject("record").get("text").getAsString();
        String created_str = post_object.getAsJsonObject("record").get("createdAt").getAsString();
        Timestamp created_time = Timestamp.from(Instant.parse(created_str));
        int word_count = content.split("\\s+").length;

        return new Post(post_Id, content, created_time, word_count);
    }

    private void parseReplies(JsonArray replies_array, Post parent_post) {
        for (JsonElement replyElement : replies_array) {
            JsonObject replyObject = replyElement.getAsJsonObject().getAsJsonObject("post");
            Post reply = parsePost(replyObject);

            parent_post.add_reply(reply);

            if (replyElement.getAsJsonObject().has("replies")) {
                parseReplies(replyElement.getAsJsonObject().getAsJsonArray("replies"), reply);
            }
        }
    }
}
