package com.example.fitnesshelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseDetailsActivity extends AppCompatActivity {
    ImageView exercise_details_Iv;
    TextView exercise_details_descriptionTv;
    EditText exercise_details_descriptionInfoEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        exercise_details_Iv = findViewById(R.id.exercise_details_Iv);
        exercise_details_descriptionTv = findViewById(R.id.exercise_details_descriptionTv);
        exercise_details_descriptionInfoEt = findViewById(R.id.exercise_details_descriptionInfoET);
    }
}