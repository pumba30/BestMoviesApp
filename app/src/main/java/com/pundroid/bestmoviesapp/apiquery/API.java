package com.pundroid.bestmoviesapp.apiquery;

import com.pundroid.bestmoviesapp.objects.AccountUser;
import com.pundroid.bestmoviesapp.objects.Biography;
import com.pundroid.bestmoviesapp.objects.Credits;
import com.pundroid.bestmoviesapp.objects.GuestSession;
import com.pundroid.bestmoviesapp.objects.MovieDetail;
import com.pundroid.bestmoviesapp.objects.QueryResultMovies;
import com.pundroid.bestmoviesapp.objects.Session;
import com.pundroid.bestmoviesapp.objects.SimilarMovie;
import com.pundroid.bestmoviesapp.objects.Token;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by pumba30 on 27.08.2015.
 */
public interface API {
    public static final String API_KEY = "?api_key=d1a2f8dc42f6388052172df57a6aba41";

    // http://api.themoviedb.org/3/movie/135397/credits?api_key=d1a2f8dc42f6388052172df57a6aba41
    @GET("/movie/{idMovie}/credits" + API_KEY)
    void getCreditsOfMovie(@Path("idMovie") int idMovie,
                           Callback<Credits> response);


    // http://api.themoviedb.org/3/person/20750?api_key=d1a2f8dc42f6388052172df57a6aba41
    @GET("/person/{idPerson}" + API_KEY)
    void getBiographyPerson(@Path("idPerson") int idPerson,
                            Callback<Biography> response);

    // /movie/{id}/similar
    // http://api.themoviedb.org/3/movie/135397/similar?api_key=d1a2f8dc42f6388052172df57a6aba41
    @GET("/movie/{idMovie}/similar" + API_KEY)
    void getSimilarMovie(@Path("idMovie") int idMovie,
                         Callback<SimilarMovie> response);

    // http://api.themoviedb.org/3/discover/movie?api_key=d1a2f8dc42f6388052172df57a6aba41&page=1&query=top_rated
    @GET("/discover/movie" + API_KEY)
    void getMoviesByType(@Query("page") int page,
                         @Query("query") String typeMovies,
                         Callback<QueryResultMovies> response);

    // get movie by id
    // http://api.themoviedb.org/3/movie/13539?api_key=d1a2f8dc42f6388052172df57a6aba41
    @GET("/movie/{idMovie}" + API_KEY)
    void getDetailMovieById(@Path("idMovie") int idMovie,
                            Callback<MovieDetail> response);

    // /movie/popular
    // http://api.themoviedb.org/3/movie/popular?api_key=d1a2f8dc42f6388052172df57a6aba41
    @GET("/movie/top_rated" + API_KEY)
    void getPopularMovies(@Query("page") int page,
                          Callback<QueryResultMovies> response);

    // http://api.themoviedb.org/3/movie/upcoming?api_key=d1a2f8dc42f6388052172df57a6aba41
    @GET("/movie/upcoming" + API_KEY)
    void getUpcomingMovies(@Query("page") int page,
                           Callback<QueryResultMovies> response);

    //token
    @GET("/authentication/token/new" + API_KEY)
    void getToken(Callback<Token> callback);

    //log in
    @GET("/authentication/token/validate_with_login" + API_KEY)
    void getAuthentication(@Query("request_token") String request_token,
                           @Query("username") String username,
                           @Query("password") String password,
                           Callback<Token> callback);

    //session
    @GET("/authentication/session/new" + API_KEY)
    void getSession(@Query("request_token") String request_token,
                    Callback<Session> callback);

    //account
    @GET("/account" + API_KEY)
    void getAccount(@Query("session_id") String session,
                    Callback<AccountUser> callback);

    @GET("/authentication/guest_session/new" + API_KEY)
    void getGuestSession(Callback<GuestSession> callback);

    // http://api.themoviedb.org/3/search/movie?api_key=d1a2f8dc42f6388052172df57a6aba41&query=MOVIE_NAME
    @GET("/search/movie" + API_KEY)
    void searchMovies(@Query("query") String title,
                      Callback<QueryResultMovies> callback);
}
