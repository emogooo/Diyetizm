package com.example.btdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class BuyukFotograf extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyuk_fotograf);

        img = findViewById(R.id.imageViewBuyukFoto);

        Intent i = getIntent();
        Fotograf f = i.getParcelableExtra("foto");

        if (f.getUrl() == null) {
            img.setImageURI(f.getUri());
        } else {
            Picasso.get().load(f.getUrl()).fit().centerCrop().into(img);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
