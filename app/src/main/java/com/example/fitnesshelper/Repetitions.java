package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.adapters.EditWorkoutTemplateAdapter;
import com.example.fitnesshelper.adapters.RepetitionAdapter;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.Repetition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Repetitions extends AppCompatActivity {

    TextView excnametTv;
    RecyclerView recyclerView;
    FloatingActionButton repetitionAddBtn;
    String excname,wtKey,excKey;
    String uid;
    String repetitionKey;
    DatabaseReference reference;
    private ArrayList<Repetition> reps;
    RepetitionAdapter repetitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetitions);

        excnametTv = findViewById(R.id.repetitionExerciseTitleTv);
        recyclerView = findViewById(R.id.repetitionExerciseRv);
        repetitionAddBtn = findViewById(R.id.repetitionAddBtn);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reps = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            excname = extras.getString("excname1");
            wtKey = extras.getString("wtKey");
            excKey = extras.getString("excKey");
        }

        excnametTv.setText(excname);
        initializeRecyclerView();

        repetitionAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actualserie = String.valueOf(reps.size()+1);
                reference = FirebaseDatabase.getInstance().getReference("Users");
                repetitionKey = reference.child(uid).child("Templates").child(wtKey).child("Exercises").child(excKey).child("Repetition").push().getKey().toString();
                Repetition rep = new Repetition(actualserie,"0","0",excname,"0",repetitionKey,"");
                //Toast.makeText(Repetitions.this, "size: " + reps.size(), Toast.LENGTH_SHORT).show();

                reference.child(uid).child("Templates").child(wtKey).child("Exercises").child(excKey).child("Repetition").child(repetitionKey).setValue(rep)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Repetitions.this, "Sikeres ismétlés felvétel", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Repetitions.this, "Hiba történt, próbáld újra!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                reps.add(reps.size(),rep);
                repetitionAdapter.notifyItemInserted(reps.size());
                //initializeRecyclerView();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    private void initializeRecyclerView() {
        repetitionAdapter = new RepetitionAdapter(this, reps, uid, wtKey, excKey);

        reps.clear();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(uid).child("Templates").child(wtKey).child("Exercises").child(excKey).child("Repetition").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Repetition repetition = dataSnapshot.getValue(Repetition.class);

                    reps.add(repetition);
                    repetitionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(repetitionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}