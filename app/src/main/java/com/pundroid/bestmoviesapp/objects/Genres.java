package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pumba30 on 29.08.2015.
 */
public class Genres implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
