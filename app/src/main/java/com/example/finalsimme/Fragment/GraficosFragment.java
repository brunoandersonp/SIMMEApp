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

//    TextView textGrafico;

    private LineChart grafico;


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

        Log.d("list graficos", PontosListAdapter.pontosSelecionados.toString());

//        grafico.setOnChartGestureListener(GraficosFragment.this);
//        grafico.setOnChartValueSelectedListener(GraficosFragment.this);

        grafico.setDragEnabled(true);
        grafico.setScaleEnabled(true);
//
//        LimitLine upper_limit = ;
//
//        ArrayList<Entry> yValues = new ArrayList<>();
//
//        yValues.add(new Entry(0, 60f));
//        yValues.add(new Entry(1, 50f));
//        yValues.add(new Entry(2, 40f));
//        yValues.add(new Entry(3, 50f));
//        yValues.add(new Entry(4, 35f));
//        yValues.add(new Entry(5, 40f));
//        yValues.add(new Entry(0, 60f));
//
//        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
//
//        set1.setFillAlpha(110);
//
//        set1.setColor(Color.RED);
//        set1.setLineWidth(3f);
//        set1.setValueTextSize(10f);
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1);
//
//        LineData data = new LineData(dataSets);
//
//        grafico.setData(data);

//        GerarGraficos gerarGraficos = new GerarGraficos();
//        gerarGraficos.execute();

        callAsynchronousTask();

        return view;
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            GerarGraficos gerarGraficos = new GerarGraficos();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            gerarGraficos.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 4000); //execute in every 50000 ms
    }


    public class GerarGraficos extends AsyncTask<Void, Void, Void>{

        List<String> pontosEscolhidos = new ArrayList<>();

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
                               pontosEscolhidos.add(JO.getString("valorglobal"));
                        }
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

//            Log.d("pontos escolhidos", pontosEscolhidos.toString());
//
//            textGrafico.setText("");
//
//            String teste = "";
//            for(int i = 0; i < pontosEscolhidos.size(); i++){
//                teste += pontosEscolhidos.get(i) + "\n";
//                textGrafico.setText(teste);
//            }
        }
    }

}
