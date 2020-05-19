package com.example.posprojekt;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class detailFragment extends Fragment {

    private TextView txt1;
    private TextView txt2;
    private Spinner spinner;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        txt1 = view.findViewById(R.id.textViewName);
        txt2 = view.findViewById(R.id.textViewGuthaben);
        spinner = view.findViewById(R.id.spinnergetraenke);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showInformation(int pos, String item) {

        List<Person> personen = new ArrayList<>();

       personen.addAll(MainActivity.personen);

        txt1.setText(personen.get(pos).vorname+" "+personen.get(pos).nachname);
        txt2.setText(String.valueOf(personen.get(pos).guthaben));



        final ArrayAdapter<Person> adapter =
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        personen
                );
        listView.setAdapter(adapter);


    }


}
