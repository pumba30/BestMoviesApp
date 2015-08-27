package com.pundroid.bestmoviesapp.utils;

import com.pundroid.bestmoviesapp.apiquery.API;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by pumba30 on 27.08.2015.
 */

//Create RestAdapter Retrofit
//Use RestClient for each request
public class RestClient {

    private static API REST_CLIENT;
    public static final String API_ROOT = "http://api.themoviedb.org/3";
    public static final String BASE_PATH_TO_IMAGE_W92 = "http://image.tmdb.org/t/p/w92";

    private RestClient() {
    }

    static {
        setupRestClient();
    }

    public static API get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(API_ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(API.class);

    }


}
