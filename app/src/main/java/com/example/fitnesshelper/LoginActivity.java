package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView registerTv;
    private Button loginbtn;
    private EditText email_et,password_et;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerTv = findViewById(R.id.login_signup);
        loginbtn = findViewById(R.id.login_signinbtn);
        email_et = findViewById(R.id.loginEmail_et);
        password_et = findViewById(R.id.loginPassword_et);
        progressBar = findViewById(R.id.login_progressBar);

        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        email_et.setText("csaba@gmail.com");
        password_et.setText("123456");

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);

        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        if (email.isEmpty()){
            email_et.setError("A jelszó üres!");
            email_et.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        } /*else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_et.setError("nem megfelelő email formátum");
            email_et.requestFocus();
            return;
        }*/


        if (password.isEmpty() ){
            password_et.setError("A Jelszó üres!");
            password_et.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }else if (password.length() < 6){
            password_et.setError("A jelszó legalább 6 karakter hosszú!");
            password_et.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Sikeres bejelentkezés -> átirányitás
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Sikeres bejelentkezés",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Sikertelen bejelenkezés!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}