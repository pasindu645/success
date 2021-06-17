package com.example.sweethome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {
    EditText fullname,email,phone;
    Button editprofile;
    FirebaseFirestore mfirestore;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    String userid;
    ImageView dp;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        fullname=findViewById(R.id.editTextTextPersonName3);
        email=findViewById(R.id.editTextTextEmailAddress);
        phone=findViewById(R.id.editTextPhone2);
        editprofile=findViewById(R.id.buttonedit);
        dp=findViewById(R.id.imageView2);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mfirestore=FirebaseFirestore.getInstance();
        user=mFirebaseAuth.getCurrentUser();
        userid= mFirebaseAuth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference();

        StorageReference image = storageReference.child("Users/" + mFirebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                Picasso.get().load(uri).into(dp);
            }
        });


                //display user details in firebase
                DocumentReference documentReference = mfirestore.collection("Users").document(userid);
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phone.setText(documentSnapshot.getString("tele"));
                email.setText(documentSnapshot.getString("email"));
                fullname.setText(documentSnapshot.getString("fname"));
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });
            // update profile details
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullname.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty()){
                    Toast.makeText(editprofile.this, "Fileds are empty ", Toast.LENGTH_SHORT).show();
                    return;
                }
                String emails=email.getText().toString();
                user.updateEmail(emails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = mfirestore.collection("Users").document(userid);
                        Map<String,Object> edit= new HashMap<>();
                        edit.put("fname",fullname.getText().toString());
                        edit.put("tele",phone.getText().toString());
                        edit.put("email",emails);

                        documentReference.update(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(editprofile.this, "profile update successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        });




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(editprofile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                Uri imageUri =data.getData();
                uploadImageToFirebase(imageUri);
            }
        }

    }
    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference imageref = storageReference.child("Users/" + mFirebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        imageref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        Picasso.get().load(uri).into(dp);
                        //updatedpDownloadUrl(uri.toString());
                      // Picasso.load(uri).into(dp);
                    }
                });
                Toast.makeText(editprofile.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(getApplicationContext(), "upload failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}