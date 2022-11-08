package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.fragments.NewExercisesFragment;
import com.example.fitnesshelper.fragments.ProfileFragment;
import com.example.fitnesshelper.fragments.ReminderFragment;
import com.example.fitnesshelper.fragments.SettingsFragment;
import com.example.fitnesshelper.fragments.VitaminFragment;
import com.example.fitnesshelper.fragments.WorkoutFragment;
import com.example.fitnesshelper.models.Image;
import com.example.fitnesshelper.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    TextView navHeaderName,navHeaderEmail;
    ImageView navheaderImageView;
    Image img;
    Bitmap image;
    Uri uri;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference userDbReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid().toString());
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid().toString());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        userDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                user = snapshot.getValue(User.class);
                if (user != null && !user.getProfileImgLink().equals("")){

                    navheaderImageView = (ImageView) headerView.findViewById(R.id.IV);
                    // Végre így jól betölti a képet, azonban spórolás miatt kikapcsolom, majd kapcsold vissza!!!!....
                    Picasso.get().load(user.getProfileImgLink()).into(navheaderImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });


        //Próba a nav_header átírására
        if (mAuth.getCurrentUser() != null){
            String fullname = mAuth.getCurrentUser().getEmail().toString();
            String[] separated = fullname.split("@");
            //String name = fullname.substring(0,6);
            navHeaderName = (TextView) headerView.findViewById(R.id.nav_header_nameTv);
            navHeaderEmail = (TextView) headerView.findViewById(R.id.nav_header_email_Tv);
            navHeaderName.setText(separated[0]);
            navHeaderEmail.setText(mAuth.getCurrentUser().getEmail().toString());

        }



        navigationView.setNavigationItemSelectedListener(MainActivity.this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar
                ,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();



        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }

    }

    //Ez tölti ki a három "..."-t a jobb felső sarokban, a toolbarban
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragments_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Ez kell, hogy ki tudd választani, melyikre klikkelt a user a jobb felső sarokban lévő toolbar opciók közül
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Toast.makeText(this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_planning:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReminderFragment()).commit();
                break;
            case R.id.nav_exercises:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NewExercisesFragment()).commit();
                //startActivity(new Intent(MainActivity.this,EditWorkoutActivity.class));
                break;
            case R.id.nav_workout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorkoutFragment()).commit();
                //startActivity(new Intent(MainActivity.this,CreateWorkoutTemplate.class));
                break;
            case R.id.nav_vitamin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VitaminFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}