package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnesshelper.models.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class EditWorkoutActivity extends AppCompatActivity {
    private EditText exercisename, mousclegroup;
    private Button saveExerciseBtn;

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        exercisename = findViewById(R.id.exerciseNameEt);
        mousclegroup = findViewById(R.id.mouscleGroupEt);

        saveExerciseBtn = findViewById(R.id.saveRepetitionBtn);

        saveExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String excname = exercisename.getText().toString();
                String mgroup = mousclegroup.getText().toString();
                String exckey = db.getReference("Exercises").push().getKey().toString();

                Exercise exercise = new Exercise(excname,"predefined"," ",mgroup," ",exckey, false);

                db.getReference("Exercises").child(exckey).setValue(exercise).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(EditWorkoutActivity.this,
                                    "Sikeres  felvétel!", Toast.LENGTH_SHORT).show();
                            exercisename.setText("");

                        }else{
                            Toast.makeText(EditWorkoutActivity.this,
                                    "Sikertelen felvétel!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }
}