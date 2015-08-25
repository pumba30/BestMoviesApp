package com.pundroid.bestmoviesapp.utils;

import android.util.Log;

import com.pundroid.bestmoviesapp.object.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pumba30 on 20.08.2015.
 */
public class DataFromJSON {

    public static final String TAG = DataFromJSON.class.getSimpleName();
    public static final int FIELD_NAME = 1;
    public static final String NAME = "name";

    public static final String BASE_IMAGE_POSTER_PATH = "http://image.tmdb.org/t/p/w342//";
    public static final String RESULTS = "results";

    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String HOME_PAGE = "homepage";
    public static final String TAG_LINE = "tagline";
    public static final String ORIG_LANGUAGE = "original_language";
    public static final String ORIG_TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_PATH = "poster_path";
    public static final String PRODUCTION_COMPANIES = "production_companies";
    public static final String PRODUCTION_COUNTRIES = "production_countries";
    public static final String GENRES = "genres";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String POPULARITY = "popularity";
    public static final String ID_MOVIE = "id";
    public static final String VOTE_COUNT = "vote_count";
    public static final String BUDGET = "budget";
    public static final String REVENUE = "revenue";
    public static final String RUN_TIME = "runtime";
    public static final String VIDEO = "video";
    public static final String ADULT = "adult";

    private String movieJSONString;
    private String movieJSONStringOnce = "";

    public DataFromJSON(String movieJSONString) throws JSONException {
        this.movieJSONString = movieJSONString;
    }


    public ArrayList<String> getPosterPathFromJSON() throws JSONException {
        Log.d(TAG, "getPosterPathFromJSON " + movieJSONString);
        JSONArray jsonArrayResult = getResultFromJSON(movieJSONString);
        ArrayList<String> pathToPosterMoviesArray = new ArrayList<>();
        for (int i = 0; i < jsonArrayResult.length(); i++) {

            JSONObject dataMovieItem = (JSONObject) jsonArrayResult.get(i);
            String pathToPoster = dataMovieItem.optString(POSTER_PATH);
            pathToPosterMoviesArray.add(BASE_IMAGE_POSTER_PATH + pathToPoster);

            Log.d(TAG, BASE_IMAGE_POSTER_PATH + pathToPoster
                    + " length array " + jsonArrayResult.length());
        }
        return pathToPosterMoviesArray;
    }

    private JSONArray getResultFromJSON(String movieJSONString) throws JSONException {
        JSONObject jsonObjectMovie = new JSONObject(movieJSONString);
        JSONArray jsonArrayResult = jsonObjectMovie.getJSONArray(RESULTS);
        return jsonArrayResult;
    }

    public Movie getMovie() throws JSONException {

        // JSONArray jsonArrayResult = getResultFromJSON(movieJSONString);
        Movie movie = new Movie();
        Log.d(TAG, " getResultFromJSON " + movieJSONStringOnce);

        try {
            JSONObject movieItem = new JSONObject(movieJSONStringOnce);
            movie.setAdult(movieItem.getBoolean(ADULT));
            movie.setBackdropPath(movieItem.optString(BACKDROP_PATH));
            movie.setId(movieItem.optInt(ID_MOVIE));
            movie.setOriginalLanguage(movieItem.optString(ORIG_LANGUAGE));
            movie.setOriginalTitle(movieItem.optString(ORIG_TITLE));
            movie.setOverview(movieItem.optString(OVERVIEW));
            movie.setReleaseDate(movieItem.optString(RELEASE_DATE));
            movie.setPopularity(movieItem.optLong(POPULARITY));
            movie.setOriginalTitle(movieItem.optString(ORIG_TITLE));
            movie.setVoteAverage(movieItem.optLong(VOTE_AVERAGE, 0));
            movie.setVideo(movieItem.optBoolean(VIDEO));
            movie.setVoteCount(movieItem.optInt(VOTE_COUNT));
            movie.setPosterPath(movieItem.optString(POSTER_PATH));
            movie.setHomePage(movieItem.optString(HOME_PAGE, "Nothing Found"));
            movie.setTagLine(movieItem.optString(TAG_LINE, "Nothing Found"));
            movie.setBudget(movieItem.optInt(BUDGET, 0));
            movie.setRevenue(movieItem.optInt(REVENUE, 0));
            movie.setRuntime(movieItem.optInt(RUN_TIME, 0));

            JSONArray genresJSON = movieItem.getJSONArray(GENRES);
            movie.setGenres(genresJSON.getString(FIELD_NAME));

            ArrayList<String> companyNames = movie.getProductionCompanies();
            JSONArray prodCompanies = movieItem.optJSONArray(PRODUCTION_COMPANIES);
            for (int i = 0; i < prodCompanies.length(); i++) {
                JSONObject company = (JSONObject) prodCompanies.get(i);
                String companyName = company.optString(NAME, "Nothing");
                companyNames.add(companyName + " / ");
            }
            movie.setProductionCompanies(companyNames);

            ArrayList<String> countryNames = movie.getProductionCountries();
            JSONArray prodCountries = movieItem.getJSONArray((PRODUCTION_COUNTRIES));
            for (int i = 0; i < prodCountries.length(); i++) {
                JSONObject country = prodCountries.optJSONObject(i);
                String countryName = country.optString(NAME, "Nothing");
                companyNames.add(countryName + " / ");
            }
            movie.setProductionCountries(countryNames);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return movie;
    }


    public int getIdMovie(int position) {
        JSONObject movieItem = null;
        try {
            JSONArray jsonArrayResult = getResultFromJSON(movieJSONString);
            movieItem = (JSONObject) jsonArrayResult.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert movieItem != null;
        return movieItem.optInt(ID_MOVIE);
    }

    public JSONArray getJsonArrayResult() throws JSONException {
        JSONArray jsonArrayResult = getResultFromJSON(movieJSONString);
        return jsonArrayResult;
    }

    public void setMovieJSONStringOnce(String movieJSONString) {
        movieJSONStringOnce = movieJSONString;
    }


}
