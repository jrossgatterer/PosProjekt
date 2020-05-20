package com.example.posprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter extends BaseAdapter {

    List<Person> personList;
    LayoutInflater inflater;
    int layoutId;


    public Adapter(List<Person> list, Context ctx, int layoutId) {
        this.personList = list;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;

    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Person person = personList.get(position);

        View listItem = (convertView == null) ? inflater.inflate(this.layoutId, null) : convertView;

        ((TextView) listItem.findViewById(R.id.textperson)).setText(person.toString());

        return null;
    }
}
