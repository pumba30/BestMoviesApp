package com.pundroid.bestmoviesapp.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by pumba30 on 22.08.2015.
 */
public class Movie implements Parcelable {

    public static String MOVIE_OBJECT = "com.pundroid.bestmoviesapp.object.movie";
    public static final String BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w342/";
    public static final String BASE_POSTER_PATH_W154 =  "http://image.tmdb.org/t/p/w154/";

    private String backdropPath;
    private String homePage;
    private String  tagLine;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private ArrayList<String> productionCompanies;
    private ArrayList<String> productionCountries;
    private ArrayList<String> genres;
    private long voteAverage;
    private long popularity;
    private int id;
    private int voteCount;
    private int budget;
    private int revenue;
    private int runtime;
    private boolean video;
    private boolean adult;

    public Movie() {

    }

    protected Movie(Parcel in) {
        backdropPath = in.readString();
        homePage = in.readString();
        tagLine = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        productionCompanies = in.createStringArrayList();
        productionCountries = in.createStringArrayList();
        genres = in.createStringArrayList();
        voteAverage = in.readLong();
        popularity = in.readLong();
        id = in.readInt();
        voteCount = in.readInt();
        budget = in.readInt();
        revenue = in.readInt();
        runtime = in.readInt();
        video = in.readByte() != 0;
        adult = in.readByte() != 0;

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public ArrayList<String> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(ArrayList<String> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public ArrayList<String> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(ArrayList<String> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public long getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(long voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdropPath);
        dest.writeString(homePage);
        dest.writeString(tagLine);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeStringList(productionCompanies);
        dest.writeStringList(productionCountries);
        dest.writeStringList(genres);
        dest.writeLong(voteAverage);
        dest.writeLong(popularity);
        dest.writeInt(id);
        dest.writeInt(voteCount);
        dest.writeInt(budget);
        dest.writeInt(revenue);
        dest.writeInt(runtime);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeByte((byte) (adult ? 1 : 0));
    }
}
