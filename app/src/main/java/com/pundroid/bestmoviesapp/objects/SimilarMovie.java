package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pumba30 on 28.08.2015.
 */
public class SimilarMovie implements Serializable {

    @Expose
    private Long page;
    @Expose
    private ArrayList<Movie> results = new ArrayList<Movie>();
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;
    @SerializedName("total_results")
    @Expose
    private Long totalResults;

    /**
     *
     * @return
     * The page
     */
    public Long getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Long page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public ArrayList<Movie> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public Long getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public Long getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }




}
