package com.pundroid.bestmoviesapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pundroid.bestmoviesapp.databases.DbHelper;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.objects.MovieDetail;
import com.pundroid.bestmoviesapp.objects.QueryResultMovies;
import com.pundroid.bestmoviesapp.utils.RestClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 15.10.2015.
 */
public class DownloadService extends IntentService {
    public static final String TAG = DownloadService.class.getSimpleName();

    private List<MovieDetail> mMovieDetails = new ArrayList<>();

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int numPage = intent.getIntExtra(DownloadHelperService.NUM_PAGE, 1);
            String typeMovies = intent.getStringExtra(DownloadHelperService.TYPE_MOVIES);

            if (isConnected()) {
                // get movie from internet
                downloadMovies(numPage, typeMovies);
            } else {
                // get movie from database
                DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
                List<MovieDetail> movies = new ArrayList<>();

                Cursor cursor = dbHelper.getAllMovies();
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        MovieDetail movie = dbHelper.getMovieFromCursor(cursor);
                        movies.add(movie);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    getMoviesByType(movies);
                }
            }
        }
    }


    //check internet connection
    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void downloadMovies(int numPage, String typeMovies) {

        if (typeMovies.equals(RestClient.TOP_RATED_MOVIES)) {
            RestClient.get().getMoviesByType(numPage, typeMovies,
                    new Callback<QueryResultMovies>() {
                        @Override
                        public void success(QueryResultMovies queryResultMovies, Response response) {
                            getMoviesFromResult(queryResultMovies);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, "Download failed");

                        }
                    });
        }

        if (typeMovies.equals(RestClient.POPULAR_MOVIES)) {
            RestClient.get().getPopularMovies(numPage, new Callback<QueryResultMovies>() {
                @Override
                public void success(QueryResultMovies queryResultMovies, Response response) {
                    getMoviesFromResult(queryResultMovies);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Download failed");
                }
            });
        }

        if (typeMovies.equals(RestClient.UPCOMING_MOVIES)) {
            RestClient.get().getUpcomingMovies(numPage, new Callback<QueryResultMovies>() {
                @Override
                public void success(QueryResultMovies queryResultMovies, Response response) {
                    getMoviesFromResult(queryResultMovies);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Download failed");
                }
            });
        }
    }

    private void getMoviesFromResult(QueryResultMovies queryResultMovies) {
        final List<Movie> movies = queryResultMovies.getResults();
        for (Movie movie : movies) {
            int movieId = movie.getId();
            RestClient.get().getDetailMovieById(movieId, new Callback<MovieDetail>() {
                @Override
                public void success(MovieDetail movieDetail, Response response) {
                    mMovieDetails.add(movieDetail);
                    if (mMovieDetails.size() == movies.size()) {
                        // fill database
                        fillDatabase(mMovieDetails);
                        getMoviesByType(mMovieDetails);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Download failed");
                }
            });
        }
    }


    private void getMoviesByType(List<MovieDetail> movies) {
        if (movies != null) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(GridMovieFragment.MOVIE_DETAIL, (Serializable) movies);
            broadcastIntent.setAction(GridMovieFragment.ACTION_SEND_MOVIE_DETAIL);
            sendBroadcast(broadcastIntent);
            stopSelf();
        }
    }

    private void fillDatabase(final List<MovieDetail> movies) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Fill Database");
                DbHelper dbHelper = DbHelper.getInstance(getApplicationContext());
                try {
                    for (MovieDetail movie : movies) {
                        if (!dbHelper.checkEntry(movie.getId())) {
                            String path = getPosterUrl(movie);
                            String pathStorage = saveImageToStorage(path, movie.getId());
                            movie.setPosterPathStorage(pathStorage);
                            dbHelper.createMovieInDb(movie);
                            Log.d(TAG, "Path Storage: " + pathStorage);
                        }
                    }
                } catch (SQLException e) {
                    Log.d(TAG, "Fail fill database");
                }
            }
        }).start();
    }

    private String getPosterUrl(MovieDetail movie) {
        return RestClient.BASE_PATH_TO_IMAGE_W780
                + movie.getPosterPath();
    }

    @Nullable
    private String saveImageToStorage(String pathWeb, int movieId) {
        String movieName = String.valueOf(movieId);
        File fileImage = new File(getApplicationContext().getCacheDir(), movieName + ".jpg");
        OutputStream outputStream;
        if (!fileImage.exists()) {
            try {
                fileImage.createNewFile();
                URL imageUrl = new URL(pathWeb);
                URLConnection connection = imageUrl.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                outputStream = new BufferedOutputStream(new FileOutputStream(fileImage));
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);
                Log.d(TAG, "Load image");
                outputStream.flush();
                outputStream.close();
                return fileImage.getPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
