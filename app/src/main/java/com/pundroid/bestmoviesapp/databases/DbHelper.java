package com.pundroid.bestmoviesapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pundroid.bestmoviesapp.databases.DbSchema.MoviePosterTable;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "messages.db";

    private static DbHelper sInstance;



    public static DbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MoviePosterTable.TABLE_NAME + "("
                + MoviePosterTable.Columns.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviePosterTable.Columns.MOVIE_ID + ", "
                + MoviePosterTable.Columns.PATH_TO_POSTER
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
