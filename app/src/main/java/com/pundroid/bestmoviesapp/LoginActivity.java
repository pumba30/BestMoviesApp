package com.pundroid.bestmoviesapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.objects.AccountUser;
import com.pundroid.bestmoviesapp.objects.GuestSession;
import com.pundroid.bestmoviesapp.objects.Session;
import com.pundroid.bestmoviesapp.objects.Token;
import com.pundroid.bestmoviesapp.utils.PrefUtils;
import com.pundroid.bestmoviesapp.utils.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private PrefUtils utils;
    private EditText editTextPass;
    private EditText editTextLogin;
    private Button btnSignIn;
    private Button btnSignUp;
    private String login;
    private String password;
    private String idSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utils = PrefUtils.getInstance(getApplicationContext());

        editTextPass = (EditText) findViewById(R.id.editTextPassword);
        int colorLinePass = getResources().getColor(R.color.dark_green);
        editTextPass.getBackground().setColorFilter(colorLinePass, PorterDuff.Mode.SRC_ATOP);


        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        int colorLineLogin;
        colorLineLogin = getResources().getColor(R.color.dark_green);
        editTextLogin.getBackground().setColorFilter(colorLineLogin, PorterDuff.Mode.SRC_ATOP);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // checkSession();
    }

    private void checkSession() {

        if (utils.isUserLogin()) {
            //if session user exist, start MainActivity
            if (utils.getSessionID() != null) {
                utils.setGuest(false);
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        } else {
            // first launch of the program
            if (utils.getGuestSessionID() == null) {
                getGuestLogin();
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                // guest session already exist
                utils.setGuest(true);
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }
    }

    private void getGuestLogin() {
        RestClient.get().getGuestSession(new Callback<GuestSession>() {
            @Override
            public void success(GuestSession guestSession, Response response) {
                utils.storeGuestSessionUser(guestSession.getGuest_session_id());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Guest Fail");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSignUp:
                login =  editTextLogin.getText().toString();
                password = editTextPass.getText().toString();
                getLogin();
                break;

            case R.id.btnSignIn:
                String url = "https://www.themoviedb.org/account/signup";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }

    private void getLogin() {
        RestClient.get().getToken(new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                userAuthentication(token.getRequest_token());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Failed to get token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userAuthentication(String token) {
        RestClient.get().getAuthentication(token, login, password, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                getSession(token.getRequest_token());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Please input correct data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getSession(String token) {
        RestClient.get().getSession(token, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                idSession = session.getSession_id();
                getAccount();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Session FAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAccount() {
        RestClient.get().getAccount(idSession, new Callback<AccountUser>() {


            @Override
            public void success(AccountUser account, Response response) {
                utils.storeSessionUser(account.getId(), account.getUsername(), idSession);

                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "AccountUser failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        utils.setGuest(true);
//        utils.setUserLogin(false);
    }

}
