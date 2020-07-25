package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfilPaneli extends AppCompatActivity {

    float x1, x2;
    private ArrayList<KullaniciBilgisi> kullanicilar = new ArrayList<KullaniciBilgisi>();
    private FirebaseAuth auth;
    private FirebaseUser kullanici;
    private DatabaseReference dRef;
    private String ePosta;
    private KullaniciBilgisi k;
    private TextView ad, soyad, kilo, boy, eposta, dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_paneli_activity);

        init();
        erisimSagla();
        veriAl();
    }

    private void init() {
        ad = findViewById(R.id.textViewProfilAd2);
        soyad = findViewById(R.id.textViewProfilSoyad2);
        kilo = findViewById(R.id.textViewProfilKilo2);
        boy = findViewById(R.id.textViewProfilBoy2);
        eposta = findViewById(R.id.textViewProfilEmail2);
        dt = findViewById(R.id.textViewProfilDT2);
    }

    private void erisimSagla() {
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
        ePosta = kullanici.getEmail();
    }

    private void kullaniciSec() {
        for (int i = 0; i < kullanicilar.size(); i++) {
            if (kullanicilar.get(i).getEmail().equalsIgnoreCase(ePosta)) {
                k = kullanicilar.get(i);
                return;
            }
        }
    }

    private void bilgiYaz() {
        ad.setText(k.getIsim());
        soyad.setText(k.getSoyisim());
        kilo.setText(k.getKilo() + " kg");
        boy.setText(k.getBoy() + " cm");
        eposta.setText(k.getEmail());
        dt.setText(k.getDogumTarihi());
    }

    private void veriAl() {
        dRef = FirebaseDatabase.getInstance().getReference().child("KullaniciBilgisi");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dS : snapshot.getChildren()) {
                    kullanicilar.add(dS.getValue(KullaniciBilgisi.class));
                }

                kullaniciSec();
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
                    Intent i = new Intent(ProfilPaneli.this, GunlukKaloriActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
        }
        return false;
    }
}
