package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.models.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class EditWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText exercisename, description ;
    private TextView mousclegroup;
    private Button saveExerciseBtn;
    private Spinner categorySpinner;
    private  String categoryText;

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        exercisename = findViewById(R.id.exerciseNameEt);
        description = findViewById(R.id.description);
        mousclegroup = findViewById(R.id.mouscleGroupTv);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveExerciseBtn = findViewById(R.id.saveRepetitionBtn);

        categoryText = " ";

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);

        saveExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String excname = exercisename.getText().toString();
                //String mgroup = mousclegroup.getText().toString();
                String descript = description.getText().toString();
                String exckey = db.getReference("Exercises").push().getKey().toString();


                Exercise exercise = new Exercise(excname,"createdbyuser",descript,categoryText,"https://firebasestorage.googleapis.com/v0/b/fitnesshelper-ab986.appspot.com/o/android.png?alt=media&token=839d5d62-cbeb-4292-b33c-ddd07b85b8f9",exckey, false);

                db.getReference("Exercises").child(exckey).setValue(exercise).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(EditWorkoutActivity.this,
                                    "Sikeres  felv??tel!", Toast.LENGTH_SHORT).show();
                            exercisename.setText("");

                            //Cancelt dobunk vissza, hogy m??sik oldalt ??rz??kelj??k, hogy bez??rult ez az activity ??s ez??ltal friss??thetj??k az adaptert.
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
                            finish();

                        }else{
                            Toast.makeText(EditWorkoutActivity.this,
                                    "Sikertelen felv??tel!", Toast.LENGTH_SHORT).show();
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