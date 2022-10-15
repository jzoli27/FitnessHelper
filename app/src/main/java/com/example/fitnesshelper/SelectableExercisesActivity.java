package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fitnesshelper.adapters.ExerciseRowItemAdapter;
import com.example.fitnesshelper.models.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectableExercisesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Exercise> exercisesList;
    private ArrayList<Exercise> exercisesStatus;
    private FloatingActionButton addToworkoutTemplateBtn;


    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DatabaseReference UsersReference;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String wtKey;
    Integer check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectable_exercises);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            wtKey = extras.getString("wtKey");
        }

        addToworkoutTemplateBtn = findViewById(R.id.addToWorkoutTemplateBtn);
        recyclerView = findViewById(R.id.exercisesRecyclerView);
        exercisesList = new ArrayList<>();
        exercisesStatus = new ArrayList<>();
        check = 0;


        initializeRecyclerView();

        addToworkoutTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWorkout();

            }
        });
    }


    private void addToWorkout() {
        reference = FirebaseDatabase.getInstance().getReference("Exercises");
        UsersReference = FirebaseDatabase.getInstance().getReference("Users");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exercisesStatus.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    if (exercise.getSelected().equals(true)){
                        exercisesStatus.add(exercise);

//                       Ez müködő kód.
                        UsersReference.child(uid).child("Templates").child(wtKey).child("Exercises").child(exercise.getExerciseKey()).setValue(exercise);

                        //talán itt lehetne updatelni az állapotot miután már
                        // hozzáadtuk a listához a true-kat(kijelölteket)
                        reference.child(exercise.getExerciseKey()).child("selected").setValue(false);
                        //ez jó lenne, csak ugye nem módusul a checkbox értéke illetve pluszba még lefut annyiszer
                        //ez a function amennyi mezőt visszadobtunk false-ra :( ettől függetlenül mőködhet...
                        //update: talán single listenerrel müködhet -- eddig jónak tűnik.
                    }
                }
                //Toast.makeText(SelectableExercisesActivity.this, "size: " + exercisesStatus.size(), Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                //onBackPressed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Toast.makeText(SelectableExercisesActivity.this, "size: " + exercisesStatus.size(), Toast.LENGTH_SHORT).show();
    }

    private void initializeRecyclerView() {
        ExerciseRowItemAdapter exerciseRowItemAdapter = new ExerciseRowItemAdapter(this, exercisesList);

        reference = FirebaseDatabase.getInstance().getReference("Exercises");


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Exercise exercise = snapshot.getValue(Exercise.class);

                exercisesList.add(exercise);
                exerciseRowItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                exerciseRowItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                exerciseRowItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(exerciseRowItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}