package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pumba30 on 02.09.2015.
 */
public class AccountState implements Serializable{

    @SerializedName("id")
    private int id;

    @SerializedName("favorite")
    private boolean favorite;

    @SerializedName("watchlist")
    private boolean watchlist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }
}
