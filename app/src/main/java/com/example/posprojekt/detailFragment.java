package com.example.posprojekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class detailFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView txt1; //Name
    private TextView txt2; //Guthaben
    private Spinner spinner; //Getraenke
    private TextView txt3; //Email
    private TextView txt4; //Telnr

    Button getraenkhinzufuegen;
    Button zurueck;
    Button loeschen;
    Button guthabenhinzufuegen;

    int position;
    Getraenk getraenk;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        initializeViews(view);
        getraenkhinzufuegen.setOnClickListener(this);
        zurueck.setOnClickListener(this);
        loeschen.setOnClickListener(this);
        guthabenhinzufuegen.setOnClickListener(this);
        return view;

    }

    private void initializeViews(View view) {
        txt1 = view.findViewById(R.id.textViewName);
        txt2 = view.findViewById(R.id.textViewGuthaben);
        spinner = view.findViewById(R.id.spinnergetraenke);
        getraenkhinzufuegen = view.findViewById(R.id.getraenkhinzufuegen);
        zurueck = view.findViewById(R.id.cancel);
        loeschen = view.findViewById(R.id.loeschen);
        txt3 = view.findViewById(R.id.textViewEmail);
        txt4 = view.findViewById(R.id.textViewTelefonnr);
        guthabenhinzufuegen = view.findViewById(R.id.guthabenerweitern);
        spinner.setAdapter(new ArrayAdapter<Getraenk>(view.getContext(), android.R.layout.simple_list_item_1, MainActivity.getraenke));

    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showInformation(int pos, String item) {

        txt1.setText(MainActivity.personen.get(pos).vorname+" "+MainActivity.personen.get(pos).nachname);
        txt2.setText(String.valueOf(MainActivity.personen.get(pos).guthaben) + " €");
        txt3.setText(MainActivity.personen.get(pos).emailAdresse);
        txt4.setText(String.valueOf(MainActivity.personen.get(pos).telefonNr));
        this.position = pos;
        spinner.setOnItemSelectedListener(this);

        if(MainActivity.personen.get(pos).guthaben < 5)
        {
            txt2.setBackgroundColor(Color.RED);
        }

    }


    @Override
    public void onClick(final View v) {

        int id = v.getId();

        switch (id)
        {
            case R.id.getraenkhinzufuegen:
                    //Spinner auswerten, Getränk hinzufügen und Geld abziehen

                if(MainActivity.personen.get(position).guthaben > 0) {
                    double preis = getraenk.preis;
                    String name = getraenk.name;
                    txt2.setText(MainActivity.personen.get(position).guthaben - preis + " €");
                    String vorNach = MainActivity.personen.get(position).vorundnachname();
                    MainActivity.personen.get(position).guthaben -= preis;
                    MainActivity.items.remove(position);

                    Person person = MainActivity.personen.get(position);

                    MainActivity.personen.remove(position);
                    MainActivity.personen.add(person);
                    MainActivity.items.add(person.toString());

                    position = MainActivity.personen.indexOf(person);


                    if (MainActivity.personen.get(position).guthaben <= 5) {
                        Toast.makeText(v.getContext(), "Achtung! Es sind noch " + MainActivity.personen.get(position).guthaben + "€ verfügbar.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(v.getContext(), "Achtung! Kein Guthaben", Toast.LENGTH_SHORT).show();
                    txt2.setBackgroundColor(Color.RED);
                }

                break;

            case R.id.cancel:

                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
                break;

            case R.id.loeschen:

                MainActivity.personen.remove(position);
                MainActivity.items.remove(position);


                Intent intent3 = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent3);

                break;

            case R.id.guthabenerweitern:

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Guthaben erweitern");
                final View view = getLayoutInflater().inflate(R.layout.guthabenerweitern,null);
                alert.setView(view);
                alert.setPositiveButton("Hinzufügen",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       EditText guthaben = view.findViewById(R.id.guthabenerweiternBetrag);

                       try {
                           double guthabenerweitern = Double.parseDouble(guthaben.getText().toString());

                           double gut = MainActivity.personen.get(position).guthaben + guthabenerweitern;


                           Person person = MainActivity.personen.get(position);
                           person.guthaben = gut;
                           MainActivity.items.remove(position);
                           MainActivity.personen.remove(position);
                           MainActivity.personen.add(person);
                           MainActivity.items.add(person.toString());

                           position = MainActivity.personen.indexOf(person);

                          txt2.setText(gut + " €");

                           Toast.makeText(v.getContext(), "Neues Guthaben: "+gut,Toast.LENGTH_SHORT).show();
                       }
                       catch (Exception ex)
                       {
                            Toast.makeText(v.getContext(),"Fehler",Toast.LENGTH_SHORT).show();

                       }



                    }}
                );
                alert.setNegativeButton("Zurück",null);
                alert.show();

                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        getraenk = MainActivity.getraenke.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}