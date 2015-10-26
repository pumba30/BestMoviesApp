package com.pundroid.bestmoviesapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pundroid.bestmoviesapp.databases.DataSource;
import com.pundroid.bestmoviesapp.databases.DbSchema.MovieTable;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;
import com.pundroid.bestmoviesapp.objects.Movie;
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
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 15.10.2015.
 */
public class DownloadService extends IntentService {
    public static final String TAG = DownloadService.class.getSimpleName();
    public static final String JPG = ".jpg";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int numPage = intent.getIntExtra(DownloadHelperService.NUM_PAGE, 1);
            String typeMovies = intent.getStringExtra(DownloadHelperService.TYPE_MOVIES);
            boolean isConnected = intent.getBooleanExtra(DownloadHelperService.IS_CONNECTED, true);
            if (isConnected) {
                // get movie from internet
                downloadMainMovies(numPage, typeMovies);
            } else {
                downloadFromDbMainPosters();
            }
        }
    }

    private void downloadFromDbMainPosters() {
        DataSource dataSource = new DataSource(getApplicationContext());
        List<Movie> movies = dataSource.getAllPostersMovies();
        getMoviesByType(movies);
    }

    private void downloadMainMovies(int numPage, String typeMovies) {
        DataSource dataSource = new DataSource(getApplicationContext());
        if (dataSource.isTableNotEmpty(MovieTable.TABLE_NAME)) {
            Log.d(TAG, "Download from Database");
            downloadFromDbMainPosters();
        } else {
            if (typeMovies.equals(RestClient.TOP_RATED_MOVIES)) {
                RestClient.get().getMoviesByType(numPage, typeMovies,
                        new Callback<QueryResultMovies>() {
                            @Override
                            public void success(QueryResultMovies queryResultMovies, Response response) {
                                List<Movie> movies = queryResultMovies.getResults();
                                fillDataToMovieTable(movies);
                                getMoviesByType(movies);
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

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "Download failed");
                    }
                });
            }
        }
    }


    private void getMoviesByType(List<Movie> movies) {
        if (movies != null) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(GridMovieFragment.MOVIE, (Serializable) movies);
            broadcastIntent.setAction(GridMovieFragment.ACTION_SEND_MOVIE);
            sendBroadcast(broadcastIntent);
            stopSelf();
        }
    }

    private void fillDataToMovieTable(final List<Movie> movies) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Fill Database");
                DataSource mDataSource = new DataSource(getApplicationContext());
                try {
                    for (int i = 0; i < movies.size(); i++) {
                        if (!mDataSource.checkEntry(movies.get(i).getId())) {
                            String pathStorage = saveImageToStorage(getPosterUrl(movies.get(i)),
                                    movies.get(i).getId());
                            movies.get(i).setPosterPathStorage(pathStorage);
                            mDataSource.saveMainMovie(movies.get(i));
                            Log.d(TAG, "  : " + pathStorage);
                        }
                    }
                } catch (SQLException e) {
                    Log.d(TAG, "Fail fill database");
                }
            }
        }).start();
    }

    private String getPosterUrl(Movie movie) {
        return RestClient.BASE_PATH_TO_IMAGE_W342
                + movie.getPosterPath();
    }

    @Nullable
    private String saveImageToStorage(String pathWeb, int movieId) {
        String movieName = String.valueOf(movieId);
        File fileImage = new File(getApplicationContext().getCacheDir(), movieName + JPG);

        if (!fileImage.exists()) {
            try {
                fileImage.createNewFile();
                URL imageUrl = new URL(pathWeb);
                URLConnection connection = imageUrl.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                OutputStream outputStream
                        = new BufferedOutputStream(new FileOutputStream(fileImage));
                createBitmap(inputStream, outputStream);
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

    private Bitmap createBitmap(InputStream is, OutputStream os) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, os);
        return bitmap;
    }
}
