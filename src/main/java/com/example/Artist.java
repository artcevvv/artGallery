package com.example;

import java.util.Objects;

public class Artist {
    private final String name;
    private final String style;

    public Artist(String name, String style) {
        this.name = name;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "{ \"name\": \"" + name + "\", \"style\": \"" + style + "\" }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Artist artist = (Artist) o;
        return Objects.equals(o, artist) && Objects.equals(style, artist.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, style);
    }
}
