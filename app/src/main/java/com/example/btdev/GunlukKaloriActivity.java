package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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

public class GunlukKaloriActivity extends AppCompatActivity {

    private float x1, x2;
    private FirebaseAuth auth;
    private FirebaseUser kullanici;
    private Toolbar acBar;
    private int vKal = 0, aKal = 0, pro = 0, kar = 0, yag = 0;
    private ArrayList<Yemek> yemekler = new ArrayList<Yemek>();
    private ArrayList<Spor> sporlar = new ArrayList<Spor>();
    private DatabaseReference dRef;
    private String ePosta, tarih;
    private Calendar calendar;
    private TextView tVNKal, tVVKal, tVAKal, tVPro, tVYag, tVKar;
    private GridView liste;
    private ArrayList<Ogun> ogunler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gunluk_kalori_activity);

        init();
        erisimSagla();
        tarihBul();
        ogunleriOlustur();

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                secilenOguneGit(position);
            }
        });

        veriAl();
    }

    private void init() {
        tVNKal = findViewById(R.id.textViewNetKalori);
        tVAKal = findViewById(R.id.textViewAlinanKalori);
        tVVKal = findViewById(R.id.textViewVerilenKalori);
        tVPro = findViewById(R.id.textViewProtein);
        tVYag = findViewById(R.id.textViewYag);
        tVKar = findViewById(R.id.textViewKarbonhidrat);
        liste = findViewById(R.id.gridOgun);

        acBar = findViewById(R.id.actionbarLogin60);
        setSupportActionBar(acBar);
        getSupportActionBar().setTitle("");
    }

    private void erisimSagla() {
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
        ePosta = kullanici.getEmail();

        if (kullanici == null) {
            Intent i = new Intent(GunlukKaloriActivity.this, GirisActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void tarihBul() {
        calendar = Calendar.getInstance();
        tarih = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
    }

    private void ogunleriOlustur() {
        ogunler = new ArrayList<Ogun>();
        ogunler.add(new Ogun(R.drawable.kahvalt1, "KAHVALTI"));
        ogunler.add(new Ogun(R.drawable.ogle_yemegi, "ÖĞLE YEMEĞİ"));
        ogunler.add(new Ogun(R.drawable.aksam_yemegi, "AKŞAM YEMEĞİ"));
        ogunler.add(new Ogun(R.drawable.atistirmalik, "ATIŞTIRMALIK"));

        OgunAdaptor adaptor = new OgunAdaptor(this, ogunler);
        liste.setAdapter(adaptor);
    }

    private void secilenOguneGit(int position) {
        Intent i = new Intent(getApplicationContext(), OgunDetay.class);
        i.putExtra("yemek", ogunler.get(position));
        i.putExtra("email", ePosta);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void yemekBilgisiYaz() {
        tVAKal.setText(aKal + " kcal");
        tVPro.setText("Protein: " + pro + " g");
        tVYag.setText("Yag: " + yag + " g");
        tVKar.setText("Karbonhidrat: " + kar + " g");
    }

    private void sporBilgisiYaz() {
        tVNKal.setText("Net Kalori: " + (aKal - vKal) + " kcal");
        tVVKal.setText(vKal + " kcal");
    }

    private void yemekSec() {
        aKal = 0;
        pro = 0;
        yag = 0;
        kar = 0;
        for (int i = 0; i < yemekler.size(); i++) {
            if (yemekler.get(i).getTarih().equalsIgnoreCase(tarih)) {
                aKal += yemekler.get(i).getKalori();
                pro += yemekler.get(i).getProtein();
                yag += yemekler.get(i).getYag();
                kar += yemekler.get(i).getKarbonhidrat();
            }
        }
    }

    private void sporSec() {
        vKal = 0;
        for (int i = 0; i < sporlar.size(); i++) {
            if (sporlar.get(i).getTarih().equalsIgnoreCase(tarih) && sporlar.get(i).getKullanici().equalsIgnoreCase(ePosta)) {
                vKal += sporlar.get(i).getYakilanKalori();
            }
        }
    }

    private void veriAl() {
        dRef = FirebaseDatabase.getInstance().getReference().child("YemekBilgisi");
        dRef.orderByChild("kullanici").equalTo(ePosta).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dS : snapshot.getChildren()) {
                    yemekler.add(dS.getValue(Yemek.class));
                }

                yemekSec();
                yemekBilgisiYaz();
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

                sporSec();
                sporBilgisiYaz();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                if (x1 < x2) {
                    Intent i = new Intent(GunlukKaloriActivity.this, SporActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (x1 > x2) {
                    Intent i = new Intent(GunlukKaloriActivity.this, HaftalikKaloriActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.menuCikisYap) {
            auth.signOut();
            kullanici = null;
            Intent i = new Intent(GunlukKaloriActivity.this, GirisActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (item.getItemId() == R.id.menuProfil) {
            Intent i = new Intent(GunlukKaloriActivity.this, ProfilPaneli.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        return true;
    }
}
