package com.example.fitnesshelper.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesshelper.LoginActivity;
import com.example.fitnesshelper.MainActivity;
import com.example.fitnesshelper.models.Image;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.User;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    ImageView profileiv, imgLoaderIv;
    TextView nameTv,emailTv;
    private Uri imageUri;
    private ProgressBar profile_progressBar;
    private Button  logoutBtn;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid().toString()).child("profileimg");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private DatabaseReference userDbReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid().toString());

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "MainActivity";
    private SignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container,false);

        profileiv = view.findViewById(R.id.profile_Iv);
        imgLoaderIv = view.findViewById(R.id.profile_imgLoader_Iv);
        profile_progressBar = view.findViewById(R.id.profile_progressBar);
        nameTv = view.findViewById(R.id.profile_nameTv);
        emailTv = view.findViewById(R.id.profile_emailTv);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        profile_progressBar.setVisibility(View.INVISIBLE);

        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //NavigationView navigationView = view.findViewById(R.id.nav_view);
        //Próba a nav_header átírására
        //View headerView = navigationView.getHeaderView(0);
        //TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_nameTv);
        //navUsername.setText("teszt");



        imgLoaderIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });

        if (mAuth.getCurrentUser() != null){
            emailTv.setText(mAuth.getCurrentUser().getEmail().toString());
        }

        userDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                user = snapshot.getValue(User.class);
                if (user != null){
                    //nameTv.setText(user.getName());
                    emailTv.setText(user.getEmail());
                    /* ez jó, csak a tesztelésekhez ne kelljen mindig töltögetni.
                    if (!user.getProfileImgLink().equals("")){
                        Picasso.get().load(user.getProfileImgLink()).into(profileiv);
                    }
                    */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });

        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Image img = new Image();
                img = snapshot.getValue(Image.class);
                //Toast.makeText(getActivity(),img.getImageUrl() , Toast.LENGTH_SHORT).show();
                if (img != null){
                    Picasso.get().load(img.getImageUrl()).into(profileiv);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });

         */

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            profileiv.setImageURI(imageUri);

            /* ez jó profilkép betöltésre, csak ameddig lehet spórolok a firebase müveletekkel.
            if (imageUri != null){
                uploadToFirebase(imageUri);

            }

             */
        }
    }


    private void uploadToFirebase(Uri imageUri) {
        profile_progressBar.setVisibility(View.VISIBLE);
        StorageReference fileref = storageReference.child("ProfileImages").child(mAuth.getCurrentUser().getUid().toString()).child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Image image = new Image(uri.toString());
                        //userDbReference.child("profileImgLink").setValue(image);

                        String link = uri.toString();
                        userDbReference.child("profileImgLink").setValue(uri.toString());

                        profile_progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Profilkép sikeresen feltöltve!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Sikertelen", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }
}
