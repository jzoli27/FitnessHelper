package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.adapters.EditWorkoutTemplateAdapter;
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
import java.util.Collections;

public class EditWorkoutTemplateActivity extends AppCompatActivity {

    TextView workoutTemplateNameTv;
    RecyclerView recyclerView;
    FloatingActionButton addExerciseToWorkoutTemplateBtn;
    private ArrayList<Exercise> exercisesList;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DatabaseReference UsersReference;
    EditWorkoutTemplateAdapter editWorkoutTemplateAdapter;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String wtKey,wtName;
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


        workoutTemplateNameTv.setText(wtName);

        initializeRecyclerView();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        addExerciseToWorkoutTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectableExercisesActivity.class);
                intent.putExtra("wtKey", wtKey);
                startActivity(intent);

            }
        });

    }

    //ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
    //        ItemTouchHelper.START | ItemTouchHelper.END, 0) {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.START | ItemTouchHelper.END, 0) {
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
            exercisesList.remove(viewHolder.getAdapterPosition());
            editWorkoutTemplateAdapter.notifyDataSetChanged();
        }
    };

    private void initializeRecyclerView() {
        editWorkoutTemplateAdapter = new EditWorkoutTemplateAdapter(this, exercisesList);

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

}