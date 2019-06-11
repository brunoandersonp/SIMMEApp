package com.example.finalsimme.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalsimme.Adapter.EquipamentoListAdapter;
import com.example.finalsimme.Adapter.RecyclerEquipamentoAdapter;
import com.example.finalsimme.GetData;
import com.example.finalsimme.Model.Equipamento;
import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.Model.RecyclerItemClickListener;
import com.example.finalsimme.R;
import com.example.finalsimme.TestConnection;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipamentoFragment extends Fragment {

    TextView textInstalacao;
    ListView listaEquipamento;
    //RecyclerView listaEquipamento;

    private ProgressBar progressBarEquipamento;

    private static final String TAG ="Log LISTA";

    //String finalJson;

    private ArrayList<Equipamento> equipamento;

    private final static String TAG_FRAGMENT = "TAG_FRAGMENT_EQUIPAMENTO";

    private static final String idBanco_ARG = "idBanco";
    private static final String idInstalacao_ARG = "idInstalacao";
    private static final String nome_ARG = "nomeInstalacao";

    private String idBanco, idInstalacao, nomeInstalacao;

    public static EquipamentoFragment newInstance(String idBanco, String idInstalacao, String nomeInstalacao){

        EquipamentoFragment fragment = new EquipamentoFragment();
        Bundle bundle = new Bundle();

        bundle.putString(idBanco_ARG, idBanco);
        bundle.putString(idInstalacao_ARG, idInstalacao);
        bundle.putString(nome_ARG, nomeInstalacao);

        fragment.setArguments(bundle);
        return fragment;
    }


    public EquipamentoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipamento, container, false);

        textInstalacao = view.findViewById(R.id.textInstalacao);
        listaEquipamento = view.findViewById(R.id.listaEquipamento);
        progressBarEquipamento = view.findViewById(R.id.progressBarEquipamento);

        if(getArguments() != null){
            idBanco = getArguments().getString(idBanco_ARG);
            idInstalacao = getArguments().getString(idInstalacao_ARG);
            nomeInstalacao = getArguments().getString(nome_ARG);
        }

        textInstalacao.setText(nomeInstalacao);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        listaEquipamento.setLayoutManager(layoutManager);
//        listaEquipamento.setHasFixedSize(true);

        if(new TestConnection(getContext()).isNetworkAvailable()){
            try{
                new JSONParserEquipamentos().execute();
            } catch(Exception e){
                Toast.makeText(getContext(), "Problema ao se conectar à base de dados", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(getContext(), "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
        }

        listaEquipamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(new TestConnection(getContext()).isNetworkAvailable()){
                    if(equipamento.get(position).getEstado()){
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Fragment pontoFragment = PontoFragment.newInstance(idBanco, equipamento.get(position).getcEquipamentoID(), nomeInstalacao, equipamento.get(position).getCodoper());

                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragmentContainer, pontoFragment, TAG_FRAGMENT);
                        transaction.commit();
                    } else{
                        Toast.makeText(getContext(), "Equipamento Off-line desde " +
                                equipamento.get(position).getDataAquisicao(), Toast.LENGTH_SHORT).show();
                    }

                } else{
                    Toast.makeText(getContext(), "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public class JSONParserEquipamentos extends AsyncTask<Void, Void, Boolean>{

        public Seconds seconds;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarEquipamento.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String finalJson;
            String dataAquisicao;

            Boolean ativo;

            equipamento = new ArrayList<>();

            try {
                GetData getData = new GetData();
                finalJson = getData.getDataSOAPEquipamento("JsonCEquipamento", idBanco, idInstalacao);

                JSONArray JA = new JSONArray(finalJson);

                equipamento.clear();

                for (int i = 0; i < JA.length(); i++) {

                    JSONObject JO = (JSONObject) JA.get(i);
                    Equipamento resultRow = new Equipamento();
                    resultRow.codoper = JO.getString("codoper");
                    resultRow.cEquipamentoID = JO.getString("cEquipamentoID");

                    try{
                        //Verificando data de aquisicação
                        dataAquisicao = getData.getDataSOAPPonto("JsonCPonto", idBanco, resultRow.cEquipamentoID);
                        JSONArray JAPonto = new JSONArray(dataAquisicao);
                        JSONObject JOPonto = (JSONObject) JAPonto.get(0);
                        String dataAquisicaoPonto = JOPonto.getString("datahora");

                        if(idBanco.equals("14")){
                            DateTime dataAtual = new DateTime().minusHours(1);
                            DateTime dataPonto = new DateTime(dataAquisicaoPonto);
                            seconds = Seconds.secondsBetween(dataPonto, dataAtual);
                            Log.d(TAG, "dataAtual: " + dataAtual + " dataPonto: " + dataPonto);
                        } else{
                            if(idBanco.equals("8")){
                                DateTime dataAtual = new DateTime().minusHours(2);
                                DateTime dataPonto = new DateTime(dataAquisicaoPonto);
                                seconds = Seconds.secondsBetween(dataPonto, dataAtual);
                                Log.d(TAG, "dataAtual: " + dataAtual + " dataPonto: " + dataPonto);
                            } else{
                                DateTime dataAtual = new DateTime();
                                DateTime dataPonto = new DateTime(dataAquisicaoPonto);
                                seconds = Seconds.secondsBetween(dataPonto, dataAtual);
                            }
                        }

                        //Formatando a data
                        Date dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dataAquisicaoPonto);
                        String dataFormatada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dt);

                        resultRow.dataAquisicao = dataFormatada;



                        if(seconds.getSeconds() > 40){
                            //Se o equipamento estiver offline
                            resultRow.estado = false;
                        } else{
                            //Se o equipamento estiver online
                            resultRow.estado = true;
                        }
                    } catch(Exception e){
                        Log.d(TAG, "Erro ao pegar a data no equipamento com banco:" + idBanco + " e idEquipamento: " + resultRow.cEquipamentoID);
                        resultRow.estado = true;
                    }

                    ativo = JO.getBoolean("ativo");

                    if(ativo){
                        equipamento.add(resultRow);
                    }

                    Log.d(TAG, "Parse feito");
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "Erro no parse");

                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean isParsed) {

            super.onPostExecute(isParsed);

            progressBarEquipamento.setVisibility(View.INVISIBLE);

            if(isParsed){
                try{

                    EquipamentoListAdapter equipamentoListAdapter = new EquipamentoListAdapter(getContext(), equipamento, idBanco);
                    listaEquipamento.setAdapter(equipamentoListAdapter);
                    Log.d(TAG, "Configurou a lista de equipamentos");
                } catch(Exception e){
                    Log.d(TAG, "Erro ao configurar o adapter");
                }
            } else{
                Toast.makeText(getContext(), "Houve um problema ao tentar acessar os dados", Toast.LENGTH_SHORT).show();
            }





        }
    }

}
