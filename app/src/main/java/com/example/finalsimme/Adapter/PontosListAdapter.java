package com.example.finalsimme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.R;

import java.util.ArrayList;
import java.util.List;


public class PontosListAdapter extends ArrayAdapter<Pontos> {

    private final List<Pontos> list;

    public static ArrayList<String> pontosSelecionados = new ArrayList<>();

    public PontosListAdapter(Context context, int resource, List<Pontos> list) {
        super(context, resource, list);
        this.list = list;
    }

    static class ViewHolder {
        //protected TextView nomePonto;
        protected CheckBox pontoCheckBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_pontos, null);
            viewHolder = new ViewHolder();
            viewHolder.pontoCheckBox = convertView.findViewById(R.id.pontoCheckbox);

            pontosSelecionados.clear();

            viewHolder.pontoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    list.get(getPosition).setSelected(buttonView.isChecked());

                    if(buttonView.isChecked()){
                        pontosSelecionados.add(String.valueOf(list.get(getPosition).getcPontoID()));
                    } else{
                        if(pontosSelecionados.contains(String.valueOf(list.get(getPosition).getcPontoID()))){
                            pontosSelecionados.remove(String.valueOf(list.get(getPosition).getcPontoID()));
                        }
                    }

                }
            });


            convertView.setTag(viewHolder);
            //convertView.setTag(R.id.itemTextPonto, viewHolder.nomePonto);
            convertView.setTag(R.id.pontoCheckbox, viewHolder.pontoCheckBox);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.pontoCheckBox.setTag(position);
        //viewHolder.nomePonto.setText(list.get(position).getDescr());
        viewHolder.pontoCheckBox.setText(list.get(position).getDescr());
        viewHolder.pontoCheckBox.setChecked(list.get(position).isSelected());

        return convertView;
    }
}
