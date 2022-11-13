package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.adapters.StartWorkoutAdapter;
import com.example.fitnesshelper.fragments.WorkoutFragment;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.Repetition;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StartWorkout extends AppCompatActivity {
    TextView startWorkout_titleTv;
    String value;

    ArrayList<WorkoutTemplate> templateListed;
    ArrayList<WorkoutDetails> detailsList;
    ArrayList<Repetition> reps;

    String wtKey;
    String wtName;
    RecyclerView recyclerView;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
    String uid;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    StartWorkoutAdapter startWorkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        value = "ures";
        startWorkout_titleTv = findViewById(R.id.startWorkout_titleTv);
        recyclerView = findViewById(R.id.startWorkoutRv);
        chronometer = findViewById(R.id.chronometer);

        templateListed = new ArrayList<>();
        detailsList = new ArrayList<>();
        reps = new ArrayList<>();

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        /*
        if (bundleObject != null){
            templateListed = (ArrayList<WorkoutTemplate>) bundleObject.getSerializable("ARRAYLIST");
            initializeRecyclerView();
        }
         */
        if (bundleObject != null){
            detailsList = (ArrayList<WorkoutDetails>) bundleObject.getSerializable("ARRAYLIST");
            templateListed = (ArrayList<WorkoutTemplate>) bundleObject.getSerializable("templatelist");
            wtKey = bundleObject.getString("wtKey");
            wtName = bundleObject.getString("wtName");

        }

        if (!uid.isEmpty()){
            reference.child("Templates").child(wtKey).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reps.clear();
                    for (DataSnapshot current_data : snapshot.getChildren()) {

                        Log.d("FOR1", " ");
                        Log.d("FOR1", "data1: " + current_data.getKey());
                        Log.d("FOR1", "for1 children: " + current_data.getChildrenCount());


                        for (DataSnapshot current_user_data : current_data.getChildren()) {

                            Log.d("FOR2", " ");
                            Log.d("FOR2", "FOR2: " + current_user_data.getKey());
                            Log.d("FOR2", "for2 children: " + current_user_data.getChildrenCount());

                            for (DataSnapshot for3 : current_user_data.getChildren()) {

                                Log.d("FOR3", " ");
                                Log.d("FOR3", "FOR3, Handling data " + for3.getKey());
                                Log.d("FOR3", "for3 children:, Handling data " + for3.getChildrenCount());
                                Repetition repetition = for3.getValue(Repetition.class);

                                reps.add(repetition);

                            }
                        }
                    }/*
                    if (!reps.isEmpty()){
                        for (int i = 0; i< reps.size(); i++){
                        Log.d("REPS:", "reps data: " + reps.get(i).getExerciseName());
                        Log.d("REPS:", "reps size: " + reps.size());
                        }
                    }
                    */
                    if (!reps.isEmpty()){
                        initializeRecyclerView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        startChronometer(this.findViewById(android.R.id.content));

        startWorkout_titleTv.setText(wtName);

    }

    private void initializeRecyclerView() {
        startWorkoutAdapter = new StartWorkoutAdapter(this, detailsList, reps);

        recyclerView.setAdapter(startWorkoutAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

}