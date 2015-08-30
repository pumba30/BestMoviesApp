package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumba30 on 27.08.2015.
 */
public class Biography implements Serializable {
    @Expose
    private Boolean adult;
    @SerializedName("also_known_as")
    @Expose
    private List<Object> alsoKnownAs = new ArrayList<Object>();
    @Expose
    private String biography;
    @Expose
    private String birthday;
    @Expose
    private String deathday;
    @Expose
    private String homepage;
    @Expose
    private Long id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @Expose
    private String name;
    @SerializedName("place_of_birth")
    @Expose
    private String placeOfBirth;
    @Expose
    private Float popularity;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    /**
     * @return The adult
     */
    public Boolean getAdult() {
        return adult;
    }

    /**
     * @param adult The adult
     */
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    /**
     * @return The alsoKnownAs
     */
    public List<Object> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    /**
     * @param alsoKnownAs The also_known_as
     */
    public void setAlsoKnownAs(List<Object> alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    /**
     * @return The biography
     */
    public String getBiography() {
        return biography;
    }

    /**
     * @param biography The biography
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     * @return The birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday The birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return The deathday
     */
    public String getDeathday() {
        return deathday;
    }

    /**
     * @param deathday The deathday
     */
    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    /**
     * @return The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return The id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The imdbId
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * @param imdbId The imdb_id
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The placeOfBirth
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * @param placeOfBirth The place_of_birth
     */
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
     * @return The popularity
     */
    public Float getPopularity() {
        return popularity;
    }

    /**
     * @param popularity The popularity
     */
    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    /**
     * @return The profilePath
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     * @param profilePath The profile_path
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }


}
