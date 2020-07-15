package com.example.btdev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SporAdaptor extends BaseAdapter {
    private Context con;
    private ArrayList<Spor> sporlar;
    TextView ad, kacDkYapildigi, yakilanKalori;

    public SporAdaptor(Context con, ArrayList<Spor> sporlar) {
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
        View sporlarView = inf.inflate(R.layout.list_view_spor, parent, false);

        ad = sporlarView.findViewById(R.id.textViewSporAdiDetay2);
        kacDkYapildigi = sporlarView.findViewById(R.id.textViewYapilanDakikaDetay2);
        yakilanKalori = sporlarView.findViewById(R.id.textViewYakilanKaloriDetay2);

        ad.setText(sporlar.get(position).getIsim());
        kacDkYapildigi.setText(sporlar.get(position).getYapilanDakika() + " dk");
        yakilanKalori.setText(sporlar.get(position).getYakilanKalori() + " kcal");

        return sporlarView;
    }
}
