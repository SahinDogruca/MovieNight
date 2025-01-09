package org.example.Models;

import java.time.LocalDate;

public class Movies {
    private int movieID;
    private String title;
    private String genre;
    private LocalDate releaseDate;
    private double rating;

    public Movies(int movieID, String title, String genre, LocalDate releaseDate, double rating) {
        if (movieID < 0) {
            throw new IllegalArgumentException("Movie ID negatif olamaz.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title boş bırakılamaz.");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre boş bırakılamaz.");
        }
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating 0 ile 10 arasında olmalıdır.");
        }
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        if (movieID < 0) {
            throw new IllegalArgumentException("Movie ID negatif olamaz.");
        }
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title boş bırakılamaz.");
        }
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre boş bırakılamaz.");
        }
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return this.rating;
    }

}
