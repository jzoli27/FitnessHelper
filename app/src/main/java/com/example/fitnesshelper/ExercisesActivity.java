package com.example.fitnesshelper;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesshelper.fragments.ExercisesFragment;
import com.example.fitnesshelper.models.Altgyakorlatmodell;
import com.example.fitnesshelper.models.Image;
import com.example.fitnesshelper.models.Machine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;

public class ExercisesActivity extends AppCompatActivity implements Serializable {
    ImageView exerciseimageView,exerciseBackIv, exerciseDeleteIv;
    EditText nameEt, linkEt;
    TextView titleTv;

    Button uploadBtn,ImgPickerBtn;
    DBHelper dbHelper;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ProgressBar progressBar;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        exerciseimageView = findViewById(R.id.exercise_imageview);
        nameEt = findViewById(R.id.exercise_name);
        uploadBtn = findViewById(R.id.exercise_uploadbutton);
        titleTv = findViewById(R.id.exercises_titleTv);
        dbHelper = new DBHelper(ExercisesActivity.this);
        progressBar = findViewById(R.id.exercises_progressBar);
        exerciseBackIv = findViewById(R.id.exercises_backIv);
        ImgPickerBtn = findViewById(R.id.exercise_ImgPickerBtn);
        exerciseDeleteIv = findViewById(R.id.exercises_delete_Iv);

        String exercise_name = getIntent().getExtras().getString("gyakorlat");
        //Altgyakorlatmodell altgyakorlatmodell = (Altgyakorlatmodell) getIntent().getSerializableExtra("object");

        titleTv.setText(exercise_name);

        //Toolbar toolbar = findViewById(R.id.exercises_toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setTitle("");
        progressBar.setVisibility(View.INVISIBLE);



        exerciseBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImgPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        exerciseDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean success = dbHelper.deleteOneExercise(exercise_name);
                if (success == true){
                    Toast.makeText(ExercisesActivity.this, "Gyakorlat törölve!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("editTextValue", "value_here");
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                }else{
                    Toast.makeText(ExercisesActivity.this, "Nem sikerült törölni!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);

                }else{
                    Toast.makeText(ExercisesActivity.this, "Kérlek válassz egy képet!", Toast.LENGTH_SHORT).show();
                }

                /*
                String machinename = nameEt.getText().toString();
                String link = linkEt.getText().toString();

                Machine machine = new Machine(2,machinename,link);
                boolean success = dbHelper.addOne(machine);
                if (success == true){
                    Toast.makeText(ExercisesActivity.this, "Siker", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ExercisesActivity.this, "Szar", Toast.LENGTH_SHORT).show();
                }

                 */

            }
        });

        //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/fir-image-af637.appspot.com/o/uploads%2F1649769456969.jpg?alt=media&token=d6ecaa6e-49be-46e1-93bf-9645a72fabc3").into(exerciseimageView);
    }

    private void uploadToFirebase(Uri imageUri) {
        StorageReference fileref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        progressBar.setVisibility(View.VISIBLE);
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Image image = new Image(uri.toString());
                        String imageid = databaseReference.push().getKey();
                        databaseReference.child(imageid).setValue(image);

                        String machinename = nameEt.getText().toString();
                        String link = uri.toString();

                        Machine machine = new Machine(2,machinename,link);
                        boolean success = dbHelper.addOne(machine);
                        if (success == true){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ExercisesActivity.this, "Sikeres", Toast.LENGTH_SHORT).show();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ExercisesActivity.this, "Rossz", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(ExercisesActivity.this, "Sikeres feltöltés!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExercisesActivity.this, "Sikertelen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            exerciseimageView.setImageURI(imageUri);
        }
    }



    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }

}
