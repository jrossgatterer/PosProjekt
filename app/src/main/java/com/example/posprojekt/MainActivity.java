package com.example.posprojekt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectionChangedListener, View.OnClickListener {


    static ArrayList<Person> personen = new ArrayList<>();//Personen der Liste
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Getraenk> getraenke = new ArrayList<>();
    static Person person = new Person();
    long zaehler = 0;

    private detailFragment detailFragment;
    private boolean showdetail;

    static String email;//login -> durch diese kann jeder zugeordnet werden
    static String passwort;//login
    static String gruppe;
    static boolean admin;

    static List<User> users = new ArrayList<>();//Personen zum Anmelden
    static List<Gruppe> gruppen = new ArrayList<>();

    static String gruppenpasswort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detailFragment = (detailFragment) getSupportFragmentManager().findFragmentById(R.id.fragright);
        showdetail = detailFragment != null && detailFragment.isInLayout();
        myRef = FirebaseDatabase.getInstance().getReference().child("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    zaehler = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                if(admin == true) {
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

                                    try {
                                        String vorn = vorname.getText().toString();
                                        String nach = nachname.getText().toString();
                                        double guthab = Double.parseDouble(guthanen.getText().toString());
                                        String emai = email.getText().toString();
                                        long teln = Long.parseLong(telnr.getText().toString());
                                        Person x = new Person(vorn, nach, guthab, emai, teln);
                                        items.add(x.toString());
                                        personen.add(x);
                                        MainActivity.person = x;
                                        masterFragment.adapter.notifyDataSetChanged();
                                        masterFragment.listView.setAdapter(masterFragment.adapter);
                                        masterFragment.adapter.notifyDataSetChanged();

                                        writePersonen();

                                    } catch (Exception io) {
                                        io.printStackTrace();
                                        Log.d("MainActivity", "Failed to parse" + "" + io);
                                    }

                                }


                        }
                );
                alert.setNegativeButton("Zurück",null);
                alert.show();
        }else
        {
            Toast.makeText(this, "Sie haben keine Berechtigungen um ein Getränk hinzuzufügen", Toast.LENGTH_SHORT).show();
        }
                break;
            case R.id.menu_aktualisieren:
                Intent intent = new Intent(this, MainActivity.class);



                startActivity(intent);


                //loadUser();
                Toast.makeText(this,"Aktualisiert",Toast.LENGTH_SHORT).show();

                break;





            case R.id.menu_newGetraenk:
                if(admin == true) {
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





                    }}
                );
                alert5.setNegativeButton("Zurück",null);
                alert5.show();

                }else
                    {
                        Toast.makeText(this, "Sie haben keine Berechtigungen um ein Getränk hinzuzufügen", Toast.LENGTH_SHORT).show();
                    }
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
                            MainActivity.passwort = passw.getText().toString();
                            MainActivity.gruppe = grupp.getText().toString();

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
                            MainActivity.passwort = passw.getText().toString();
                            MainActivity.gruppe = grupp.getText().toString();

                            User us = new User(MainActivity.email,MainActivity.passwort, MainActivity.gruppe,false);

                            if(users.contains(us))
                            {
                                //Login
                                Toast.makeText(view7.getContext(), MainActivity.email+"sie sind bereits Registriert",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(view7.getContext(),"Registriert",Toast.LENGTH_SHORT).show();
                                users.add(new User(email, passwort, gruppe,false));
                            }

                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(view7.getContext(), "Registrieren Fehlgeschlagen",Toast.LENGTH_SHORT).show();
                        }

                        writeUser();
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
                alert8.setPositiveButton("Login als Admin",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText user = view8.findViewById(R.id.admin_email);
                        EditText passw = view8.findViewById(R.id.admin_passwort);
                        EditText grupp = view8.findViewById(R.id.admin_Gruppe);
                        Switch sw = view8.findViewById(R.id.admin_switch);
                        EditText grupppas = view8.findViewById(R.id.admin_grupppasswort);
                        try {

                            MainActivity.email = user.getText().toString();
                            MainActivity.passwort = passw.getText().toString();
                            MainActivity.gruppe = grupp.getText().toString();
                            MainActivity.admin = sw.isChecked();
                            MainActivity.gruppenpasswort = grupppas.getText().toString();


                            User us = new User(MainActivity.email,MainActivity.passwort, MainActivity.gruppe,admin);


                            Gruppe gruppe1 = new Gruppe(MainActivity.gruppe, MainActivity.gruppenpasswort);

                            if(gruppen.contains(gruppe1))
                            {
                                Toast.makeText(view8.getContext(), "Name oder Passwort nicht korrekt",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(users.contains(us) && !users.contains(us.gruppe))
                                {
                                    //Login
                                    //
                                    

                                }
                                else
                                {
                                    Toast.makeText(view8.getContext(),"Neuer Admin",Toast.LENGTH_SHORT).show();
                                    users.add(new User(email, passwort, gruppe,admin));
                                }
                            }



                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(view8.getContext(), "Fehlgeschlagen",Toast.LENGTH_SHORT).show();
                        }
                    writeUser();

                    }}
                );
                alert8.setNegativeButton("Zurück",null);
                alert8.show();
                break;


            case R.id.menu_gruppehinzufügen:

                if(admin == true) {
                AlertDialog.Builder alert9 = new AlertDialog.Builder(this);
                alert9.setTitle("Gruppe hinzufügen");
                final View view9 = getLayoutInflater().inflate(R.layout.gruppehinzufegenadmin,null);
                alert9.setView(view9);
                alert9.setPositiveButton("Hinzufügen",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText gruppenna = view9.findViewById(R.id.gruppehinz_name);
                        EditText gruppenpass = view9.findViewById(R.id.gruppehinz_passwort);

                        try {

                            String group = gruppenna.getText().toString();


                            if(MainActivity.gruppen.contains(group))
                            {
                                Toast.makeText(view9.getContext(), "Gruppe existiert bereits",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                MainActivity.gruppe = gruppenna.getText().toString();
                                MainActivity.gruppenpasswort = gruppenpass.getText().toString();
                                gruppen.add(new Gruppe(MainActivity.gruppe, MainActivity.gruppenpasswort));
                            }

                        }
                        catch (Exception ex)
                        {

                        }

                    }}
                );
                alert9.setNegativeButton("Zurück",null);
                alert9.show();
                }else
                {
                    Toast.makeText(this, "Sie haben keine Berechtigungen um eine Gruppe hinzuzufügen", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
    }
    DatabaseReference myRef;

    public void writeUser()
    {
        User user = new User("","","",false);
        user.setEmail(email);
        user.setPasswort(passwort);
        user.setGruppe(gruppe);
        user.setAdmin(admin);
            myRef.child(String.valueOf(zaehler+1)).setValue(user);
        }

    static String value;
    static User userload = new User("","","",false);

    public void loadUser()
    {
       myRef = FirebaseDatabase.getInstance().getReference().child("User").child("");
       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String email = dataSnapshot.child("email").getValue().toString();
               String passwort = dataSnapshot.child("passwort").getValue().toString();
               String gruppe = dataSnapshot.child("gruppe").getValue().toString();
               String admin = dataSnapshot.child("admin").getValue().toString();

               userload.setEmail(email);
               userload.setAdmin(Boolean.getBoolean(admin));
               userload.setPasswort(passwort);
               userload.setGruppe(gruppe);

               users.add(userload);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

    public void writePersonen()
    {
        DatabaseReference myReference;
        myReference = FirebaseDatabase.getInstance().getReference().child("Personen");
        myReference.push().setValue(person);
    }
    public void loadPersonen()
    {
        DatabaseReference myReference;
        myReference = FirebaseDatabase.getInstance().getReference().child("Personen");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personen.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    Person person = keyNode.getValue(Person.class);
                    personen.add(person);
                    MainActivity.items.add(person.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(this, personen.get(0).toString(),Toast.LENGTH_LONG).show();
    }
    public void writeGruppen()
    {

    }
    public void loadGruppen()
    {

    }

}