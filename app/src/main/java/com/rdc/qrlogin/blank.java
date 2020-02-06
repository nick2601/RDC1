package com.rdc.qrlogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class blank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(blank.this , MainActivity.class);
        finish();
        startActivity(i);

    }
}

