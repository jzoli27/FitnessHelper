package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.models.User;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private TextView registerTv, login_forgotpwTv;
    private Button loginbtn;
    private ImageView googleBtn;
    private EditText email_et,password_et;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private BeginSignInRequest signInRequest;
    private SignInClient oneTapClient;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");

    /* Bezárja az appot
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerTv = findViewById(R.id.login_signup);
        login_forgotpwTv = findViewById(R.id.login_forgotpwTv);
        loginbtn = findViewById(R.id.login_signinbtn);
        email_et = findViewById(R.id.loginEmail_et);
        password_et = findViewById(R.id.loginPassword_et);
        progressBar = findViewById(R.id.login_progressBar);
        googleBtn = findViewById(R.id.signinWithGoogleBtn);

        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        email_et.setText("csaba@gmail.com");
        password_et.setText("123456");

        //----google
        createRequest();
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //----google

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login_forgotpwTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage() + "hiba", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Mükődik, csa első belépéskor hozza létre a fiókot realtime db-ben
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNew){
                                User googleuser = new User("",user.getEmail(),"","");
                                userReference.child(user.getUid()).setValue(googleuser);
                            }
                            //Mükődik, csak első belépéskor hozza létre a fiókot realtime db-ben
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("507148898474-v53ttsjsbgjqaog0mqu7qq114evgcfpb.apps.googleusercontent.com")
                .requestEmail()
                .build();
        
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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