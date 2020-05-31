package com.example.posprojekt;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferencePersons;
    private List<Person> personList;

    public interface DataStatus
    {
        void dataIsLoaded(List<Person> personList, List<String> keys);
        void dataIsUpdated();
        void dataIsDeleted();
    }
    public FireBaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePersons = mDatabase.getReference("Personen");

    }
    public void readPersons(final DataStatus dataStatus)
    {
        mReferencePersons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personList.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    Person person = keyNode.getValue(Person.class);
                    personList.add(person);
                }
                dataStatus.dataIsLoaded(personList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
