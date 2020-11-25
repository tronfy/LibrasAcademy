package br.unicamp.cotuca.librasacademy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.unicamp.cotuca.librasacademy.CameraActivity;
import br.unicamp.cotuca.librasacademy.HttpManager;
import br.unicamp.cotuca.librasacademy.R;
import br.unicamp.cotuca.librasacademy.VolleyCallback;
import br.unicamp.cotuca.librasacademy.WordActivity;

public class Dictionary2Fragment extends Fragment {
    private String letter;
    private final ArrayList<String> words = new ArrayList<>();
    private Context context;
    private String server;

    private ListView lstWords;
    private EditText txtSearch;
    private ImageButton search;
    private ImageButton camera;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle recived_letter = this.getArguments();
        letter = recived_letter.getString("letter");

        server = getResources().getString(R.string.server);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lstWords = view.findViewById(R.id.list_dictionary);
        search = view.findViewById(R.id.buttonSearch);
        txtSearch = view.findViewById(R.id.edtTextSerach);
        camera = view.findViewById(R.id.buttonCamera);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("letra", letter);

        HttpManager.get(
                context, server + "/palavras", params, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray words_json = result.getJSONArray("palavras");
                    for(int i = 0; i < words_json.length(); i++){
                        words.add(words_json.getJSONObject(i).getString("palavra"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, words);

                lstWords.setAdapter(adapter);
            }

            @Override
            public void onError(JSONObject result) {
                Toast.makeText(context, "erro", Toast.LENGTH_SHORT).show();
            }
        });

        lstWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent word_data = new Intent(getActivity(), WordActivity.class);

                Bundle mWord = new Bundle();
                mWord.putString("word", words.get(i));

                word_data.putExtras(mWord);
                startActivity(word_data);


            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent word_data = new Intent(getActivity(), WordActivity.class);

                Bundle mWord = new Bundle();
                mWord.putString("word", txtSearch.getText().toString());
                word_data.putExtras(mWord);
                startActivity(word_data);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(getActivity(), CameraActivity.class);
                startActivity(camera);
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dictionary2, container, false);
    }
}
