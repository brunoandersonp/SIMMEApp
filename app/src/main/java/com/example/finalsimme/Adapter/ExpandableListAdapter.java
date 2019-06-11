package com.example.finalsimme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.finalsimme.Model.Instalacoes;
import com.example.finalsimme.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> tipoInstalacao;
    private HashMap<String, ArrayList<Instalacoes>> nomeInstalacao;

    public ExpandableListAdapter(Context context, List<String> tipoInstalacao, HashMap<String, ArrayList<Instalacoes>> nomeInstalacao) {
        this.context = context;
        this.tipoInstalacao = tipoInstalacao;
        this.nomeInstalacao = nomeInstalacao;
    }

    @Override
    public int getGroupCount() {
        return this.tipoInstalacao.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.nomeInstalacao.get(this.tipoInstalacao.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.tipoInstalacao.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.nomeInstalacao.get(this.tipoInstalacao.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView textTipoInstalacao = (TextView) convertView.findViewById(R.id.tipoInstalacao);

        textTipoInstalacao.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Instalacoes childText = (Instalacoes) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView textNomeInstalacao = (TextView) convertView.findViewById(R.id.nomeInstalacao);

        textNomeInstalacao.setText(childText.nome);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
