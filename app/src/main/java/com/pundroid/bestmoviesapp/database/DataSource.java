package com.pundroid.bestmoviesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pundroid.bestmoviesapp.database.DbSchema.DetailsMovieTable;
import com.pundroid.bestmoviesapp.database.DbSchema.GenreTable;
import com.pundroid.bestmoviesapp.database.DbSchema.MovieTable;
import com.pundroid.bestmoviesapp.database.DbSchema.ProductionCountryTable;
import com.pundroid.bestmoviesapp.objects.Actor;
import com.pundroid.bestmoviesapp.objects.Genres;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.objects.MovieDetails;
import com.pundroid.bestmoviesapp.objects.ProductionCompanies;
import com.pundroid.bestmoviesapp.objects.ProductionCountries;

import java.util.ArrayList;
import java.util.List;

import static com.pundroid.bestmoviesapp.database.DbSchema.*;

/**
 * Created by pumba30 on 23.10.2015.
 */
public class DataSource {
    private static final String TAG = DataSource.class.getSimpleName();
    public static final String ROW_LIMIT = "20";

    public static final String[] MAIN_MOVIE_FIELDS = new String[]{
            MovieTable.Column.MOVIE_ID,
            MovieTable.Column.POSTER_PATH_STORAGE,
            MovieTable.Column.POSTER_PATH_WEB,
            MovieTable.Column.MOVIE_TITLE
    };
    public static final String MOVIE_ID = "movie_id";

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
            values.put(MovieTable.Column.MOVIE_ID, movie.getId());
        }
        if (movie.getPosterPath() != null) {
            values.put(MovieTable.Column.POSTER_PATH_WEB, movie.getPosterPath());
        }
        if (movie.getPosterPathStorage() != null) {
            values.put(MovieTable.Column.POSTER_PATH_STORAGE, movie.getPosterPathStorage());
        }
        if (movie.getTitle() != null) {
            values.put(MovieTable.Column.MOVIE_TITLE, movie.getTitle());
        }

        return mDatabase.insert(MovieTable.TABLE_NAME, null, values);
    }

    public long saveMovieDetail(MovieDetails movieDetails) {
        long insertIdMovie = 0;
        if (!checkEntry(DetailsMovieTable.TABLE_NAME, DetailsMovieTable.Column.MOVIE_ID,
                movieDetails.getId())) {
            ContentValues values = new ContentValues();
            if (movieDetails.getId() != 0) {
                values.put(DetailsMovieTable.Column.MOVIE_ID, movieDetails.getId());
            }
            if (movieDetails.getBackdropPath() != null) {
                values.put(DetailsMovieTable.Column.BACKDROP_PATH_WEB, movieDetails.getBackdropPath());
            }
            if (movieDetails.getBackdropPathStorage() != null) {
                values.put(DetailsMovieTable.Column.BACKDROP_PATH_STORAGE, movieDetails.getBackdropPathStorage());
            }
            if (movieDetails.getPosterPath() != null) {
                values.put(DetailsMovieTable.Column.POSTER_PATH_WEB, movieDetails.getPosterPath());
            }
            if (movieDetails.getPosterPathStorage() != null) {
                values.put(DetailsMovieTable.Column.POSTER_PATH_STORAGE, movieDetails.getPosterPathStorage());
            }
            if (movieDetails.getOriginalTitle() != null) {
                values.put(DetailsMovieTable.Column.ORIGINAL_TITLE, movieDetails.getOriginalTitle());
            }
            if (movieDetails.getOverview() != null) {
                values.put(DetailsMovieTable.Column.OVERVIEW, movieDetails.getOverview());
            }
            if (movieDetails.getReleaseDate() != null) {
                values.put(DetailsMovieTable.Column.RELEASE_DATE, movieDetails.getReleaseDate());
            }
            if (movieDetails.getPopularity() != null) {
                values.put(DetailsMovieTable.Column.POPULARITY, movieDetails.getPopularity());
            }
            if (movieDetails.getTitle() != null) {
                values.put(DetailsMovieTable.Column.TITLE, movieDetails.getTitle());
            }
            if (movieDetails.getVoteAverage() != null) {
                values.put(DetailsMovieTable.Column.VOTE_AVERAGE, movieDetails.getVoteAverage());
            }
            if (movieDetails.getVoteCount() != null) {
                values.put(DetailsMovieTable.Column.VOTE_COUNT, movieDetails.getVoteCount());
            }
            if (movieDetails.getBudget() != null) {
                values.put(DetailsMovieTable.Column.BUDGET, movieDetails.getBudget());
            }
            if (movieDetails.getRevenue() != null) {
                values.put(DetailsMovieTable.Column.REVENUE, movieDetails.getRevenue());
            }
            if (movieDetails.getRuntime() != null) {
                values.put(DetailsMovieTable.Column.RUNTIME, movieDetails.getRuntime());
            }
            if (movieDetails.getHomepage() != null) {
                values.put(DetailsMovieTable.Column.HOMEPAGE, movieDetails.getHomepage());
            }
            insertIdMovie = mDatabase.insert(DetailsMovieTable.TABLE_NAME, null, values);
            Log.d(TAG, "Insert movie details");

        }
        return insertIdMovie;
    }


    public long[] saveGenres(MovieDetails details) {
        long[] insertGenresIds = new long[details.getGenres().size()];
        ContentValues values = new ContentValues();
        if (details.getGenres() != null) {
            for (int i = 0; i < details.getGenres().size(); i++) {
                values.put(GenreTable.Column.GENRE_NAME, details.getGenres().get(i).getName());
                long insertId = mDatabase.insert(GenreTable.TABLE_NAME, null, values);
                insertGenresIds[i] = insertId;
            }
        }
        Log.d(TAG, "Insert genres");
        return insertGenresIds;
    }

    public void insertGenresMovie(long insertMovieId, long[] insertGenresIds) {
        ContentValues values = new ContentValues();
        for (long insertId : insertGenresIds) {
            values.put(MovieGenreTable.Column.MOVIE_ROW_ID, insertMovieId);
            values.put(MovieGenreTable.Column.GENRE_ROW_ID, insertId);
            mDatabase.insert(MovieGenreTable.TABLE_NAME, null, values);
        }
    }


    private List<Genres> fetchGenresByMovieId(int movieId) {
        List<Genres> genres = new ArrayList<>();
        String query = "SELECT genre_name FROM movie, genre, movie_genre " +
                "WHERE movie.movie_id=? AND movie_genre.movie_row_id=" +
                "movie.movie_id AND movie_genre.genre_row_id=genre._id";
        String[] args = new String[]{Integer.toString(movieId)};
        Cursor cursor = mDatabase.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Genres newGenres = new Genres();
                newGenres.setName(cursor.getString(cursor.getColumnIndex(GenreTable.Column.GENRE_NAME)));
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
                values.put(ProductionCountryTable.Column.COUNTRY_NAME, details.getProductionCountries().get(i).getName());
                long insertId = mDatabase.insert(ProductionCountryTable.TABLE_NAME, null, values);
                insertProdCountriesIds[i] = insertId;
            }
        }
        Log.d(TAG, "Insert Productions Countries");
        return insertProdCountriesIds;
    }

    public void insertProdCountryMovie(long insertMovieId, long[] insertProdCountriesIds) {
        ContentValues values = new ContentValues();
        for (long insertId : insertProdCountriesIds) {
            values.put(MovieCountryTable.Column.MOVIE_ROW_ID, insertMovieId);
            values.put(MovieCountryTable.Column.COUNTRY_ROW_ID, insertId);
            mDatabase.insert(MovieCountryTable.TABLE_NAME, null, values);
        }
    }


    private List<ProductionCountries> fetchProdCountriesByMovieId(int movieId) {
        List<ProductionCountries> countriesList = new ArrayList<>();
        String query = "SELECT country_name FROM movie, production_country, movie_prod_country " +
                "WHERE movie.movie_id=? AND movie_prod_country.movie_row_id=" +
                "movie.movie_id AND movie_prod_country.country_row_id=production_country._id";
        String[] args = new String[]{Integer.toString(movieId)};
        Cursor cursor = mDatabase.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProductionCountries newCountry = new ProductionCountries();
                newCountry.setName(cursor.getString
                        (cursor.getColumnIndex(ProductionCountryTable.Column.COUNTRY_NAME)));
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
                values.put(ProductionCompanyTable.Column.COMPANY_NAME,
                        details.getProductionCompanies().get(i).getName());
                long insertId = mDatabase.insert(ProductionCompanyTable.TABLE_NAME, null, values);
                insertProdCompaniesIds[i] = insertId;
            }
        }
        Log.d(TAG, "Insert Productions Companies");
        return insertProdCompaniesIds;
    }

    public void insertProdCompanyMovie(long insertMovieId, long[] insertProdCompaniesIds) {
        ContentValues values = new ContentValues();
        for (long insertId : insertProdCompaniesIds) {
            values.put(MovieCompanyTable.Column.MOVIE_ROW_ID, insertMovieId);
            values.put(MovieCompanyTable.Column.COMPANY_ROW_ID, insertId);
            mDatabase.insert(MovieCompanyTable.TABLE_NAME, null, values);
        }
    }


    private List<ProductionCompanies> fetchProdCompaniesByMovieId(int movieId) {
        List<ProductionCompanies> companiesList = new ArrayList<>();
        String query = "SELECT company_name FROM movie, production_company, movie_prod_company " +
                "WHERE movie.movie_id=? AND movie_prod_company.movie_row_id=" +
                "movie.movie_id AND movie_prod_company.company_row_id=production_company._id";
        String[] args = new String[]{Integer.toString(movieId)};
        Cursor cursor = mDatabase.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProductionCompanies newCompany = new ProductionCompanies();
                newCompany.setName(cursor.getString
                        (cursor.getColumnIndex(ProductionCompanyTable.Column.COMPANY_NAME)));
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
        Cursor cursor = mDatabase.query(DetailsMovieTable.TABLE_NAME, null,
                DetailsMovieTable.Column.MOVIE_ID + "=" + movieId, null, null, null, null);
        if (cursor.moveToFirst()) {
            details.setBackdropPath(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.BACKDROP_PATH_WEB)));
            details.setBackdropPathStorage(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.BACKDROP_PATH_STORAGE)));
            details.setPosterPath(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.POSTER_PATH_WEB)));
            details.setPosterPathStorage(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.POSTER_PATH_STORAGE)));
            details.setId(cursor.getInt
                    (cursor.getColumnIndex(DetailsMovieTable.Column.MOVIE_ID)));
            details.setOriginalTitle(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.ORIGINAL_TITLE)));
            details.setOverview(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.OVERVIEW)));
            details.setReleaseDate(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.RELEASE_DATE)));
            details.setPopularity(cursor.getFloat
                    (cursor.getColumnIndex(DetailsMovieTable.Column.POPULARITY)));
            details.setTitle(cursor.getString
                    (cursor.getColumnIndex(DetailsMovieTable.Column.TITLE)));
            details.setVoteAverage(cursor.getFloat
                    (cursor.getColumnIndex(DetailsMovieTable.Column.VOTE_AVERAGE)));
            details.setVoteCount(cursor.getInt
                    (cursor.getColumnIndex(DetailsMovieTable.Column.VOTE_COUNT)));
            details.setGenres(fetchGenresByMovieId(movieId));
            details.setProductionCountries(fetchProdCountriesByMovieId(movieId));
            details.setProductionCompanies(fetchProdCompaniesByMovieId(movieId));
            details.setBudget(cursor.getInt
                    (cursor.getColumnIndex(DetailsMovieTable.Column.BUDGET)));
            details.setRevenue(cursor.getInt
                    (cursor.getColumnIndex(DetailsMovieTable.Column.REVENUE)));
            details.setRuntime(cursor.getInt
                    (cursor.getColumnIndex(DetailsMovieTable.Column.RUNTIME)));
            details.setHomepage(cursor.getString(cursor.getColumnIndex(DetailsMovieTable.Column.HOMEPAGE)));
        }

        cursor.close();
        return details;
    }

    //check for entries in the table
    public boolean checkEntry(String tableName, String columnName, int objectId) {
        boolean result = false;
        Cursor cursor = mDatabase.query(tableName,
                new String[]{columnName},
                columnName + "=" + objectId, null, null, null, null, null);
        Log.d(TAG, "Count cursor before: " + String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            Log.d(TAG, "Count cursor in: " + String.valueOf(cursor.getCount()));
            result = true;
        }
        cursor.close();
        return result;
    }

//    public boolean checkEntry(String tableName, String columnName, String objectName) {
//        boolean result = false;
//        Cursor cursor = mDatabase.query(tableName, new String[]{columnName},
//                columnName + " LIKE " + objectName, null, null, null, null, null);
//        if (cursor.getCount() > 0) {
//            result = true;
//        }
//        cursor.close();
//        return result;
//    }


    public List<Movie> getAllPostersMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = mDatabase.query(MovieTable.TABLE_NAME, MAIN_MOVIE_FIELDS,
                null, null, null, null, null, ROW_LIMIT);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setPosterPathStorage(cursor.getString(
                        cursor.getColumnIndex(MovieTable.Column.POSTER_PATH_STORAGE)));
                movie.setId(cursor.getInt(
                        cursor.getColumnIndex(MovieTable.Column.MOVIE_ID)));
                movie.setPosterPath(cursor.getString(
                        cursor.getColumnIndex(MovieTable.Column.POSTER_PATH_WEB)));
                movie.setTitle(cursor.getString(
                        cursor.getColumnIndex(MovieTable.Column.MOVIE_TITLE)));
                movies.add(movie);

                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }
        }

        return movies;
    }

    public long saveActor(Actor actor) {
        ContentValues values = new ContentValues();
        if (actor.getId() != 0) {
            values.put(ActorTable.Column.ACTOR_ID, actor.getId());
        }
        if (actor.getName() != null) {
            values.put(ActorTable.Column.NAME, actor.getName());
        }
        if (actor.getCharacter() != null) {
            values.put(ActorTable.Column.CHARACTER, actor.getCharacter());
        }
        if (actor.getProfilePathWeb() != null) {
            values.put(ActorTable.Column.PROFILE_PHOTO_PATH, actor.getProfilePathWeb());
        }
        if (actor.getProfilePathToStorage() != null) {
            values.put(ActorTable.Column.PROFILE_PHOTO_PATH_TO_STORAGE, actor.getProfilePathToStorage());
        }
        long insertId = mDatabase.insert(ActorTable.TABLE_NAME, null, values);

        return insertId;
    }

    public void insertMovieActor(long insertIdMovie, long[] insertActorsId) {
        ContentValues values = new ContentValues();
        for (long insertIdActor : insertActorsId) {
            values.put(MovieActorTable.Column.MOVIE_ID, insertIdMovie);
            values.put(MovieActorTable.Column.ACTOR_ID, insertIdActor);
            mDatabase.insert(MovieActorTable.TABLE_NAME, null, values);
        }
    }


    public List<Actor> fetchFromActorTable(int movieId) {
        List<Actor> actors = new ArrayList<>();
        String query = "SELECT character, name, photo_path_web, photo_path_storage FROM details_movie, actor, movie_actor " +
                "WHERE details_movie.movie_id=? AND movie_actor.movie_id=details_movie.movie_id AND " +
                "movie_actor.actor_id=actor.actor_id";
        String[] args = new String[]{Integer.toString(movieId)};
        Cursor cursor = mDatabase.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Actor newActor = new Actor();
                newActor.setName(cursor.getString(cursor.getColumnIndex(ActorTable.Column.NAME)));
                newActor.setCharacter(cursor.getString
                        (cursor.getColumnIndex(ActorTable.Column.CHARACTER)));
                newActor.setProfilePathToStorage(cursor.getString
                        (cursor.getColumnIndex(ActorTable.Column.PROFILE_PHOTO_PATH_TO_STORAGE)));
                newActor.setProfilePath(cursor.getString
                        (cursor.getColumnIndex(ActorTable.Column.PROFILE_PHOTO_PATH)));
                actors.add(newActor);
            }
            if (cursor.isAfterLast()) {
                cursor.close();
            }
        }
        return actors;
    }


    public boolean isTableNotEmpty(String tableName) {
        return DatabaseUtils.queryNumEntries(mDatabase, tableName) > 0;
    }
}
