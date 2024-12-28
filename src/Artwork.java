import java.util.*;

public class Artwork {
    private String title;
    private String medium;
    private Artist artist;

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
        return "Title of artwork: " + title + " | Medium: " + medium + " | Artist: " + artist + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artwork artwork = (Artwork) o;
        return Objects.equals(o, artwork) && Objects.equals(title, artwork.title) && Objects.equals(medium, artwork.medium) && Objects.equals(artist, artwork.artist);
    }
}