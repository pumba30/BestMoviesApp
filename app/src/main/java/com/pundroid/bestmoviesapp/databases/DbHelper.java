package com.pundroid.bestmoviesapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pundroid.bestmoviesapp.databases.DbSchema.ActorsTable;
import com.pundroid.bestmoviesapp.databases.DbSchema.BiographyActorTable;
import com.pundroid.bestmoviesapp.databases.DbSchema.CrewTable;
import com.pundroid.bestmoviesapp.databases.DbSchema.DetailsMovieTable;
import com.pundroid.bestmoviesapp.databases.DbSchema.GenresTable;
import com.pundroid.bestmoviesapp.databases.DbSchema.MovieTable;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "messages.db";
    public static final String COMMA = ", ";
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
    public static final String FOREIGN_KEY = " FOREIGN KEY(";
    public static final String REFERENCES = ") REFERENCES ";

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
        db.execSQL(CREATE_TABLE + MovieTable.TABLE_NAME + "("
                + MovieTable.Column.ROW_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT
                + MovieTable.Column.MOVIE_ID + COMMA
                + MovieTable.Column.POSTER_PATH_STORAGE + COMMA
                + MovieTable.Column.POSTER_PATH_WEB
                + ");");

        db.execSQL(CREATE_TABLE + DetailsMovieTable.TABLE_NAME + "("
                + DetailsMovieTable.Column.ROW_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT
                + DetailsMovieTable.Column.MOVIE_ID + COMMA
                + DetailsMovieTable.Column.BACKDROP_PATH_STORAGE + COMMA
                + DetailsMovieTable.Column.BACKDROP_PATH_WEB + COMMA
                + DetailsMovieTable.Column.POSTER_PATH_STORAGE + COMMA
                + DetailsMovieTable.Column.POSTER_PATH_WEB + COMMA
                + DetailsMovieTable.Column.ORIGINAL_TITLE + COMMA
                + DetailsMovieTable.Column.OVERVIEW + COMMA
                + DetailsMovieTable.Column.RELEASE_DATE + COMMA
                + DetailsMovieTable.Column.POPULARITY + COMMA
                + DetailsMovieTable.Column.TITLE + COMMA
                + DetailsMovieTable.Column.VOTE_AVERAGE + COMMA
                + DetailsMovieTable.Column.VOTE_COUNT + COMMA
                + DetailsMovieTable.Column.GENRES + COMMA
                + DetailsMovieTable.Column.BUDGET + COMMA
                + DetailsMovieTable.Column.PRODUCTION_COMPANIES + COMMA
                + DetailsMovieTable.Column.PRODUCTION_COUNTRIES + COMMA
                + DetailsMovieTable.Column.REVENUE + COMMA
                + DetailsMovieTable.Column.RUNTIME + COMMA
                + DetailsMovieTable.Column.HOMEPAGE
                + ");");

        db.execSQL(CREATE_TABLE + GenresTable.TABLE_NAME + "("
                + GenresTable.Column.ROW_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT
                + GenresTable.Column.GENRE_NAME + COMMA
                + FOREIGN_KEY + GenresTable.Column.GENRE_NAME + REFERENCES
                + DetailsMovieTable.TABLE_NAME + " (" + DetailsMovieTable.Column.ROW_ID + "));");

        db.execSQL(CREATE_TABLE + ActorsTable.TABLE_NAME + "("
                + ActorsTable.Column.ROW_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT
                + ActorsTable.Column.ACTOR_ID + COMMA
                + ActorsTable.Column.CHARACTER + COMMA
                + ActorsTable.Column.NAME + COMMA
                + ActorsTable.Column.PROFILE_PHOTO_PATH + COMMA
                + ActorsTable.Column.PROFILE_PHOTO_PATH_TO_STORAGE + COMMA
                + FOREIGN_KEY + ActorsTable.Column.ACTOR_ID + REFERENCES
                + DetailsMovieTable.TABLE_NAME + " (" + DetailsMovieTable.Column.ROW_ID + "));");

        db.execSQL(CREATE_TABLE + BiographyActorTable.TABLE_NAME + "("
                + BiographyActorTable.Column.ROW_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT
                + BiographyActorTable.Column.BIOGRAPHY + COMMA
                + BiographyActorTable.Column.BIRTHDAY + COMMA
                + BiographyActorTable.Column.DEATHDAY + COMMA
                + BiographyActorTable.Column.HOMEPAGE + COMMA
                + BiographyActorTable.Column.ACTOR_ID + COMMA
                + BiographyActorTable.Column.NAME + COMMA
                + BiographyActorTable.Column.PLACE_BIRTH + COMMA
                + BiographyActorTable.Column.PROFILE_PHOTO_PATH + COMMA
                + BiographyActorTable.Column.PROFILE_PHOTO_PATH_TO_STORAGE + COMMA
                + FOREIGN_KEY + BiographyActorTable.Column.ACTOR_ID + REFERENCES
                + ActorsTable.TABLE_NAME + "(" + ActorsTable.Column.ROW_ID + "))");

        db.execSQL(CREATE_TABLE + CrewTable.TABLE_NAME + " ("
                + CrewTable.Column.ROW_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT
                + CrewTable.Column.NAME + COMMA
                + CrewTable.Column.JOB + COMMA
                + CrewTable.Column.MEMBER_CREW_ID + COMMA
                + CrewTable.Column.PROFILE_PHOTO_PATH + COMMA
                + CrewTable.Column.PROFILE_PHOTO_PATH_TO_STORAGE + COMMA
                + FOREIGN_KEY + CrewTable.Column.MEMBER_CREW_ID + REFERENCES
                + DetailsMovieTable.TABLE_NAME + "(" + DetailsMovieTable.Column.ROW_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
