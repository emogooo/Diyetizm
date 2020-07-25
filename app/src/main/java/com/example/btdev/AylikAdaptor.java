package com.example.btdev;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AylikAdaptor extends BaseAdapter {

    private ArrayList<AylikBilgi> aylikBilgiler;
    private Context con;
    private TextView textViewTarih, textViewNetKalori, textViewAlinanKalori, textViewVerilenKalori, textViewProtein, textViewYag, textViewKarbonhidrat;

    public AylikAdaptor(ArrayList<AylikBilgi> aylikBilgiler, Context con) {
        this.aylikBilgiler = aylikBilgiler;
        this.con = con;
    }

    @Override
    public int getCount() {
        return aylikBilgiler.size();
    }

    @Override
    public Object getItem(int position) {
        return aylikBilgiler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View aylikView = inf.inflate(R.layout.list_view_aylik, parent, false);

        textViewTarih = aylikView.findViewById(R.id.textViewListAylikTarih);
        textViewNetKalori = aylikView.findViewById(R.id.textViewListAylikNetKalori);
        textViewAlinanKalori = aylikView.findViewById(R.id.textViewListAylikAlinanKalori);
        textViewVerilenKalori = aylikView.findViewById(R.id.textViewListAylikVerilenKalori);
        textViewProtein = aylikView.findViewById(R.id.textViewListAylikProtein);
        textViewYag = aylikView.findViewById(R.id.textViewListAylikYag);
        textViewKarbonhidrat = aylikView.findViewById(R.id.textViewListAylikKarbonhidrat);

        textViewTarih.setText(aylikBilgiler.get(position).getTarih() + " - " + aylikBilgiler.get(position).getAy());
        textViewNetKalori.setText(aylikBilgiler.get(position).getNetKalori() + " kcal");
        textViewAlinanKalori.setText(aylikBilgiler.get(position).getAlinanKalori() + " kcal");
        textViewVerilenKalori.setText(aylikBilgiler.get(position).getVerilenKalori() + " kcal");
        textViewProtein.setText(aylikBilgiler.get(position).getProtein() + " g");
        textViewYag.setText(aylikBilgiler.get(position).getYag() + " g");
        textViewKarbonhidrat.setText(aylikBilgiler.get(position).getKarbonhidrat() + " g");

        if (position % 2 == 1) {
            aylikView.setBackgroundColor(Color.argb(15,0,0,255));
        }

        return aylikView;
    }
}
