package com.example.finalsimme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.finalsimme.Adapter.ExpandableListAdapter;
import com.example.finalsimme.Fragment.EquipamentoFragment;
import com.example.finalsimme.Fragment.PaginaInicialFragment;
import com.example.finalsimme.Model.Instalacoes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    private static final String TAG ="Log";
    private static List<String> listDataHeader;
    private static HashMap<String, ArrayList<Instalacoes>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandableListView);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawer.openDrawer(GravityCompat.START);

        expandableListView.setChildDivider(getResources().getDrawable(R.color.transparente));

        new JSONParserInstalacoes().execute();

        PaginaInicialFragment paginaInicialFragment = new PaginaInicialFragment();
        FragmentManager paginaManager = getSupportFragmentManager();
        paginaManager.beginTransaction().replace(R.id.fragmentContainer, paginaInicialFragment).commit();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {



                if(new TestConnection(getApplicationContext()).isNetworkAvailable()){
                    try{
                        EquipamentoFragment equipamentoFragment = EquipamentoFragment.newInstance(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).banco,
                                listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).cInstalacaoID,
                                listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).nome);

                        FragmentManager manager = getSupportFragmentManager();

                        manager.beginTransaction().replace(R.id.fragmentContainer, equipamentoFragment).addToBackStack(null).commit();

                        if(drawer.isDrawerOpen(GravityCompat.START)){
                            drawer.closeDrawer(GravityCompat.START);
                        }
                    } catch(Exception e){
                        Toast.makeText(getApplicationContext(), "Opa! Algo de errado aconteceu", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1 ) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public class JSONParserInstalacoes extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            GetData getData = new GetData();
            String finalJson = getData.getDataSOAP("JsonInstalacoes");

            //String finalJson = getData.getDataURL();

            if(finalJson != null){
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, ArrayList<Instalacoes>>();

                listDataHeader.add("Estação Repetidora");
                listDataHeader.add("Compensador Síncrono");
                listDataHeader.add("Usina Hidrelétrica");

                ArrayList<Instalacoes> er = new ArrayList<Instalacoes>();
                ArrayList<Instalacoes> cs = new ArrayList<Instalacoes>();
                ArrayList<Instalacoes> uhe = new ArrayList<Instalacoes>();


                try {
                    JSONArray JA = new JSONArray(finalJson);

                    for(int i = 0; i < JA.length(); i++){

                        JSONObject JO = (JSONObject) JA.get(i);

                        Instalacoes resultRow = new Instalacoes();

                        resultRow.nome = JO.getString("Nome");
                        resultRow.tipo = JO.getString("Tipo");
                        resultRow.banco = JO.getString("Banco");
                        resultRow.cInstalacaoID = JO.getString("cInstalacaoID");

                        if(resultRow.tipo.equals("CS")){
                            cs.add(resultRow);
                        } else{
                            if(resultRow.tipo.equals("UH")){
                                uhe.add(resultRow);
                            } else{
                                er.add(resultRow);
                            }
                        }

                        listDataChild.put(listDataHeader.get(0),er);
                        listDataChild.put(listDataHeader.get(1),cs);
                        listDataChild.put(listDataHeader.get(2),uhe);

                    }
                    Log.d(TAG, "JSON Parse feito");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "JSONException");
                }
            }

            return finalJson;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid != null){
                try{
                    expandableListAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
                    expandableListView.setAdapter(expandableListAdapter);
                    Log.d(TAG, "Configurou a lista");
                } catch(Exception e){
                    Log.d(TAG, "Erro ao configurar lista");
                    Toast.makeText(getApplicationContext(), "Opa! Algo de errado aconteceu", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Algo de errado ocorreu. Verifique sua conexão com a Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
