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
import android.widget.Toast;

import com.example.fitnesshelper.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText name_et,email_et,password_et,confirmpw_et;
    Button registerBtn;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_et = findViewById(R.id.register_name_et);
        email_et = findViewById(R.id.register_email_et);
        password_et = findViewById(R.id.register_password_et);
        confirmpw_et = findViewById(R.id.register_confirmpw_et);
        registerBtn = findViewById(R.id.register_create_btn);

        progressBar = findViewById(R.id.register_progressBar);

        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = name_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirmpw = confirmpw_et.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);

        if (name.isEmpty()){
            name_et.setError("A név üres!");
            name_et.requestFocus();
            return;
        }
        if (email.isEmpty()){
            email_et.setError("Az email üres!");
            email_et.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_et.setError("nem megfelelő email formátum");
            email_et.requestFocus();
            return;
        }


        if (password.isEmpty() ){
            password_et.setError("A Jelszó üres!");
            password_et.requestFocus();
            return;
        }else if (password.length() < 6){
            password_et.setError("A jelszó legalább 6 karakter hosszú!");
            password_et.requestFocus();
            return;
        }

        if (confirmpw.isEmpty()){
            confirmpw_et.setError("A jelszó megerősítő üres vagy rövid!");
            confirmpw_et.requestFocus();
            return;
        }else if (confirmpw.length() < 6){
            confirmpw_et.setError("A megerősítő jelszó legalább 6 karakter hosszú!");
            confirmpw_et.requestFocus();
            return;
        }

        if (!(password.equals(confirmpw))){
            confirmpw_et.setError("A két jelszó nem egyezik!");
            confirmpw_et.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(name,email,password, "");

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(RegisterActivity.this,
                                                "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();
                                        // itt kell visszadobni a bejelentkezés fülre.
                                        startActivity(new Intent(RegisterActivity.this,
                                                LoginActivity.class));
                                    }else{
                                        Toast.makeText(RegisterActivity.this,
                                                "Sikertelen regisztráció!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterActivity.this, "Nem jó,Sikertelen regisztráció!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}