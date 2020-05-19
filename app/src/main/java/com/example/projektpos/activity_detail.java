package com.example.posprojekt;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class activity_detail extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation != Configuration.ORIENTATION_PORTRAIT)
        {
            finish();
            return;
        }
        handleIntent();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleIntent() {
        Intent intent = getIntent();
        if(intent == null) return;
        detailFragment detailFragment = (detailFragment) getSupportFragmentManager().findFragmentById(R.id.fragright);
        int pos = intent.getIntExtra("pos", -1);//Position
        String item = intent.getStringExtra("item");//Abteilung
        detailFragment.showInformation(pos,item);

    }


}
