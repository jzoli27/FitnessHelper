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
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.Image;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.User;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    ImageView profileiv, imgLoaderIv;
    TextView nameTv,emailTv;
    private Uri imageUri;
    private ProgressBar profile_progressBar;
    private Button  logoutBtn, logBtn;
    private ArrayList<String> sablonnev;
    private ArrayList<String> gyakorlatnev;
    private ArrayList<WorkoutDetails> hope;

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
        logBtn = view.findViewById(R.id.logBtn);

        profile_progressBar.setVisibility(View.INVISIBLE);
        sablonnev = new ArrayList<>();
        gyakorlatnev = new ArrayList<>();
        hope = new ArrayList<>();

        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ellenőrzés miatt kapcsoltam ki
                //FirebaseAuth.getInstance().signOut();
                //Intent intent = new Intent(getActivity(), LoginActivity.class);
                //startActivity(intent);
                Log.d("HOSSZ", "Sablonnev lista hossza: " + sablonnev.size());
                Log.d("HOSSZ", "Gyakorlatnev lista hossza: " + gyakorlatnev.size());
                for (int i = 0; i < hope.size(); i++){
                    Log.d("Hope:", " nev " + hope.get(i).getName() + " db: " + hope.get(i).getRepetitionnumber() + " gyakorlatnev: " + hope.get(i).getExercisename() + " size " + hope.size());
                }


            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDbReference.child("Templates").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        hope.clear();
                        for(DataSnapshot current_data: snapshot.getChildren()){
                            WorkoutTemplate workoutTemplate = current_data.getValue(WorkoutTemplate.class);
                            //sablon név kell a listába
                            String name = workoutTemplate.getName();
                            sablonnev.add(name);
                            Log.d("FOR1", " ");
                            Log.d("FOR1", "data1: " + current_data.getKey() );
                            Log.d("FOR1", "for1 children: " + current_data.getChildrenCount() );


                            for(DataSnapshot current_user_data: current_data.getChildren()){


                                Log.d("FOR2", " ");
                                Log.d("FOR2", "FOR2: "+current_user_data.getKey());
                                Log.d("FOR2", "for2 children: "+current_user_data.getChildrenCount());

                                for(DataSnapshot for3: current_user_data.getChildren()){
                                    Exercise exercise = for3.getValue(Exercise.class);
                                    //gyak név kell a listába
                                    String excname = exercise.getExerciseName();
                                    gyakorlatnev.add(excname);
                                    Log.d("FOR3", " ");
                                    Log.d("FOR3", "FOR3, Handling data "+for3.getKey());
                                    Log.d("FOR3", "for3 children:, Handling data "+for3.getChildrenCount());

                                    for(DataSnapshot for4: for3.getChildren()){
                                        //lehetne egy counter ellenőrizni, hogy hányadszor tesszük be a listába
                                        //itt lehetne a kiszedett változókkal létrehozni model osztály szerinti példányt és beletenni egy listába
                                        //Log.d("FOR4", " f1: " + current_data.getKey() + " for3: " + for3.getKey() +" for4: "+for4.getKey());
                                        //Log.d("FOR4", "wtname: " + name);
                                        Log.d("FOR4", " ");
                                        Log.d("FOR4", "for4: " + for4.getKey());
                                        //Log.d("FOR4", "excname: " + excname);
                                        //Log.d("FOR4", "childs after for1: " + current_data.getChildrenCount() + "childs after for3: " + for3.getChildrenCount());
                                        //repetition count kellene a listába
                                        Log.d("FOR4", "childs after for4: " + for4.getChildrenCount());
                                        String count = String.valueOf(for4.getChildrenCount());
                                        int checksum = Integer.valueOf(count);
                                        if (checksum != 0){
                                            WorkoutDetails workoutDetails = new WorkoutDetails(name,count,excname);
                                            hope.add(workoutDetails);
                                        }
                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


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
                    //ez jó, csak a tesztelésekhez ne kelljen mindig töltögetni.
                    if (!user.getProfileImgLink().equals("")){
                        Picasso.get().load(user.getProfileImgLink()).into(profileiv);
                    }

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
