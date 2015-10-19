package com.pundroid.bestmoviesapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.pundroid.bestmoviesapp.databases.DbSchema.MovieTable;
import com.pundroid.bestmoviesapp.objects.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "messages.db";
    public static final String COMMA = ", ";
    public static final String ROW_LIMIT = "20";
    public static final String[] MOVIE_FIELDS = new String[]{
            MovieTable.Column.ROW_ID,
            MovieTable.Column.MOVIE_ID,
            MovieTable.Column.BACKDROP_PATH_STORAGE,
            MovieTable.Column.BACKDROP_PATH_WEB,
            MovieTable.Column.BACKDROP_PATH_STORAGE,
            MovieTable.Column.POSTER_PATH_STORAGE,
            MovieTable.Column.POSTER_PATH_WEB,
            MovieTable.Column.ORIGINAL_TITLE,
            MovieTable.Column.OVERVIEW,
            MovieTable.Column.RELEASE_DATE,
            MovieTable.Column.POPULARITY,
            MovieTable.Column.TITLE,
            MovieTable.Column.VOTE_AVERAGE,
            MovieTable.Column.VOTE_COUNT,
            MovieTable.Column.GENRES
    };

    private static DbHelper sInstance;
    private SQLiteDatabase mDatabase;

    public static synchronized DbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MovieTable.TABLE_NAME + "("
                + MovieTable.Column.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieTable.Column.MOVIE_ID + COMMA
                + MovieTable.Column.BACKDROP_PATH_STORAGE + COMMA
                + MovieTable.Column.BACKDROP_PATH_WEB + COMMA
                + MovieTable.Column.POSTER_PATH_STORAGE + COMMA
                + MovieTable.Column.POSTER_PATH_WEB + COMMA
                + MovieTable.Column.ORIGINAL_TITLE + COMMA
                + MovieTable.Column.OVERVIEW + COMMA
                + MovieTable.Column.RELEASE_DATE + COMMA
                + MovieTable.Column.POPULARITY + COMMA
                + MovieTable.Column.TITLE + COMMA
                + MovieTable.Column.VOTE_AVERAGE + COMMA
                + MovieTable.Column.VOTE_COUNT + COMMA
                + MovieTable.Column.GENRES
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long createMovieInDb(MovieDetail movie) {
        ContentValues values = new ContentValues();
        if (movie.getId() != 0) {
            values.put(MovieTable.Column.MOVIE_ID, movie.getId());
        }
        if (movie.getBackdropPath() != null) {
            values.put(MovieTable.Column.BACKDROP_PATH_WEB, movie.getBackdropPath());
        }
        if (movie.getPosterPath() != null) {
            values.put(MovieTable.Column.POSTER_PATH_WEB, movie.getPosterPath());
        }
        if (movie.getPosterPathStorage() != null) {
            values.put(MovieTable.Column.POSTER_PATH_STORAGE, movie.getPosterPathStorage());
        }
        if (movie.getOriginalTitle() != null) {
            values.put(MovieTable.Column.ORIGINAL_TITLE, movie.getOriginalTitle());
        }
        if (movie.getOverview() != null) {
            values.put(MovieTable.Column.OVERVIEW, movie.getOverview());
        }
        if (movie.getReleaseDate() != null) {
            values.put(MovieTable.Column.RELEASE_DATE, movie.getReleaseDate());
        }
        if (movie.getPopularity() != null) {
            values.put(MovieTable.Column.POPULARITY, movie.getPopularity());
        }
        if (movie.getTitle() != null) {
            values.put(MovieTable.Column.TITLE, movie.getTitle());
        }
        if (movie.getVoteAverage() != null) {
            values.put(MovieTable.Column.VOTE_AVERAGE, movie.getVoteAverage());
        }
        if (movie.getVoteCount() != null) {
            values.put(MovieTable.Column.VOTE_COUNT, movie.getVoteCount());
        }
//        if (movie.getGenreIds() != null) {
//            String genreIdsString = listToStringJson(movie.getGenreIds());
//            values.put(MovieTable.Column.GENRES, genreIdsString);
//        }
        return mDatabase.insert(MovieTable.TABLE_NAME, null, values);
    }

    private String listToStringJson(List<Long> genres) {
        Gson gson = new Gson();
        return gson.toJson(genres);
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


    public Cursor getMovieById(String movieId) throws SQLException {
        Cursor cursor = mDatabase.query(true, MovieTable.TABLE_NAME, MOVIE_FIELDS,
                MovieTable.Column.MOVIE_ID + "='" + movieId + "'", null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getAllMovies() {
        Cursor cursor = mDatabase.query(true, MovieTable.TABLE_NAME,
                MOVIE_FIELDS, null, null, null, null, null, ROW_LIMIT);
        return cursor;
    }

    public MovieDetail getMovieFromCursor(Cursor cursor) {
        MovieDetail movie = new MovieDetail();
        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieTable.Column.MOVIE_ID)));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieTable.Column.BACKDROP_PATH_WEB)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieTable.Column.POSTER_PATH_WEB)));
        movie.setPosterPathStorage(cursor.getString(cursor.getColumnIndex(MovieTable.Column.POSTER_PATH_STORAGE)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieTable.Column.ORIGINAL_TITLE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieTable.Column.OVERVIEW)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieTable.Column.RELEASE_DATE)));
        movie.setPopularity(cursor.getFloat(cursor.getColumnIndex(MovieTable.Column.POPULARITY)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieTable.Column.TITLE)));
        movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(MovieTable.Column.VOTE_AVERAGE)));
        movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieTable.Column.VOTE_COUNT)));
        String genreFromCursor = cursor.getString(cursor.getColumnIndex(MovieTable.Column.GENRES));
//        Log.d(TAG, "Genre JSON String: " + genreFromCursor);
//        try {
//            List<Long> genreIds =
//                    getListFromStringJson(genreFromCursor);
//            movie.setGenreIds(genreIds);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return movie;
    }


}
