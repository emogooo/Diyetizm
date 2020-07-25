package com.example.btdev;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TarihSporAdaptor extends BaseAdapter {
    Context con;
    ArrayList<Spor> sporlar;

    public TarihSporAdaptor(Context con, ArrayList<Spor> sporlar) {
        this.con = con;
        this.sporlar = sporlar;
    }

    @Override
    public int getCount() {
        return sporlar.size();
    }

    @Override
    public Object getItem(int position) {
        return sporlar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sporlarView = inf.inflate(R.layout.list_view_tarih_spor, parent, false);

        TextView isim = sporlarView.findViewById(R.id.textViewSporAdiDetayTarih);
        TextView kacDk = sporlarView.findViewById(R.id.textViewYapilanDakikaDetayTarih);
        TextView yakilanKal = sporlarView.findViewById(R.id.textViewYakilanKaloriDetayTarih);

        isim.setText(sporlar.get(position).getIsim());
        kacDk.setText(sporlar.get(position).getYapilanDakika() + " dk");
        yakilanKal.setText(sporlar.get(position).getYakilanKalori() + " kcal");

        if (position % 2 == 1) {
            sporlarView.setBackgroundColor(Color.argb(15,0,0,255));
        }

        return sporlarView;
    }
}
