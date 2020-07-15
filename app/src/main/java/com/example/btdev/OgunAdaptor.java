package com.example.btdev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class OgunAdaptor extends BaseAdapter {

    Context con;
    ArrayList<Ogun> ogunler;

    public OgunAdaptor(Context con, ArrayList<Ogun> ogunler) {
        this.con = con;
        this.ogunler = ogunler;
    }

    @Override
    public int getCount() {
        return ogunler.size();
    }

    @Override
    public Object getItem(int position) {
        return ogunler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View yemekler = inf.inflate(R.layout.grid_yemek, parent, false);

        ImageView fotograf = yemekler.findViewById(R.id.imageYemekFotografi);
        TextView isim = yemekler.findViewById(R.id.textViewYemekAdi);

        isim.setText(ogunler.get(position).isim);
        fotograf.setImageResource(ogunler.get(position).fotograf);

        return yemekler;
    }
}
