package br.unicamp.cotuca.librasacademy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.unicamp.cotuca.librasacademy.R;

public class CategoriasFragment extends Fragment {
    String mtitle[] = {"Alfabeto Manual"};
    String mDescription[] = {"Que tal aprende sobre o alfabeto utilizade na libras"};
    String mAuthor[] = {"Desconhecido"};

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

        listActivitys = (ListView) view.findViewById(R.id.list_activitys);

        CategoriasFragment.MyAdapter adapter = new CategoriasFragment.MyAdapter( context, mtitle, mDescription, mAuthor);

        listActivitys.setAdapter(adapter);
        listActivitys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Toast.makeText(context, "clicou", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hub, container, false);
    }

    public static class MyAdapter extends ArrayAdapter<String>
    {
        Context context;
        String rTitle[];
        String rDescription[];
        String rAuthor[];

        MyAdapter(Context c, String title[], String description[], String author[])
        {
            super(c, R.layout.item_row, R.id.text_title, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rAuthor = author;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.item_row, parent, false);
            TextView myTitle = row.findViewById(R.id.text_title);
            TextView myDescription = row.findViewById(R.id.text_description);
            TextView myAuthor = row.findViewById(R.id.text_author);

            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);
            myAuthor.setText(rAuthor[position]);


            return row;
        }
    }
}
