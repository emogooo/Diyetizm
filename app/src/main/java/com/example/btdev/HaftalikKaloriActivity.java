package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TextView;

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

public class HaftalikKaloriActivity extends AppCompatActivity {

    private float x1, x2;
    private TextView baslik;
    private ListView haftalikListe;
    private DatabaseReference dRef;
    private ArrayList<Yemek> yemekler = new ArrayList<Yemek>();
    private ArrayList<Spor> sporlar = new ArrayList<Spor>();
    private ArrayList<HaftalikBilgi> haftalikBilgiler;
    private String ePosta, tarih, gun;
    private FirebaseAuth auth;
    private FirebaseUser kullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.haftalik_kalori_activity);

        init();
        erisimSagla();
        veriAl();
    }

    private void init() {
        baslik = findViewById(R.id.textViewHaftalikKaloriGecmisi);
        haftalikListe = findViewById(R.id.listViewHaftalikListe);
    }

    private void erisimSagla() {
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
        ePosta = kullanici.getEmail();
    }

    private void bilgileriYazdir() {
        haftalikBilgiler = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            tarihHesapla(i);
            haftalikBilgiOlustur();
        }
        HaftalikAdaptor haftalikAdaptor = new HaftalikAdaptor(haftalikBilgiler, HaftalikKaloriActivity.this);
        haftalikListe.setAdapter(haftalikAdaptor);
    }


    private void haftalikBilgiOlustur() {
        int alkal = 0, p = 0, y = 0, k = 0, verkal = 0, netkal = 0;
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getTarih().equalsIgnoreCase(tarih) && yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta)) {
                alkal += yemekler.get(i).getKalori();
                p += yemekler.get(i).getProtein();
                y += yemekler.get(i).getYag();
                k += yemekler.get(i).getKarbonhidrat();
            }
        }
        for (int i = 0; i < sporlar.size(); i++) {
            if (sporlar.get(i).getTarih().equalsIgnoreCase(tarih) && sporlar.get(i).getKullanici().equalsIgnoreCase(ePosta)) {
                verkal += sporlar.get(i).getYakilanKalori();
            }
        }
        netkal = alkal - verkal;
        haftalikBilgiler.add(new HaftalikBilgi(tarih, gun, netkal, alkal, verkal, p, y, k));

    }

    private void tarihHesapla(int kacGunOnce) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -kacGunOnce);
        tarih = dateFormat.format(cal.getTime());
        int bugun = cal.get(Calendar.DAY_OF_WEEK);

        if (bugun == 1) {
            gun = "Pazar";
        } else if (bugun == 2) {
            gun = "Pazartesi";
        } else if (bugun == 3) {
            gun = "Salı";
        } else if (bugun == 4) {
            gun = "Carsamba";
        } else if (bugun == 5) {
            gun = "Persembe";
        } else if (bugun == 6) {
            gun = "Cuma";
        } else if (bugun == 7) {
            gun = "Cumartesi";
        }
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
                    Intent i = new Intent(HaftalikKaloriActivity.this, GunlukKaloriActivity.class);
                    startActivity(i);
                    finish();
                } else if (x1 > x2) {
                    Intent i = new Intent(HaftalikKaloriActivity.this, AylikKaloriActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
        }
        return false;
    }
}
