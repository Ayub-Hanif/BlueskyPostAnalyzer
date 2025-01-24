package com.ecs160.hw1;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {

        try (PrintWriter file_out = new PrintWriter(new FileWriter("output.txt"))) {

            Json_file_parser parser = new Json_file_parser();
            List<Post> social_posts = parser.json_parser("input.json");

            //Right now I made it so it will output to a text file. To see what we get.
            for (Post post : social_posts) {
                String postContent = "Post: " + post.get_content();
                file_out.println(postContent);

                String repliesCount = "Replies count: " + post.get_post_replies().size();
                file_out.println(repliesCount);

                for (Post reply : post.get_post_replies()) {
                    String replyContent = "    Reply: " + reply.get_content();
                    file_out.println(replyContent);
                }
            }
            System.out.println("\nFinished!!!\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
