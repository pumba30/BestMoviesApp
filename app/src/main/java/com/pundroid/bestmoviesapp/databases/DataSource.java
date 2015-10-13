package com.pundroid.bestmoviesapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pundroid.bestmoviesapp.databases.DataModel.Poster;
import com.pundroid.bestmoviesapp.databases.DbSchema.MoviePosterTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DataSource {

    private SQLiteDatabase database;
    private DbHelper dbHelper;

    private String[] getAllPosters = {MoviePosterTable.Columns.MOVIE_ID,
            MoviePosterTable.Columns.PATH_TO_POSTER};

    public DataSource(Context context) {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }

    public Poster createPoster(String movieId, String path) {
        ContentValues cv = new ContentValues();
        cv.put(MoviePosterTable.Columns.COLUMN_ID, movieId);
        cv.put(MoviePosterTable.Columns.PATH_TO_POSTER, path);

        database.insert(MoviePosterTable.TABLE_NAME, null, cv);

        Cursor cursor = database.query(MoviePosterTable.TABLE_NAME, getAllPosters,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Poster newPoster = cursorToPoster(cursor);
        cursor.close();
        return newPoster;
    }

    public void deletePoster(Poster poster) {
        int movieId = poster.getMovieId();
        database.delete(MoviePosterTable.TABLE_NAME,
                MoviePosterTable.Columns.MOVIE_ID + " = " + movieId, null);
    }

    public List<Poster> getPosters() {
        List<Poster> posters = new ArrayList<>();

        Cursor cursor = database.query(MoviePosterTable.TABLE_NAME, getAllPosters,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (cursor.isAfterLast()) {
            Poster poster = cursorToPoster(cursor);
            posters.add(poster);
            cursor.moveToNext();
        }

        cursor.close();
        return posters;
    }

    private Poster cursorToPoster(Cursor cursor) {
        Poster poster = new Poster();
        poster.setMovieId(cursor.getInt(cursor.getColumnIndex(MoviePosterTable.Columns.MOVIE_ID)));
        poster.setPathToPoster(cursor.getString(cursor.getColumnIndex(MoviePosterTable.Columns.PATH_TO_POSTER)));

        return poster;
    }


}
