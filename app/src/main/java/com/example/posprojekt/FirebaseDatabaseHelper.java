package com.example.posprojekt;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferencePersons;
    private DatabaseReference mReferenceDrinks;
    private List<Person> personList = new ArrayList<>();

    public FirebaseDatabaseHelper()
    {
        mDatabase = FirebaseDatabase.getInstance();

        mReferencePersons = mDatabase.getReference("Personen");
        mReferenceDrinks = mDatabase.getReference("Getraenke");
    }

    public interface DataStatus{
        void DataIsLoaded(List<Person> personList, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public void readBooks(final DataStatus dataStatus){
        mReferencePersons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personList.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Person person = keyNode.getValue(Person.class);
                    personList.add(person);
                }
                dataStatus.DataIsLoaded(personList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void addPerson(Person person,final DataStatus dataStatus)
    {
        String key = mReferencePersons.push().getKey();
        mReferencePersons.child(key).setValue(person)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
}
