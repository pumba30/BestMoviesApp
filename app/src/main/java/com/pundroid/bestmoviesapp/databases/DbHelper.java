package com.pundroid.bestmoviesapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pundroid.bestmoviesapp.databases.DbSchema.MoviePosterTable;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG = DbHelper.class.getSimpleName();

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "messages.db";
    public static final String COMMA = ", ";
    public static final String BRACKET = ");";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MoviePosterTable.TABLE_NAME + "("
                + MoviePosterTable.Columns.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviePosterTable.Columns.MOVIE_ID + COMMA
                + MoviePosterTable.Columns.PATH_TO_POSTER
                + BRACKET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will desrtoy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + MoviePosterTable.TABLE_NAME);
        onCreate(db);
    }
}
