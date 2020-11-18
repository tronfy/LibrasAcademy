package br.unicamp.cotuca.librasacademy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.cotuca.librasacademy.HttpManager;
import br.unicamp.cotuca.librasacademy.LicaoAdapter;
import br.unicamp.cotuca.librasacademy.MainActivity;
import br.unicamp.cotuca.librasacademy.R;
import br.unicamp.cotuca.librasacademy.VolleyCallback;
import br.unicamp.cotuca.librasacademy.dbo.Licao;

public class CategoriasFragment extends Fragment {
    String server;
    List<Licao> categorias;
    LicaoAdapter adapter;

    ListView listActivitys;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        server = getResources().getString(R.string.server);

        listActivitys = view.findViewById(R.id.list_activitys);

        categorias = new ArrayList<Licao>();
        adapter = new LicaoAdapter(context, categorias);
        listActivitys.setAdapter(adapter);
        try {
            listActivitys.setDivider(context.getResources().getDrawable(R.color.colorBgLight, context.getTheme()));
        } catch (Exception e) {}
        listActivitys.setDividerHeight(20);

        try {
            HttpManager.get(context, server + "/categorias", new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        JSONArray res = (JSONArray) result.get("resultado");
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject o = res.getJSONObject(i);
                            String nome = o.getString("nome");
                            String descricao = o.getString("descricao");
                            Licao categoria = new Licao(nome, descricao);
                            categorias.add(categoria);
                        }
                        System.out.println(categorias);
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
                ((MainActivity) getActivity()).getClick(categorias.get(i));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hub, container, false);
    }
}
