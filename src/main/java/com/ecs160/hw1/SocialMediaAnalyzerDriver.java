package com.ecs160.hw1;

import com.ecs160.db.DatabaseHandler;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class SocialMediaAnalyzerDriver {
    public static void main(String[] args) {
        DatabaseHandler dbHandler = null;

        try (PrintWriter file_out = new PrintWriter(new FileWriter("output.txt"))) {
            // Step 1: Get the connection to the database
            dbHandler = new DatabaseHandler();

            // Step 2: Parse the JSON file to create Post objects
            Json_file_parser parser = new Json_file_parser();
            List<Post> social_posts = parser.json_parser("input.json");

            // Step 3: Store posts and replies into the database
            for (Post post : social_posts) {
                String postContent = "Post: " + post.get_content();
                file_out.println(postContent);

                String repliesCount = "Replies count: " + post.get_post_replies().size();
                file_out.println(repliesCount);

                // Save post to database
                dbHandler.addPost(post.get_post_Id(), post.get_content(), post.get_creation_date(), post.get_word_count(), null);

                // Save replies to database
                for (Post reply : post.get_post_replies()) {
                    String replyContent = "    Reply: " + reply.get_content();
                    file_out.println(replyContent);
                    dbHandler.addPost(reply.get_post_Id(), reply.get_content(), reply.get_creation_date(), reply.get_word_count(), post.get_post_Id());
                }
            }

            // Step 4: Parse command-line arguments
            boolean weighted = false;
            for (String arg : args) {
                if (arg.equals("weighted=true")) {
                    weighted = true;
                }
            }

            // Step 5: Select the appropriate Analyzer
            Analyzer_data analyzer = weighted ? new Weighted_Analyzer(social_posts) : new Analyzer_basics(social_posts);

            // Step 6: Compute statistics
            int totalPosts = analyzer.compute_total_posts();
            double avgReplies = analyzer.compute_average_replies();
            String avgInterval = analyzer.compute_average_interval();
            double longest_post = analyzer.get_longest_post();

            // Step 7: Log the results to output.txt
            file_out.println("\nStatistics:");
            file_out.println("Total Posts: " + totalPosts);
            file_out.println("Average Replies: " + avgReplies);
            file_out.println("Average Interval: " + avgInterval);
            file_out.println("longest post: " + longest_post);

            System.out.println("\nFinished! Results saved to output.txt.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Step 8: Close the database connection
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
