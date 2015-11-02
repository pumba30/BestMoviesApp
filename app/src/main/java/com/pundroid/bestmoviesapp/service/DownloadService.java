package com.pundroid.bestmoviesapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pundroid.bestmoviesapp.databases.DataSource;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.objects.MovieDetails;
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
    public static final int LAST_SEGMENT = 99;
    private DataSource mDataSource;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDataSource = new DataSource(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.hasExtra(DownloadHelperService.TYPE_MOVIES)) {
            int numPage = intent.getIntExtra(DownloadHelperService.NUM_PAGE, 1);
            String typeMovies = intent.getStringExtra(DownloadHelperService.TYPE_MOVIES);
            boolean isConnected = intent.getBooleanExtra(DownloadHelperService.IS_CONNECTED, true);
            Log.d(TAG, "Intent TYPE_MOVIES");
            if (isConnected) {
                // get movie from internet
                downloadMainMovies(numPage, typeMovies);
            } else {
                downloadFromDbMainPosters();
            }
        }
        //for download details movie
        if (intent != null && intent.hasExtra(DownloadHelperService.MOVIE_ID)) {
            int movieId = intent.getIntExtra(DownloadHelperService.MOVIE_ID, 0);
            boolean isConnected = intent.getBooleanExtra(DownloadHelperService.IS_CONNECTED, true);
            Log.d(TAG, "Intent MOVIE_ID");
            if (isConnected) {
                // get movie from internet
                downloadDetailMovie(movieId);
            } else {
                downloadFromDbDetailMovies(movieId);
                Log.d(TAG, "Load from DB MovieDetails");
            }

        }
    }

    private void downloadDetailMovie(int movieId) {
        RestClient.get().getDetailMovieById(movieId, new Callback<MovieDetails>() {
            @Override
            public void success(MovieDetails movieDetail, Response response) {
                if (movieDetail != null) {
                    fillDataDetailMovieToTable(movieDetail);
                    sendMovieDetails(movieDetail);

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Load movie failed");
            }
        });
    }

    private void fillDataDetailMovieToTable(final MovieDetails details) {
        Log.d(TAG, "Fill Database Table MovieDetail");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //reuse poster image
                    String posterPathStorage = saveImageToStorage(getPosterUrl(details), details.getId() + 77);
                    String backdropPathToStorage = saveImageToStorage(getBackdropUrl(details), details.getId() + 88);
                    details.setPosterPathStorage(posterPathStorage);
                    details.setBackdropPathStorage(backdropPathToStorage);

                    //insert in table details_movie
                    long movieInsertId = mDataSource.saveMovieDetail(details);
                    // // insert in table genres
                    long[] genreInsertId = mDataSource.saveGenres(details);
                    // // insert in table movie_genres
                    mDataSource.insertGenresMovie(movieInsertId, genreInsertId);

                    long[] countryInsertIds = mDataSource.saveProductionCountries(details);
                    mDataSource.insertProdCountryMovie(movieInsertId, countryInsertIds);

                    long[] companiesInsertIds = mDataSource.saveProductionCompanies(details);
                    mDataSource.insertProdCompanyMovie(movieInsertId, companiesInsertIds);


                } catch (SQLException e) {
                    Log.d(TAG, "Fail fill database table Movies");
                }
            }
        }).start();
    }


    private void downloadFromDbDetailMovies(int movieId) {
        Log.d(TAG, "downloadFromDbDetailMovies");
        MovieDetails details = mDataSource.getMovieDetails(movieId);
        sendMovieDetails(details);
    }


    private void sendMovieDetails(MovieDetails details) {
        if (details != null) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(DetailMovieActivityFragment.MOVIE_DETAIL, details);
            broadcastIntent.setAction(DetailMovieActivityFragment.ACTION_SEND_MOVIE_DETAIL);
            sendBroadcast(broadcastIntent);
            stopSelf();
        }
    }

    private void downloadFromDbMainPosters() {
        List<Movie> movies = mDataSource.getAllPostersMovies();
        sendMoviesByType(movies);
    }

    private void downloadMainMovies(int numPage, String typeMovies) {
        mDataSource = new DataSource(getApplicationContext());
        if (mDataSource.isTableNotEmpty("movies")) {
            Log.d(TAG, "Download from Movie Table");
            downloadFromDbMainPosters();
        } else {
            if (typeMovies.equals(RestClient.TOP_RATED_MOVIES)) {
                RestClient.get().getMoviesByType(numPage, typeMovies,
                        new Callback<QueryResultMovies>() {
                            @Override
                            public void success(QueryResultMovies queryResultMovies, Response response) {
                                List<Movie> movies = queryResultMovies.getResults();
                                fillDataToMovieTable(movies);
                                sendMoviesByType(movies);
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


    private void sendMoviesByType(List<Movie> movies) {
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
                Log.d(TAG, "Fill Database Table Movies");
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
                    Log.d(TAG, "Fail fill database table Movies");
                }
            }
        }).start();
    }

    private String getPosterUrl(Movie movie) {
        return RestClient.BASE_PATH_TO_IMAGE_W154
                + movie.getPosterPath();
    }

    private String getPosterUrl(MovieDetails details) {
        return RestClient.BASE_PATH_TO_IMAGE_W154
                + details.getPosterPath();
    }


    private String getBackdropUrl(MovieDetails details) {
        return RestClient.BASE_PATH_TO_IMAGE_W342
                + details.getBackdropPath();
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
                Log.d(TAG, "Save image " + fileImage.getName());
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return bitmap;
    }


}
