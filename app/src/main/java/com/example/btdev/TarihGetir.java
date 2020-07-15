package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TarihGetir extends AppCompatActivity {

    private float x1,x2;
    private String tarih,ePosta;
    private TextView tvTarih,tvNetKal,tvAlKal,tvVerKal,tvPro,tvYag,tvKar;
    private ListView lvTarihYemek,lvTarihSpor;
    private ArrayList<Yemek> yemekler = new ArrayList<>();
    private ArrayList<Spor> sporlar = new ArrayList<>();
    private ArrayList<Yemek> secilenYemekler;
    private ArrayList<Spor> secilenSporlar;
    private DatabaseReference dRef;
    private FirebaseAuth auth;
    private FirebaseUser kullanici;
    private int netKal,alKal,verKal,p,y,k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarih_getir_activity);

        init();
        erisimSagla();
        oncekiActivitydenBilgiAl();
        veriAl();

        lvTarihYemek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buyukResmeGec(position);
            }
        });
    }

    private void init(){
        tvTarih=findViewById(R.id.textViewTarihYiyilenler);
        lvTarihYemek = findViewById(R.id.listViewTarihliYemekListesi);
        lvTarihSpor = findViewById(R.id.listViewTarihliSporListesi);
        tvNetKal = findViewById(R.id.textViewTarihNetKalori);
        tvAlKal = findViewById(R.id.textViewTarihAlinanKalori);
        tvVerKal = findViewById(R.id.textViewTarihVerilenKalori);
        tvPro = findViewById(R.id.textViewTarihProtein);
        tvYag = findViewById(R.id.textViewTarihYag);
        tvKar = findViewById(R.id.textViewTarihKarbonhidrat);
    }

    private void erisimSagla(){
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
        ePosta = kullanici.getEmail();
    }

    private void oncekiActivitydenBilgiAl(){
        Intent i = getIntent();
        tarih = i.getStringExtra("tarih");
        tvTarih.setText(tarih+" Kayıt Geçmişi");
    }

    private void buyukResmeGec(int position){
        Intent i = new Intent(TarihGetir.this,BuyukFotograf.class);
        i.putExtra("foto",new Fotograf(secilenYemekler.get(position).getResimUrl()));
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void bilgiYaz(){
        TarihYemekAdaptor tarihYemekAdaptor = new TarihYemekAdaptor(TarihGetir.this, secilenYemekler);
        lvTarihYemek.setAdapter(tarihYemekAdaptor);

        TarihSporAdaptor tarihSporAdaptor = new TarihSporAdaptor(TarihGetir.this, secilenSporlar);
        lvTarihSpor.setAdapter(tarihSporAdaptor);

        tvNetKal.setText(netKal + " kcal");
        tvAlKal.setText("Alınan Kalori: "+alKal+" kcal");
        tvVerKal.setText("Verilen Kalori: "+verKal+" kcal");
        tvPro.setText("P: "+p+" g");
        tvYag.setText("Y: "+y+" g");
        tvKar.setText("K: "+k+" g");
    }

    private void veriAl() {
        dRef = FirebaseDatabase.getInstance().getReference().child("YemekBilgisi");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dS : snapshot.getChildren()) {
                    yemekler.add(dS.getValue(Yemek.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dRef = FirebaseDatabase.getInstance().getReference().child("SporBilgisi");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dS : snapshot.getChildren()) {
                    sporlar.add(dS.getValue(Spor.class));
                }

                yemekSec();
                sporSec();
                bilgiYaz();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void yemekSec() {
        secilenYemekler=new ArrayList<>();
        alKal=0;
        p=0;
        y=0;
        k=0;
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getTarih().equalsIgnoreCase(tarih) && yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta) && yemekler.get(i).getOgunTuru().equalsIgnoreCase("KAHVALTI")) {
                secilenYemekler.add(yemekler.get(i));
                alKal+=yemekler.get(i).getKalori();
                p+=yemekler.get(i).getProtein();
                y+=yemekler.get(i).getYag();
                k+=yemekler.get(i).getKarbonhidrat();
            }
        }
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getTarih().equalsIgnoreCase(tarih) && yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta) && yemekler.get(i).getOgunTuru().equalsIgnoreCase("ÖĞLE YEMEĞİ")) {
                secilenYemekler.add(yemekler.get(i));
                alKal+=yemekler.get(i).getKalori();
                p+=yemekler.get(i).getProtein();
                y+=yemekler.get(i).getYag();
                k+=yemekler.get(i).getKarbonhidrat();
            }
        }
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getTarih().equalsIgnoreCase(tarih) && yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta) && yemekler.get(i).getOgunTuru().equalsIgnoreCase("AKŞAM YEMEĞİ")) {
                secilenYemekler.add(yemekler.get(i));
                alKal+=yemekler.get(i).getKalori();
                p+=yemekler.get(i).getProtein();
                y+=yemekler.get(i).getYag();
                k+=yemekler.get(i).getKarbonhidrat();
            }
        }
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getTarih().equalsIgnoreCase(tarih) && yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta) && yemekler.get(i).getOgunTuru().equalsIgnoreCase("ATIŞTIRMALIK")) {
                secilenYemekler.add(yemekler.get(i));
                alKal+=yemekler.get(i).getKalori();
                p+=yemekler.get(i).getProtein();
                y+=yemekler.get(i).getYag();
                k+=yemekler.get(i).getKarbonhidrat();
            }
        }
    }

    private void sporSec() {
        netKal=0;
        verKal=0;
        secilenSporlar = new ArrayList<>();
        for (int i = 0; i < sporlar.size(); i++) {
            if (sporlar.get(i).getTarih().equalsIgnoreCase(tarih) && sporlar.get(i).getKullanici().equalsIgnoreCase(ePosta)) {
                verKal+=sporlar.get(i).getYakilanKalori();
                secilenSporlar.add(sporlar.get(i));
            }
        }
        netKal = alKal-verKal;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                if (x1 < x2) {
                    Intent i = new Intent(TarihGetir.this, ToplamKaloriActivity.class);
                    startActivity(i);
                    finish();
                }
        }
        return false;
    }
}
