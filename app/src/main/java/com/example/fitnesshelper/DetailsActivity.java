package com.example.fitnesshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitnesshelper.models.FitnessMachine;
import com.example.fitnesshelper.models.Image;
import com.example.fitnesshelper.models.Machine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView, cameraIv;
    EditText detailsMachineNameEt;
    Button saveMachineImageBtn;
    private Uri imageUri = null;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference db;
    Intent datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        imageView = (ImageView) findViewById(R.id.imageView1);
        detailsMachineNameEt = findViewById(R.id.detailsMachineNameEt);
        cameraIv = (ImageView) findViewById(R.id.cameraIv);
        saveMachineImageBtn = (Button) findViewById(R.id.saveMachineImageBtn);

        if (ContextCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DetailsActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }

        cameraIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
                /*
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, 100);

                 */
            }
        });

        saveMachineImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(DetailsActivity.this, "Uri: " + imageUri + "Path: " + getRealPathFromURI(imageUri), Toast.LENGTH_SHORT).show();

                if(imageUri == null){
                    Toast.makeText(DetailsActivity.this, "Üres a Uri", Toast.LENGTH_SHORT).show();
                }

                if (imageUri != null){
                    //Toast.makeText(DetailsActivity.this, "típus: " + getFileExtension(imageUri), Toast.LENGTH_SHORT).show();
                    uploadToFirebase(imageUri);
                }else{
                    Toast.makeText(DetailsActivity.this, "Kérlek válassz egy képet!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void uploadToFirebase(Uri imageUri) {
        StorageReference fileref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(DetailsActivity.this, "Siker", Toast.LENGTH_SHORT).show();
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageid = databaseReference.push().getKey();
                        Image image = new Image(uri.toString());
                        String machineName = detailsMachineNameEt.getText().toString();
                        FitnessMachine fitnessMachine = new FitnessMachine(machineName,uri.toString(),imageid);

                        db =  FirebaseDatabase.getInstance().getReference().child("FitnessMachine");
                        db.setValue(fitnessMachine).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                detailsMachineNameEt.setText("");
                            }
                        });

                        databaseReference.child(imageid).setValue(image).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(DetailsActivity.this, "Sikeres felvétel", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(DetailsActivity.this, "Sikertelen felvétel", Toast.LENGTH_SHORT).show();
                                }
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
                Toast.makeText(DetailsActivity.this, "Hiba: " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null){
            //Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //captureImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //String path = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), captureImage, "Title", null);
            //imageUri = Uri.parse(path);
            //imageView.setImageBitmap(captureImage);

            //régi kép illesztés az Iv-be
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
            imageUri = getImageUri(getApplicationContext(), captureImage);
            datas = data;

            //imageUri = data.getData();
            //imageView.setImageURI(imageUri);

            //File object of camera image
            //File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");

            //Uri of camera image
            //Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
            //imageUri = uri;
        }else if(requestCode == RESULT_CANCELED){
            Toast.makeText(DetailsActivity.this, "Nem készült kép", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(DetailsActivity.this.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }
}