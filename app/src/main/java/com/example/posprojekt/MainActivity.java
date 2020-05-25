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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectionChangedListener, View.OnClickListener {


    static ArrayList<Person> personen = new ArrayList<>();
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Getraenk> getraenke = new ArrayList<>();

    private detailFragment detailFragment;
    private boolean showdetail;

    static String name;//login
    static String password;//login


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



                        try {
                            String vorn = vorname.getText().toString();
                            String nach = nachname.getText().toString();
                            double guthab = Double.parseDouble(guthanen.getText().toString());
                            String emai = email.getText().toString();
                            long teln = Long.parseLong(telnr.getText().toString());
                            Person x = new Person(vorn,nach,guthab,emai,teln);
                            items.add(x.toString());
                            personen.add(x);
                            masterFragment.adapter.notifyDataSetChanged();
                            masterFragment.listView.setAdapter(masterFragment.adapter);
                            masterFragment.adapter.notifyDataSetChanged();

                        }
                        catch(Exception io)
                        {
                            io.printStackTrace();
                            Log.d("MainActivity","Failed to parse"+""+io);
                        }


                    }}
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
                alert5.setTitle("Neue Person");
                final View view5 = getLayoutInflater().inflate(R.layout.getraenke_hinzufuegen,null);
                alert5.setView(view5);
                alert5.setPositiveButton("Hinzufügen",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText getraenkename = view5.findViewById(R.id.getraenke_name);
                        EditText getraenkepreis = view5.findViewById(R.id.getraenke_preis);
                        String name = getraenkename.getText().toString();
                        Double preis = Double.parseDouble(getraenkepreis.getText().toString());


                        MainActivity.getraenke.add(new Getraenk(name, preis));


                    }}
                );
                alert5.setNegativeButton("Zurück",null);
                alert5.show();
                break;

        }

        return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();



    }
}