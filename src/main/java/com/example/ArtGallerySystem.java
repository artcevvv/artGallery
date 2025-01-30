package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ArtGallerySystem {
    private static final String URL = "jdbc:postgresql://localhost:5432/artGallery";
    private static final String USER = "artGalleryUser";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to PostgreSQL database!");

            createTables(conn);

            int artistId = insertArtist(conn, "Pablo Picasso", "Cubism");
            insertArtwork(conn, "Guernica", "Painting", artistId);

            readArtists(conn);

            // Update data
            // updateArtist(conn, artistId, "Modern Cubism");

            // Delete data
            // deleteArtist(conn, artistId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String sqlArtists = "CREATE TABLE IF NOT EXISTS artists ("
                + "id SERIAL PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "style VARCHAR(255) NOT NULL)";

        String sqlArtworks = "CREATE TABLE IF NOT EXISTS artworks ("
                + "id SERIAL PRIMARY KEY, "
                + "title VARCHAR(255) NOT NULL, "
                + "medium VARCHAR(100) NOT NULL, "
                + "artist_id INT, "
                + "FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE)";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlArtists);
            stmt.execute(sqlArtworks);
        }
    }

    private static int insertArtist(Connection conn, String name, String style) throws SQLException {
        String sql = "INSERT INTO artists (name, style) VALUES (?, ?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, style);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    private static void insertArtwork(Connection conn, String title, String medium, int artistId) throws SQLException {
        String sql = "INSERT INTO artworks (title, medium, artist_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, medium);
            pstmt.setInt(3, artistId);
            pstmt.executeUpdate();
        }
    }

    private static void readArtists(Connection conn) throws SQLException {
        String sql = "SELECT * FROM artists";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Style: " + rs.getString("style"));
            }
        }
    }

    private static void updateArtist(Connection conn, int id, String newStyle) throws SQLException {
        String sql = "UPDATE artists SET style = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStyle);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }

    private static void deleteArtist(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM artists WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
