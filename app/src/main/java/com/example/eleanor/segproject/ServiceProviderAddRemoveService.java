package com.example.eleanor.segproject;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.eleanor.segproject.ServiceList.LISTOFSERVICES;

public class ServiceProviderAddRemoveService extends ServiceProvider {

    //public ServiceList serviceList = new ServiceList();

    DatabaseReference mDatabase, spDB;
    EditText serviceName;
    ListView servicesAvailable,viewMyServices;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> listKeys = new ArrayList<String>();
    ArrayList<String> myServicesArray = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter,  myServicesAdapter;
    private Button findButton;
    private Button addButton;
    private Button deleteButton;
    private Boolean searchMode = false;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_add_service);

        serviceName = findViewById(R.id.SPserviceToAdd);

        addButton = findViewById(R.id.addItem);
        deleteButton = findViewById(R.id.removeServiceAndReturnHome);

        servicesAvailable = findViewById(R.id.serviceListFromAdmin);
        viewMyServices = findViewById(R.id.myServices);

        ViewList serviceList = new ViewList();
        System.out.println(serviceList);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("service");
        spDB = FirebaseDatabase.getInstance().getReference().child("serviceProviders");

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_single_choice,
                listItems);

        myServicesAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myServicesArray);

        servicesAvailable.setAdapter(arrayAdapter);
        servicesAvailable.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        viewMyServices.setAdapter(myServicesAdapter);

        servicesAvailable.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedPosition = position;
                        itemSelected = true;
                        addButton.setEnabled(true);

                        String selectedFromList = (String) (servicesAvailable.getItemAtPosition(position));
                        serviceName.setText(selectedFromList);
                    }
                });


        addValueEventListener();
    }

    private void addValueEventListener() {
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                listKeys.clear();

                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();

                    String key = next.getKey();
                    String serviceType = (String) next.child("serviceType").getValue();
                    String rate = (String) next.child("rate").getValue();

                    listKeys.add(key);
                    arrayAdapter.add(ViewList.serviceToString(serviceType, rate));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(valueEventListener);
    }

    public void addToMyServices(View view) {
        myServicesArray.add(serviceName.getText().toString());
        System.out.println(serviceName.getText().toString());
        System.out.println(myServicesArray);

        myServicesAdapter.notifyDataSetChanged();

        FirebaseUser currentUser = this.mAuth.getCurrentUser();
        System.out.println("USER UID:" + currentUser.getUid());

        spDB.child(currentUser.getUid()).child("myServices").setValue(myServicesArray);
    }
}




