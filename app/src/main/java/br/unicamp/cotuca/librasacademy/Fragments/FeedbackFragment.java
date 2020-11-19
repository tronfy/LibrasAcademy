package br.unicamp.cotuca.librasacademy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.util.HashMap;

import br.unicamp.cotuca.librasacademy.HttpManager;
import br.unicamp.cotuca.librasacademy.R;
import br.unicamp.cotuca.librasacademy.VolleyCallback;

public class FeedbackFragment extends Fragment {

    private Spinner spinnerTipo;
    private Button btnSend;
    private String server;

    private Context context;
    private EditText txtDesc;


    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        server = getResources().getString(R.string.server);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerTipo = view.findViewById(R.id.spinner);
        btnSend = view.findViewById(R.id.buttonEnviar);
        txtDesc = view.findViewById(R.id.txtDesc);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.feedback_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTipo.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user", "teste");
                params.put("tipo", spinnerTipo.getSelectedItem().toString());
                params.put("descricao", txtDesc.getText().toString());

                HttpManager.post(context, server + "/feedback", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast.makeText(context, "Enviado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(JSONObject result) {
                        Toast.makeText(context, "Enviado", Toast.LENGTH_SHORT).show();
                    }
                });

                Fragment main = new CategoriasFragment();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, main).commit();
            }
        });


    }
}
