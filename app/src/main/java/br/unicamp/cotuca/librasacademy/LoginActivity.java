package br.unicamp.cotuca.librasacademy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;

    private String username, password;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        server = getResources().getString(R.string.server);

        editTextUsername = findViewById(R.id.editTexUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        try {
            Bundle extras = getIntent().getExtras();
            boolean clear = extras.getBoolean("CLEAR");

            if (clear) {
                loginPrefsEditor = loginPreferences.edit();
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();
            }
        } catch (Exception e) {}

        String prefsUser = loginPreferences.getString("username", "");
        String prefsPass = loginPreferences.getString("password", "");

        if (prefsUser != "" && prefsPass != "") {
            System.out.println("Using prefs");
            username = prefsUser;
            password = prefsPass;

            editTextUsername.setText(username);
            editTextPassword.setText(password);

            login();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                login();
            }
        });
    }

    private void login() {
        try {
            if (!conexaoDisponivel()) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
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
                       //Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_SHORT).show();
                       loginPrefsEditor = loginPreferences.edit();
                       loginPrefsEditor.putString("username", username);
                       loginPrefsEditor.putString("password", password);
                       loginPrefsEditor.apply();

                       Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(mainActivity);
                   }

                   @Override
                   public void onError(JSONObject result) {
                       try {
                           Toast.makeText(getApplicationContext(), result.getString("err"), Toast.LENGTH_SHORT).show();
                       } catch (JSONException e) {

                       }

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