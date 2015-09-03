package com.pundroid.bestmoviesapp.objects;

import java.io.Serializable;

/**
 * Created by pumba30 on 02.09.2015.
 */
public class Favorite implements Serializable {
    private String media_type;
    private Integer media_id;
    private Boolean favorite;

    public Favorite(String media_type, Integer media_id, Boolean favorite) {
        this.media_type = media_type;
        this.media_id = media_id;
        this.favorite = favorite;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public Integer getMedia_id() {
        return media_id;
    }

    public void setMedia_id(Integer media_id) {
        this.media_id = media_id;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

}
