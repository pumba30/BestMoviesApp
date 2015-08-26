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

    public DataFromJSON() throws JSONException {
    }


    public ArrayList<String> getPosterPathFromJSON() throws JSONException {
        Log.d(TAG, movieJSONString);
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

        Movie movie = new Movie();
        Log.d(TAG, " getResultFromJSON " + movieJSONString);

        try {
            JSONObject movieItem = new JSONObject(movieJSONString);
            movie.setAdult(movieItem.optBoolean(ADULT, false));
            movie.setBackdropPath(movieItem.optString(BACKDROP_PATH));
            movie.setId(movieItem.optInt(ID_MOVIE, 0));
            movie.setOriginalLanguage(movieItem.optString(ORIG_LANGUAGE, "Nothing Found"));
            movie.setOriginalTitle(movieItem.optString(ORIG_TITLE, "Nothing Found"));
            movie.setOverview(movieItem.optString(OVERVIEW, "Nothing Found"));
            movie.setReleaseDate(movieItem.optString(RELEASE_DATE, "Nothing Found"));
            movie.setPopularity(movieItem.optLong(POPULARITY, 0));
            movie.setOriginalTitle(movieItem.optString(ORIG_TITLE, "Nothing Found"));
            movie.setVoteAverage(movieItem.optLong(VOTE_AVERAGE, 0));
            movie.setVideo(movieItem.optBoolean(VIDEO, false));
            movie.setVoteCount(movieItem.optInt(VOTE_COUNT, 0));
            movie.setPosterPath(movieItem.optString(POSTER_PATH));
            movie.setHomePage(movieItem.optString(HOME_PAGE, "Nothing Found"));
            movie.setTagLine(movieItem.optString(TAG_LINE, "Nothing Found"));
            movie.setBudget(movieItem.optInt(BUDGET, 0));
            movie.setRevenue(movieItem.optInt(REVENUE, 0));
            movie.setRuntime(movieItem.optInt(RUN_TIME, 0));

            ArrayList<String> genresList = new ArrayList<>();
            JSONArray genresJSON = movieItem.getJSONArray(GENRES);
            for (int i = 0; i < genresJSON.length(); i++) {
                JSONObject objGenre = (JSONObject) genresJSON.get(i);
                String genreStr = objGenre.optString(NAME, "Nothing found");
                genresList.add(genreStr + " / ");
            }
            movie.setGenres(genresList);

            ArrayList<String> companyNames = new ArrayList<>();
            JSONArray prodCompanies = movieItem.optJSONArray(PRODUCTION_COMPANIES);
            for (int i = 0; i < prodCompanies.length(); i++) {
                JSONObject company = (JSONObject) prodCompanies.get(i);
                String companyName = company.optString(NAME, "Nothing");
                companyNames.add(companyName + " / ");
            }
            movie.setProductionCompanies(companyNames);

            ArrayList<String> countryNames = new ArrayList<>();
            JSONArray prodCountries = movieItem.getJSONArray(PRODUCTION_COUNTRIES);
            for (int i = 0; i < prodCountries.length(); i++) {
                JSONObject country = prodCountries.optJSONObject(i);
                String countryName = country.optString(NAME, "Nothing");
                countryNames.add(countryName + " / ");
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

    public void setMovieJSONString(String movieJSONString) {
        this.movieJSONString = movieJSONString;
    }

}
