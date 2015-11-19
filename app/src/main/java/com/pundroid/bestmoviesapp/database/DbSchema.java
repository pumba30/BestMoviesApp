package com.pundroid.bestmoviesapp.database;

/**
 * Created by pumba30 on 12.10.2015.
 */
public class DbSchema {
    public static final class MovieTable {
        public static final String TABLE_NAME = "movie";

        public static final class Column {
            public static final String MOVIE_ID = "movie_id";
            public static final String POSTER_PATH_STORAGE = "poster_path_storage";
            public static final String POSTER_PATH_WEB = "poster_path_web";
            public static final String MOVIE_TITLE = "movie_title";
        }
    }

    public static final class DetailsMovieTable {
        public static final String TABLE_NAME = "details_movie";

        public static final class Column {
            public static final String MOVIE_ID = "movie_id";
            public static final String BACKDROP_PATH_STORAGE = "backdrop_path_storage";
            public static final String BACKDROP_PATH_WEB = "backdrop_path_web";
            public static final String POSTER_PATH_STORAGE = "poster_path_storage";
            public static final String POSTER_PATH_WEB = "poster_path_web";
            public static final String ORIGINAL_TITLE = "original_title";
            public static final String OVERVIEW = "overview";
            public static final String RELEASE_DATE = "release_date";
            public static final String POPULARITY = "popularity";
            public static final String TITLE = "title";
            public static final String VOTE_AVERAGE = "vote_average";
            public static final String VOTE_COUNT = "vote_count";
            public static final String BUDGET = "budget";
            public static final String REVENUE = "revenue";
            public static final String RUNTIME = "runtime";
            public static final String HOMEPAGE = "homepage";


        }
    }

    public static final class GenreTable {
        public static final String TABLE_NAME = "genre";

        public static final class Column {
            public static final String GENRE_NAME = "genre_name";
        }
    }

    public static final class ProductionCountryTable {
        public static final String TABLE_NAME = "production_country";

        public static final class Column {
            public static final String COUNTRY_NAME = "country_name";
        }
    }

    public static final class ProductionCompanyTable {
        public static final String TABLE_NAME = "production_company";

        public static final class Column {
            public static final String COMPANY_NAME = "company_name";
        }
    }


    public static final class MovieGenreTable {
        public static final String TABLE_NAME = "movie_genre";

        public static final class Column {
            public static final String MOVIE_ROW_ID = "movie_row_id";
            public static final String GENRE_ROW_ID = "genre_row_id";
        }
    }

    public static final class MovieCountryTable {
        public static final String TABLE_NAME = "movie_prod_country";

        public static final class Column {
            public static final String MOVIE_ROW_ID = "movie_row_id";
            public static final String COUNTRY_ROW_ID = "country_row_id";
        }
    }

    public static final class MovieCompanyTable {
        public static final String TABLE_NAME = "movie_prod_company";

        public static final class Column {
            public static final String MOVIE_ROW_ID = "movie_row_id";
            public static final String COMPANY_ROW_ID = "company_row_id";
        }
    }

    public static final class ActorTable {
        public static final String TABLE_NAME = "actor";

        public static final class Column {
            public static final String CHARACTER = "character";
            public static final String NAME = "name";
            public static final String ACTOR_ID = "actor_id";
            public static final String PROFILE_PHOTO_PATH = "photo_path_web";
            public static final String PROFILE_PHOTO_PATH_TO_STORAGE = "photo_path_storage";
        }
    }

    public static final class MovieActorTable {
        public static final String TABLE_NAME = "movie_actor";

        public static final class Column {
            public static final String MOVIE_ID = "movie_id";
            public static final String ACTOR_ID = "actor_id";
        }
    }

    public static final class BiographyActorTable {
        public static final String TABLE_NAME = "biography_actor";

        public static final class Column {
            public static final String ROW_ID = "_id";
            public static final String BIOGRAPHY = "biography";
            public static final String BIRTHDAY = "birthday";
            public static final String DEATHDAY = "deathday";
            public static final String HOMEPAGE = "homepage";
            public static final String ACTOR_ID = "actor_id";
            public static final String NAME = "name";
            public static final String PLACE_BIRTH = "place_of_birth";
            public static final String PROFILE_PHOTO_PATH = "profile_photo_path";
            public static final String PROFILE_PHOTO_PATH_TO_STORAGE = "profile_photo_path_to_storage";

        }
    }

    public static final class CrewTable {
        public static final String TABLE_NAME = "crew";

        public static final class Column {
            public static final String ROW_ID = "_id";
            public static final String NAME = "name";
            public static final String JOB = "job";
            public static final String MEMBER_CREW_ID = "member_crew_id";
            public static final String PROFILE_PHOTO_PATH = "profile_photo_path";
            public static final String PROFILE_PHOTO_PATH_TO_STORAGE = "profile_photo_path_to_storage";
        }
    }


}
