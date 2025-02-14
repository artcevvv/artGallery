package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ArtGallerySystem {
    private static final String URL = "jdbc:postgresql://localhost:5432/artGallery";
    private static final String USER = "artGalleryUser";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/artists", new ArtistHandler());
        server.createContext("/artworks", new ArtworkHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080...");
    }

    static class ArtistHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" -> handleGetArtists(exchange);
                case "POST" -> handleCreateArtist(exchange);
                default -> sendResponse(exchange, 405, "Method Not Allowed");
            }
        }

        private void handleGetArtists(HttpExchange exchange) throws IOException {
            JSONArray artists = new JSONArray();
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM artists")) {

                while (rs.next()) {
                    JSONObject artist = new JSONObject();
                    artist.put("id", rs.getInt("id"));
                    artist.put("name", rs.getString("name"));
                    artist.put("style", rs.getString("style"));
                    artists.put(artist);
                }
            } catch (SQLException e) {
                sendResponse(exchange, 500, "Database Error");
                return;
            }
            sendResponse(exchange, 200, artists.toString());
        }

        private void handleCreateArtist(HttpExchange exchange) throws IOException {
            String requestBody = readRequestBody(exchange);
            JSONObject json = new JSONObject(requestBody);

            String name = json.getString("name");
            String style = json.getString("style");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO artists (name, style) VALUES (?, ?) RETURNING id")) {
                pstmt.setString(1, name);
                pstmt.setString(2, style);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int artistId = rs.getInt("id");
                    sendResponse(exchange, 201, "{\"id\":" + artistId + "}");
                }
            } catch (SQLException e) {
                sendResponse(exchange, 500, "Database Error");
            }
        }
    }

    static class ArtworkHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "POST" -> handleCreateArtwork(exchange);
                case "GET" -> handleGetArtwork(exchange);
                default -> sendResponse(exchange, 405, "Method Not Allowed");
            }
        }

        private void handleCreateArtwork(HttpExchange exchange) throws IOException {
            String requestBody = readRequestBody(exchange);
            JSONObject json = new JSONObject(requestBody);

            String title = json.getString("title");
            String medium = json.getString("medium");
            int artistId = json.getInt("artist_id");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO artworks (title, medium, artist_id) VALUES (?, ?, ?)")) {
                pstmt.setString(1, title);
                pstmt.setString(2, medium);
                pstmt.setInt(3, artistId);
                pstmt.executeUpdate();
                sendResponse(exchange, 201, "{\"message\":\"Artwork created\"}");
            } catch (SQLException e) {
                sendResponse(exchange, 500, "Database Error");
            }
        }
    }

    private static void handleGetArtwork(HttpExchange exchange) throws IOException {
        JSONArray artworks = new JSONArray();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM artworks")) {

            while (rs.next()) {
                JSONObject artwork = new JSONObject();
                artwork.put("id", rs.getInt("id"));
                artwork.put("name", rs.getString("name"));
                artwork.put("style", rs.getString("style"));
                artworks.put(artwork);
            }
        } catch (SQLException e) {
            sendResponse(exchange, 500, "Database Error");
            return;
        }
        sendResponse(exchange, 200, artworks.toString());
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
