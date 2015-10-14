package com.pundroid.bestmoviesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pundroid.bestmoviesapp.R;
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


    private TextView namNameActoreActor;
    private TextView mBirthday;
    private TextView mDeathday;
    private TextView mPlaceBirthday;
    private TextView mBiographyAct;
    private TextView mHomepage;
    private ImageView mActorimage;
    private AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actor_biography_fragment);

        // Google Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("adMob").build();
        mAdView.loadAd(adRequest);
        //********

        namNameActoreActor = (TextView) findViewById(R.id.tv_name_actor);
        mBirthday = (TextView) findViewById(R.id.tv_birthday);
        mDeathday = (TextView) findViewById(R.id.tv_deathday);
        mPlaceBirthday = (TextView) findViewById(R.id.tv_place_birthday);
        mBiographyAct = (TextView) findViewById(R.id.tv_biography_desc);
        mHomepage = (TextView) findViewById(R.id.tv_homepage_actor_description);
        mActorimage = (ImageView) findViewById(R.id.imageView_poster_actor_w154);

        Intent intent = getIntent();
        int idPerson = (int) intent.getLongExtra(CastFragment.ACTOR_ID, 0);
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
            mActorimage.getLayoutParams().height = 300;
            mActorimage.getLayoutParams().width = 200;
            Picasso.with(getApplicationContext()).load(RestClient.BASE_PATH_TO_IMAGE_W154
                    + biography.getProfilePath()).into(mActorimage);
        } else {
            mActorimage.setImageResource(android.R.drawable.ic_menu_help);
        }


        if (biography.getName() != null) {
            namNameActoreActor.setText(biography.getName());
        }
        if ("".equals(biography.getName())) {
            namNameActoreActor.setText(R.string.nothing_not_found);
        }


        if (biography.getBirthday() != null) {
            mBirthday.setText(biography.getBirthday());
        }
        if ("".equals(biography.getBirthday())) {
            mBirthday.setText(R.string.nothing_not_found);
        }


        if (biography.getDeathday() != null) {
            mDeathday.setText(biography.getDeathday());
        }
        if ("".equals(biography.getDeathday())) {
            mDeathday.setText(R.string.nothing_not_found);
        }


        if (biography.getPlaceOfBirth() != null) {
            mPlaceBirthday.setText(biography.getPlaceOfBirth());
        }
        if ("".equals(biography.getPlaceOfBirth())) {
            mHomepage.setText(R.string.nothing_not_found);
        }

        if (biography.getBiography() != null) {
            mBiographyAct.setText(biography.getBiography());
        }
        if ("".equals(biography.getBiography())) {
            mBiographyAct.setText(R.string.nothing_not_found);
        }


        if (biography.getHomepage() != null) {
            mHomepage.setText(biography.getHomepage());
        }
        if ("".equals(biography.getHomepage())) {
            mHomepage.setText(R.string.nothing_not_found);
        }

    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
