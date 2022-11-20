package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.fitnesshelper.adapters.StartWorkoutAdapter;
import com.example.fitnesshelper.fragments.WorkoutFragment;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.Image;
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
    TextView startWorkout_titleTv, start_workout_cancelTv;
    String value;
    ImageView start_workout_startIv, start_workout_pauseIv, start_workout_restart;
    Button start_workout_row_item_finishBtn;

    ArrayList<WorkoutTemplate> templateListed;
    ArrayList<WorkoutDetails> detailsList;
    ArrayList<Repetition> reps;
    ArrayList<Repetition> savedReps;
    ArrayList<Exercise> savedExercises;

    String wtKey;
    String wtName;
    RecyclerView recyclerView;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
    DatabaseReference finishedReference = FirebaseDatabase.getInstance().getReference("Finished");

    String uid;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    StartWorkoutAdapter startWorkoutAdapter;
    WorkoutTemplate wTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        value = "ures";
        startWorkout_titleTv = findViewById(R.id.startWorkout_titleTv);
        start_workout_cancelTv = findViewById(R.id.start_workout_cancelTv);
        recyclerView = findViewById(R.id.startWorkoutRv);
        chronometer = findViewById(R.id.chronometer);
        start_workout_startIv = findViewById(R.id.start_workout_startIv);
        start_workout_pauseIv = findViewById(R.id.start_workout_pauseIv);
        start_workout_restart = findViewById(R.id.start_workout_restart);
        start_workout_row_item_finishBtn = findViewById(R.id.start_workout_row_item_finishBtn);



        templateListed = new ArrayList<>();
        detailsList = new ArrayList<>();
        reps = new ArrayList<>();
        savedReps = new ArrayList<>();
        savedExercises = new ArrayList<>();

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
        start_workout_cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseChronometer(view);
                cancelDialog();
            }
        });

        /*
        if (!detailsList.isEmpty()){
            if (bundleObject != null){

            }
        }
         */

        if (bundleObject != null){
            detailsList.clear();
            templateListed.clear();
            detailsList = (ArrayList<WorkoutDetails>) bundleObject.getSerializable("ARRAYLIST");
            templateListed = (ArrayList<WorkoutTemplate>) bundleObject.getSerializable("templatelist");
            wtKey = bundleObject.getString("wtKey");
            wtName = bundleObject.getString("wtName");
        }

        for (int i = 0; i < templateListed.size(); i++){
            if (templateListed.get(i).getWtKey().equals(wtKey)){
                wTemplate = templateListed.get(i);
            }
        }

        startChronometer(this.findViewById(android.R.id.content));

        startWorkout_titleTv.setText(wtName);

        //ki kéne nullázni az összeset első futáskor itt..., hogy ne legyen egyik se kiválasztva alapvetően
        //előbb logold ki a kulcsokat fixen, ne hogy rosszul törölj, mert baszhatod.....

        reference.child("Templates").child(wtKey).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot current_data : snapshot.getChildren()) {
                    for (DataSnapshot current_user_data : current_data.getChildren()) {
                        for (DataSnapshot for3 : current_user_data.getChildren()) {
                            Repetition repetition = for3.getValue(Repetition.class);
                            if(repetition.getState().equals("1")){
                                //reference.child("Templates").child(wtKey).child("Exercises").
                                Log.d("DEFAULT", "exckey: " + current_data.getKey());
                                String exckey = current_data.getKey().toString();
                                reference.child("Templates").child(wtKey).child("Exercises").child(exckey).child("Repetition").child(repetition.getRepetitionKey()).child("state").setValue("0");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        start_workout_startIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChronometer(view);
            }
        });

        start_workout_pauseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseChronometer(view);
            }
        });

        start_workout_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetChronometer(view);
            }
        });
        savedReps.clear();

        reference.child("Templates").child(wtKey).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        start_workout_row_item_finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseChronometer(view);
                finishWorkoutDialog();
            }
        });


        if (!uid.isEmpty()){
            reps.clear();
            reference.child("Templates").child(wtKey).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

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

                    if (!reps.isEmpty() && bundleObject != null){
                        //detailsList.clear();
                        //templateListed.clear();
                        //detailsList = (ArrayList<WorkoutDetails>) bundleObject.getSerializable("ARRAYLIST");
                        //templateListed = (ArrayList<WorkoutTemplate>) bundleObject.getSerializable("templatelist");

                        initializeRecyclerView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void cancelDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Megszakítás");
            alert.setMessage("Biztos meg akarod szakítani az edzést?");
            alert.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(StartWorkout.this, "Edzés megszakítva", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            alert.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    startChronometer(findViewById(android.R.id.content));
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
    }

    private void finishWorkoutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edzés vége");
        alert.setMessage("Biztos be akarod fejezni az edzést");
        alert.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveWorkout();
                Toast.makeText(StartWorkout.this, "Edzés mentve", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alert.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                startChronometer(findViewById(android.R.id.content));
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }


    private void saveWorkout() {
        savedReps.clear();
        String savedWtKey = reference.push().getKey();
        reference.child("Templates").child(wtKey).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot current_data : snapshot.getChildren()) {
                    Exercise exercise = current_data.getValue(Exercise.class);
                    for (DataSnapshot current_user_data : current_data.getChildren()) {
                        for (DataSnapshot for3 : current_user_data.getChildren()) {
                            Repetition repetition = for3.getValue(Repetition.class);
                            if(repetition.getState().equals("1")){
                                savedReps.add(repetition);
                                savedExercises.add(exercise);
                            }
                        }
                    }
                }

                if(!savedReps.isEmpty() && !savedExercises.isEmpty()){
                    //1.lépés
                    finishedReference.child(uid).child(savedWtKey).child("Templates").child(wTemplate.getWtKey()).setValue(wTemplate);

                    //2.lépés
                    for (int i = 0; i < savedExercises.size(); i++){
                        finishedReference.child(uid).child(savedWtKey).child("Templates").child(wTemplate.getWtKey()).child("Exercises").child(savedExercises.get(i).getExerciseKey()).setValue(savedExercises.get(i));
                    }

                    //3.lépés

                    for (int i = 0; i < savedExercises.size(); i++){
                        for (int j = 0; j < savedReps.size(); j++){
                            if (savedExercises.get(i).getExerciseName().equals(savedReps.get(i).getExerciseName())){
                                finishedReference.child(uid).child(savedWtKey).child("Templates").child(wTemplate.getWtKey()).child("Exercises").
                                        child(savedExercises.get(i).getExerciseKey()).child("Repetition").child(savedReps.get(i).getRepetitionKey()).setValue(savedReps.get(i));
                            }
                        }
                    }



                            /*
                            for (int i = 0; i < savedReps.size(); i++){
                                Log.d("DETAIL", " template name: " + wTemplate.getName()+" exckey: "+ savedExercises.get(i).getExerciseKey() + " saved: " + savedReps.get(i).getExerciseName());
                                //saveWorkout();
                            }

                             */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initializeRecyclerView() {
        startWorkoutAdapter = new StartWorkoutAdapter(this, detailsList, reps, wtKey);

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