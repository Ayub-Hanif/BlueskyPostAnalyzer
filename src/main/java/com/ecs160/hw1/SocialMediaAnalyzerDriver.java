package com.ecs160.hw1;

import com.ecs160.db.DatabaseHandler;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
        DatabaseHandler dbHandler = null;

        // we are using try-with-resources to automatically close the file
        try (PrintWriter file_out = new PrintWriter(new FileWriter("output.txt"))) {

            // create the database handler
            dbHandler = new DatabaseHandler();
            
            // then parse the json file
            Json_file_parser parser = new Json_file_parser();
            List<Post> social_posts = parser.json_parser("input.json");

            // we iterate over the posts and write them to the file
            for (Post post : social_posts) {
                String postContent = "Post: " + post.get_content();
                file_out.println(postContent);

                String repliesCount = "Replies count: " + post.get_post_replies().size();
                file_out.println(repliesCount);

                // as we go through the posts, we add them to the database
                dbHandler.addPost(post.get_post_Id(), post.get_content(), post.get_creation_date(), post.get_word_count(), null);
                for (Post reply : post.get_post_replies()) {
                    String replyContent = "    Reply: " + reply.get_content();
                    file_out.println(replyContent);
                    // we also add the replies to the database
                    dbHandler.addPost(reply.get_post_Id(), reply.get_content(), reply.get_creation_date(), reply.get_word_count(), post.get_post_Id());
                }
            }
            // we then close the database connection
            boolean weighted = false;
            // we check if the user wants to use the weighted analyzer
            for (String arg : args) {
                if (arg.equals("weighted=true")) {
                    weighted = true;
                }
            }
            // if so we use the weighted analyzer, otherwise we use the basic analyzer
            Analyzer_data analyzer = weighted ? new Weighted_Analyzer(social_posts) : new Analyzer_basics(social_posts);

            int totalPosts = analyzer.compute_total_posts();
            double avgReplies = analyzer.compute_average_replies();
            String avgInterval = analyzer.compute_average_interval();
            double longest_post = analyzer.get_longest_post();

            // this will be outputed to the end of the file
            file_out.println("\nStatistics:");
            file_out.println("Total Posts: " + totalPosts);
            file_out.println("Average Replies: " + avgReplies);
            file_out.println("Average Interval: " + avgInterval);
            file_out.println("longest post: " + longest_post);

            System.out.println("\nFinished! Results saved to output.txt.");
            // we print the results to the console
            // we also add the results to the database
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
