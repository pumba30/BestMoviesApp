package com.pundroid.bestmoviesapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.pundroid.bestmoviesapp.databases.DbSchema.DetailsMovieTable;
import com.pundroid.bestmoviesapp.databases.DbSchema.MovieTable;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.objects.MovieDetails;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumba30 on 23.10.2015.
 */
public class DataSource {
    private static final String TAG = DataSource.class.getSimpleName();
    public static final String ROW_LIMIT = "20";

    public static final String[] MAIN_MOVIE_FIELDS = new String[]{
            DbSchema.MovieTable.Column.ROW_ID,
            DbSchema.MovieTable.Column.MOVIE_ID,
            DbSchema.MovieTable.Column.POSTER_PATH_STORAGE,
            DbSchema.MovieTable.Column.POSTER_PATH_WEB,
    };

    private SQLiteDatabase mDatabase;
    private DbHelper mDbHelper;

    public DataSource(Context context) {
        mDbHelper = DbHelper.getInstance(context);
        mDatabase = open();
    }

    private SQLiteDatabase open() throws SQLException {
        return mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    // creating movie poster for main screen with movie_id
    public long saveMainMovie(Movie movie) {

        mDatabase.beginTransaction();
        ContentValues values = new ContentValues();
        if (movie.getId() != 0) {
            values.put(MovieTable.Column.MOVIE_ID, movie.getId());
        }
        if (movie.getPosterPath() != null) {
            values.put(MovieTable.Column.POSTER_PATH_WEB, movie.getPosterPath());
        }
        if (movie.getPosterPathStorage() != null) {
            values.put(MovieTable.Column.POSTER_PATH_STORAGE, movie.getPosterPathStorage());
        }
        if (movie.getTitle() != null) {
            values.put(MovieTable.Column.MOVIE_TITLE, movie.getTitle());
        }
        long insertId = mDatabase.insert(MovieTable.TABLE_NAME, null, values);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

        return insertId;
    }

    public long saveMovieDetail(MovieDetails movieDetails) {

        mDatabase.beginTransaction();
        ContentValues values = new ContentValues();
        if (movieDetails.getId() != 0) {
            values.put(DetailsMovieTable.Column.MOVIE_ID, movieDetails.getId());
        }
        if (movieDetails.getBackdropPath() != null) {
            values.put(DetailsMovieTable.Column.BACKDROP_PATH_WEB,
                    movieDetails.getBackdropPath());
        }
        if (movieDetails.getBackdropPathStorage() != null) {
            values.put(DetailsMovieTable.Column.BACKDROP_PATH_STORAGE,
                    movieDetails.getPosterPathStorage());
        }
        if (movieDetails.getPosterPath() != null) {
            values.put(DetailsMovieTable.Column.POSTER_PATH_WEB,
                    movieDetails.getPosterPath());
        }
        if (movieDetails.getPosterPathStorage() != null) {
            values.put(DetailsMovieTable.Column.POSTER_PATH_STORAGE,
                    movieDetails.getPosterPathStorage());
        }
        if (movieDetails.getOriginalTitle() != null) {
            values.put(DetailsMovieTable.Column.ORIGINAL_TITLE,
                    movieDetails.getOriginalTitle());
        }
        if (movieDetails.getOverview() != null) {
            values.put(DetailsMovieTable.Column.OVERVIEW,
                    movieDetails.getOverview());
        }
        if (movieDetails.getReleaseDate() != null) {
            values.put(DetailsMovieTable.Column.RELEASE_DATE,
                    movieDetails.getReleaseDate());
        }
        if (movieDetails.getPopularity() != null) {
            values.put(DetailsMovieTable.Column.POPULARITY,
                    movieDetails.getPopularity());
        }
        if (movieDetails.getTitle() != null) {
            values.put(DetailsMovieTable.Column.TITLE,
                    movieDetails.getTitle());
        }
        if (movieDetails.getVoteAverage() != null) {
            values.put(DetailsMovieTable.Column.VOTE_AVERAGE,
                    movieDetails.getVoteAverage());
        }
        if (movieDetails.getVoteCount() != null) {
            values.put(DetailsMovieTable.Column.VOTE_COUNT,
                    movieDetails.getVoteCount());
        }
        if (movieDetails.getGenres() != null) {
            values.put(DetailsMovieTable.Column.GENRES,
                    getStringFromList(movieDetails.getGenres()));

            Log.d(TAG, "GENRES " + getStringFromList(movieDetails.getGenres()));
        }
        if (movieDetails.getBudget() != null) {
            values.put(DetailsMovieTable.Column.BUDGET,
                    movieDetails.getBudget());
        }
        if (movieDetails.getProductionCompanies() != null) {
            values.put(DetailsMovieTable.Column.PRODUCTION_COMPANIES,
                    getStringFromList(movieDetails.getProductionCompanies()));
        }
        if (movieDetails.getProductionCountries() != null) {
            values.put(DetailsMovieTable.Column.PRODUCTION_COUNTRIES,
                    getStringFromList(movieDetails.getProductionCountries()));
        }
        if (movieDetails.getRevenue() != null) {
            values.put(DetailsMovieTable.Column.REVENUE,
                    movieDetails.getRevenue());
        }
        if (movieDetails.getRuntime() != null) {
            values.put(DetailsMovieTable.Column.RUNTIME,
                    movieDetails.getRuntime());
        }
        if (movieDetails.getHomepage() != null) {
            values.put(DetailsMovieTable.Column.HOMEPAGE,
                    movieDetails.getHomepage());
        }
        long insertId = mDatabase.insert(DetailsMovieTable.TABLE_NAME, null, values);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        Log.d(TAG, "Save movie details");
        return insertId;
    }

    public MovieDetails getMovieDetails(int movieId) {
        MovieDetails details = new MovieDetails();
        Cursor cursor = mDatabase.query(DetailsMovieTable.TABLE_NAME, null,
                DetailsMovieTable.Column.MOVIE_ID + "=" + movieId, null, null, null, null);
        cursor.moveToFirst();
        details.setBackdropPath(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.BACKDROP_PATH_WEB)));
        details.setBackdropPathStorage(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.BACKDROP_PATH_STORAGE)));
        details.setPosterPath(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.POSTER_PATH_WEB)));
        details.setPosterPathStorage(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.POSTER_PATH_STORAGE)));
        details.setId(cursor.getInt(
                cursor.getColumnIndex(DetailsMovieTable.Column.MOVIE_ID)));
        details.setOriginalTitle(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.ORIGINAL_TITLE)));
        details.setOverview(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.OVERVIEW)));
        details.setReleaseDate(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.RELEASE_DATE)));
        details.setPopularity(cursor.getFloat(
                cursor.getColumnIndex(DetailsMovieTable.Column.POPULARITY)));
        details.setTitle(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.TITLE)));
        details.setVoteAverage(cursor.getFloat(
                cursor.getColumnIndex(DetailsMovieTable.Column.VOTE_AVERAGE)));
        details.setVoteCount(cursor.getInt(
                cursor.getColumnIndex(DetailsMovieTable.Column.VOTE_COUNT)));
//        details.setGenres(cursor.getString(
//                cursor.getColumnIndex(DetailsMovieTable.Column.GENRES)));
//        details.setProductionCompanies(cursor.getString(
//                cursor.getColumnIndex(DetailsMovieTable.Column.PRODUCTION_COMPANIES)));
//        details.setProductionCountries(cursor.getString(
//                cursor.getColumnIndex(DetailsMovieTable.Column.PRODUCTION_COUNTRIES)));
        details.setBudget(cursor.getInt(
                cursor.getColumnIndex(DetailsMovieTable.Column.BUDGET)));
        details.setRevenue(cursor.getInt(
                cursor.getColumnIndex(DetailsMovieTable.Column.REVENUE)));
        details.setRuntime(cursor.getInt(
                cursor.getColumnIndex(DetailsMovieTable.Column.RUNTIME)));
        details.setHomepage(cursor.getString(
                cursor.getColumnIndex(DetailsMovieTable.Column.HOMEPAGE)));

        cursor.close();
        return details;
    }

    //check for entries in the table
    public boolean checkEntry(int movieId) {
        String movieIdToStr = String.valueOf(movieId);
        boolean result = false;
        Cursor cursor = mDatabase.query(MovieTable.TABLE_NAME,
                new String[]{MovieTable.Column.MOVIE_ID},
                MovieTable.Column.MOVIE_ID + "=" + movieIdToStr, null, null, null, null, null);
        Log.d(TAG, "Count cursor before: " + String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            Log.d(TAG, "Count cursor in: " + String.valueOf(cursor.getCount()));
            result = true;
        }
        cursor.close();
        return result;
    }


    public List<Movie> getAllPostersMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = mDatabase.query(true, MovieTable.TABLE_NAME, MAIN_MOVIE_FIELDS,
                null, null, null, null, null, ROW_LIMIT);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setPosterPathStorage(cursor.getString(
                        cursor.getColumnIndex(MovieTable.Column.POSTER_PATH_STORAGE)));
                movie.setId(cursor.getInt(
                        cursor.getColumnIndex(MovieTable.Column.MOVIE_ID)));
                movie.setPosterPath(cursor.getString(
                        cursor.getColumnIndex(MovieTable.Column.POSTER_PATH_WEB)));
//                movie.setTitle(cursor.getString(
//                        cursor.getColumnIndex(MovieTable.Column.MOVIE_TITLE)));
                movies.add(movie);
                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }
        }

        return movies;
    }

    public boolean isTableNotEmpty(String tableName) {
        if (DatabaseUtils.queryNumEntries(mDatabase, tableName) > 0) {
            return true;
        }
        return false;
    }


    private List<Long> getListFromStringJson(String string) throws JSONException {
        JSONArray jsonArray = new JSONArray(string);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Long aLong = jsonArray.getLong(i);
            list.add(aLong);
        }
        return list;
    }

    public String getStringFromList(List<?> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }


}
