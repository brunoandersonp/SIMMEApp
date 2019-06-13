package com.example.finalsimme.Fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalsimme.Adapter.PontosListAdapter;
import com.example.finalsimme.ChartActivity;
import com.example.finalsimme.GetData;
import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraficosFragment extends Fragment{

    private TextView descrPonto;
    private LineChart grafico;
    private int flag;
    private long startTime;
    private ArrayList<Entry> entriesData = new ArrayList<>();
    Timer timer = new Timer();
    ArrayList<Pontos> pontosEscolhidos = new ArrayList<>();


    private String idBanco, idEquipamento;

    private static final String idBanco_ARG = "idBanco";
    private static final String equipamento = "idEquipamento";

    public static GraficosFragment newInstance(String idBanco, String idEquipamento){

        GraficosFragment fragment = new GraficosFragment();
        Bundle bundle = new Bundle();

        bundle.putString(idBanco_ARG, idBanco);
        bundle.putString(equipamento, idEquipamento);


        fragment.setArguments(bundle);
        return fragment;
    }

    public GraficosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graficos, container, false);

        if(getArguments() != null){
            idBanco = getArguments().getString(idBanco_ARG);
            idEquipamento = getArguments().getString(equipamento);
        }

//        textGrafico = view.findViewById(R.id.textGrafico);

        grafico = view.findViewById(R.id.lineChart);
        descrPonto = view.findViewById(R.id.descrPonto);

        Log.d("list graficos", PontosListAdapter.pontosSelecionados.toString());


        grafico.setDragEnabled(true);
        grafico.setScaleEnabled(true);

        callAsynchronousTask();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
        timer = null;
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        //Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if(flag == 0){
                                startTime = System.currentTimeMillis();
                                flag = 1;
                            }
                            GerarGraficos gerarGraficos = new GerarGraficos();
                            gerarGraficos.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getContext(), "Houve um problema ao se comunicar com o banco", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 4000); //execute in every 50000 ms
    }


    public class GerarGraficos extends AsyncTask<Void, Void, Void>{


        long timeElapsed;

        @Override
        protected Void doInBackground(Void... voids) {

            GetData ponto = new GetData();
            String pontos = ponto.getDataSOAPPonto("JsonCPonto", idBanco, idEquipamento);

            if(pontos != null){
                try{
                    pontosEscolhidos.clear();

                    JSONArray JA = new JSONArray(pontos);
                    for(int i = 0; i < JA.length(); i++){
                        JSONObject JO = (JSONObject) JA.get(i);
                        if(PontosListAdapter.pontosSelecionados.contains(String.valueOf(JO.getInt("cPontoID")))){

                            Pontos pontoEscolhido = new Pontos();

                            pontoEscolhido.valorglobal = JO.getString("valorglobal");
                            pontoEscolhido.descr = JO.getString("Descr");
                            pontoEscolhido.unidade = JO.getString("unidade");

                            pontosEscolhidos.add(pontoEscolhido);

                        }
                    }

                    if(!pontosEscolhidos.isEmpty()){
                        timeElapsed = (System.currentTimeMillis() - startTime)/1000;
                        entriesData.add(new Entry((float) timeElapsed, Float.valueOf(pontosEscolhidos.get(0).getValorglobal())));
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

            LineDataSet dataset1 = new LineDataSet(entriesData, "DataSet Teste");

            if(!pontosEscolhidos.isEmpty()){
                descrPonto.setText(pontosEscolhidos.get(0).getDescr() + " (" + pontosEscolhidos.get(0).getUnidade() + ")");
            }

            dataset1.setColor(Color.BLUE);

            dataset1.setDrawCircles(true);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataset1);

            LineData data = new LineData(dataSets);
            grafico.setData(data);
            grafico.notifyDataSetChanged();
            grafico.setVisibleXRangeMaximum(10);
            grafico.moveViewToX(data.getEntryCount());
            grafico.invalidate();
        }
    }

}
