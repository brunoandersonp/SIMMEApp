package com.example.finalsimme.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class EquipamentoListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Equipamento> equipamento = new ArrayList<>();
    String idBanco;

    private static final String TAG ="Log PDE";

    public EquipamentoListAdapter(Context context, ArrayList<Equipamento> equipamento, String idBanco){
        this.context = context;
        this.equipamento = equipamento;
        this.idBanco = idBanco;
    }

    @Override
    public int getCount() {
        return equipamento.size();
    }

    @Override
    public Object getItem(int position) {
        return equipamento.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_equipamento, parent, false);
        }

        Equipamento tempEquipamento = (Equipamento) getItem(position);

        TextView codEquipamento = convertView.findViewById(R.id.itemTextEquipamento);

        if(tempEquipamento.getEstado() == true){
            codEquipamento.setText(tempEquipamento.getCodoper());
        } else{
            codEquipamento.setText(tempEquipamento.getCodoper() + " (Off-line)");
        }


        return convertView;
    }
}
