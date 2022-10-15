package com.example.fitnesshelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Repetitions extends AppCompatActivity {

    TextView excnametTv;
    String excname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetitions);

        excnametTv = findViewById(R.id.repetitionExerciseTitleTv);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            excname = extras.getString("excname1");
        }

        excnametTv.setText(excname);
    }
}