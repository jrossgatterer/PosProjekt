package com.example.posprojekt;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class masterFragment extends Fragment {

    private ListView listView;
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Person> personen = new ArrayList<>();

    private OnSelectionChangedListener listener;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnSelectionChangedListener) {
            listener = (OnSelectionChangedListener) context;
        } else {
            Log.d("masterFragment", "onAttach: Activity does not implement OnSelectionChangedListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_master, container, false);
        initializeViews(view);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeViews(View view) {

        listView = view.findViewById(R.id.listview);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = items.get(position);
                listener.onSelectionChanged(position, item);
            }
        });


}

        @Override
    public void onStart() {
        super.onStart();
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        items
                );
        listView.setAdapter(adapter);
    }





    }

