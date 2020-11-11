package br.unicamp.cotuca.librasacademy;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

    private TextView txtWord;
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
        txtWord.setText(word);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("word", word);

        try {
            HttpManager.get(this, server + ":5000/getGif/", params, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    String txt = null;
                    try {
                        txt = result.getString("link");
                        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();

                        Uri video = Uri.parse(txt);
                        videoSinais.setVideoURI(video);
                        videoSinais.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                                videoSinais.start();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(JSONObject result) {
                    Toast.makeText(getApplicationContext(), "Deu errado", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //                            Glide.with(context)
//                                    .load("https://inthecheesefactory.com/uploads/source/glidepicasso/gifanimation2.gif")
//                                    .into(idDoTeuImageView);
    }
}
