package br.unicamp.cotuca.librasacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import br.unicamp.cotuca.librasacademy.Fragments.AboutFragment;
import br.unicamp.cotuca.librasacademy.Fragments.DictionaryFragment;
import br.unicamp.cotuca.librasacademy.Fragments.FeedbackFragment;
import br.unicamp.cotuca.librasacademy.Fragments.LicaoFragment;
import br.unicamp.cotuca.librasacademy.dbo.Licao;

public class CategoriaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private ListView listview;
    private List<Licao> licoes;
    private String server;
    private LicaoAdapter adapter;
    private Licao categoria;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = getResources().getString(R.string.server);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBgLight));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(CategoriaActivity.this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Bundle extras = getIntent().getExtras();
        categoria = (Licao) extras.getSerializable("CATEGORIA");

        titulo = findViewById(R.id.name);
        titulo.setText(categoria.getNome());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new LicaoFragment(categoria)).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        listview = findViewById(R.id.list_itens);

        /*licoes = new ArrayList<Licao>();
        adapter = new LicaoAdapter(CategoriaActivity.this, licoes);
        listview.setAdapter(adapter);*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(CategoriaActivity.this, "clicou " + i, Toast.LENGTH_SHORT).show();
            }
        });

        /*try {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("categoria", categoria.getNome());
            HttpManager.get(this, server + "/licoes", params, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        JSONArray res = (JSONArray) result.get("resultado");
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject o = res.getJSONObject(i);
                            String nome = o.getString("nome");
                            String descricao = o.getString("descricao");
                            Licao licao = new Licao(nome, descricao);
                            licoes.add(licao);
                        }
                        System.out.println(licoes);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(JSONObject result) {
                    try {
                        Toast.makeText(getApplicationContext(), result.getString("err"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {

                    }

                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }*/
    }

    public void getClick(Licao licao) {
        //System.out.println(licao);
        Intent intent = new Intent(getApplicationContext(), LicaoActivity.class);
        intent.putExtra("LICAO", licao);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_lesson:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LicaoFragment(categoria)).commit();
                break;

            case R.id.nav_dictionary:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DictionaryFragment()).commit();
                break;

            case R.id.nav_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FeedbackFragment()).commit();
                break;

            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}