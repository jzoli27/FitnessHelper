package com.example.fitnesshelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.adapters.StartWorkoutAdapter;
import com.example.fitnesshelper.models.WorkoutTemplate;

import java.util.ArrayList;

public class StartWorkout extends AppCompatActivity {
    TextView startWorkout_titleTv;
    String value;

    ArrayList<WorkoutTemplate> templateListed;
    ArrayList<WorkoutTemplate> lists;
    RecyclerView recyclerView;

    StartWorkoutAdapter startWorkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        value = "ures";
        startWorkout_titleTv = findViewById(R.id.startWorkout_titleTv);
        recyclerView = findViewById(R.id.startWorkoutRv);
        templateListed = new ArrayList<>();

        //WorkoutTemplate workoutTemplate = new WorkoutTemplate("asd","dsa","dsd","ddd","ert","xdd");
        //templateList.add(workoutTemplate);
        //Bundle extras = getIntent().getExtras();
        //if (extras != null){
            //value = extras.getString("size");
            //templateList = (ArrayList<WorkoutTemplate>) getIntent().getSerializableExtra("mylist");
        //}
        //templateList = (ArrayList<WorkoutTemplate>) getIntent().getSerializableExtra("mylist");

        //Intent intent = getIntent();
        //Bundle args = intent.getBundleExtra("BUNDLE");
        //lists = (ArrayList<WorkoutTemplate>) args.getSerializable("ARRAYLIST");
        Bundle bundleObject = getIntent().getExtras();

        if (bundleObject != null){
            templateListed = (ArrayList<WorkoutTemplate>) bundleObject.getSerializable("ARRAYLIST");
            initializeRecyclerView();
        }


        startWorkout_titleTv.setText(String.valueOf(templateListed.size()));

        for (int i = 0; i< templateListed.size(); i++){
            Toast.makeText(this, "name: " + templateListed.get(i).getName(), Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeRecyclerView() {
        startWorkoutAdapter = new StartWorkoutAdapter(this, templateListed);

        recyclerView.setAdapter(startWorkoutAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}