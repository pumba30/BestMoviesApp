package com.pundroid.bestmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.objects.Biography;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 27.08.2015.
 */
public class BiographyActorActivity extends ActionBarActivity {

    private static final String TAG = BiographyActorActivity.class.getSimpleName();

    private ScrollView scrollView;
    private int idPerson;

    TextView nameActor;
    TextView birthday;
    TextView deathday;
    TextView placeBirthday;
    TextView biographyAct;
    TextView homepage;
    ImageView actorImage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actor_biography_fragment);

        // scrollView = (ScrollView) findViewById(R.id.scrollView_actor_fragment);
        nameActor = (TextView) findViewById(R.id.tv_name_actor);
        birthday = (TextView) findViewById(R.id.tv_birthday);
        deathday = (TextView) findViewById(R.id.tv_deathday);
        placeBirthday = (TextView) findViewById(R.id.tv_place_birthday);
        biographyAct = (TextView) findViewById(R.id.tv_biography_desc);
        homepage = (TextView) findViewById(R.id.tv_homepage_actor_description);
        actorImage = (ImageView) findViewById(R.id.imageView_poster_actor_w154);

        Intent intent = getIntent();
        idPerson = (int) intent.getLongExtra(CastFragment.ACTOR_ID, 0);
        loadBiographyActor(idPerson);

    }


    private void loadBiographyActor(int idPerson) {
        RestClient.get().getBiographyPerson(idPerson, new Callback<Biography>() {
            @Override
            public void success(Biography biography, Response response) {
                setValuesView(biography);

                if (biography == null) {
                    Toast.makeText(getApplicationContext(),
                            "Biography loading failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Actor's  biography loading failed");
            }
        });
    }

    private void setValuesView(Biography biography) {


        if (biography.getProfilePath() != null) {
            actorImage.getLayoutParams().height = 300;
            actorImage.getLayoutParams().width = 200;
            Picasso.with(getApplicationContext()).load(RestClient.BASE_PATH_TO_IMAGE_W154
                    + biography.getProfilePath()).into(actorImage);
        } else {
            actorImage.setImageResource(android.R.drawable.ic_menu_help);
        }


        if (biography.getName() != null) {
            nameActor.setText(biography.getName());
        }
        if ("".equals(biography.getName())) {
            nameActor.setText(R.string.nothing_not_found);
        }


        if (biography.getBirthday() != null) {
            birthday.setText(biography.getBirthday());
        }
        if ("".equals(biography.getBirthday())) {
            birthday.setText(R.string.nothing_not_found);
        }


        if (biography.getDeathday() != null) {
            deathday.setText(biography.getDeathday());
        }
        if ("".equals(biography.getDeathday())) {
            deathday.setText(R.string.nothing_not_found);
        }


        if (biography.getPlaceOfBirth() != null) {
            placeBirthday.setText(biography.getPlaceOfBirth());
        }
        if ("".equals(biography.getPlaceOfBirth())) {
            homepage.setText(R.string.nothing_not_found);
        }

        if (biography.getBiography() != null) {
            biographyAct.setText(biography.getBiography());
        }
        if ("".equals(biography.getBiography())) {
            biographyAct.setText(R.string.nothing_not_found);
        }


        if (biography.getHomepage() != null) {
            homepage.setText(biography.getHomepage());
        }
        if ("".equals(biography.getHomepage())) {
            homepage.setText(R.string.nothing_not_found);
        }

    }

}
