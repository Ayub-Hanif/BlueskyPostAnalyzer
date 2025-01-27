package com.ecs160.db;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String URL = "jdbc:postgresql://localhost:5432/socialmedia_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "9981";

    private Connection connection;

    // we need to use this constructor for database connection and initialization
    public DatabaseHandler() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to the database successfully!");
    }

    // closing the database connection
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }

    // getter for connection object
    public Connection getConnection() {
        return connection;
    }

    //adding post into database
    public void addPost(int post_id, String post_content,Timestamp creation_time, int word_count, Integer parent_post_Id) throws SQLException {
        String sql_query = "INSERT INTO posts (post_id, content, creation_date, word_count, parent_post_id) " + 
        "VALUES (?, ?, ?, ?, ?) " + "ON CONFLICT (post_id) DO NOTHING";

        try(PreparedStatement statement = connection.prepareStatement(sql_query)) {
            statement.setInt(1, post_id);
            statement.setString(2, post_content);
            statement.setTimestamp(3, creation_time);
            statement.setInt(4, word_count);
            if(parent_post_Id == null) {
                statement.setNull(5, java.sql.Types.INTEGER);
            } else {
                statement.setInt(5, parent_post_Id);
            }
            statement.executeUpdate();
            System.out.println("Post added with post_id: " + post_id);
        }
    }
}
