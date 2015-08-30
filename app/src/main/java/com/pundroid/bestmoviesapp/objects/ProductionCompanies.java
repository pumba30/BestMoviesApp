package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by pumba30 on 29.08.2015.
 */
public class ProductionCompanies implements Serializable {

    @Expose
    private String name;
    @Expose
    private Integer id;

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
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }


}
