package com.example.btdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class SporKayitActivity extends AppCompatActivity {

    private EditText eTAd, eTSaatlikYakilanKalori, eTYapilanSure;
    private Button btEkle;
    private Calendar calendar;
    private String tarih, kullanici;
    private FirebaseDatabase db;
    private Spor s;
    private float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spor_kayit_activity);

        init();
        tarihBul();
        erisimSagla();
        oncekiActivitydenBilgiAl();

        btEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sporEkle();
            }
        });
    }

    private void init() {
        eTAd = findViewById(R.id.editTextSporAd);
        eTSaatlikYakilanKalori = findViewById(R.id.editTextSporSaatlikYakilanKalori);
        eTYapilanSure = findViewById(R.id.editTextSporYapilmaSuresi);
        btEkle = findViewById(R.id.buttonSporKaydet);
    }

    private void tarihBul() {
        calendar = Calendar.getInstance();
        tarih = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
    }

    private void erisimSagla() {
        db = FirebaseDatabase.getInstance();
    }

    private void oncekiActivitydenBilgiAl() {
        Intent i = getIntent();
        kullanici = i.getStringExtra("email");
    }

    private void sporEkle() {
        String ad, saatlikKaloriYakma, yapilanSure;
        ad = eTAd.getText().toString();
        saatlikKaloriYakma = eTSaatlikYakilanKalori.getText().toString();
        yapilanSure = eTYapilanSure.getText().toString();

        if (TextUtils.isEmpty(ad) || TextUtils.isEmpty(saatlikKaloriYakma) || TextUtils.isEmpty(yapilanSure)) {
            Toast.makeText(this, "Tüm alanları doldurunuz!", Toast.LENGTH_LONG).show();
        } else {
            btEkle.setEnabled(false);
            s = new Spor(ad, kullanici, tarih, Integer.parseInt(saatlikKaloriYakma), Integer.parseInt(yapilanSure));
            sporDBEkle();
            btEkle.setEnabled(true);
            Toast.makeText(this, "Spor eklendi!", Toast.LENGTH_SHORT).show();
        }
    }


    private void sporDBEkle() {
        DatabaseReference dbRef = db.getReference("SporBilgisi");
        String key = dbRef.push().getKey();
        DatabaseReference dbRef2 = db.getReference("SporBilgisi/" + key);
        dbRef2.setValue(s);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                if (x1 > x2) {
                    Intent i = new Intent(SporKayitActivity.this, SporActivity.class);
                    startActivity(i);
                    finish();
                }
        }
        return false;
    }
}
