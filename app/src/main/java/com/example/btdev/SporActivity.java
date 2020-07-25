package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SporActivity extends AppCompatActivity {

    private float x1, x2;
    private String ePosta, tarih;
    private ListView sporListesi;
    private TextView yakilanToplamKalori, yapilanToplamSure;
    private Calendar calendar;
    private DatabaseReference dRef;
    private FirebaseAuth auth;
    private FirebaseUser kullanici;
    private ArrayList<Spor> sporlar;
    private ArrayList<Spor> secilenSporlar;
    private SporAdaptor sporAdaptor;
    private int yakilanKalori, toplamSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spor_activity);

        init();
        erisimSagla();
        tarihBul();
        veriAl();

    }

    private void init() {
        sporListesi = findViewById(R.id.listViewSporListesi);
        yakilanToplamKalori = findViewById(R.id.textViewSporYakilanKalori2);
        yapilanToplamSure = findViewById(R.id.textViewSure);
    }

    private void erisimSagla() {
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
        ePosta = kullanici.getEmail();
    }

    private void tarihBul() {
        calendar = Calendar.getInstance();
        tarih = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
    }

    private void bilgiYaz() {
        sporAdaptor = new SporAdaptor(SporActivity.this, secilenSporlar);
        sporListesi.setAdapter(sporAdaptor);
        yakilanToplamKalori.setText(yakilanKalori + " kcal");
        yapilanToplamSure.setText("Toplam Egzersiz Süresi: " + toplamSure + " dk");
    }

    private void veriAl() {
        dRef = FirebaseDatabase.getInstance().getReference().child("SporBilgisi");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sporlar = new ArrayList<>();
                for (DataSnapshot dS : snapshot.getChildren()) {
                    sporlar.add(dS.getValue(Spor.class));
                }
                sporSec();
                if (secilenSporlar.size() != 0) {
                    bilgiYaz();
                }else {
                    Toast.makeText(SporActivity.this, "Yaptığınız egzersizleri eklemek için sağa kaydırın!",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sporSec() {
        secilenSporlar = new ArrayList<>();
        yakilanKalori = 0;
        toplamSure = 0;
        for (int i = 0; i < sporlar.size(); i++) {
            if (sporlar.get(i).getTarih().equalsIgnoreCase(tarih) && sporlar.get(i).getKullanici().equalsIgnoreCase(ePosta)) {
                secilenSporlar.add(sporlar.get(i));
                yakilanKalori += sporlar.get(i).getYakilanKalori();
                toplamSure += sporlar.get(i).getYapilanDakika();
            }
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
                if (x1 > x2) {
                    Intent i = new Intent(SporActivity.this, GunlukKaloriActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (x1 < x2) {
                    Intent i = new Intent(SporActivity.this, SporKayitActivity.class);
                    i.putExtra("email", ePosta);
                    startActivity(i);
                    finish();
                }
        }
        return false;
    }
}
