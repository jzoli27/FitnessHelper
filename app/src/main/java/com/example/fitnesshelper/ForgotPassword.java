package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextView forgotPasswordTitleTv;
    private EditText forgotPasswordEmailEt;
    private Button forgotPasswordBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPasswordTitleTv = findViewById(R.id.forgotPasswordTitleTv);
        forgotPasswordEmailEt = findViewById(R.id.forgotPasswordEmailEt);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        progressBar = findViewById(R.id.forgotPasswordProgressBar);
        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = forgotPasswordEmailEt.getText().toString().trim();

        if (email.isEmpty()){
            forgotPasswordEmailEt.setError("Az email kitöltése kötelező!");
            forgotPasswordEmailEt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            forgotPasswordEmailEt.setError("Valós emailt írj be!");
            forgotPasswordEmailEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Visszaállító email elküldve!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    onBackPressed();
                }else{
                    Toast.makeText(ForgotPassword.this, "Hiba történt, próbáld meg újra!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}