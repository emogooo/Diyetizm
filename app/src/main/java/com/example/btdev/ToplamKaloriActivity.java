package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ToplamKaloriActivity extends AppCompatActivity {

    private float x1, x2;
    private TextView tvbaslik, tvalinantoplamkalori, tvverilentoplamkalori, tvpro, tvyag, tvkar, tvortalkal, tvortverkal, tvortpro, tvortyag, tvortkar;
    private ImageView aramaButonu;
    private DatabaseReference dRef;
    private ArrayList<Yemek> yemekler = new ArrayList<Yemek>();
    private ArrayList<Spor> sporlar = new ArrayList<Spor>();
    private ArrayList<Yemek> secilenYemekler;
    private ArrayList<Spor> secilenSporlar;
    private FirebaseAuth auth;
    private FirebaseUser kullanici;
    private String ePosta, tarih;
    private int alKal, p, y, k, verKal, netKal, ortAlKal, ortVerKal, ortPro, ortKar, ortYag, yemekTarihSayaci, sporTarihSayaci;
    private ArrayList<String> yemekTarihleri;
    private ArrayList<String> sporTarihleri;
    private boolean kontrol;
    private DatePickerDialog.OnDateSetListener dPTarih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toplam_kalori_activity);

        init();
        erisimSagla();
        veriAl();

        aramaButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarihSec();
            }
        });

        dPTarih = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tarihDuzenle(year, month, dayOfMonth);
            }
        };
    }

    private void init() {
        tvbaslik = findViewById(R.id.textViewToplamNetKalori);
        tvalinantoplamkalori = findViewById(R.id.textViewToplamAlinanKalori);
        tvverilentoplamkalori = findViewById(R.id.textViewToplamVerilenKalori);
        tvpro = findViewById(R.id.textViewToplamProtein);
        tvyag = findViewById(R.id.textViewToplamYag);
        tvkar = findViewById(R.id.textViewToplamKarbonhidrat);
        tvortalkal = findViewById(R.id.textViewOrtalamaAlinanKalori);
        tvortverkal = findViewById(R.id.textViewOrtalamaVerilenKalori);
        tvortpro = findViewById(R.id.textViewOrtalamaProtein);
        tvortyag = findViewById(R.id.textViewOrtalamaYag);
        tvortkar = findViewById(R.id.textViewOrtalamaKarbonhidrat);
        aramaButonu = findViewById(R.id.imageAramaFotografi);
    }

    private void tarihSec() {
        Calendar cal = Calendar.getInstance();
        int yil = cal.get(Calendar.YEAR);
        int ay = cal.get(Calendar.MONTH);
        int gun = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(ToplamKaloriActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dPTarih, yil, ay, gun);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void tarihDuzenle(int year, int month, int dayOfMonth) {
        String ay = ++month + "", gun = dayOfMonth + "";
        if (month < 10) {
            ay = "0" + month;
        }
        if (dayOfMonth < 10) {
            gun = "0" + dayOfMonth;
        }
        tarih = gun + "." + ay + "." + year;

        Intent i = new Intent(ToplamKaloriActivity.this, TarihGetir.class);
        i.putExtra("tarih", tarih);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void erisimSagla() {
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
        ePosta = kullanici.getEmail();
    }

    private void toplamKaloriHesapla() {
        alKal = 0;
        p = 0;
        y = 0;
        k = 0;
        verKal = 0;
        netKal = 0;
        secilenYemekler = new ArrayList<Yemek>();
        secilenSporlar = new ArrayList<Spor>();
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta)) {
                secilenYemekler.add(yemekler.get(i));
                alKal += yemekler.get(i).getKalori();
                p += yemekler.get(i).getProtein();
                y += yemekler.get(i).getYag();
                k += yemekler.get(i).getKarbonhidrat();
            }
        }
        for (int i = 0; i < sporlar.size(); i++) {
            if (sporlar.get(i).getKullanici().equalsIgnoreCase(ePosta)) {
                secilenSporlar.add(sporlar.get(i));
                verKal += sporlar.get(i).getYakilanKalori();
            }
        }
        netKal = alKal - verKal;
    }

    private void toplamGunHesapla() {
        yemekTarihSayaci = 0;
        sporTarihSayaci = 0;
        yemekTarihleri = new ArrayList<String>();
        sporTarihleri = new ArrayList<String>();

        for (int i = 0; i < secilenYemekler.size(); i++) {
            kontrol = true;
            for (int j = 0; j < yemekTarihleri.size(); j++) {
                if (secilenYemekler.get(i).getTarih().equalsIgnoreCase(yemekTarihleri.get(j))) {
                    kontrol = false;
                    break;
                }
            }
            if (kontrol) {
                yemekTarihSayaci++;
                yemekTarihleri.add(secilenYemekler.get(i).getTarih());
            }
        }

        for (int i = 0; i < secilenSporlar.size(); i++) {
            kontrol = true;
            for (int j = 0; j < sporTarihleri.size(); j++) {
                if (secilenSporlar.get(i).getTarih().equalsIgnoreCase(sporTarihleri.get(j))) {
                    kontrol = false;
                    break;
                }
            }
            if (kontrol) {
                sporTarihSayaci++;
                sporTarihleri.add(secilenSporlar.get(i).getTarih());
            }
        }
    }

    private void ortalamaDegerleriHesapla() {
        if (yemekTarihSayaci != 0) {
            ortAlKal = alKal / yemekTarihSayaci;
            ortYag = y / yemekTarihSayaci;
            ortPro = p / yemekTarihSayaci;
            ortKar = k / yemekTarihSayaci;
        }
        if (sporTarihSayaci !=0){
            ortVerKal = verKal / sporTarihSayaci;
        }
    }

    private void bilgiYaz() {
        tvbaslik.setText("Toplam Kalori Değişimi: " + netKal + " kcal");
        tvalinantoplamkalori.setText(alKal + " kcal");
        tvverilentoplamkalori.setText(verKal + " kcal");
        tvpro.setText("Protein: " + p + " g");
        tvyag.setText("Yag: " + y + " g");
        tvkar.setText("Karbonhidrat: " + k + " g");
        tvortalkal.setText("Ortalama Alınan Kalori (Günlük): " + ortAlKal + " kcal");
        tvortverkal.setText("Ortalama Verilen Kalori (Günlük): " + ortVerKal + " kcal");
        tvortpro.setText("Ortalama Alınan Protein (Günlük): " + ortPro + " g");
        tvortyag.setText("Ortalama Alınan Yag (Günlük): " + ortYag + " g");
        tvortkar.setText("Ortalama Alınan Karbonhidrat (Günlük): " + ortKar + " g");
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
                toplamKaloriHesapla();
                toplamGunHesapla();
                ortalamaDegerleriHesapla();
                bilgiYaz();
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
                    Intent i = new Intent(ToplamKaloriActivity.this, AylikKaloriActivity.class);
                    startActivity(i);
                    finish();
                }
        }
        return false;
    }
}
