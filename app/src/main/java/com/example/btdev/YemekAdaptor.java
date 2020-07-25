package com.example.btdev;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class YemekAdaptor extends BaseAdapter {

    Context con;
    ArrayList<Yemek> yemekler = new ArrayList<Yemek>();

    public YemekAdaptor(Context con, ArrayList<Yemek> yemekler) {
        this.con = con;
        this.yemekler = yemekler;
    }

    @Override
    public int getCount() {
        return yemekler.size();
    }

    @Override
    public Object getItem(int position) {
        return yemekler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View yemeklerView = inf.inflate(R.layout.list_view_yemek, parent, false);

        TextView isim = yemeklerView.findViewById(R.id.textViewYemekAdiDetay2);
        TextView kalori = yemeklerView.findViewById(R.id.textViewYemekKalorisiDetay2);
        TextView protein = yemeklerView.findViewById(R.id.textViewYemekProteinDetay2);
        TextView yag = yemeklerView.findViewById(R.id.textViewYemekYagDetay2);
        TextView karbonhidrat = yemeklerView.findViewById(R.id.textViewYemekKarbonhidratDetay2);
        ImageView img = yemeklerView.findViewById(R.id.imageViewAslaAnnemeElBombasiAtmamDiyenler1Yazsin);

        isim.setText(yemekler.get(position).getIsim());
        kalori.setText(yemekler.get(position).getKalori() +" kcal");
        protein.setText("P: "+yemekler.get(position).getProtein() + " g");
        yag.setText("Y: "+yemekler.get(position).getYag() + " g");
        karbonhidrat.setText("K: "+yemekler.get(position).getKarbonhidrat() + " g");
        Picasso.get().load(yemekler.get(position).getResimUrl()).fit().centerCrop().into(img);

        if (position % 2 == 1) {
            yemeklerView.setBackgroundColor(Color.argb(15,0,0,255));
        }

        return yemeklerView;
    }
}
