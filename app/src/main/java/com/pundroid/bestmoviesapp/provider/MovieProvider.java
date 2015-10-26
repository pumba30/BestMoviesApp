package com.pundroid.bestmoviesapp.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.pundroid.bestmoviesapp.databases.DbHelper;
import com.pundroid.bestmoviesapp.databases.DbSchema.MovieTable;

/**
 * Created by pumba30 on 16.10.2015.
 */

public class MovieProvider extends ContentProvider {

    public static final String AUTHORITY = "com.pundroid.bestmoviesapp.provider";
    public static final String BASE_PATH = "movies";
    public static final int MOVIES = 1;
    public static final int MOVIE_ID = 2;
    public static final String ROW_LIMIT = " LIMIT 20";

    private DbHelper mDbHelper;
    private Context mContext;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIE_ID);
    }

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    @Override
    public boolean onCreate() {
        mDbHelper = DbHelper.getInstance(getContext());
        if (getContext() != null) {
            mContext = getContext();
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sUriMatcher.match(uri);
        Cursor cursor = null;
        if (uriType == MOVIES) {
            cursor = mDbHelper.getWritableDatabase().query(true, MovieTable.TABLE_NAME, projection,
                    selection, selectionArgs, null, null, sortOrder, ROW_LIMIT);
            cursor.setNotificationUri(mContext.getContentResolver(), uri);
        }
        if (uriType == MOVIE_ID) {
           // cursor = mDbHelper.getMovieById(uri.getLastPathSegment());
            //cursor.setNotificationUri(mContext.getContentResolver(), uri);
        } else {
            throw new IllegalArgumentException("Unknown URI");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriType = sUriMatcher.match(uri);
        if (uriType == MOVIES) {
            return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd."
                    + AUTHORITY + "." + BASE_PATH;
        }
        if (uriType == MOVIE_ID) {
            return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd."
                    + AUTHORITY + "." + BASE_PATH;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long newId = mDbHelper.getWritableDatabase().insert(
                MovieTable.TABLE_NAME, null, values);
        if (newId > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, newId);
            mContext.getContentResolver().notifyChange(uri, null);
            return newUri;
        } else {
            throw new SQLException("Filed to insert row into " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDbHelper.getWritableDatabase().update(MovieTable.TABLE_NAME,
                values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mDbHelper.getWritableDatabase().delete(MovieTable.TABLE_NAME,
                selection, selectionArgs);
    }


}
