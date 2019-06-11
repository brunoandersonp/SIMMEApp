package com.example.finalsimme.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.R;

import java.util.ArrayList;

public class PontosAdapter extends RecyclerView.Adapter<PontosAdapter.MyViewHolder> {

    Context c;
    private ArrayList<Pontos> pontos;

    public PontosAdapter(Context c, ArrayList<Pontos> pontos) {
        this.c = c;
        this.pontos = pontos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_pontos, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.nome.setText(pontos.get(i).getpNome());
        myViewHolder.descricao.setText(pontos.get(i).getDescr());
        myViewHolder.alarmeinf.setText("> " + pontos.get(i).getAlarmeinf());
        myViewHolder.alarmesup.setText("< " + pontos.get(i).getAlarmesup());
        myViewHolder.valor.setText(String.format("%.2f", Double.parseDouble((pontos.get(i).getValorglobal()))) + " " + pontos.get(i).getUnidade());

        if(Double.parseDouble(pontos.get(i).getValorglobal()) < Double.parseDouble(pontos.get(i).getAlarmeinf())
                || Double.parseDouble(pontos.get(i).getValorglobal()) > Double.parseDouble(pontos.get(i).getAlarmesup()) ){
            myViewHolder.valor.setTextColor(Color.parseColor("#f90000"));
        } else {
            myViewHolder.valor.setTextColor(Color.parseColor("#00E116"));
        }

    }

    @Override
    public int getItemCount() {
        return pontos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome;
        TextView descricao;
        TextView valor;
        TextView alarmeinf;
        TextView alarmesup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textPonto);
            descricao = itemView.findViewById(R.id.textDescricao);
            valor = itemView.findViewById(R.id.textValor);
            alarmeinf = itemView.findViewById(R.id.textAlarmeInf);
            alarmesup = itemView.findViewById(R.id.textAlarmeSup);

        }
    }

}
