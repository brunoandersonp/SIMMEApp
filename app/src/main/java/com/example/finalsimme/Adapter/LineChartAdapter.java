package com.example.finalsimme.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finalsimme.Model.Pontos;
import com.example.finalsimme.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LineChartAdapter extends ArrayAdapter<LineData> {

    ArrayList<Pontos> pontosEscolhidos;
    ViewHolder holder;
    int position;

    public LineChartAdapter(Context context, List<LineData> objects, ArrayList<Pontos> pontosEscolhidos) {
        super(context, 0, objects);
        this.pontosEscolhidos = pontosEscolhidos;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LineData data = getItem(position);

        if (convertView == null) {
            holder = new LineChartAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_graficos, null);
            holder.grafico = convertView.findViewById(R.id.lineChart);
            holder.descrPonto = convertView.findViewById(R.id.descrPonto);
            convertView.setTag(holder);
        } else {
            holder = (LineChartAdapter.ViewHolder) convertView.getTag();
        }

        holder.descrPonto.setText(pontosEscolhidos.get(position).getDescr());

        if (data != null) {
            data.setValueTextColor(Color.BLACK);
        }

        holder.grafico.getDescription().setEnabled(false);
        holder.grafico.setDrawGridBackground(false);
        holder.grafico.setTouchEnabled(true);
        holder.grafico.setDragEnabled(true);
        holder.grafico.setScaleEnabled(true);
        holder.grafico.setPinchZoom(true);

        holder.grafico.setVisibleXRangeMaximum(6);

        XAxis xl = holder.grafico.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss", new Locale("PT","BR"));

            @Override
            public String getFormattedValue(float value) {

                DateTime dataPonto = new DateTime(pontosEscolhidos.get(position).getDatahora());
                long inMilliseconds = dataPonto.getMillis();
                return mFormat.format(new Date(inMilliseconds));
            }
        });

        YAxis leftAxis = holder.grafico.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = holder.grafico.getAxisRight();
        rightAxis.setEnabled(false);

        holder.grafico.setData(data);

        adicionarEntry(position);

        //holder.grafico.notifyDataSetChanged();

        return convertView;
    }

    private void adicionarEntry(int position){

        LineData data = holder.grafico.getData();

        if(data != null){
            ILineDataSet set = data.getDataSetByIndex(position);

            if(set == null){
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), Float.valueOf(pontosEscolhidos.get(position).getValorglobal())), position);

            data.notifyDataChanged();
            holder.grafico.notifyDataSetChanged();
            holder.grafico.invalidate();
            holder.grafico.moveViewToX(data.getEntryCount());
        }

//        if(data == null){
//            data = new LineData();
//            holder.grafico.setData(data);
//        }
//
//        ILineDataSet set = data.getDataSetByIndex(position);
//
//        if(set == null){
//            set = createSet();
//            data.addDataSet(set);
//        }
//
//        data.addEntry(new Entry(set.getEntryCount(), Float.valueOf(pontosEscolhidos.get(position).getValorglobal())), position);
//
//        holder.grafico.notifyDataSetChanged();
//        holder.grafico.invalidate();
//        holder.grafico.moveViewToX(data.getEntryCount());
    }

    private LineDataSet createSet(){

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.CYAN);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;

    }

    private class ViewHolder {
        LineChart grafico;
        TextView descrPonto;
    }
}