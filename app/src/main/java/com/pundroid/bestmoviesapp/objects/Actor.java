package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pumba30 on 26.08.2015.
 */
public class Actor {

    @Expose
    private String character;
    @Expose
    private Long id;
    @Expose
    private String name;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    private String profilePathToStorage;

    public String getProfilePathToStorage() {
        return profilePathToStorage;
    }

    public void setProfilePathToStorage(String profilePathToStorage) {
        this.profilePathToStorage = profilePathToStorage;
    }

    /**
     *
     * @return
     * The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     *
     * @param character
     * The character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The profilePath
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     *
     * @param profilePath
     * The profile_path
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
