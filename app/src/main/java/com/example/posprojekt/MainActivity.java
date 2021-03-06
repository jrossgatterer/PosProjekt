package com.example.posprojekt;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements OnSelectionChangedListener, View.OnClickListener {

    private static final int MY_Permission = 0;
    private static final int RQ_PREFERENCES = 1;
    static ArrayList<Person> personen = new ArrayList<>();//Personen der Liste
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Getraenk> getraenke = new ArrayList<>();
    static Person person = null;
    static Getraenk getraenk = null;
    static Gruppe neueGruppe = null;
    static boolean personVorhanden;
    static boolean getraenkVorhanden;
    static boolean gruppeVorhanden;
    static long zaehlerPerson;
    long zaehlerGruppe;
    long zaehlerGetraenke;
    private DetailFragment detailFragment;
    private boolean showdetail;
    static int id = 0;

    static List<Person> einleseAnzahlList = new ArrayList<>();
    static List<Getraenk> einlesenAnzahlList = new ArrayList<>();

    static List<Person> personCounter = new ArrayList<>();
    static List<Getraenk> getaenkCounter = new ArrayList<>();

    static String email;//login -> durch diese kann jeder zugeordnet werden
    static String passwort;//login
    static String gruppe = null;
    static boolean admin = false;

    static List<User> users = new ArrayList<>();//Personen zum Anmelden
    static List<Gruppe> gruppen = new ArrayList<>();

    static String gruppenpasswort;
    View backgroundLayout;
    View fragbackgroundLayout;
    static double lat;
    static double lon;
    static String locationName;
    static LocationManager lm;
    Location location;

    static DatabaseReference myPersonenRef;
    DatabaseReference myGruppenRef;
    DatabaseReference myGetraenkeRef;

    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public static final String CHANNEL_1_ID = "channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Notifications
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),10000,pendingIntent); // Wenn die App läuft wird alle 5 Minuten eine Aufforderung als Notification zum Trinken gesendet

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        backgroundLayout = findViewById(R.id.changeColor);

        String storeColor = prefs.getString(getString(R.string.key_color),"#FFFFFF");
        backgroundLayout.setBackgroundColor(Color.parseColor(storeColor));

        myPersonenRef = FirebaseDatabase.getInstance().getReference().child("Personen");
        myPersonenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    zaehlerPerson = (dataSnapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(personen.size() == 0)
        {
            AlertDialog.Builder alert5 = new AlertDialog.Builder(this);

            final View view5 = getLayoutInflater().inflate(R.layout.starttext, null);
            alert5.setView(view5);
            alert5.show();

        }





        myGruppenRef = FirebaseDatabase.getInstance().getReference().child("Gruppen");
        myGruppenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    zaehlerGruppe = (dataSnapshot.getChildrenCount());
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myGetraenkeRef = FirebaseDatabase.getInstance().getReference().child("Getraenke");
        myGetraenkeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    zaehlerGetraenke = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_Permission);

            }

        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_Permission);

            }
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},MY_Permission);

            }
        }

        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragright);
        showdetail = detailFragment != null && detailFragment.isInLayout();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                MainActivity.lon = location.getLongitude();
                MainActivity.lat = location.getLatitude();




            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

                Toast.makeText(getApplicationContext(),"Standort wurde verändert",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });


        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geo.getFromLocation(lat,lon,1);
            if (addresses.isEmpty()) {
                Toast.makeText(this,"Auf Standort warten",Toast.LENGTH_SHORT).show();
            }
            else {
                MainActivity.locationName = addresses.get(0).getLocality();
                MainActivity.locationName +=  "  "+addresses.get(0).getCountryCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults)
    {
        switch(requestCode)
        {
            case MY_Permission:
            {
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Zugang gewährt",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSelectionChanged(int pos, String item) {
        if(showdetail) detailFragment.showInformation(pos,item);
        else callDetailActivity(pos,item);
    }

    private void callDetailActivity(int pos, String item) {
        Intent intent = new Intent(this, Activity_detail.class);
        intent.putExtra("pos",pos);
        intent.putExtra("item",item);
        Log.d("MainActicity",item);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Rechts oben 3 Punkte

        int id = item.getItemId();
        final int sizebefore = personen.size();
        switch (id) {
            case R.id.menu_newPerson:
                if (admin == true) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Neue Person");
                    final View view = getLayoutInflater().inflate(R.layout.newperson, null);
                    alert.setView(view);
                    alert.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText vorname = view.findViewById(R.id.vorname);
                                    EditText nachname = view.findViewById(R.id.nachname);
                                    EditText guthanen = view.findViewById(R.id.guthaben);
                                    EditText email = view.findViewById(R.id.email);
                                    EditText telnr = view.findViewById(R.id.number);

                                    try {
                                        String vorn = vorname.getText().toString();
                                        String nach = nachname.getText().toString();
                                        double guthab = Double.parseDouble(guthanen.getText().toString());
                                        String emai = email.getText().toString();
                                        String teln =  telnr.getText().toString();
                                        Person x = new Person(vorn, nach, guthab, emai, teln, gruppe);
                                        items.add(x.toString());
                                        personen.add(x);

                                        MainActivity.person = x;
                                        MasterFragment.adapter.notifyDataSetChanged();
                                        MasterFragment.listView.setAdapter(MasterFragment.adapter);
                                        MasterFragment.adapter.notifyDataSetChanged();

                                        if(MainActivity.personVorhanden==true)
                                        {
                                            writeFirstPersonen();
                                        }
                                        else
                                        {
                                            writePersonen();
                                        }

                                        personCounter.add(x);
                                        Log.d("Items:",String.valueOf(items.size()));

                                    } catch (Exception io) {
                                        io.printStackTrace();
                                        Log.d("MainActivity", "Failed to parse" + "" + io);
                                    }

                                }


                            }
                    );
                    alert.setNegativeButton("Zurück", null);
                    alert.show();
                } else {
                    Toast.makeText(this, "Sie haben keine Berechtigungen um ein Getränk hinzuzufügen", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.menu_statistiken:


                AlertDialog.Builder alert3 = new AlertDialog.Builder(this);
                alert3.setTitle("Statistik");
                final View view3 = getLayoutInflater().inflate(R.layout.statistiken, null);
                alert3.setView(view3);

                ListView listView = view3.findViewById(R.id.listview_statistiken);

                List<String> listStatistik = new ArrayList<>();

                for (int i = 0; i < getraenke.size(); i++) {

                    listStatistik.add(getraenke.get(i).GetraenkeNameundAnzahl());

                }

                ArrayAdapter<String> adapter;
                adapter =
                        new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item_1,
                                listStatistik
                        );
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                alert3.setNegativeButton("Okay", null);
                alert3.show();
                break;


            case R.id.menu_newGetraenk:
                if (admin == true) {
                    AlertDialog.Builder alert5 = new AlertDialog.Builder(this);
                    alert5.setTitle("Neues Getränk");
                    final View view5 = getLayoutInflater().inflate(R.layout.getraenke_hinzufuegen, null);
                    alert5.setView(view5);
                    alert5.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText getraenkename = view5.findViewById(R.id.getraenke_name);
                                    EditText getraenkepreis = view5.findViewById(R.id.getraenke_preis);
                                    String name = getraenkename.getText().toString();

                                    try {
                                        String getrPreis = getraenkepreis.getText().toString();
                                        String[] arr = getrPreis.split("");

                                        for (int i = 0; i < arr.length; i++) {

                                            if (arr[i].equals(",")) {
                                                arr[i] = ".";
                                            }

                                        }


                                        getrPreis = "";

                                        for (int i = 0; i < arr.length; i++) {

                                            getrPreis += arr[i];

                                        }

                                        Double preis = Double.parseDouble(getrPreis);
                                        MainActivity.getraenke.add(new Getraenk(name, preis, gruppe, 0));
                                        getraenk = new Getraenk(name, preis, gruppe);

                                        if(MainActivity.getraenkVorhanden==true) {
                                            writeFirstGetraenke();
                                        }
                                        else
                                        {
                                            writeGetraenke();
                                        }

                                        MainActivity.getaenkCounter.add(getraenk);
                                    } catch (Exception ex) {
                                        Toast.makeText(view5.getContext(), "Hinzufügen Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                    );
                    alert5.setNegativeButton("Zurück", null);
                    alert5.show();

                } else {
                    Toast.makeText(this, "Sie haben keine Berechtigungen um ein Getränk hinzuzufügen", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu_login:

                AlertDialog.Builder alert7 = new AlertDialog.Builder(this);
                alert7.setTitle("Login");
                loadGruppen();
                final View view7 = getLayoutInflater().inflate(R.layout.loginandregistrieren, null);
                alert7.setView(view7);
                alert7.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText user = view7.findViewById(R.id.login_username);

                                try {

                                    MainActivity.gruppe = user.getText().toString();

                                    for (int i = 0; i < gruppen.size(); i++) {

                                        if (gruppen.get(i).guppenName.equals(MainActivity.gruppe)) {
                                            MainActivity.admin = false;
                                            Toast.makeText(view7.getContext(), "Erfolgreiche anmeldung", Toast.LENGTH_SHORT).show();
                                            users.add(new User(email, passwort, gruppe, false));
                                            loadPersonen();
                                            loadGetaenke();
                                        }

                                    }

                                } catch (Exception ex) {
                                    Toast.makeText(view7.getContext(), "Registrieren Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
                alert7.setNegativeButton("Zurück", null);
                alert7.show();
                break;

            case R.id.menu_admin:
                AlertDialog.Builder alert8 = new AlertDialog.Builder(this);
                alert8.setTitle("Admin");
                loadGruppen();
                final View view8 = getLayoutInflater().inflate(R.layout.loginandregistrierenadmin, null);
                alert8.setView(view8);
                alert8.setPositiveButton("Login als Admin", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText grupp = view8.findViewById(R.id.admin_Gruppe);
                                Switch sw = view8.findViewById(R.id.admin_switch);
                                EditText grupppas = view8.findViewById(R.id.admin_grupppasswort);
                                try {

                                    MainActivity.gruppe = grupp.getText().toString();
                                    MainActivity.admin = sw.isChecked();
                                    MainActivity.gruppenpasswort = grupppas.getText().toString();


                                    User us = new User(MainActivity.email, MainActivity.passwort, MainActivity.gruppe, admin);


                                    Gruppe gr = new Gruppe("", "");

                                    for (int i = 0; i < gruppen.size(); i++) {

                                        if (MainActivity.gruppe.equals(gruppen.get(i).guppenName)) {

                                            if (MainActivity.gruppenpasswort.equals(gruppen.get(i).gruppenPasswort)) {
                                                gr = gruppen.get(i);
                                            } else {

                                                Toast.makeText(view8.getContext(), "Falsches Passwort für die Gruppe", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }


                                    if ((gr.guppenName.equals(grupp.getText().toString()) && (gr.gruppenPasswort.equals(grupppas.getText().toString())))) {
                                        loadPersonen();
                                        loadGetaenke();

                                        Toast.makeText(view8.getContext(), "Als Admin angemeldet", Toast.LENGTH_SHORT).show();
                                    } else {
                                        neueGruppe = new Gruppe(MainActivity.gruppe, MainActivity.gruppenpasswort);
                                        if(MainActivity.gruppeVorhanden==true)
                                        {
                                            writeFirstGruppen();
                                        }
                                        else
                                        {
                                            writeGruppen();
                                        }


                                        Toast.makeText(view8.getContext(), "Neue Gruppe wurde erstellt", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (Exception ex) {
                                    Toast.makeText(view8.getContext(), "Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                );
                alert8.setNegativeButton("Zurück", null);
                alert8.show();

                break;

            case R.id.preferences:
                if(admin==true || admin!=true) {
                    finish();
                    Intent intent = new Intent(this, MySettingsActivity.class);
                    startActivityForResult(intent, RQ_PREFERENCES);
                }
                else
                {
                    Toast.makeText(this, "Sie haben keine Berechtigung die Einstellungen zu ändern!",Toast.LENGTH_SHORT).show();
                }
                break;

        }

        return true;
    }

    public void showPrefs(View view)
    {

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
    }


    public void writePersonen()
    {
        myPersonenRef.child(String.valueOf(zaehlerPerson+1)).setValue(person);
    }
    public void writeFirstPersonen()
    {
        myPersonenRef.getRoot().child("Personen").child(String.valueOf(zaehlerPerson+1)).setValue(person);
    }

    public void loadPersonen()
    {
        for (int i = 1; i <= zaehlerPerson; i++) {

            myPersonenRef = FirebaseDatabase.getInstance().getReference().child("Personen").child(String.valueOf(i));
            myPersonenRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String email = dataSnapshot.child("emailAdresse").getValue().toString();
                    Double guthaben = Double.parseDouble(dataSnapshot.child("guthaben").getValue().toString());
                    String vorname = dataSnapshot.child("vorname").getValue().toString();
                    String nachname = dataSnapshot.child("nachname").getValue().toString();
                    String telefonNr = dataSnapshot.child("telefonNr").getValue().toString();
                    String gruppe = dataSnapshot.child("gruppenName").getValue().toString();




                    if(MainActivity.einleseAnzahlList.size() <= zaehlerPerson)
                    {
                        MainActivity.einleseAnzahlList.add(new Person(vorname, nachname, guthaben, email, telefonNr, gruppe));
                    }



                    if(MainActivity.gruppe.equals(gruppe))
                    {

                        if(MainActivity.personen.contains(new Person(vorname,nachname,guthaben,email,telefonNr,gruppe)))
                        {

                        }
                        else {
                            MainActivity.items.add(new Person(vorname, nachname, guthaben, email, telefonNr, gruppe).toString());
                            MainActivity.personen.add(new Person(vorname, nachname, guthaben, email, telefonNr, gruppe));

                            MainActivity.personCounter.add(new Person(vorname, nachname, guthaben, email,telefonNr,gruppe));

                            MasterFragment.adapter.notifyDataSetChanged();
                            MasterFragment.listView.setAdapter(MasterFragment.adapter);
                            MasterFragment.adapter.notifyDataSetChanged();
                        }
                    }



                    if(zaehlerPerson==0)
                    {
                        personVorhanden=false;
                    }
                    else
                    {
                        personVorhanden=true;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void writeGruppen()
    {
        myGruppenRef.child(String.valueOf(zaehlerGruppe+1)).setValue(neueGruppe);
    }

    public void writeFirstGruppen()
    {
        myGruppenRef.getRoot().child("Gruppen").child(String.valueOf(zaehlerGruppe+1)).setValue(neueGruppe);
    }

    public void loadGruppen()
    {
        for (int i = 1; i <= zaehlerGruppe; i++) {
            myGruppenRef = FirebaseDatabase.getInstance().getReference().child("Gruppen").child(String.valueOf(i));
            myGruppenRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String gruppenName = dataSnapshot.child("guppenName").getValue().toString();
                    String gruppenPw = dataSnapshot.child("gruppenPasswort").getValue().toString();
                    Gruppe gr = new Gruppe(gruppenName,gruppenPw);

                    if(gruppen.contains(gr))
                    {
                        Log.d("MainActivity9090", "contains");
                    }
                    else
                    {
                        gruppen.add(gr);
                        Log.d("MainActivity9090", gr.guppenName);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(zaehlerGruppe==0)
        {
            gruppeVorhanden=false;
        }
        else
        {
            gruppeVorhanden=true;
        }
    }

    public void writeGetraenke()
    {
        myGetraenkeRef.child(String.valueOf(zaehlerGetraenke+1)).setValue(getraenk);
    }
    public void writeFirstGetraenke()
    {
        myGetraenkeRef.getRoot().child("Getraenke").child(String.valueOf(zaehlerGetraenke+1)).setValue(getraenk);
    }

    public void loadGetaenke()
    {

        for (int i = 1; i <= zaehlerGetraenke; i++) {


            myGetraenkeRef = FirebaseDatabase.getInstance().getReference().child("Getraenke").child(String.valueOf(i));
            myGetraenkeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String getraenkeName = dataSnapshot.child("name").getValue().toString();
                    double getraenkePreis = Double.parseDouble(dataSnapshot.child("preis").getValue().toString());
                    String gruppenName = dataSnapshot.child("gruppenName").getValue().toString();
                    int anzahl = 0;
                    try {
                        anzahl = Integer.parseInt(dataSnapshot.child("anzahl").getValue().toString());
                    }
                    catch (Exception ex)
                    {

                    }


                    einlesenAnzahlList.add(new Getraenk(getraenkeName, getraenkePreis, gruppenName,anzahl));

                    if(gruppenName.equals(MainActivity.gruppe)) {


                        if (MainActivity.getraenke.contains(new Getraenk(getraenkeName, getraenkePreis, gruppenName,anzahl))) {

                        } else {
                            MainActivity.getraenke.add(new Getraenk(getraenkeName, getraenkePreis, gruppenName,anzahl));

                            MainActivity.getaenkCounter.add(new Getraenk(getraenkeName, getraenkePreis, gruppenName, anzahl));
                        }
                    }
                    else
                    {
                        Log.d("MainActivity","Andere Gruppe");
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(zaehlerGetraenke==0)
        {
            getraenkVorhanden=false;
        }
        else
        {
            getraenkVorhanden=true;
        }
    }

}