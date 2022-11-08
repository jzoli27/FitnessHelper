package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesshelper.adapters.ExercisesAdapter;
import com.example.fitnesshelper.adapters.FMDetailsAdapter;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.FitnessMachine;
import com.example.fitnesshelper.models.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FitnessMachineDetails extends AppCompatActivity {
    private TextView fmName;
    private ImageView fmImg, fmDetails_cameraIv, fmDetails_galleryIv;
    private RecyclerView recyclerView;
    //private Button logBtn;
    private FloatingActionButton addSettingBtn;

    private String fitnessMName;
    private String fmKey;
    private String fmImgLink;


    private HashMap<String,String> hashmap;
    private HashMap<String,String> key_value;
    private ArrayList<String> Settingkey;
    private ArrayList<String> Settingvalue;

    FMDetailsAdapter fmDetailsAdapter;

    private DatabaseReference SettingsReference = FirebaseDatabase.getInstance().getReference("FitnessMachine");
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference db;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Uri imaggeUri;

    Intent datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_machine_details);

        fmName = findViewById(R.id.fmDetailsNameTv);
        fmImg = findViewById(R.id.fmDetailsIv);
        fmDetails_cameraIv = findViewById(R.id.fmDetails_cameraIv);
        fmDetails_galleryIv = findViewById(R.id.fmDetails_galleryIv);
        recyclerView = findViewById(R.id.fmDetailsRv);
        //logBtn = findViewById(R.id.logBtn);
        addSettingBtn = findViewById(R.id.addSettingBtn);

        hashmap = new HashMap<>();
        key_value = new HashMap<>();
        Settingkey = new ArrayList<String>();
        Settingvalue = new ArrayList<String>();



        Bundle extras = getIntent().getExtras();
        if (extras != null){

            fitnessMName = extras.getString("name");
            fmKey = extras.getString("fmKey");
            fmImgLink = extras.getString("imgLink");
        }

        fmName.setText(fitnessMName);

        initializeData();

        SettingsReference.child(uid).child(fmKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FitnessMachine fitnessMachine = snapshot.getValue(FitnessMachine.class);
                if (!fitnessMachine.getImgLink().equals("") && !fitnessMachine.getImgLink().isEmpty()){
                    Picasso.get().load(fitnessMachine.getImgLink()).into(fmImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fmDetails_cameraIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        fmDetails_galleryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        addSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = "id";
                String value = "id";


                key_value.put(key,value);
                SettingsReference.child(uid).child(fmKey).child("Settings").push().setValue(key_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Settingkey.add(key);
                            Settingvalue.add(value);
                            fmDetailsAdapter.notifyItemInserted(Settingkey.size());
                        }else{
                            Toast.makeText(FitnessMachineDetails.this, "Sikertelen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        /*
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Map.Entry<String, String> entry : hashmap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Log.d("HASHMAP", "key =  " + key + " value: " + value);
                }

                for (int i = 0; i < Settingkey.size(); i++){
                    Log.d("HASHMAP", "key =  " + Settingkey.get(i) + " value: " + Settingvalue.get(i));
                }
                 for (Map.Entry<String, String> entry : hashmap.entrySet()) {
                    //String key = entry.getKey();
                    //Object value = entry.getValue();
                    Settingkey.add(entry.getKey());
                    Settingvalue.add(entry.getValue());
                }



                for (int i = 0; i < Settingkey.size(); i++){
                    Log.d("HASHMAP", "key =  " + Settingkey.get(i) /*+ " value: " + Settingvalue.get(i));
                }
            }
        });
         */

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    private void initializeData() {
        SettingsReference.child(uid).child(fmKey).child("Settings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot current_data: snapshot.getChildren()){
                    Log.d("FOR1", "data: " + current_data.getKey() );

                    for(DataSnapshot current_user_data: current_data.getChildren()){
                        Log.d("FOR2", "FOR2: " + current_user_data.getKey());
                        hashmap = (HashMap<String, String>) current_data.getValue();
                        if (!hashmap.isEmpty()){
                            handleData();
                            initializeRecyclerView();
                        }
                    }
                    /*hashmap = (HashMap<String, String>) current_data.getValue();
                    if (!hashmap.isEmpty()){
                        handleData();
                        initializeRecyclerView();
                    }
                     */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleData() {
        //Settingkey.clear();
        //Settingvalue.clear();
        for (Map.Entry<String, String> entry : hashmap.entrySet()) {
            Settingkey.add(entry.getKey() + " :");
            Settingvalue.add(entry.getValue());
        }
    }

    private void initializeRecyclerView() {
        //initializeData();

        fmDetailsAdapter = new FMDetailsAdapter(this, Settingkey, Settingvalue);

        recyclerView.setAdapter(fmDetailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imaggeUri = getImageUri(getApplicationContext(), selectedImage);
                fmImg.setImageBitmap(selectedImage);

                if(imaggeUri!= null){
                    uploadToFirebase(imaggeUri);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(FitnessMachineDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(FitnessMachineDetails.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
        /*
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            //imageUri = data.getData();
            //fmImg.setImageURI(imageUri);
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            fmImg.setImageBitmap(captureImage);

            /* ez jó profilkép feltöltésre?? lehet ez nem kellene ide..vagy de :)
            if (imageUri != null){
                uploadToFirebase(imageUri);

            }


        } */

        if (requestCode == 100 && resultCode == RESULT_OK && data != null){

            //régi kép illesztés az Iv-be
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            fmImg.setImageBitmap(captureImage);
            imaggeUri = getImageUri(getApplicationContext(), captureImage);
            datas = data;
            if(imaggeUri!= null){
                uploadToFirebase(imaggeUri);
            }

        }else if(requestCode == RESULT_CANCELED){
            Toast.makeText(FitnessMachineDetails.this, "Nem készült kép", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(FitnessMachineDetails.this.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }

    private void uploadToFirebase(Uri imageUri) {
        StorageReference fileref = storageReference.child("FitnessMachine").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(DetailsActivity.this, "Siker", Toast.LENGTH_SHORT).show();
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        db =  FirebaseDatabase.getInstance().getReference().child("FitnessMachine").child(uid).child(fmKey);
                        db.child("imgLink").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(FitnessMachineDetails.this, "Siker", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //String machinename = nameEt.getText().toString();
                        //String link = uri.toString();

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
                Toast.makeText(FitnessMachineDetails.this, "Hiba: " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}