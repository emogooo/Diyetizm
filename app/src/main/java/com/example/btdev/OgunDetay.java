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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OgunDetay extends AppCompatActivity {

    private TextView yemekAdi;
    private float x1, x2;
    private ArrayList<Yemek> yemekler = new ArrayList<Yemek>();
    private ArrayList<Yemek> secilenYemekler;
    private FirebaseDatabase db;
    private DatabaseReference dRef;
    private ListView yemekListesi;
    private YemekAdaptor yemekAdaptor;
    private String ePosta, tarih;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_detay_activity);

        init();
        tarihBul();
        oncekiActivitydenBilgiAl();
        veriAl();

        yemekListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buyukFotografaGec(position);
            }
        });

    }

    private void init() {
        yemekAdi = findViewById(R.id.textViewDetayYemekAdi);
        yemekListesi = findViewById(R.id.listViewYemekListesi);
    }

    private void tarihBul() {
        calendar = Calendar.getInstance();
        tarih = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        db = FirebaseDatabase.getInstance();
    }

    private void oncekiActivitydenBilgiAl() {
        Intent i = getIntent();
        Ogun ogun = i.getParcelableExtra("yemek");
        ePosta = i.getStringExtra("email");
        yemekAdi.setText(ogun.isim);
    }

    private void bilgiYaz() {
        secilenYemekler = new ArrayList<Yemek>();
        yemekSec();
        if (secilenYemekler.size() != 0) {
            yemekAdaptor = new YemekAdaptor(OgunDetay.this, secilenYemekler);
            yemekListesi.setAdapter(yemekAdaptor);
        } else {
            Toast.makeText(this, "Yemek eklemek için sağa kaydırın!", Toast.LENGTH_LONG).show();
        }

    }

    private void buyukFotografaGec(int position) {
        Intent i = new Intent(OgunDetay.this, BuyukFotograf.class);
        i.putExtra("foto", new Fotograf(secilenYemekler.get(position).getResimUrl()));
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void yemekSec() {
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getTarih().equalsIgnoreCase(tarih) && yemekler.get(i).getKullanici().equalsIgnoreCase(ePosta) && yemekler.get(i).getOgunTuru().equalsIgnoreCase(yemekAdi.getText().toString())) {
                secilenYemekler.add(yemekler.get(i));
            }
        }
    }

    private void veriAl() {
        dRef = FirebaseDatabase.getInstance().getReference().child("YemekBilgisi");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yemekler = new ArrayList<>();

                for (DataSnapshot dS : snapshot.getChildren()) {
                    yemekler.add(dS.getValue(Yemek.class));
                }

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
        Intent i = new Intent(OgunDetay.this, GunlukKaloriActivity.class);
        startActivity(i);
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
                    finish();
                }
                if (x1 > x2) {
                    Intent i = new Intent(OgunDetay.this, YemekKayit.class);
                    i.putExtra("ogunTuru", yemekAdi.getText().toString());
                    i.putExtra("eposta", ePosta);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
        }
        return false;
    }
}
