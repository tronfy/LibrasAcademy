package br.unicamp.cotuca.librasacademy;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WordActivity extends AppCompatActivity {
    private String server;
    private String word;
    private String site = "http://www.acessibilidadebrasil.org.br/libras_3/";

    private TextView txtWord;
    private TextView txtInfo;
    private VideoView videoSinais;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        server = getResources().getString(R.string.server);
        Bundle b = getIntent().getExtras();
        word = b.getString("word");

        videoSinais = (VideoView) findViewById(R.id.videoSinais);
        txtWord = (TextView) findViewById(R.id.txtWord);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtWord.setText(word);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("palavra", word);

        HttpManager.get(this, server + ":80/dados_palavra/", params, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = null;
                String link = site;
                String info = "";
                try {
                    data = result.getJSONObject("palavras");
                    site += data.getString("gif");
//                        Toast.makeText(getApplicationContext(), site, Toast.LENGTH_SHORT).show();

                    Uri video = Uri.parse(site);
                    videoSinais.setVideoURI(video);
                    videoSinais.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            videoSinais.start();
                        }
                    });

                    info = " Significao: " + data.getString("significado") + "\n Gênero: " + data.getString("genero")
                        + "\n Origem: " + data.getString("origem") + "\n Exemplo em português: " + data.getString("exemploPT")
                        + "\n Exemplo em Libras: " + data.getString("exemploLibras");
                    txtInfo.setText(info);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Erro ao pegar dados da palavra", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(JSONObject result) {
                Toast.makeText(getApplicationContext(), "Deu errado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
