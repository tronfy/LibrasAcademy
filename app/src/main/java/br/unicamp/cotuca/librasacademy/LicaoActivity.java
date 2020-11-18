package br.unicamp.cotuca.librasacademy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button btnPrev;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licao);

        server = getResources().getString(R.string.server);

        tvNome = (TextView) findViewById(R.id.name);
        tvTexto = (TextView) findViewById(R.id.tvTexto);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);

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
                            SubLicao sublicao = new SubLicao(texto, codigo);
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
            tvTexto.setText(sublicoes.get(sublicaoIndex).getTexto());
        } else {
            LicaoActivity.this.finish();
        }
    }
}