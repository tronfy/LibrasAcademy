package br.unicamp.cotuca.librasacademy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.unicamp.cotuca.librasacademy.dbo.Licao;
import br.unicamp.cotuca.librasacademy.dbo.SubLicao;

public class LicaoActivity extends AppCompatActivity {

    private String server;

    private Licao licao;
    private int sublicaoIndex;
    private ArrayList<SubLicao> sublicoes;

    private TextView tvNome;
    private TextView tvTexto;
    private ImageView ivImagem;
    private Button btnPrev;
    private Button btnNext;

    private SharedPreferences userPrefs;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licao);

        server = getResources().getString(R.string.server);

        userPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = userPrefs.getString("username", "");

        tvNome = findViewById(R.id.name);
        tvTexto = findViewById(R.id.tvTexto);
        ivImagem = findViewById(R.id.ivImagem);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        Bundle extras = getIntent().getExtras();
        licao = (Licao) extras.getSerializable("LICAO");
        sublicaoIndex = 0;

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sublicaoIndex--;
                updateView();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sublicaoIndex++;
                updateView();
            }
        });

        sublicoes = new ArrayList<SubLicao>();

        try {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("licao", licao.getCodigo() + "");
            HttpManager.get(this, server + "/sublicao", params, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        JSONArray res = (JSONArray) result.get("resultado");
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject o = res.getJSONObject(i);
                            String texto = o.getString("texto");
                            int codigo = o.getInt("codigo");
                            String imagem = o.getString("imagem");
                            SubLicao sublicao = new SubLicao(texto, codigo, imagem);
                            sublicoes.add(sublicao);
                        }
                        System.out.println(sublicoes);
                        tvNome.setText(licao.getNome());
                        updateView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(JSONObject result) {
                    System.err.println("ERRO: " + result);
                    //Toast.makeText(getApplicationContext(), result.getString("err"), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void updateView() {
        if (sublicaoIndex < sublicoes.size() && sublicaoIndex >= 0) {
            tvNome.setText((licao.getNome() + " (" + (sublicaoIndex+1) + "/" + sublicoes.size() + ")"));

            String texto = sublicoes.get(sublicaoIndex).getTexto().replaceAll("\\\\n", System.getProperty("line.separator"));
            String file = sublicoes.get(sublicaoIndex).getImagem();
            String imagem = "https://imgur.com/" + file;

            tvTexto.setText(texto);
            if (file != null) {
                Glide.with(LicaoActivity.this)
                        .load(imagem)
                        .into(ivImagem);
            }

            if (sublicaoIndex == sublicoes.size() - 1) {
                btnNext.setText("concluir");
                btnNext.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
            }
            else {
                btnNext.setText("avançar");
                btnNext.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_forward, 0);
            }
        } else {
            try {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("licao", licao.getCodigo() + "");

                HttpManager.post(this, server + "/concluir", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        //LicaoActivity.this.finish();
                    }

                    @Override
                    public void onError(JSONObject result) {
                        Toast.makeText(getApplicationContext(), "Erro ao concluir lição", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                LicaoActivity.this.finish();
            }
        }
    }
}