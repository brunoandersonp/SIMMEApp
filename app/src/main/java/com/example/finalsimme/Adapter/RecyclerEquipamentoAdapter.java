package com.example.finalsimme.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalsimme.GetData;
import com.example.finalsimme.Model.Equipamento;
import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.R;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerEquipamentoAdapter extends RecyclerView.Adapter<RecyclerEquipamentoAdapter.MyViewHolder>{

    Context context;
    ArrayList<Equipamento> equipamento;
    String idBanco;

    public RecyclerEquipamentoAdapter(Context context, ArrayList<Equipamento> equipamento, String idBanco) {
        this.context = context;
        this.equipamento = equipamento;
        this.idBanco = idBanco;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listaEquipamento = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_equipamento, viewGroup, false);
        return  new RecyclerEquipamentoAdapter.MyViewHolder(listaEquipamento);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Equipamento tempEquipamento = (Equipamento) getItem(i);

        new GetDate(idBanco, tempEquipamento.getcEquipamentoID(), tempEquipamento.getCodoper(), tempEquipamento, myViewHolder).execute();

    }

    public Object getItem(int position){
        return equipamento.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return super.getItemId(position);
        return position;
    }

    @Override
    public int getItemCount() {
        return equipamento.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeEquipamento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeEquipamento = itemView.findViewById(R.id.itemTextEquipamento);
        }
    }



    public class GetDate extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<Pontos> pontos = new ArrayList<>();
        public String idBanco, idEquipamento, resultRow, codOper;
        private static final String TAG ="Log PDE";

        MyViewHolder myViewHolder;

        Seconds seconds;
        Equipamento tempEquipamento;

        public GetDate(String idBanco, String idEquipamento, String codOper, Equipamento tempEquipamento, MyViewHolder myViewHolder) {
            this.idBanco = idBanco;
            this.idEquipamento = idEquipamento;
            this.codOper = codOper;
            this.tempEquipamento = tempEquipamento;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            GetData getData = new GetData();
            String finalJson = getData.getDataSOAPPonto("JsonCPonto", idBanco, idEquipamento);

            try {


                JSONArray JA = new JSONArray(finalJson);

                JSONObject JO = (JSONObject) JA.get(0);
                resultRow = JO.getString("datahora");

                Log.d(TAG, "Pegou a data: " + resultRow);
                DateTime dataAtual = new DateTime();
                DateTime dataAquisicao = new DateTime(resultRow);
                seconds = Seconds.secondsBetween(dataAquisicao, dataAtual);
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "Erro ao pegar a data: " + resultRow);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean){

                try {

                    if(seconds.getSeconds() > 40){
                        myViewHolder.nomeEquipamento.setText(tempEquipamento.getCodoper() + " (OffLine)");
                        //codEquipamento.setText(tempEquipamento.getCodoper() + " (OffLine)");
                    } else{
                        myViewHolder.nomeEquipamento.setText(tempEquipamento.getCodoper());
                        //codEquipamento.setText(tempEquipamento.getCodoper());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Parse Exception");
                }
            }
        }
    }
}
