package br.unicamp.cotuca.librasacademy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    private String username, password;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        server = getResources().getString(R.string.server);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        String prefsUser = loginPreferences.getString("username", "");
        String prefsPass = loginPreferences.getString("password", "");

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
}