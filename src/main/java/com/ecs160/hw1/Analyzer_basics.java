package com.ecs160.hw1;

import java.util.List;

public class Analyzer_basics implements Analyzer_data {

    private List<Post> posts;

    public Analyzer_basics(List<Post> posts) {
        this.posts = posts;
    }
    @Override
    public double get_longest_post() {
            return posts.stream().mapToInt(Post::get_word_count).max().orElse(1);
    }
    
    @Override
    public double compute_average_replies() {
        int totalReplies = 0;
        for (Post post : posts) {
            totalReplies += post.get_post_replies().size();
        }
        return (double) totalReplies / posts.size();
    }

    @Override
    public int compute_total_posts() {
        return posts.size();
    }

    @Override
    public String compute_average_interval() {
        long totalInterval = 0;
        int count = 0;
        for (Post post : posts) {
            List<Post> replies = post.get_post_replies();
            if (!replies.isEmpty()) {
                for (Post reply : replies) {
                    totalInterval += reply.get_creation_date().getTime() - post.get_creation_date().getTime();
                    count++;
                }
            }
        }
        if (count == 0) return "00:00:00";

        long avgIntervalMillis = totalInterval / count;
        long seconds = avgIntervalMillis / 1000 % 60;
        long minutes = avgIntervalMillis / (1000 * 60) % 60;
        long hours = avgIntervalMillis / (1000 * 60 * 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
