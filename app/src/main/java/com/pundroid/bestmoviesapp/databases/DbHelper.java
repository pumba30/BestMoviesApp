package com.pundroid.bestmoviesapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";

    public static DbHelper sInstance;

    public static synchronized DbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        sInstance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE movies (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_id, " +
                "poster_path_storage, " +
                "poster_path_web, " +
                "movie_title);");

        db.execSQL("CREATE TABLE details_movie (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_id, " +
                "backdrop_path_storage, " +
                "backdrop_path_web, " +
                "poster_path_storage, " +
                "poster_path_web," +
                "original_title, " +
                "overview, " +
                "release_date, " +
                "popularity, " +
                "title, " +
                "vote_average, " +
                "vote_count, " +
                "genres, " +
                "budget, " +
                "production_companies, " +
                "production_countries, " +
                "revenue, " +
                "runtime, " +
                "homepage" + ");");

        db.execSQL("CREATE TABLE genres(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "genre_name" + ")");
        db.execSQL("CREATE TABLE movie_genres (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_row_id, " +
                "genre_row_id, " +
                "FOREIGN KEY (movie_row_id) REFERENCES details_movie(movie_id) " +
                "FOREIGN KEY (genre_row_id) REFERENCES genres(_id)" + ");");

        db.execSQL("CREATE TABLE production_countries (_id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                "country_name" + ")");
        db.execSQL("CREATE TABLE movie_prod_countries (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_row_id, " +
                "country_row_id, " +
                "FOREIGN KEY (movie_row_id) REFERENCES details_movie(movie_id) " +
                "FOREIGN KEY (country_row_id) REFERENCES production_countries(_id)" + ");");

        db.execSQL("CREATE TABLE production_companies (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "company_name" + ");");
        db.execSQL("CREATE TABLE movie_prod_companies (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_row_id, " +
                "company_row_id, " +
                "FOREIGN KEY (movie_row_id) REFERENCES details_movie(movie_id) " +
                "FOREIGN KEY (company_row_id) REFERENCES production_companies(_id)" + ");");

        //these tables are not used
        db.execSQL("CREATE TABLE  actor_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "character, " +
                "name, " +
                "photo_path_web, " +
                "photo_path_storage " + ")");

        db.execSQL("CREATE TABLE  biography_actor_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "biography, " +
                "birthday, " +
                "deathday, " +
                "homepage, " +
                "name, " +
                "place_birth, " +
                "photo_path_web, " +
                "photo_path_storage " + ")");

        db.execSQL("CREATE TABLE  member_crew_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name, " +
                "job" +
                "photo_path_web, " +
                "photo_path_storage " + ")");

        Log.d(TAG, "Create tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
