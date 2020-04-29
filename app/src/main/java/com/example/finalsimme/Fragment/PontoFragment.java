package com.example.finalsimme.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalsimme.Adapter.PontosAdapter;
import com.example.finalsimme.ChartActivity;
import com.example.finalsimme.GetData;
import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.R;
import com.example.finalsimme.TestConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PontoFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textPonto;

    private ProgressBar progressBarPonto;
    private FloatingActionButton floatingActionButton;

    private static final String idBanco_ARG = "idBanco";
    private static final String idEquipamento_ARG = "idEquipamento";
    private static final String nomeInstalacao_ARG = "nomeInstalacao";
    private static final String nomeEquipamento_ARG = "nomeEquipamento";

    private String idBanco, idEquipamento, nomeInstalacao, nomeEquipamento;

    public static PontoFragment newInstance(String idBanco, String idEquipamento, String nomeInstalacao, String nomeEquipamento){

        PontoFragment fragment = new PontoFragment();
        Bundle bundle = new Bundle();

        bundle.putString(idBanco_ARG, idBanco);
        bundle.putString(idEquipamento_ARG, idEquipamento);
        bundle.putString(nomeInstalacao_ARG, nomeInstalacao);
        bundle.putString(nomeEquipamento_ARG, nomeEquipamento);

        fragment.setArguments(bundle);
        return fragment;
    }


    public PontoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ponto, container, false);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setSize(FloatingActionButton.SIZE_MINI);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        textPonto = view.findViewById(R.id.textPonto);
        progressBarPonto = view.findViewById(R.id.progressBarPonto);

        recyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if(getArguments() != null){
            idBanco = getArguments().getString(idBanco_ARG);
            idEquipamento = getArguments().getString(idEquipamento_ARG);
            nomeInstalacao = getArguments().getString(nomeInstalacao_ARG);
            nomeEquipamento = getArguments().getString(nomeEquipamento_ARG);
        }

        textPonto.setText(nomeInstalacao + "  -  " + nomeEquipamento);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(new TestConnection(getContext()).isNetworkAvailable()){
                    swipeRefreshLayout.setRefreshing(false);
                    try{
                        new JSONParserPontos(getContext(), recyclerView, idBanco, idEquipamento, true).execute();
                    } catch(Exception e){
                        Toast.makeText(getContext(), "Erro ao atualizar os dados", Toast.LENGTH_SHORT).show();
                    }

                } else{
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        new JSONParserPontos(getContext(), recyclerView, idBanco, idEquipamento, false).execute();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChartActivity.class);
                //Passando dados para nova activity
                intent.putExtra("equipamento", idEquipamento);
                intent.putExtra("banco", idBanco);
                intent.putExtra("instalacao",nomeInstalacao);
                intent.putExtra("nomeEquipamento",nomeEquipamento);
                startActivity(intent);
            }
        });

        return view;
    }

    public class JSONParserPontos extends AsyncTask<Void, Void, Boolean>{

        private Context c;
        private ArrayList<Pontos> pontos = new ArrayList<>();
        private RecyclerView recyclerView;
        private static final String TAG ="Log";
        private Boolean isRefresh;
        private RecyclerView.Adapter adapter;
        public String idBanco, idEquipamento;

        public JSONParserPontos(Context c, RecyclerView recyclerView, String idBanco, String idEquipamento, Boolean isRefresh) {
            this.c = c;
            this.recyclerView = recyclerView;
            this.idBanco = idBanco;
            this.idEquipamento = idEquipamento;
            this.isRefresh = isRefresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(isRefresh){
                progressBarPonto.setVisibility(View.INVISIBLE);
            } else{
                progressBarPonto.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            GetData getData = new GetData();
            String finalJson = getData.getDataSOAPPonto("JsonCPonto", idBanco, idEquipamento);

            if(finalJson.contains("[]") ||  finalJson.contains("Server was unable to process request.")){
                return false;
            } else{
                try {


                    JSONArray JA = new JSONArray(finalJson);

                    pontos.clear();

                    for (int i = 0; i < JA.length(); i++) {

                        JSONObject JO = (JSONObject) JA.get(i);

                        Pontos resultRow = new Pontos();

                        resultRow.alarmeinf = JO.getString("alarmeinf");
                        resultRow.alarmesup = JO.getString("alarmesup");
                        resultRow.descr = JO.getString("Descr");
                        resultRow.datahora = JO.getString("datahora");
                        resultRow.pNome = JO.getString("pNome");
                        resultRow.unidade = JO.getString("unidade");
                        resultRow.instrume = JO.getString("Instrume");
                        resultRow.valorglobal = JO.getString("valorglobal");
                        resultRow.cPontoID = JO.getString("cPontoID");

                        pontos.add(resultRow);

                    }
                    Log.d(TAG, "JSON Parse feito");
                    return true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "JSONException");
                    return false;
                }
            }

        }

        @Override
        protected void onPostExecute(Boolean isParsed) {
            super.onPostExecute(isParsed);

            progressBarPonto.setVisibility(View.INVISIBLE);

            if(isParsed){
                try{
                    PontosAdapter adapter = new PontosAdapter(c, pontos);
                    recyclerView.setAdapter(adapter);
                    Log.d(TAG, "Configurou o Adapter");
                    if(isRefresh){
                        Toast.makeText(getContext(), "valores atualizados", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e){
                    Log.d(TAG, "Erro ao configurar o adapter");
                    Toast.makeText(c, "Algo deu errado!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(c, "Algo deu errado, o web serice não pôde ser acessado", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
