package com.pundroid.bestmoviesapp.databases;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbSchema {
    public static final class MovieTable {
        public static final String TABLE_NAME = "movies";

        public static final class Column {
            public static final String ROW_ID = "_id";
            public static final String MOVIE_ID = "movie_id";
            public static final String BACKDROP_PATH_STORAGE = "backdrop_path_storage";
            public static final String BACKDROP_PATH_WEB = "backdrop_path_web";
            public static final String POSTER_PATH_STORAGE = "poster_path_storage";
            public static final String POSTER_PATH_WEB = "poster_path_web";
            public static final String ORIGINAL_TITLE = "original_title";
            public static final String OVERVIEW = "overview";
            public static final String RELEASE_DATE = "release_data";
            public static final String POPULARITY = "popularity";
            public static final String TITLE = "title";
            public static final String VOTE_AVERAGE = "vote_average";
            public static final String VOTE_COUNT = "vote_count";
            public static final String GENRES = "genre_id";
        }
    }
}
