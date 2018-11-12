package com.example.eleanor.segproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceConfigurationError;

public class ServiceList extends AppCompatActivity {

    public static ArrayList<Service> LISTOFSERVICES = new ArrayList<>(10);

    public ServiceList(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void addService(String serviceName, double payrate){
        Service aService = new Service(serviceName, payrate);
        LISTOFSERVICES.add(aService);
        
    }

    public void removeService(String serviceName){
        setContentView(R.layout.activity_remove_service);

        int indexOfItemToRemove = 0;
        for (int i = 0; i < LISTOFSERVICES.size(); i++){
            if (serviceName.equals(LISTOFSERVICES.get(i).getServiceName())){
                indexOfItemToRemove = i;
            }
        }
        LISTOFSERVICES.remove(indexOfItemToRemove);
    }


    public void editService(String serviceName, double newPrice){
        int indexOfItemToEdit = 0;
        for (int i = 0; i < LISTOFSERVICES.size(); i++){
            if (serviceName.equals(LISTOFSERVICES.get(i).getServiceName())){
                indexOfItemToEdit = i;
            }
        }
        LISTOFSERVICES.get(indexOfItemToEdit).setServiceRate(newPrice);

    }

    public boolean isIn(String serviceName){
        for (int i = 0; i < LISTOFSERVICES.size(); i++){
            if (serviceName.equals(LISTOFSERVICES.get(i).getServiceName())){
                return true;
            }
        }
        return false;
    }

    public static String getSize(){
        if(LISTOFSERVICES.isEmpty()){
            return "0";
        }
        return Integer.toString(LISTOFSERVICES.size());
    }

    public static String getLast(){
        if(LISTOFSERVICES.isEmpty()){
            return "List is Empty";
        }
        return (LISTOFSERVICES.get(LISTOFSERVICES.size()-1).getServiceName());
    }

    public void onRemoveServiceClick(View view){

        EditText serviceName = findViewById(R.id.ServiceToRemove);

        TextView ServiceDoesNotExist = findViewById(R.id.ServiceDoesNotExist);

        if (!LISTOFSERVICES.isIn(serviceName.getText().toString())){
            ServiceDoesNotExist.setText("That Service Does Not Exist");
        }
        else {
            LISTOFSERVICES.removeService(serviceName.getText().toString());

            Intent HOIntent = new Intent(RemoveService.this, Admin.class);
            startActivity(HOIntent);
        }
    }


}
