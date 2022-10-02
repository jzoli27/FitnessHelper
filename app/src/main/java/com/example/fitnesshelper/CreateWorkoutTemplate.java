package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.models.WorkoutTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateWorkoutTemplate extends AppCompatActivity {

    private TextView templateTitleTv;
    private EditText templateNameEt, noteEt, timeEt, mouscleGroupEt;
    private Button saveBtn;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout_template);

        templateTitleTv = findViewById(R.id.templateTitle);

        templateNameEt = findViewById(R.id.templateNameEt);
        noteEt = findViewById(R.id.noteEt);
        timeEt = findViewById(R.id.timeEt);
        mouscleGroupEt = findViewById(R.id.mouscleGroupEt);

        saveBtn = findViewById(R.id.workoutTemplateSaveButton);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = templateNameEt.getText().toString();
                String note = noteEt.getText().toString();
                String time = timeEt.getText().toString();
                String mouscle = mouscleGroupEt.getText().toString();
                String wtKey = db.getReference("Users").push().getKey().toString();

                WorkoutTemplate workoutTemplate = new WorkoutTemplate(name,note,time,mouscle, wtKey,"createdByUser");

                db.getReference("Users").child(uid).child("Templates").child(wtKey).setValue(workoutTemplate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CreateWorkoutTemplate.this,
                                    "Sikeres sablon felvétel!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }else{
                            Toast.makeText(CreateWorkoutTemplate.this,
                                    "Sikertelen felvétel!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}