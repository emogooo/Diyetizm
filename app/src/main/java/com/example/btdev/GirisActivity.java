package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisActivity extends AppCompatActivity {

    private Button btnGiris;
    private FirebaseUser kullanici;
    private EditText eTePosta, eTSifre;
    private FirebaseAuth auth;
    private TextView btnKayit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris_activity);

        init();
        erisimSagla();
        kullaniciKontrol();

        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GirisActivity.this, KayitActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap();
            }
        });
    }

    private void init() {
        btnKayit = findViewById(R.id.textViewKayitOl);
        btnGiris = findViewById(R.id.buttonGiris);
        eTePosta = findViewById(R.id.editTextKA);
        eTSifre = findViewById(R.id.editTextSifreGiris);
    }

    private void erisimSagla() {
        auth = FirebaseAuth.getInstance();
        kullanici = auth.getCurrentUser();
    }

    private void kullaniciKontrol() {
        if (kullanici != null) {
            Intent i = new Intent(GirisActivity.this, GunlukKaloriActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    }

    private void girisYap() {
        String email = eTePosta.getText().toString();
        String sifre = eTSifre.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(sifre)) {
            Toast.makeText(this, "E-Posta veya Şifre alanı boş bırakılamaz!", Toast.LENGTH_LONG).show();
        } else {
            btnGiris.setEnabled(false);
            auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(GirisActivity.this, GunlukKaloriActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        Toast.makeText(GirisActivity.this, "Kullanıcı adı veya parola yanlış!", Toast.LENGTH_LONG).show();
                        btnGiris.setEnabled(true);
                    }
                }
            });
        }
    }

}
