package com.example.finalsimme;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.finalsimme.Fragment.CheckFragment;

public class ChartActivity extends AppCompatActivity {

    private String equipamento, idBanco, instalacao, nomeEquipamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //Recuperar dados da outra activity
        Bundle dados = getIntent().getExtras();
        equipamento = dados.getString("equipamento");
        idBanco = dados.getString("banco");
        instalacao = dados.getString("instalacao");
        nomeEquipamento = dados.getString("nomeEquipamento");

        setupToolBar();

        Log.d("instalacao", instalacao);
        Log.d("nomeEquipamento", nomeEquipamento);

        CheckFragment checkFragment = CheckFragment.newInstance(idBanco, equipamento);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutChart, checkFragment).commit();
    }

    private void setupToolBar(){

        Toolbar toolbar = findViewById(R.id.toolbar);

        if(toolbar == null) return;

        //setTitle("Gráficos");
        toolbar.setTitle("Gráficos");
        toolbar.setSubtitle(instalacao + " - " + nomeEquipamento);

        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    //    public void callAssynchronousTask(){
//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//        TimerTask doAsynchronousTask = new TimerTask(){
//
//            @Override
//            public void run() {
//                try{
//                    GetData ponto = new GetData();
//                    String pontos = ponto.getDataSOAPPonto("JsonCPonto", idBanco, equipamento);
//                } catch(){
//
//                }
//            }
//        }
//    }
}
