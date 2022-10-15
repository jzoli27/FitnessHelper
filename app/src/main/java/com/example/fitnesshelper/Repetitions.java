package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.models.Repetition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Repetitions extends AppCompatActivity {

    TextView excnametTv;
    RecyclerView recyclerView;
    FloatingActionButton repetitionAddBtn;
    String excname,wtKey,excKey;
    String uid;
    String repetitionKey;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetitions);

        excnametTv = findViewById(R.id.repetitionExerciseTitleTv);
        recyclerView = findViewById(R.id.repetitionExerciseRv);
        repetitionAddBtn = findViewById(R.id.repetitionAddBtn);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            excname = extras.getString("excname1");
            wtKey = extras.getString("wtKey");
            excKey = extras.getString("excKey");
        }

        excnametTv.setText(excname);

        repetitionAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("Users");
                repetitionKey = reference.child(uid).child("Templates").child(wtKey).child("Exercises").child(excKey).child("Repetition").push().getKey().toString();
                Repetition rep = new Repetition("1","0","0",excname,repetitionKey,"");

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
            }
        });
    }
}