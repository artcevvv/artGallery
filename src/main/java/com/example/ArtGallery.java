package com.example;

import java.util.ArrayList;
import java.util.List;

public class ArtGallery {
    private String name;
    private final List<Artwork> artworks;

    public ArtGallery(String name) {
        this.name = name;
        this.artworks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artwork> getArtworks() {
        return new ArrayList<>(artworks);
    }

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
    }

    public List<Artwork> filterArtworksByMedium(String medium) {
        List<Artwork> filteredArtworks = new ArrayList<>();
        for (Artwork artwork : artworks) {
            if (artwork.getMedium().equals(medium)) {
                filteredArtworks.add(artwork);
            }
        }
        return filteredArtworks;
    }

    @Override
    public String toString() {
        return "ArtGallery:\n" +
                "Name of the Gallery: '" + name + "\n" +
                "Artworks: " + artworks;
    }
}
