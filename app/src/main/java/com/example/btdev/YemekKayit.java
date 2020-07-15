package com.example.btdev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class YemekKayit extends AppCompatActivity {

    private EditText eTAd, eTKalori, eTProtein, eTYag, eTKarbonhidrat;
    private Button btEkle, btFotoCek;
    private float x1, x2;
    private FirebaseDatabase db;
    private Yemek y;
    private String ogunTuru, ePosta, tarih, resimUrl;
    private Calendar calendar;
    private Uri uri;
    private ImageView img;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_kayit_activity);

        init();
        tarihBul();
        erisimSagla();
        oncekiActivitydenBilgiAl();

        btEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yemekEkle();
            }
        });

        btFotoCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotografSec();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyukFotografaGec();
            }
        });
    }

    private void init() {
        eTAd = findViewById(R.id.editTextYemekAd);
        eTKalori = findViewById(R.id.editTextYemekKalori);
        eTProtein = findViewById(R.id.editTextYemekProtein);
        eTYag = findViewById(R.id.editTextYemekYag);
        eTKarbonhidrat = findViewById(R.id.editTextYemekKarbonhidrat);
        btEkle = findViewById(R.id.buttonYemekKaydet);
        btFotoCek = findViewById(R.id.buttonFotoCek);
        img = findViewById(R.id.imageViewYemekFotosu);
        img.setEnabled(false);
    }

    private void tarihBul() {
        calendar = Calendar.getInstance();
        tarih = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
    }

    private void erisimSagla() {
        db = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("yuklenenler");
    }

    private void oncekiActivitydenBilgiAl() {
        Intent i = getIntent();
        ogunTuru = i.getStringExtra("ogunTuru");
        ePosta = i.getStringExtra("eposta");
    }

    private void yemekEkle() {
        String ad, kalori, protein, yag, karbonhidrat;
        ad = eTAd.getText().toString();
        kalori = eTKalori.getText().toString();
        protein = eTProtein.getText().toString();
        yag = eTYag.getText().toString();
        karbonhidrat = eTKarbonhidrat.getText().toString();

        if (TextUtils.isEmpty(ad) || TextUtils.isEmpty(kalori) || TextUtils.isEmpty(protein) || TextUtils.isEmpty(yag) || TextUtils.isEmpty(karbonhidrat) || TextUtils.isEmpty(resimUrl)) {
            Toast.makeText(this, "Tüm alanları doldurunuz!", Toast.LENGTH_LONG).show();
        } else {
            btEkle.setEnabled(false);
            y = new Yemek(tarih, ePosta, ad, ogunTuru, Integer.parseInt(kalori), Integer.parseInt(protein), Integer.parseInt(yag), Integer.parseInt(karbonhidrat), resimUrl);
            yemekDBEkle();
            btEkle.setEnabled(true);
            Toast.makeText(this, "Yemek eklendi!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fotografSec() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 71);
    }

    private void buyukFotografaGec() {
        Intent i = new Intent(YemekKayit.this, BuyukFotograf.class);
        i.putExtra("foto", new Fotograf(resimUrl));
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void yemekDBEkle() {
        DatabaseReference dbRef = db.getReference("YemekBilgisi");
        String key = dbRef.push().getKey();
        DatabaseReference dbRef2 = db.getReference("YemekBilgisi/" + key);
        dbRef2.setValue(y);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
        img.setImageURI(uri);
        img.setEnabled(true);

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        resimUrl = uri.toString();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(YemekKayit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

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
                    finish();
                }
        }
        return false;
    }

}
