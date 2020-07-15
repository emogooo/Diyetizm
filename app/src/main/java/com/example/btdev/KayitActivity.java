package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class KayitActivity extends AppCompatActivity {

    private TextView tVDogumTarihi;
    private DatePickerDialog.OnDateSetListener dPDogumTarihi;
    private Button uyeOl;
    private EditText eTAd, eTSoyad, eTEPosta, eTParola, eTBoy, eTKilo;
    private FirebaseAuth auth;
    float x1, x2;
    private KullaniciBilgisi k;
    private FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kayit_activity);

        init();
        erisimSagla();

        tVDogumTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarihSec();
            }
        });

        dPDogumTarihi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tarihiDuzenle(year,month,dayOfMonth);
            }
        };

        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hesapOlustur();
            }
        });

    }

    private void init() {
        eTAd = findViewById(R.id.editTextAd);
        eTSoyad = findViewById(R.id.editTextSoyad);
        eTEPosta = findViewById(R.id.editTextMail);
        eTParola = findViewById(R.id.editTextSifre);
        eTBoy = findViewById(R.id.editTextBoy);
        eTKilo = findViewById(R.id.editTextKilo);
        tVDogumTarihi = findViewById(R.id.textViewDogumTarihiSec);
        uyeOl = findViewById(R.id.buttonUyeOl);
    }

    private void erisimSagla() {
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void tarihSec(){
        Calendar cal = Calendar.getInstance();
        int yil = cal.get(Calendar.YEAR);
        int ay = cal.get(Calendar.MONTH);
        int gun = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(KayitActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dPDogumTarihi, yil, ay, gun);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void tarihiDuzenle(int year, int month, int dayOfMonth) {
        String ay = ++month + "", gun = dayOfMonth + "";
        if (month < 10) {
            ay = "0" + month;
        }
        if (dayOfMonth < 10) {
            gun = "0" + dayOfMonth;
        }
        tVDogumTarihi.setText(gun + "." + ay + "." + year);
    }

    private void bilgileriEkle() {
        DatabaseReference dbRef = db.getReference("KullaniciBilgisi");
        String key = dbRef.push().getKey();
        DatabaseReference dbRef2 = db.getReference("KullaniciBilgisi/" + key);
        dbRef2.setValue(k);
    }

    private void hesapOlustur() {
        String ad, soyad, email, sifre, boy, kilo, dogumTarihi;
        ad = eTAd.getText().toString();
        soyad = eTSoyad.getText().toString();
        email = eTEPosta.getText().toString();
        sifre = eTParola.getText().toString();
        boy = eTBoy.getText().toString();
        kilo = eTKilo.getText().toString();
        dogumTarihi = tVDogumTarihi.getText().toString();

        if (TextUtils.isEmpty(ad) || TextUtils.isEmpty(soyad) || TextUtils.isEmpty(email) || TextUtils.isEmpty(sifre) || TextUtils.isEmpty(boy) || TextUtils.isEmpty(kilo)) {
            Toast.makeText(this, "Tüm alanları doldurunuz!", Toast.LENGTH_LONG).show();
        } else {
            uyeOl.setEnabled(false);
            k = new KullaniciBilgisi(email, sifre, ad, soyad, dogumTarihi, Integer.parseInt(boy), Integer.parseInt(kilo));
            auth.createUserWithEmailAndPassword(k.getEmail(), k.getParola()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            bilgileriEkle();
                            Intent i = new Intent(KayitActivity.this, GirisActivity.class);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            Toast.makeText(KayitActivity.this, "HATA!", Toast.LENGTH_LONG).show();
                            uyeOl.setEnabled(true);
                        }
                    }
            });
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
                    Intent i = new Intent(KayitActivity.this, GirisActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
        }
        return false;
    }
}
