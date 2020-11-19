package br.unicamp.cotuca.librasacademy;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        videoSinais = findViewById(R.id.videoSinais);
        txtWord = findViewById(R.id.txtWord);
        txtInfo = findViewById(R.id.txtInfo);
        txtWord.setText(word);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("palavra", word);

        HttpManager.get(this, server + "/palavra/", params, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject data = null;
                String info = "";
                try {
                    data = result.getJSONObject("palavras");
                    site += data.getString("gif");

                    final Uri video = Uri.parse(site);
                    videoSinais.setVideoURI(video);
                    videoSinais.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            videoSinais.start();
                        }
                    });
                    videoSinais.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            Toast.makeText(getApplicationContext(), "Erro ao tocar vídeo", Toast.LENGTH_SHORT).show();
                            videoSinais.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    info = "Significado: " + data.getString("significado") + "\nGênero: " + data.getString("genero")
                        + "\nOrigem: " + data.getString("origem") + "\nExemplo em português: " + data.getString("exemploPT")
                        + "\nExemplo em Libras: " + data.getString("exemploLibras");
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
