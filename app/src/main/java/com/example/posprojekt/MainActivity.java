package com.example.posprojekt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectionChangedListener, View.OnClickListener {


    static ArrayList<Person> personen = new ArrayList<>();//Personen der Liste
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Getraenk> getraenke = new ArrayList<>();

    private detailFragment detailFragment;
    private boolean showdetail;

    static String email;//login -> durch diese kann jeder zugeordnet werden
    static String passwort;//login
    static String gruppe;
    static boolean admin;

    static List<User> users = new ArrayList<>();//Personen zum Anmelden



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailFragment = (detailFragment) getSupportFragmentManager().findFragmentById(R.id.fragright);
        showdetail = detailFragment != null && detailFragment.isInLayout();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSelectionChanged(int pos, String item) {
        if(showdetail) detailFragment.showInformation(pos,item);
        else callDetailActivity(pos,item);
    }


    private void callDetailActivity(int pos, String item) {
        Intent intent = new Intent(this, activity_detail.class);
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


        switch (id)
        {
            case R.id.menu_newPerson:

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Neue Person");
                final View view = getLayoutInflater().inflate(R.layout.newperson,null);
                alert.setView(view);
                alert.setPositiveButton("Hinzufügen",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText vorname = view.findViewById(R.id.vorname);
                                EditText nachname = view.findViewById(R.id.nachname);
                                EditText guthanen = view.findViewById(R.id.guthaben);
                                EditText email = view.findViewById(R.id.email);
                                EditText telnr = view.findViewById(R.id.number);

                                if (admin == true) {

                                    try {
                                        String vorn = vorname.getText().toString();
                                        String nach = nachname.getText().toString();
                                        double guthab = Double.parseDouble(guthanen.getText().toString());
                                        String emai = email.getText().toString();
                                        long teln = Long.parseLong(telnr.getText().toString());
                                        Person x = new Person(vorn, nach, guthab, emai, teln);
                                        items.add(x.toString());
                                        personen.add(x);
                                        masterFragment.adapter.notifyDataSetChanged();
                                        masterFragment.listView.setAdapter(masterFragment.adapter);
                                        masterFragment.adapter.notifyDataSetChanged();

                                    } catch (Exception io) {
                                        io.printStackTrace();
                                        Log.d("MainActivity", "Failed to parse" + "" + io);
                                    }


                                }
                                else
                                {
                                    Toast.makeText(view.getContext(), "Sie haben keine Berechtigungen um eine Person hinzuzufügen", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
                alert.setNegativeButton("Zurück",null);
                alert.show();



                break;
            case R.id.menu_aktualisieren:
                Intent intent = new Intent(this, MainActivity.class);


                startActivity(intent);
                Toast.makeText(this,"Aktualisiert",Toast.LENGTH_SHORT).show();

                break;

            case R.id.menu_newGetraenk:
                AlertDialog.Builder alert5 = new AlertDialog.Builder(this);
                alert5.setTitle("Neues Getränk");
                final View view5 = getLayoutInflater().inflate(R.layout.getraenke_hinzufuegen,null);
                alert5.setView(view5);
                alert5.setPositiveButton("Hinzufügen",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText getraenkename = view5.findViewById(R.id.getraenke_name);
                        EditText getraenkepreis = view5.findViewById(R.id.getraenke_preis);
                        String name = getraenkename.getText().toString();




                        if(admin == true) {
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
                                MainActivity.getraenke.add(new Getraenk(name, preis));
                            } catch (Exception ex) {
                                Toast.makeText(view5.getContext(), "Hinzufügen Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(view5.getContext(), "Sie haben keine Berechtigungen um ein Getränk hinzuzufügen", Toast.LENGTH_SHORT).show();
                        }





                    }}
                );
                alert5.setNegativeButton("Zurück",null);
                alert5.show();
                break;


            case R.id.menu_login:
                //login
                AlertDialog.Builder alert6 = new AlertDialog.Builder(this);
                alert6.setTitle("Login");
                final View view6 = getLayoutInflater().inflate(R.layout.loginandregistrieren,null);
                alert6.setView(view6);
                alert6.setPositiveButton("Login",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText user = view6.findViewById(R.id.login_username);
                        EditText passw = view6.findViewById(R.id.login_passwort);
                        EditText grupp = view6.findViewById(R.id.login_gruppe);

                        try {

                            MainActivity.email = user.getText().toString();
                            MainActivity.passwort = user.getText().toString();
                            MainActivity.gruppe = user.getText().toString();

                            User us = new User(MainActivity.email,MainActivity.passwort, MainActivity.gruppe,false);

                            if(users.contains(us))
                            {
                                //Login in Firebase



                            }
                            else
                            {
                                Toast.makeText(view6.getContext(), MainActivity.email+": Anmeldedaten sind nicht Korrekt",Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(view6.getContext(), "Login Fehlgeschlagen",Toast.LENGTH_SHORT).show();
                        }


                    }}
                );
                alert6.setNegativeButton("Zurück",null);
                alert6.show();
                break;



            case R.id.menu_registrieren:

                AlertDialog.Builder alert7 = new AlertDialog.Builder(this);
                alert7.setTitle("Registrieren");
                final View view7 = getLayoutInflater().inflate(R.layout.loginandregistrieren,null);
                alert7.setView(view7);
                alert7.setPositiveButton("Login",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText user = view7.findViewById(R.id.login_username);
                        EditText passw = view7.findViewById(R.id.login_passwort);
                        EditText grupp = view7.findViewById(R.id.login_gruppe);

                        try {

                            MainActivity.email = user.getText().toString();
                            MainActivity.passwort = user.getText().toString();
                            MainActivity.gruppe = user.getText().toString();

                            User us = new User(MainActivity.email,MainActivity.passwort, MainActivity.gruppe,false);

                            if(users.contains(us))
                            {
                                //Login
                                Toast.makeText(view7.getContext(), MainActivity.email+"sie sind bereits Registriert",Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                users.add(new User(email, passwort, gruppe,false));
                            }

                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(view7.getContext(), "Registrieren Fehlgeschlagen",Toast.LENGTH_SHORT).show();
                        }


                    }}
                );
                alert7.setNegativeButton("Zurück",null);
                alert7.show();
                break;

            case R.id.menu_admin:
                AlertDialog.Builder alert8 = new AlertDialog.Builder(this);
                alert8.setTitle("Admin");
                final View view8 = getLayoutInflater().inflate(R.layout.loginandregistrierenadmin,null);
                alert8.setView(view8);
                alert8.setPositiveButton("Admin",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText user = view8.findViewById(R.id.admin_email);
                        EditText passw = view8.findViewById(R.id.admin_passwort);
                        EditText grupp = view8.findViewById(R.id.admin_Gruppe);
                        Switch sw = view8.findViewById(R.id.admin_switch);

                        try {

                            MainActivity.email = user.getText().toString();
                            MainActivity.passwort = user.getText().toString();
                            MainActivity.gruppe = user.getText().toString();
                            MainActivity.admin = sw.isChecked();
                            Toast.makeText(view8.getContext(), sw.isChecked()+"",Toast.LENGTH_SHORT).show();


                            User us = new User(MainActivity.email,MainActivity.passwort, MainActivity.gruppe,admin);

                            if(users.contains(us) && !users.contains(us.gruppe))
                            {
                                //Login



                            }
                            else
                            {
                                users.add(new User(email, passwort, gruppe,admin));
                            }

                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(view8.getContext(), "Fehlgeschlagen",Toast.LENGTH_SHORT).show();
                        }


                    }}
                );
                alert8.setNegativeButton("Zurück",null);
                alert8.show();
                break;

        }

        return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();



    }
}