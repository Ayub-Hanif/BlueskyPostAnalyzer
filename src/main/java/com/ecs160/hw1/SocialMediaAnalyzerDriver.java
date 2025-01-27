package com.ecs160.hw1;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import com.ecs160.db.DatabaseHandler;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {

        DatabaseHandler dbHandler = null;

        try (PrintWriter file_out = new PrintWriter(new FileWriter("output.txt"))) {
            // get the connection to the database
            dbHandler = new DatabaseHandler();

            // then we can use the connection to add posts to the database
            Json_file_parser parser = new Json_file_parser();
            List<Post> social_posts = parser.json_parser("input.json");

            for (Post post : social_posts) {
                String postContent = "Post: " + post.get_content();
                file_out.println(postContent);

                String repliesCount = "Replies count: " + post.get_post_replies().size();
                file_out.println(repliesCount);
                
                dbHandler.addPost(post.get_post_Id(), post.get_content(), post.get_creation_date(), post.get_word_count(), null);

                for (Post reply : post.get_post_replies()) {
                    String replyContent = "    Reply: " + reply.get_content();
                    file_out.println(replyContent);
                    dbHandler.addPost(reply.get_post_Id(), reply.get_content(), reply.get_creation_date(), reply.get_word_count(), post.get_post_Id());
                }

            }
            System.out.println("\nFinished!!!\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dbHandler != null) {
                    dbHandler.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
