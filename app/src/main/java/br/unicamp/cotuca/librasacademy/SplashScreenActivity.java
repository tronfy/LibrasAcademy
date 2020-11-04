package br.unicamp.cotuca.librasacademy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    private String username, password, prefsUser, prefsPass;

    private SharedPreferences loginPreferences;

    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        server = getResources().getString(R.string.server);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        prefsUser = loginPreferences.getString("username", "");
        prefsPass = loginPreferences.getString("password", "");

        if (!conexaoDisponivel()) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
        }

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tryLogin();
            }
        }, 1500);*/
        tryLogin();
    }

    private void tryLogin() {
        if (prefsUser != "" && prefsPass != "") {
            username = prefsUser;
            password = prefsPass;
            login();
        } else {
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
        }
    }



    private void login() {
        try {
            if (!conexaoDisponivel()) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                return;
            }
            String url = server + "/login";

            try {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                HttpManager.post(this, server + "/login", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                    }

                    @Override
                    public void onError(JSONObject result) {
                        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginActivity);
                    }
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean conexaoDisponivel(){
        Context context = getApplicationContext();
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect != null)
        {
            NetworkInfo information = connect.getActiveNetworkInfo();
            return (information != null && information.getState() == NetworkInfo.State.CONNECTED);
        }
        return false;
    }
}