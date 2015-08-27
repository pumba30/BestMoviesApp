package com.pundroid.bestmoviesapp.apiquery;

import com.pundroid.bestmoviesapp.objects.Credits;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by pumba30 on 27.08.2015.
 */
public interface API {
    public static final String API_KEY = "?api_key=d1a2f8dc42f6388052172df57a6aba41";

    // http://api.themoviedb.org/3/movie/135397/credits?api_key=d1a2f8dc42f6388052172df57a6aba41
    @GET("/movie/{idMovie}/credits" + API_KEY)
    void getActorOfMovie(@Path("idMovie") int idMovie,
                         Callback<Credits> responce);


}
