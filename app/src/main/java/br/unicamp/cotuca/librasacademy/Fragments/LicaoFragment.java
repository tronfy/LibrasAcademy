package br.unicamp.cotuca.librasacademy.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.unicamp.cotuca.librasacademy.CategoriaActivity;
import br.unicamp.cotuca.librasacademy.HttpManager;
import br.unicamp.cotuca.librasacademy.LicaoAdapter;
import br.unicamp.cotuca.librasacademy.R;
import br.unicamp.cotuca.librasacademy.VolleyCallback;
import br.unicamp.cotuca.librasacademy.dbo.Licao;

import static android.content.Context.MODE_PRIVATE;

public class LicaoFragment extends Fragment {
    String server;
    Licao categoria;
    List<Licao> licoes;
    LicaoAdapter adapter;

    ListView listActivitys;
    Context context;

    SharedPreferences userPrefs;
    String username;

    public LicaoFragment(Licao categoria) {
        this.categoria = categoria;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        server = getResources().getString(R.string.server);

        userPrefs = context.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = userPrefs.getString("username", "");

        listActivitys = view.findViewById(R.id.list_activitys);

        licoes = new ArrayList<Licao>();
        adapter = new LicaoAdapter(context, licoes);
        listActivitys.setAdapter(adapter);
        try {
            listActivitys.setDivider(context.getResources().getDrawable(R.color.colorBgLight, context.getTheme()));
        } catch (Exception e) {}
        listActivitys.setDividerHeight(20);

        try {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("categoria", categoria.getNome());
            HttpManager.get(context, server + "/licoes", params, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        JSONArray res = (JSONArray) result.get("resultado");
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject o = res.getJSONObject(i);
                            String nome = o.getString("nome");
                            String descricao = o.getString("descricao");
                            int codigo = o.getInt("codigo");
                            Licao licao = new Licao(nome, descricao, codigo);
                            licoes.add(licao);
                        }
                        //System.out.println(licoes);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(JSONObject result) {
                    try {
                        Toast.makeText(context, result.getString("err"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {

                    }

                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            System.out.println("get concluidas");
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("username", username);
            HttpManager.get(context, server + "/usuariolicao", params, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        JSONArray res = (JSONArray) result.get("resultado");
                        System.out.println("LICOES RES: " + res);
                        ArrayList<Integer> concluidas = new ArrayList<Integer>();
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject o = res.getJSONObject(i);
                            int licao = o.getInt("licao");
                            concluidas.add(licao);
                        }

                        for (int i = 0; i < licoes.size(); i++) {
                            int cod = licoes.get(i).getCodigo();
                            for (int j = 0; j < concluidas.size(); j++) {
                                if (cod == concluidas.get(j)) {
                                    licoes.get(i).setConcluida(true);
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(JSONObject result) {
                    try {
                        Toast.makeText(context, result.getString("err"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {

                    }

                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        listActivitys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((CategoriaActivity) getActivity()).getClick(licoes.get(i));
                licoes.get(i).setConcluida(true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hub, container, false);
    }
}
