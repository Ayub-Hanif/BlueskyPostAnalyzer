package com.ecs160.hw1;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private int post_Id;
    private String content;
    private Timestamp creation_date;
    private int word_count;
    private List<Post> post_replies;

    public Post(int post_Id, String content, Timestamp creation_date, int word_count) {
        this.post_Id = post_Id;
        this.content = content;
        this.creation_date = creation_date;
        this.word_count = word_count;
        this.post_replies = new ArrayList<>();
    }

    public void add_reply(Post reply) {
        this.post_replies.add(reply);
    }

    public int get_post_Id() {
        return post_Id;
    }

    public String get_content() {
        return content;
    }

    public Timestamp get_creation_date() {
        return creation_date;
    }

    public int get_word_count() {
        return word_count;
    }

    public List<Post> get_post_replies() {
        return post_replies;
    }
}
