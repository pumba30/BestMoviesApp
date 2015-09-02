package com.pundroid.bestmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.objects.AccountUser;
import com.pundroid.bestmoviesapp.objects.Session;
import com.pundroid.bestmoviesapp.objects.Token;
import com.pundroid.bestmoviesapp.utils.PrefUtils;
import com.pundroid.bestmoviesapp.utils.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    private String loginStr;
    private String passStr;
    private EditText etLogin;
    private EditText etPass;
    private String sessionId;

    PrefUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //utils class which stores user data (login , session , name)
        utils = PrefUtils.getInstance(getApplicationContext());

        Button btnRegister = (Button) findViewById(R.id.btnSignIn);
        Button btnLogin = (Button) findViewById(R.id.btnSignUp);
        etLogin = (EditText) findViewById(R.id.editTextLogin);
        etPass = (EditText) findViewById(R.id.editTextPassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.themoviedb.org/account/signup";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginStr = etLogin.getText().toString();
                passStr = etPass.getText().toString();
                //start a login
                getLogin();
            }
        });

    }

    // get token for  request a session id
    private void getLogin() {
        RestClient.get().getToken(new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                // for authentification receive requested token
                getAuthentication(token.getRequest_token());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Failed to get token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //authentication
    private void getAuthentication(String token) {
        RestClient.get().getAuthentication(token, loginStr, passStr, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                getSession(token.getRequest_token());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Enter correct data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // get session
    private void getSession(String token) {
        RestClient.get().getSession(token, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                sessionId = session.getSession_id();
                getAccount();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Session failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //get account with store  data of user
    private void getAccount() {
        RestClient.get().getAccount(sessionId, new Callback<AccountUser>() {
            @Override
            public void success(AccountUser accountUser, Response response) {
                utils.storeSessionUser(accountUser.getId(), accountUser.getUsername(), sessionId);

                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Account failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
