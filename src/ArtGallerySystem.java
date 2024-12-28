import java.util.*;

public class ArtGallerySystem {
    public static void main(String[] args) {
        Artist artist1 = new Artist("Pablo Picasso", "Cubism");
        Artist artist2 = new Artist("Vincent van Gogh", "Post-Impressionism");

        Artwork artwork1 = new Artwork("Guernica", "Painting", artist1);
        Artwork artwork2 = new Artwork("Starry Night", "Painting", artist2);

        ArtGallery gallery = new ArtGallery("The Louvre");
        gallery.addArtwork(artwork1);
        gallery.addArtwork(artwork2);

        System.out.println(artist1);
        System.out.println(artwork1);
        System.out.println(gallery);

        System.out.println("Artwork 1 equals Artwork 2: " + artwork1.equals(artwork2)); 
        System.out.println("Artist 1 equals Artist 2: " + artist1.equals(artist2)); 

        List<Artwork> paintings = gallery.filterArtworksByMedium("Painting");
        System.out.println("Paintings in the gallery:");
        for (Artwork artwork : paintings) {
            System.out.println(artwork);
        }
    }
}