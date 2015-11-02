package com.pundroid.bestmoviesapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pundroid.bestmoviesapp.objects.Genres;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.objects.MovieDetails;
import com.pundroid.bestmoviesapp.objects.ProductionCompanies;
import com.pundroid.bestmoviesapp.objects.ProductionCountries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumba30 on 23.10.2015.
 */
public class DataSource {
    private static final String TAG = DataSource.class.getSimpleName();
    public static final String ROW_LIMIT = "20";

    public static final String[] MAIN_MOVIE_FIELDS = new String[]{
            "movie_id",
            "poster_path_storage",
            "poster_path_web",
            "movie_title"
    };

    private SQLiteDatabase mDatabase;
    private DbHelper mDbHelper;

    public DataSource(Context context) {
        mDbHelper = DbHelper.getInstance(context);
        mDatabase = open();
    }

    private SQLiteDatabase open() throws SQLException {
        return mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    // creating movie poster for main screen with movie_id
    public long saveMainMovie(Movie movie) {

        ContentValues values = new ContentValues();
        if (movie.getId() != 0) {
            values.put("movie_id", movie.getId());
        }
        if (movie.getPosterPath() != null) {
            values.put("poster_path_web", movie.getPosterPath());
        }
        if (movie.getPosterPathStorage() != null) {
            values.put("poster_path_storage", movie.getPosterPathStorage());
        }
        if (movie.getTitle() != null) {
            values.put("movie_title", movie.getTitle());
        }

        return mDatabase.insert("movies", null, values);
    }

    public long saveMovieDetail(MovieDetails movieDetails) {
        ContentValues values = new ContentValues();
        if (movieDetails.getId() != 0) {
            values.put("movie_id", movieDetails.getId());
        }
        if (movieDetails.getBackdropPath() != null) {
            values.put("backdrop_path_web", movieDetails.getBackdropPath());
        }
        if (movieDetails.getBackdropPathStorage() != null) {
            values.put("backdrop_path_storage", movieDetails.getBackdropPathStorage());
        }
        if (movieDetails.getPosterPath() != null) {
            values.put("poster_path_web", movieDetails.getPosterPath());
        }
        if (movieDetails.getPosterPathStorage() != null) {
            values.put("poster_path_storage", movieDetails.getPosterPathStorage());
        }
        if (movieDetails.getOriginalTitle() != null) {
            values.put("original_title", movieDetails.getOriginalTitle());
        }
        if (movieDetails.getOverview() != null) {
            values.put("overview", movieDetails.getOverview());
        }
        if (movieDetails.getReleaseDate() != null) {
            values.put("release_date", movieDetails.getReleaseDate());
        }
        if (movieDetails.getPopularity() != null) {
            values.put("popularity", movieDetails.getPopularity());
        }
        if (movieDetails.getTitle() != null) {
            values.put("title", movieDetails.getTitle());
        }
        if (movieDetails.getVoteAverage() != null) {
            values.put("vote_average", movieDetails.getVoteAverage());
        }
        if (movieDetails.getVoteCount() != null) {
            values.put("vote_count", movieDetails.getVoteCount());
        }
        if (movieDetails.getBudget() != null) {
            values.put("budget", movieDetails.getBudget());
        }
        if (movieDetails.getRevenue() != null) {
            values.put("revenue", movieDetails.getRevenue());
        }
        if (movieDetails.getRuntime() != null) {
            values.put("runtime", movieDetails.getRuntime());
        }
        if (movieDetails.getHomepage() != null) {
            values.put("homepage", movieDetails.getHomepage());
        }
        long insertIdMovie = mDatabase.insert("details_movie", null, values);
        Log.d(TAG, "Insert movie details");

        return insertIdMovie;
    }


    public long[] saveGenres(MovieDetails details) {
        long[] insertGenresIds = new long[details.getGenres().size()];
        ContentValues values = new ContentValues();
        if (details.getGenres() != null) {
            for (int i = 0; i < details.getGenres().size(); i++) {
                values.put("genre_name", details.getGenres().get(i).getName());
                long insertId = mDatabase.insert("genres", null, values);
                insertGenresIds[i] = insertId;
            }
        }
        Log.d(TAG, "Insert genres");
        return insertGenresIds;
    }

    public void insertGenresMovie(long insertMovieId, long[] insertGenresIds) {
        ContentValues values = new ContentValues();
        for (long insertId : insertGenresIds) {
            values.put("movie_row_id", insertMovieId);
            values.put("genre_row_id", insertId);
            mDatabase.insert("movie_genres", null, values);
        }
    }

    private String queryGenres = "SELECT genre_name FROM movies, genres, movie_genres " +
            "WHERE movies.movie_id LIKE ? " + "AND movie_genres.movie_row_id=" +
            "movies._id AND movie_genres.genre_row_id=genres._id";

    public List<Genres> fetchGenresByMovieId(int movieId, String query) {
        List<Genres> genres = new ArrayList<>();
        String[] args = new String[]{Integer.toString(movieId)};
        Cursor cursor = mDatabase.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Genres newGenres = new Genres();
                newGenres.setName(cursor.getString(cursor.getColumnIndex("genre_name")));
                genres.add(newGenres);
                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }
        }
        return genres;
    }

    public long[] saveProductionCountries(MovieDetails details) {
        long[] insertProdCountriesIds = new long[details.getProductionCountries().size()];
        ContentValues values = new ContentValues();
        if (details.getProductionCountries() != null) {
            for (int i = 0; i < details.getProductionCountries().size(); i++) {
                values.put("country_name", details.getProductionCountries().get(i).getName());
                long insertId = mDatabase.insert("production_countries", null, values);
                insertProdCountriesIds[i] = insertId;
            }
        }
        Log.d(TAG, "Insert Productions Countries");
        return insertProdCountriesIds;
    }

    public void insertProdCountryMovie(long insertMovieId, long[] insertProdCountriesIds) {
        ContentValues values = new ContentValues();
        for (long insertId : insertProdCountriesIds) {
            values.put("movie_row_id", insertMovieId);
            values.put("country_row_id", insertId);
            mDatabase.insert("movie_prod_countries", null, values);
        }
    }

    private String queryProdCountries = "SELECT country_name FROM movies, production_countries, movie_prod_countries " +
            "WHERE movies.movie_id LIKE ? " + "AND movie_prod_countries.movie_row_id=" +
            "movies._id AND movie_prod_countries.country_row_id=production_countries._id";

    public List<ProductionCountries> fetchProdCountriesByMovieId(int movieId, String query) {
        List<ProductionCountries> countriesList = new ArrayList<>();
        String[] args = new String[]{Integer.toString(movieId)};
        Cursor cursor = mDatabase.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProductionCountries newCountry = new ProductionCountries();
                newCountry.setName(cursor.getString(cursor.getColumnIndex("country_name")));
                countriesList.add(newCountry);
                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }
        }
        return countriesList;
    }

    public long[] saveProductionCompanies(MovieDetails details) {
        long[] insertProdCompaniesIds = new long[details.getProductionCompanies().size()];
        ContentValues values = new ContentValues();
        if (details.getProductionCompanies() != null) {
            for (int i = 0; i < details.getProductionCompanies().size(); i++) {
                values.put("company_name", details.getProductionCompanies().get(i).getName());
                long insertId = mDatabase.insert("production_companies", null, values);
                insertProdCompaniesIds[i] = insertId;
            }
        }
        Log.d(TAG, "Insert Productions Companies");
        return insertProdCompaniesIds;
    }

    public void insertProdCompanyMovie(long insertMovieId, long[] insertProdCompaniesIds) {
        ContentValues values = new ContentValues();
        for (long insertId : insertProdCompaniesIds) {
            values.put("movie_row_id", insertMovieId);
            values.put("company_row_id", insertId);
            mDatabase.insert("movie_prod_companies", null, values);
        }
    }

    private String queryProdCompanies = "SELECT company_name FROM movies, production_companies, movie_prod_companies " +
            "WHERE movies.movie_id LIKE ? " + "AND movie_prod_companies.movie_row_id=" +
            "movies._id AND movie_prod_companies.company_row_id=production_companies._id";

    public List<ProductionCompanies> fetchProdCompaniesByMovieId(int movieId, String query) {
        List<ProductionCompanies> companiesList = new ArrayList<>();
        String[] args = new String[]{Integer.toString(movieId)};
        Cursor cursor = mDatabase.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProductionCompanies newCompany = new ProductionCompanies();
                newCompany.setName(cursor.getString(cursor.getColumnIndex("company_name")));
                companiesList.add(newCompany);
                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }
        }
        return companiesList;
    }


    public MovieDetails getMovieDetails(int movieId) {
        MovieDetails details = new MovieDetails();
        Cursor cursor = mDatabase.query("details_movie", null,
                "movie_id" + "=" + movieId, null, null, null, null);
        if (cursor.moveToFirst()) {
            details.setBackdropPath(cursor.getString(cursor.getColumnIndex("backdrop_path_web")));
            details.setBackdropPathStorage(cursor.getString(cursor.getColumnIndex("backdrop_path_storage")));
            details.setPosterPath(cursor.getString(cursor.getColumnIndex("poster_path_web")));
            details.setPosterPathStorage(cursor.getString(cursor.getColumnIndex("poster_path_storage")));
            details.setId(cursor.getInt(cursor.getColumnIndex("movie_id")));
            details.setOriginalTitle(cursor.getString(cursor.getColumnIndex("original_title")));
            details.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
            details.setReleaseDate(cursor.getString(cursor.getColumnIndex("release_date")));
            details.setPopularity(cursor.getFloat(cursor.getColumnIndex("popularity")));
            details.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            details.setVoteAverage(cursor.getFloat(cursor.getColumnIndex("vote_average")));
            details.setVoteCount(cursor.getInt(cursor.getColumnIndex("vote_count")));
            details.setGenres(fetchGenresByMovieId(movieId, queryGenres));
            details.setProductionCountries(fetchProdCountriesByMovieId(movieId, queryProdCountries));
            details.setProductionCompanies(fetchProdCompaniesByMovieId(movieId, queryProdCompanies));
            details.setBudget(cursor.getInt(cursor.getColumnIndex("budget")));
            details.setRevenue(cursor.getInt(cursor.getColumnIndex("revenue")));
            details.setRuntime(cursor.getInt(cursor.getColumnIndex("runtime")));
            details.setHomepage(cursor.getString(cursor.getColumnIndex("homepage")));
        }

        cursor.close();
        return details;
    }

    //check for entries in the table
    public boolean checkEntry(int movieId) {
        String movieIdToStr = String.valueOf(movieId);
        boolean result = false;
        Cursor cursor = mDatabase.query("movies",
                new String[]{"movie_id"},
                "movie_id" + "=" + movieIdToStr, null, null, null, null, null);
        Log.d(TAG, "Count cursor before: " + String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            Log.d(TAG, "Count cursor in: " + String.valueOf(cursor.getCount()));
            result = true;
        }
        cursor.close();
        return result;
    }


    public List<Movie> getAllPostersMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = mDatabase.query(true, "movies", MAIN_MOVIE_FIELDS,
                null, null, null, null, null, ROW_LIMIT);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setPosterPathStorage(cursor.getString(
                        cursor.getColumnIndex("poster_path_storage")));
                movie.setId(cursor.getInt(
                        cursor.getColumnIndex("movie_id")));
                movie.setPosterPath(cursor.getString(
                        cursor.getColumnIndex("poster_path_web")));
                movie.setTitle(cursor.getString(
                        cursor.getColumnIndex("movie_title")));
                movies.add(movie);

                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }
        }

        return movies;
    }


    public boolean isTableNotEmpty(String tableName) {
        if (DatabaseUtils.queryNumEntries(mDatabase, tableName) > 0) {
            return true;
        }
        return false;
    }
}
