package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.models.WorkoutTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateWorkoutTemplate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView templateTitleTv, mouscleGroupTv;
    private EditText templateNameEt, noteEt, timeEt;
    private Button saveBtn;

    private Spinner categorySpinner;
    private  String categoryText;

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
        mouscleGroupTv = findViewById(R.id.mouscleGroupTv);
        saveBtn = findViewById(R.id.workoutTemplateSaveButton);

        categorySpinner = findViewById(R.id.categorySpinner);

        categoryText = " ";

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = templateNameEt.getText().toString();
                String note = noteEt.getText().toString();
                String time = timeEt.getText().toString();
                //String mouscle = mouscleGroupTv.getText().toString();
                String wtKey = db.getReference("Users").push().getKey().toString();

                WorkoutTemplate workoutTemplate = new WorkoutTemplate(name,note,time,categoryText, wtKey,"createdByUser");

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categoryText = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}