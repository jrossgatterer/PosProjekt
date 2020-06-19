package com.example.posprojekt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.security.Provider;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.NETWORK_STATS_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class detailFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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

    protected LocationManager locationManager;
    Location location;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initializeViews(view);
        getraenkhinzufuegen.setOnClickListener(this);
        zurueck.setOnClickListener(this);
        loeschen.setOnClickListener(this);
        guthabenhinzufuegen.setOnClickListener(this);
        sms.setOnClickListener(this);
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

        if (MainActivity.personen.get(pos).guthaben < 5) {
            txt2.setBackgroundColor(Color.RED);
        }

    }


    @Override
    public void onClick(final View v) {

        int id = v.getId();

        switch (id) {
            case R.id.getraenkhinzufuegen:
                //Spinner auswerten, Getränk hinzufügen und Geld abziehen


                if (MainActivity.admin == true) {

                    if (MainActivity.personen.get(position).guthaben > 0) {
                        double preis = getraenk.preis;
                        String name = getraenk.name;

                        for (int i = 0; i < MainActivity.getraenke.size(); i++) {

                            if(MainActivity.getraenke.get(i).getName().equals(name))
                            {
                                int anzVorher = MainActivity.getraenke.get(i).getAnzahl();
                                MainActivity.getraenke.get(i).setAnzahl(anzVorher+=1);
                            }


                        }

                        final double endpreis = MainActivity.personen.get(position).guthaben - preis;
                        txt2.setText(MainActivity.personen.get(position).guthaben - preis + " €");
                        String vorNach = MainActivity.personen.get(position).vorundnachname();
                        MainActivity.personen.get(position).guthaben -= preis;
                        MainActivity.items.remove(position);


                        Person person = MainActivity.personen.get(position);
                        MainActivity.personen.remove(position);
                        MainActivity.personen.add(person);
                        MainActivity.items.add(person.toString());

                        position = MainActivity.personen.indexOf(person);

                        MainActivity.myPersonenRef.setValue(person);


                        if (MainActivity.personen.get(position).guthaben <= 5) {
                            Toast.makeText(v.getContext(), "Achtung! Es sind noch " + MainActivity.personen.get(position).guthaben + "€ verfügbar.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Achtung! Kein Guthaben", Toast.LENGTH_SHORT).show();
                        txt2.setBackgroundColor(Color.RED);
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

                    MainActivity.personen.remove(position);
                    MainActivity.items.remove(position);

                    MainActivity.myPersonenRef.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Toast.makeText(getContext(), "Geloescht",Toast.LENGTH_SHORT).show();
                        }
                    });

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
                                        double guthabenerweitern = Double.parseDouble(guthaben.getText().toString());
                                        double gut = MainActivity.personen.get(position).guthaben + guthabenerweitern;

                                        Person person = MainActivity.personen.get(position);

                                        person.guthaben = gut;
                                        MainActivity.items.remove(position);
                                        MainActivity.personen.remove(position);
                                        MainActivity.personen.add(person);

                                        position = MainActivity.personen.indexOf(person);

                                        MainActivity.myPersonenRef.setValue(person);

                                        txt2.setText(gut+" €");

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

                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                                try {
                                    List<Address> list = geocoder.getFromLocation(MainActivity.lat, MainActivity.lon,1);
                                    Toast.makeText(getContext(),list.get(0).getLocality(),Toast.LENGTH_SHORT).show();
                                    smstext+= "\n"+list.get(0).getLocality();

                                } catch (IOException e) {
                                    Toast.makeText(getContext(), "Fehler beim finden des Standorts", Toast.LENGTH_SHORT).show();
                                }

                                long telnr = MainActivity.personen.get(position).telefonNr;
                                try {
                                    sms.sendTextMessage(String.valueOf(telnr),null, smstext, null,null);
                                    Toast.makeText(getContext(),"SMS mit Standort wurde gesendet",Toast.LENGTH_SHORT).show();
                                }
                                catch(Exception ex)
                                {
                                    Toast.makeText(getContext(),"Fehler beim Senden der SMS",Toast.LENGTH_SHORT).show();
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