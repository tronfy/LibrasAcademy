package br.unicamp.cotuca.librasacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import br.unicamp.cotuca.librasacademy.Fragments.AboutFragment;
import br.unicamp.cotuca.librasacademy.Fragments.CategoriasFragment;
import br.unicamp.cotuca.librasacademy.Fragments.DictionaryFragment;
import br.unicamp.cotuca.librasacademy.Fragments.FeedbackFragment;
import br.unicamp.cotuca.librasacademy.dbo.Licao;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private ListView listview;
    private String server;
    private LicaoAdapter adapter;

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
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CategoriasFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        listview = findViewById(R.id.list_itens);

        /*categorias = new ArrayList<Licao>();
        adapter = new LicaoAdapter(MainActivity.this, categorias);
        listview.setAdapter(adapter);*/

        /*try {
            HttpManager.get(this, server + "/categorias", new VolleyCallback() {
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
        System.out.println(licao);
        Intent intent = new Intent(getApplicationContext(), CategoriaActivity.class);
        intent.putExtra("CATEGORIA", licao);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CategoriasFragment()).commit();
                break;

            case R.id.nav_dictionary:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DictionaryFragment()).commit();
                break;

            case R.id.nav_camera:
                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(cameraIntent);
                break;

            case R.id.nav_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FeedbackFragment()).commit();
                break;

            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;

            case R.id.nav_logout:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("CLEAR", true);
                startActivity(intent);

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