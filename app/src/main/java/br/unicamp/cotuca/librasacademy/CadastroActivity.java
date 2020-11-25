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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextFullName, editTextPassword, editTextPassConf;
    private Button buttonCadastro;
    private TextView btnTemConta;

    private String username, fullname, password, passConf;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        server = getResources().getString(R.string.server);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassConf = findViewById(R.id.editTextPasswordConfirm);
        buttonCadastro = findViewById(R.id.buttonCadastrar);
        btnTemConta = findViewById(R.id.tvTemConta);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = editTextUsername.getText().toString().trim();
                fullname = editTextFullName.getText().toString().trim();
                password = editTextPassword.getText().toString();
                passConf = editTextPassConf.getText().toString();

                if (username == "")
                    Toast.makeText(CadastroActivity.this, "Nome de usuário inválido", Toast.LENGTH_SHORT).show();
                else if (fullname == "")
                    Toast.makeText(CadastroActivity.this, "Nome completo inválido", Toast.LENGTH_SHORT).show();
                else if (!password.equals(passConf))
                    Toast.makeText(CadastroActivity.this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
                else cadastrar();
            }
        });

        btnTemConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
            }
        });
    }

    private void cadastrar() {
        try {
            if (!conexaoDisponivel()) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("fullname", fullname);
                params.put("password", password);

                HttpManager.post(this, server + "/cadastro", params, new VolleyCallback() {
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