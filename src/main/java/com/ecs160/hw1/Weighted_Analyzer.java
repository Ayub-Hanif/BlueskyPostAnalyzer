package com.ecs160.hw1;

import java.util.List;

public class Weighted_Analyzer implements Analyzer_data {

    private List<Post> posts;
    private int longest_post;

    public Weighted_Analyzer(List<Post> posts) {
        this.posts = posts;
        this.longest_post = posts.stream().mapToInt(Post::get_word_count).max().orElse(1);
    }

    @Override
    public double get_longest_post() {
            return longest_post;
    }

    @Override
    public double compute_average_replies() {
        double weightedReplyCount = 0;
        double totalWeight = 0;

        for (Post post : posts) {
            double weight = 1 + ((double) post.get_word_count() / longest_post);
            totalWeight += weight;
            for (Post reply : post.get_post_replies()) {
                double replyWeight = 1 + ((double) reply.get_word_count() / longest_post);
                weightedReplyCount += replyWeight;
            }
        }

        return weightedReplyCount / totalWeight;
    }

    @Override
    public int compute_total_posts() {
        return (int) posts.stream()
        .mapToDouble(post -> 1 + ((double) post.get_word_count() / longest_post))
        .sum();
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
