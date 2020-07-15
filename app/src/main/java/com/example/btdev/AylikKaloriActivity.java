package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AylikKaloriActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser kullanici;
    private float x1, x2;
    private String ePosta, ayIsmi, tarih, ay, yil;
    private int aySayi, yilSayi;
    private ListView aylikListe;
    private DatabaseReference dRef;
    private ArrayList<Yemek> yemekler = new ArrayList<Yemek>();
    private ArrayList<Spor> sporlar = new ArrayList<Spor>();
    private ArrayList<AylikBilgi> aylikBilgiler;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aylik_kalori_activity);

        erisimSagla();
        tarihBul();
        init();
        veriAl();
    }

    private void bilgileriYazdir(){
        aylikBilgiler = new ArrayList<AylikBilgi>();
        for (int i = 0; i < 12; i++) {
            tarihHesapla();
            aylikBilgiOlustur();
        }
        AylikAdaptor aylikAdaptor = new AylikAdaptor(aylikBilgiler, AylikKaloriActivity.this);
        aylikListe.setAdapter(aylikAdaptor);
    }

    private void tarihBul(){
        cal = Calendar.getInstance();
        DateFormat dFAy = new SimpleDateFormat("MM");
        DateFormat dFYil = new SimpleDateFormat("yyyy");
        ay = dFAy.format(cal.getTime());
        yil = dFYil.format(cal.getTime());
        aySayi = Integer.parseInt(ay);
        yilSayi = Integer.parseInt(yil);
    }

    private void erisimSagla(){
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
        ePosta = kullanici.getEmail();
    }

    private void init(){
        aylikListe = findViewById(R.id.listViewAylikListe);
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
                bilgileriYazdir();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void aylikBilgiOlustur() {
        int alkal = 0, p = 0, y = 0, k = 0, verkal = 0, netkal = 0;
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta) && yemekler.get(i).getTarih().substring(3).equalsIgnoreCase(tarih)) {
                alkal += yemekler.get(i).getKalori();
                p += yemekler.get(i).getProtein();
                y += yemekler.get(i).getYag();
                k += yemekler.get(i).getKarbonhidrat();
            }
        }
        for (int i = 0; i < sporlar.size(); i++) {
            if (sporlar.get(i).getKullanici().equalsIgnoreCase(ePosta) && sporlar.get(i).getTarih().substring(3).equalsIgnoreCase(tarih)) {
                verkal += sporlar.get(i).getYakilanKalori();
            }
        }
        netkal = alkal - verkal;
        aylikBilgiler.add(new AylikBilgi((yilSayi+""), ayIsmi, netkal, alkal, verkal, p, y, k));

    }

    private void tarihHesapla() {

        if (aySayi < 11 && aySayi > 1) {
            tarih = "0" + --aySayi + "." + yilSayi;
        } else if (aySayi > 10) {
            tarih = --aySayi + "." + yilSayi;
        } else if (aySayi == 1) {
            aySayi = 12;
            tarih = aySayi + "." + --yilSayi;
        }

        if (aySayi == 1) {
            ayIsmi = "Ocak";
        } else if (aySayi == 2) {
            ayIsmi = "Şubat";
        } else if (aySayi == 3) {
            ayIsmi = "Mart";
        } else if (aySayi == 4) {
            ayIsmi = "Nisan";
        } else if (aySayi == 5) {
            ayIsmi = "Mayıs";
        } else if (aySayi == 6) {
            ayIsmi = "Haziran";
        } else if (aySayi == 7) {
            ayIsmi = "Temmuz";
        } else if (aySayi == 8) {
            ayIsmi = "Ağustos";
        } else if (aySayi == 9) {
            ayIsmi = "Eylül";
        } else if (aySayi == 10) {
            ayIsmi = "Ekim";
        } else if (aySayi == 11) {
            ayIsmi = "Kasım";
        } else if (aySayi == 12) {
            ayIsmi = "Aralık";
        }
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
                    Intent i = new Intent(AylikKaloriActivity.this, HaftalikKaloriActivity.class);
                    startActivity(i);
                    finish();
                } else if (x1 > x2) {
                    Intent i = new Intent(AylikKaloriActivity.this, ToplamKaloriActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
        }
        return false;
    }
}
