package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnesshelper.adapters.ExercisesAdapter;
import com.example.fitnesshelper.adapters.FMDetailsAdapter;
import com.example.fitnesshelper.models.Exercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FitnessMachineDetails extends AppCompatActivity {
    private TextView fmName;
    private ImageView fmImg;
    private RecyclerView recyclerView;
    private Button logBtn;

    private String fitnessMName;
    private String fmKey;
    private String fmImgLink;


    private HashMap<String,String> hashmap;
    private ArrayList<String> Settingkey;
    private ArrayList<String> Settingvalue;

    FMDetailsAdapter fmDetailsAdapter;

    private DatabaseReference SettingsReference = FirebaseDatabase.getInstance().getReference("FitnessMachine");
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_machine_details);

        fmName = findViewById(R.id.fmDetailsNameTv);
        fmImg = findViewById(R.id.fmDetailsIv);
        recyclerView = findViewById(R.id.fmDetailsRv);
        logBtn = findViewById(R.id.logBtn);

        hashmap = new HashMap<>();
        Settingkey = new ArrayList<String>();
        Settingvalue = new ArrayList<String>();



        Bundle extras = getIntent().getExtras();
        if (extras != null){

            fitnessMName = extras.getString("name");
            fmKey = extras.getString("fmKey");
            fmImgLink = extras.getString("imgLink");
        }
        //Intent i = getIntent();
        //Settingkey.clear();
        //Settingvalue.clear();
        //Settingkey =  (ArrayList<String>) getIntent().getSerializableExtra("keys");
        //Settingvalue =  (ArrayList<String>) getIntent().getSerializableExtra("values");


        fmName.setText(fitnessMName);

        //Settingkey.add("asd");
        //Settingkey.add("asd");
        //Settingvalue.add("qwe");
        //Settingvalue.add("qwe");

        initializeData();
        //initializeRecyclerView();

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                for (Map.Entry<String, String> entry : hashmap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Log.d("HASHMAP", "key =  " + key + " value: " + value);
                }

                for (int i = 0; i < Settingkey.size(); i++){
                    Log.d("HASHMAP", "key =  " + Settingkey.get(i) + " value: " + Settingvalue.get(i));
                }
                 for (Map.Entry<String, String> entry : hashmap.entrySet()) {
                    //String key = entry.getKey();
                    //Object value = entry.getValue();
                    Settingkey.add(entry.getKey());
                    Settingvalue.add(entry.getValue());
                }

                */

                for (int i = 0; i < Settingkey.size(); i++){
                    Log.d("HASHMAP", "key =  " + Settingkey.get(i) /*+ " value: " + Settingvalue.get(i)*/);
                }
            }
        });
    }

    private void initializeData() {
        SettingsReference.child(uid).child(fmKey).child("Settings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hashmap = (HashMap<String, String>) snapshot.getValue();
                if (!hashmap.isEmpty()){
                    handleData();
                    initializeRecyclerView();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleData() {
        Settingkey.clear();
        Settingvalue.clear();
        for (Map.Entry<String, String> entry : hashmap.entrySet()) {
            Settingkey.add(entry.getKey() + " :");
            Settingvalue.add(entry.getValue());
        }
    }

    private void initializeRecyclerView() {
        //initializeData();

        fmDetailsAdapter = new FMDetailsAdapter(this, Settingkey, Settingvalue);

        recyclerView.setAdapter(fmDetailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}