package com.example.posprojekt;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_detail extends AppCompatActivity {

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
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragright);
        int pos = intent.getIntExtra("pos", -1);
        String item = intent.getStringExtra("item");
        detailFragment.showInformation(pos,item);

    }


}