package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pumba30 on 27.08.2015.
 */
public class Credits implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("cast")
    private ArrayList<Actor> actors;

    @SerializedName("crew")
    private ArrayList<CrewMember> crew;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setCast(ArrayList<Actor> actor) {
        this.actors = actor;
    }

    public ArrayList<CrewMember> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<CrewMember> crew) {
        this.crew = crew;
    }
}
