package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.adapters.EditWorkoutTemplateAdapter;
import com.example.fitnesshelper.adapters.ExerciseRowItemAdapter;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class EditWorkoutTemplateActivity extends AppCompatActivity implements EditWorkoutTemplateAdapter.OnNoteListener {

    TextView workoutTemplateNameTv;
    RecyclerView recyclerView;
    FloatingActionButton addExerciseToWorkoutTemplateBtn;
    private ArrayList<Exercise> exercisesList;
    ArrayList<String> repnumber;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DatabaseReference UsersReference;
    EditWorkoutTemplateAdapter editWorkoutTemplateAdapter;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String wtKey,wtName;
    int LAUNCH_SECOND_ACTIVITY = 1;
    private DatabaseReference userDbReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout_template);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            wtKey = extras.getString("wtKey");
            wtName = extras.getString("name");

        }

        recyclerView = findViewById(R.id.editWorkoutTemplateRv);
        workoutTemplateNameTv = findViewById(R.id.editWorkoutTemplateNameTv);
        addExerciseToWorkoutTemplateBtn = findViewById(R.id.addToWorkoutTemplateBtn);

        exercisesList = new ArrayList<>();
        repnumber = new ArrayList<>();


        workoutTemplateNameTv.setText(wtName);


        initializeRecyclerView();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        addExerciseToWorkoutTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectableExercisesActivity.class);
                intent.putExtra("wtKey", wtKey);
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
                //startActivity(intent);
            }
        });
    }

    //ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
    //        ItemTouchHelper.START | ItemTouchHelper.END, 0) {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            //Toast.makeText(EditWorkoutTemplateActivity.this, "name: " + exercisesList.get(fromPosition).getExerciseName(), Toast.LENGTH_SHORT).show();
            Collections.swap(exercisesList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(uid).child("Templates").child(wtKey).child("Exercises").child(exercisesList.get(viewHolder.getAdapterPosition()).getExerciseKey()).removeValue();
            exercisesList.remove(viewHolder.getAdapterPosition());
            editWorkoutTemplateAdapter.notifyDataSetChanged();
        }
    };

    private void initializeRecyclerView() {
        showLogcat();

        editWorkoutTemplateAdapter = new EditWorkoutTemplateAdapter(this, exercisesList, repnumber,this);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(uid).child("Templates").child(wtKey).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);

                    exercisesList.add(exercise);
                    editWorkoutTemplateAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(editWorkoutTemplateAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showLogcat() {
        userDbReference.child("Templates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot current_data: snapshot.getChildren()){
                    WorkoutTemplate workoutTemplate = current_data.getValue(WorkoutTemplate.class);
                    //sablon név kell a listába
                    String name = workoutTemplate.getName();

                    for(DataSnapshot current_user_data: current_data.getChildren()){

                        for(DataSnapshot for3: current_user_data.getChildren()){
                            Exercise exercise = for3.getValue(Exercise.class);
                            //gyak név kell a listába
                            String excname = exercise.getExerciseName();

                            for(DataSnapshot for4: for3.getChildren()){

                                String count = String.valueOf(for4.getChildrenCount());
                                int checksum = Integer.valueOf(count);
                                if (checksum != 0){
                                    WorkoutDetails workoutDetails = new WorkoutDetails(name,count,excname);
                                    repnumber.add(count);
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateListForRv(){
        reference = FirebaseDatabase.getInstance().getReference("Users");

        exercisesList.clear();
        reference.child(uid).child("Templates").child(wtKey).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);

                    exercisesList.add(exercise);
                    editWorkoutTemplateAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this, "most lett nyomva a result", Toast.LENGTH_SHORT).show();
                //exercisesList.clear();
                updateListForRv();
                //initializeRecyclerView();
                // Write your code if there's no result
            }
        }
    }


    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, Repetitions.class);
        intent.putExtra("excname1",exercisesList.get(position).getExerciseName().toString());
        intent.putExtra("wtKey", wtKey);
        intent.putExtra("excKey", exercisesList.get(position).getExerciseKey());
        startActivity(intent);
    }
}