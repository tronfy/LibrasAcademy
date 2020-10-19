package br.unicamp.cotuca.librasacademy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

        editTextUsername = (EditText)findViewById(R.id.editTexUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        String prefsUser = loginPreferences.getString("username", "");
        String prefsPass = loginPreferences.getString("password", "");

        if (prefsUser != "" && prefsPass != "") {
            System.out.println("Using prefs");
            username = prefsUser;
            password = prefsPass;

            editTextUsername.setText(username);

            login();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);*/

                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                login();
            }
        });
    }

    private void login() {
        //HttpManager.post(server + "/login", "test");
        try {
            String url = server + "/login";

            try {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

               HttpManager.post(this, server + "/login", params, new VolleyCallback() {
                   @Override
                   public void onSuccess(JSONObject result) {
                       System.out.println("SUCCESS: " + result);
                       Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_SHORT).show();
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

        /*startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();*/
    }
}