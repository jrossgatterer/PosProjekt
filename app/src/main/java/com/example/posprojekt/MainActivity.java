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
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnSelectionChangedListener  {


    static ArrayList<Person> personen = new ArrayList<>();
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Getraenk> getraenke = new ArrayList<>();

    private detailFragment detailFragment;
    private boolean showdetail;

    static String name;//login
    static String password;//login

    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailFragment = (detailFragment) getSupportFragmentManager().findFragmentById(R.id.fragright);
        showdetail = detailFragment != null && detailFragment.isInLayout();
        adapter = new Adapter(MainActivity.personen,MainActivity.this,R.layout.startlayoutperson);
        masterFragment.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                        EditText guthanen = view.findViewById(R.id.number);
                        EditText email = view.findViewById(R.id.email);
                        EditText telnr = view.findViewById(R.id.number);

                        try {
                            String vorn = vorname.getText().toString();
                            String nach = nachname.toString();
                            double guthab = Double.parseDouble(guthanen.getText().toString());
                            String emai = email.toString();
                            int teln = Integer.parseInt(telnr.getText().toString());
                            personen.add(new Person(vorn,nach,guthab,emai,teln));
                        }
                        catch(Exception io)
                        {
                            io.printStackTrace();
                        }
                    }}
                    );
                alert.setNegativeButton("Zurück",null);
                alert.show();
                adapter.notifyDataSetChanged();



                break;




        }

        return true;
    }

}