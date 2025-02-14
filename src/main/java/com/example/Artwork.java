package com.example;

import java.util.Objects;

public class Artwork {
    private final String title;
    private final String medium;
    private final Artist artist;

    public Artwork(String title, String medium, Artist artist) {
        this.title = title;
        this.medium = medium;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getMedium() {
        return medium;
    }

    public Artist getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "{ \"title\": \"" + title + "\", \"medium\": \"" + medium + "\", \"artist\": \"" + artist + "\" }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Artwork artwork = (Artwork) o;
        return Objects.equals(o, artwork) && Objects.equals(title, artwork.title)
                && Objects.equals(medium, artwork.medium) && Objects.equals(artist, artwork.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, medium, artist);
    }
}