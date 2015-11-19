package com.pundroid.bestmoviesapp.database;

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
        db.execSQL("CREATE TABLE movie (movie_id INTEGER PRIMARY KEY, " +
                "poster_path_storage TEXT, " +
                "poster_path_web TEXT, " +
                "movie_title TEXT);");

        db.execSQL("CREATE TABLE details_movie (movie_id INTEGER PRIMARY KEY, " +
                "backdrop_path_storage TEXT, " +
                "backdrop_path_web TEXT, " +
                "poster_path_storage TEXT, " +
                "poster_path_web TEXT," +
                "original_title TEXT, " +
                "overview TEXT, " +
                "release_date TEXT, " +
                "popularity REAL, " +
                "title TEXT, " +
                "vote_average REAL, " +
                "vote_count INTEGER, " +
                "budget INTEGER, " +
                "revenue INTEGER, " +
                "runtime INTEGER, " +
                "homepage TEXT" + ");");

        db.execSQL("CREATE TABLE genre(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "genre_name TEXT" + ");");
        db.execSQL("CREATE TABLE movie_genre (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_row_id INTEGER, " +
                "genre_row_id INTEGER, " +
                "FOREIGN KEY (movie_row_id) REFERENCES details_movie(movie_id) " +
                "FOREIGN KEY (genre_row_id) REFERENCES genres(_id)" + ");");

        db.execSQL("CREATE TABLE production_country (_id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                "country_name TEXT" + ");");
        db.execSQL("CREATE TABLE movie_prod_country (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_row_id INTEGER, " +
                "country_row_id INTEGER, " +
                "FOREIGN KEY (movie_row_id) REFERENCES details_movie(movie_id) " +
                "FOREIGN KEY (country_row_id) REFERENCES production_country(_id)" + ");");

        db.execSQL("CREATE TABLE production_company (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "company_name TEXT" + ");");
        db.execSQL("CREATE TABLE movie_prod_company (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_row_id INTEGER, " +
                "company_row_id INTEGER, " +
                "FOREIGN KEY (movie_row_id) REFERENCES details_movie(movie_id) " +
                "FOREIGN KEY (company_row_id) REFERENCES production_company(_id)" + ");");

        db.execSQL("CREATE TABLE  actor (actor_id INTEGER PRIMARY KEY, " +
                "character TEXT, " +
                "name TEXT, " +
                "photo_path_web TEXT, " +
                "photo_path_storage TEXT" + ");");
        db.execSQL("CREATE TABLE movie_actor (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "movie_id INTEGER, " +
                "actor_id INTEGER, " +
                "FOREIGN KEY (movie_id) REFERENCES  details_movie(movie_id) " +
                "FOREIGN KEY (actor_id) REFERENCES actor(actor_id)" + ");");

        //*********************************************************************

        //these tables are not used
        db.execSQL("CREATE TABLE  member_crew (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "job TEXT," +
                "photo_path_web TEXT, " +
                "photo_path_storage TEXT" + ");");

        db.execSQL("CREATE TABLE  biography_actor (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "biography TEXT, " +
                "birthday TEXT, " +
                "deathday TEXT, " +
                "homepage TEXT, " +
                "name TEXT, " +
                "place_birth TEXT, " +
                "photo_path_web TEXT, " +
                "photo_path_storage TEXT" + ");");


        Log.d(TAG, "Create tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
