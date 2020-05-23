package com.example.posprojekt;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class detailFragment extends Fragment implements View.OnClickListener {

    private TextView txt1;
    private TextView txt2;
    private Spinner spinner;

    Button getraenkhinzufuegen;
    Button zurueck;
    Button loeschen;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        initializeViews(view);
        getraenkhinzufuegen.setOnClickListener(this);
        zurueck.setOnClickListener(this);
        loeschen.setOnClickListener(this);
        return view;

    }

    private void initializeViews(View view) {
        txt1 = view.findViewById(R.id.textViewName);
        txt2 = view.findViewById(R.id.textViewGuthaben);
        spinner = view.findViewById(R.id.spinnergetraenke);
        getraenkhinzufuegen = view.findViewById(R.id.getraenkhinzufuegen);
        zurueck = view.findViewById(R.id.cancel);
        loeschen = view.findViewById(R.id.loeschen);


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showInformation(int pos, String item) {

        txt1.setText(MainActivity.personen.get(pos).vorname+" "+MainActivity.personen.get(pos).nachname);
        txt2.setText(String.valueOf(MainActivity.personen.get(pos).guthaben));

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id)
        {
            case R.id.getraenkhinzufuegen:

                break;

            case R.id.cancel:

                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
                break;

            case R.id.loeschen:

                //MainActivity.personen.remove();

                Intent intent3 = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent3);

                break;
        }

    }
}