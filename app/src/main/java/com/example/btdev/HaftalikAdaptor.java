package com.example.btdev;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HaftalikAdaptor extends BaseAdapter {

    private ArrayList<HaftalikBilgi> haftalikBilgiler;
    private Context con;
    private TextView textViewTarih, textViewNetKalori, textViewAlinanKalori, textViewVerilenKalori, textViewProtein, textViewYag, textViewKarbonhidrat;

    public HaftalikAdaptor(ArrayList<HaftalikBilgi> haftalikBilgiler, Context con) {
        this.haftalikBilgiler = haftalikBilgiler;
        this.con = con;
    }

    @Override
    public int getCount() {
        return haftalikBilgiler.size();
    }

    @Override
    public Object getItem(int position) {
        return haftalikBilgiler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View haftalikView = inf.inflate(R.layout.list_view_haftalik, parent, false);

        textViewTarih = haftalikView.findViewById(R.id.textViewListHaftalikTarih);
        textViewNetKalori = haftalikView.findViewById(R.id.textViewListHaftalikNetKalori);
        textViewAlinanKalori = haftalikView.findViewById(R.id.textViewListHaftalikAlinanKalori);
        textViewVerilenKalori = haftalikView.findViewById(R.id.textViewListHaftalikVerilenKalori);
        textViewProtein = haftalikView.findViewById(R.id.textViewListHaftalikProtein);
        textViewYag = haftalikView.findViewById(R.id.textViewListHaftalikYag);
        textViewKarbonhidrat = haftalikView.findViewById(R.id.textViewListHaftalikKarbonhidrat);

        textViewTarih.setText(haftalikBilgiler.get(position).getTarih() + " - " + haftalikBilgiler.get(position).getGun());
        textViewNetKalori.setText(haftalikBilgiler.get(position).getNetKalori() + " kcal");
        textViewAlinanKalori.setText(haftalikBilgiler.get(position).getAlinanKalori() + " kcal");
        textViewVerilenKalori.setText(haftalikBilgiler.get(position).getVerilenKalori() + " kcal");
        textViewProtein.setText(haftalikBilgiler.get(position).getProtein() + " g");
        textViewYag.setText(haftalikBilgiler.get(position).getYag() + " g");
        textViewKarbonhidrat.setText(haftalikBilgiler.get(position).getKarbonhidrat() + " g");

        if (position % 2 == 1) {
            haftalikView.setBackgroundColor(Color.argb(15,0,0,255));
        }

        return haftalikView;
    }
}
