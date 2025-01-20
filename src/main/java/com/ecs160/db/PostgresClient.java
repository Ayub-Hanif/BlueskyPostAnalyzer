package com.ecs160.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresClient {

    private static final String PROPERTIES_FILE = "src/main/java/com/ecs160/resources/database.properties";
    private String url;
    private String user;
    private String password;

    /**
     * Loads database credentials from the properties file.
     */
    public PostgresClient() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            props.load(input);
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
    }

    /**
     * Establishes and returns a connection to the PostgreSQL database.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Inserts a new post into the 'posts' table.
     *
     * @param postId    Unique identifier for the post.
     * @param content   Content of the post.
     * @param wordCount Number of words in the post.
     * @param parentId  ID of the parent post (null if it's a standalone post).
     */
    public void insertPost(String postId, String content, int wordCount, Integer parentId) {
        String sql = "INSERT INTO posts (post_id, content, word_count, parent_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postId);
            pstmt.setString(2, content);
            pstmt.setInt(3, wordCount);

            if (parentId == null) {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(4, parentId);
            }

            pstmt.executeUpdate();
            System.out.println("Post inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the database ID for a given post_id.
     *
     * @param postId The unique identifier of the post.
     * @return The database ID of the post, or null if not found.
     */
    public Integer getPostDbId(String postId) {
        String sql = "SELECT id FROM posts WHERE post_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
