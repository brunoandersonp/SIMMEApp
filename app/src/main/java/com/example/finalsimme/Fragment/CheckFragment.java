package com.example.finalsimme.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalsimme.Adapter.PontosListAdapter;
import com.example.finalsimme.GetData;
import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {

    ProgressBar progressBarListaPontos;
    Button buttonGerar;
    ListView listaPontos;

    private String idBanco, idEquipamento;

    private static final String idBanco_ARG = "idBanco";
    private static final String equipamento = "idEquipamento";

    public static CheckFragment newInstance(String idBanco, String idEquipamento){

        CheckFragment fragment = new CheckFragment();
        Bundle bundle = new Bundle();

        bundle.putString(idBanco_ARG, idBanco);
        bundle.putString(equipamento, idEquipamento);

        fragment.setArguments(bundle);
        return fragment;
    }


    public CheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check, container, false);

        progressBarListaPontos = view.findViewById(R.id.progressBarListaPontos);
        buttonGerar = view.findViewById(R.id.buttonGerar);
        listaPontos = view.findViewById(R.id.listaPontos);


        if(getArguments() != null){
            idBanco = getArguments().getString(idBanco_ARG);
            idEquipamento = getArguments().getString(equipamento);
        }


        CriarLista criarLista = new CriarLista();
        criarLista.execute();

        return view;
    }

    public class CriarLista extends AsyncTask<Void, Void, Void> {

        ArrayList<Pontos> lista_pontos = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarListaPontos.setVisibility(View.VISIBLE);
            buttonGerar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            GetData ponto = new GetData();
            String pontos = ponto.getDataSOAPPonto("JsonCPonto", idBanco, idEquipamento);

            if(pontos != null){
                try{
                    JSONArray JA = new JSONArray(pontos);
                    for(int i = 0; i < JA.length(); i++){
                        Pontos pontosObject = new Pontos();
                        JSONObject JO = (JSONObject) JA.get(i);
                        pontosObject.setDescr(JO.getString("Descr"));
                        pontosObject.setcPontoID(String.valueOf(JO.getInt("cPontoID")));
                        //pontosObject.setSelected(false);
                        lista_pontos.add(pontosObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBarListaPontos.setVisibility(View.GONE);
            buttonGerar.setVisibility(View.VISIBLE);

            final PontosListAdapter pontosListAdapter = new PontosListAdapter(getContext(), R.layout.list_pontos, lista_pontos);
            listaPontos.setAdapter(pontosListAdapter);

            buttonGerar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!PontosListAdapter.pontosSelecionados.isEmpty()){

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Fragment graficosFragment = GraficosFragment.newInstance(idBanco, idEquipamento);

                        transaction.addToBackStack(null);
                        transaction.replace(R.id.frameLayoutChart, graficosFragment);
                        transaction.commit();

                    } else{
                        Toast.makeText(getContext(), "Selecione pelo menos 1 ponto", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
