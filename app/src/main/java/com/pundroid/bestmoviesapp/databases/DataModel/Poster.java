package com.pundroid.bestmoviesapp.databases.DataModel;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class Poster {

    private int movieId;
    private String pathToPoster;


    public Poster() {

    }

    public Poster(int movieId, String pathToPoster) {
        this.movieId = movieId;
        this.pathToPoster = pathToPoster;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getPathToPoster() {
        return pathToPoster;
    }

    public void setPathToPoster(String pathToPoster) {
        this.pathToPoster = pathToPoster;
    }
}
