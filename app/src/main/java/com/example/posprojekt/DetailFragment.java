package com.example.posprojekt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;






public class DetailFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView txt1; //Name
    private TextView txt2; //Guthaben
    private Spinner spinner; //Getraenke
    private TextView txt3; //Email
    private TextView txt4; //Telnr
    private EditText smstxt;

    Button getraenkhinzufuegen;
    Button zurueck;
    Button loeschen;
    Button guthabenhinzufuegen;
    Button sms;

    int position;
    Getraenk getraenk;

    DatabaseReference myPersonenRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initializeViews(view);
        getraenkhinzufuegen.setOnClickListener(this);
        zurueck.setOnClickListener(this);
        loeschen.setOnClickListener(this);
        guthabenhinzufuegen.setOnClickListener(this);
        sms.setOnClickListener(this);
        myPersonenRef = FirebaseDatabase.getInstance().getReference("Personen");

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
        sms = view.findViewById(R.id.sms);
        smstxt = view.findViewById(R.id.smsText);


    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showInformation(int pos, String item) {

        txt1.setText(MainActivity.personen.get(pos).vorname + " " + MainActivity.personen.get(pos).nachname);
        txt2.setText(String.valueOf(MainActivity.personen.get(pos).guthaben) + " €");
        txt3.setText(MainActivity.personen.get(pos).emailAdresse);
        txt4.setText(String.valueOf(MainActivity.personen.get(pos).telefonNr));
        this.position = pos;
        spinner.setOnItemSelectedListener(this);
        spinner.setPrompt("Wähle dein Getränk");


    }


    @Override
    public void onClick(final View v) {

        int id = v.getId();
        double gut = 0;
        switch (id) {
            case R.id.getraenkhinzufuegen:
                //Spinner auswerten, Getränk hinzufügen und Geld abziehen


                if (MainActivity.admin == true) {

                    if (MainActivity.personen.get(position).guthaben > 0) {
                        double preis = getraenk.preis;
                        String name = getraenk.name;

                        int anzahlGetreankeVorher = 0;

                        Getraenk getraenk1 = null;

                        for (int i = 0; i < MainActivity.getraenke.size(); i++) {

                            if (MainActivity.getraenke.get(i).getName().equals(name)) {
                                int anzVorher = MainActivity.getraenke.get(i).getAnzahl();
                                anzahlGetreankeVorher = anzVorher;
                                MainActivity.getraenke.get(i).setAnzahl(anzVorher += 1);
                                getraenk1 = MainActivity.getraenke.get(i);
                            }
                        }

                        Person persVorher = MainActivity.personen.get(position);

                        final double endpreis = MainActivity.personen.get(position).guthaben - preis;
                        txt2.setText(MainActivity.personen.get(position).guthaben - preis + " €");
                        String vorNach = MainActivity.personen.get(position).vorundnachname();
                        MainActivity.personen.get(position).guthaben -= preis;
                        MainActivity.items.remove(position);

                        Person person = MainActivity.personen.get(position);


                        MainActivity.personen.remove(position);
                        MainActivity.personen.add(person);

                        MainActivity.personCounter.remove(position);

                        int posi = 0;

                        for (int i = 0; i < MainActivity.einleseAnzahlList.size(); i++) {

                            if(MainActivity.einleseAnzahlList.get(i).vorundnachname().equals(persVorher.vorundnachname()))
                            {
                                posi = i;
                                MainActivity.einleseAnzahlList.get(i).guthaben = person.getGuthaben();

                            }


                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Personen").child(posi+1+"");

                        int posit = 0;

                        for (int i = 0; i < MainActivity.einlesenAnzahlList.size(); i++) {

                            if(MainActivity.einlesenAnzahlList.get(i).getName().equals(name))
                            {
                                posit = i;
                                MainActivity.einlesenAnzahlList.get(i).anzahl = anzahlGetreankeVorher;
                            }

                        }

                        DatabaseReference referenceGetraenke = FirebaseDatabase.getInstance().getReference().child("Getraenke").child(posit+1+"");

                        try {
                            referenceGetraenke.setValue(getraenk1);
                            reference.setValue(person);

                        } catch (Exception ex) {

                        }
                    }

                } else {
                    Toast.makeText(v.getContext(), "Sie haben keine Berechtigung um ein Getränk hinzuzufügen", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.cancel:

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                break;


            case R.id.loeschen:
                if (MainActivity.admin == true) {

                    Person persVorher = MainActivity.personen.get(position);
                    Person person = MainActivity.personen.get(position);
                    MainActivity.personen.remove(position);
                    MainActivity.items.remove(position);
                    person.setGruppenName("DeletedPersons");
                    MainActivity.personCounter.remove(position);

                    Toast.makeText(getContext(), "Geloescht", Toast.LENGTH_SHORT).show();



                    int posi = 0;

                    for (int i = 0; i < MainActivity.einleseAnzahlList.size(); i++) {

                        if(MainActivity.einleseAnzahlList.get(i).vorundnachname().equals(persVorher.vorundnachname()))
                        {
                            posi = i;
                            MainActivity.einleseAnzahlList.get(i).gruppenName = person.getGruppenName();

                        }


                    }



                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Personen").child(posi+1+"");

                    try {

                        reference.setValue(person);


                    } catch (Exception ex) {

                    }


                    Intent intent3 = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent3);



                } else {
                    Toast.makeText(v.getContext(), "Sie haben keine Berechtigung um " + MainActivity.personen.get(position).nachname + " zu löschen", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.guthabenerweitern:

                if (MainActivity.admin == true) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle("Guthaben erweitern");
                    final View view = getLayoutInflater().inflate(R.layout.guthabenerweitern, null);
                    alert.setView(view);
                    alert.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText guthaben = view.findViewById(R.id.guthabenerweiternBetrag);

                                    try {
                                        Person persVorher = MainActivity.personen.get(position);
                                        double guthabenerweitern = Double.parseDouble(guthaben.getText().toString());
                                        double gut = MainActivity.personen.get(position).guthaben + guthabenerweitern;

                                        Person person = MainActivity.personen.get(position);

                                        person.guthaben = gut;
                                        MainActivity.items.remove(position);
                                        MainActivity.personen.remove(position);
                                        MainActivity.personen.add(person);


                                        MainActivity.personCounter.remove(position);
                                        MainActivity.personCounter.add(person);


                                        position = MainActivity.personen.indexOf(person);

                                        txt2.setText(gut + " €");


                                        int posi = 0;

                                        for (int i = 0; i < MainActivity.einleseAnzahlList.size(); i++) {

                                            if(MainActivity.einleseAnzahlList.get(i).vorundnachname().equals(persVorher.vorundnachname()))
                                            {
                                                posi = i;
                                                MainActivity.einleseAnzahlList.get(i).guthaben = person.getGuthaben();

                                            }


                                        }



                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Personen").child(posi+1+"");

                                        try {

                                            reference.setValue(person);


                                        } catch (Exception ex) {

                                        }


                                        Toast.makeText(v.getContext(), "Neues Guthaben: " + gut, Toast.LENGTH_SHORT).show();
                                    } catch (Exception ex) {
                                        Toast.makeText(v.getContext(), "Fehler beim hinzufügen des Guthaben", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                    alert.setNegativeButton("Zurück", null);
                    alert.show();
                } else {
                }

                break;

            case R.id.sms:

                AlertDialog.Builder alert2 = new AlertDialog.Builder(v.getContext());
                alert2.setTitle("SMS");
                final View view = getLayoutInflater().inflate(R.layout.smssendenlayout, null);
                alert2.setView(view);
                alert2.setPositiveButton("Senden", new DialogInterface.OnClickListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String smstext = "";
                                smstxt = view.findViewById(R.id.smsText);
                                try {
                                    smstext = smstxt.getText().toString();
                                } catch (Exception ex) {
                                    Toast.makeText(v.getContext(), "Fehler beim Formatieren", Toast.LENGTH_SHORT).show();
                                }
                                SmsManager sms = SmsManager.getDefault();


                                smstext += "\n \n"
                                        +"Latitude: "+MainActivity.lat
                                        +"\n Longitude: "+MainActivity.lon;




                                String telnr = MainActivity.personen.get(position).telefonNr;
                                try {
                                    sms.sendTextMessage(String.valueOf(telnr), null, smstext, null, null);
                                    Toast.makeText(getContext(), "SMS mit Standort wurde gesendet", Toast.LENGTH_SHORT).show();

                                } catch (Exception ex) {
                                    Toast.makeText(getContext(), "Fehler beim Senden der SMS", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
                alert2.setNegativeButton("Zurück", null);
                alert2.show();
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





