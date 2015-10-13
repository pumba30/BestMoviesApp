package com.pundroid.bestmoviesapp.databases;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbSchema {
    public static final class MoviePosterTable {
        public static final String TABLE_NAME = "movie_poster";

        public static final class Columns {
            public static final String COLUMN_ID = "_id";
            public static final String MOVIE_ID = "movie_id";
            public static final String PATH_TO_POSTER = "path_to_poster";
        }
    }


}
