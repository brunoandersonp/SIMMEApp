//package com.example.finalsimme.Fragment;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.example.finalsimme.Adapter.LineChartAdapter;
//import com.example.finalsimme.Adapter.PontosListAdapter;
//import com.example.finalsimme.Model.Pontos;
//import com.example.finalsimme.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class GraficosFragment extends Fragment {
//
//    ListView listaGraficos;
//    ArrayList<Pontos> pontosEscolhidos = new ArrayList<>();
//
//    private String idBanco, idEquipamento;
//
//    private static final String idBanco_ARG = "idBanco";
//    private static final String equipamento = "idEquipamento";
//
//    Timer timer = new Timer();
//    //int index;
//
//    public static GraficosFragment newInstance(String idBanco, String idEquipamento) {
//
//        GraficosFragment fragment = new GraficosFragment();
//        Bundle bundle = new Bundle();
//
//        bundle.putString(idBanco_ARG, idBanco);
//        bundle.putString(equipamento, idEquipamento);
//
//
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    public GraficosFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_graficos, container, false);
//
//        if (getArguments() != null) {
//            idBanco = getArguments().getString(idBanco_ARG);
//            idEquipamento = getArguments().getString(equipamento);
//        }
//
//        listaGraficos = view.findViewById(R.id.listaGraficos);
//
//        callAsynchronousTask();
//
//        return view;
//    }
//
//
//    public void callAsynchronousTask(){
//        final Handler handler = new Handler();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            GetDataPonto getDataPonto = new GetDataPonto();
//                            getDataPonto.execute();
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            Toast.makeText(getContext(), "Houve um problema ao se comunicar com o banco", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(doAsynchronousTask, 0, 4000); //execute in every 4000 ms
//    }
//
//    //VERIFICAR ISSO
//    @Override
//    public void onPause() {
//        super.onPause();
//        timer.cancel();
//    }
//
//    //AsynchTask<>
//    public class GetDataPonto extends AsyncTask<Void, Void, Void> {
//
//        String pontos;
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            com.example.finalsimme.GetData ponto = new com.example.finalsimme.GetData();
//            pontos = ponto.getDataSOAPPonto("JsonCPonto", idBanco, idEquipamento);
//
//            if (pontos != null) {
//                try {
//                    pontosEscolhidos.clear();
//                    JSONArray JA = new JSONArray(pontos);
//                    for (int i = 0; i < JA.length(); i++) {
//                        JSONObject JO = (JSONObject) JA.get(i);
//                        if (PontosListAdapter.pontosSelecionados.contains(String.valueOf(JO.getInt("cPontoID")))) {
//
//                            Pontos pontoEscolhido = new Pontos();
//
//                            pontoEscolhido.valorglobal = JO.getString("valorglobal");
//                            pontoEscolhido.descr = JO.getString("Descr");
//                            pontoEscolhido.unidade = JO.getString("unidade");
//                            pontoEscolhido.datahora = JO.getString("datahora");
//
//                            pontosEscolhidos.add(pontoEscolhido);
//
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
////            LineChartAdapter lineChartAdapter = new LineChartAdapter(getContext(), list, pontosEscolhidos);
////            listaGraficos.setAdapter(lineChartAdapter);
////            lineChartAdapter.notifyDataSetChanged();
//
//
//        }
//    }
//}
//
