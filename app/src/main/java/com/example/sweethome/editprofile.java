package com.example.sweethome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {
    EditText fullname,email,phone;
    Button editprofile;
    FirebaseFirestore mfirestore;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        fullname=findViewById(R.id.editTextTextPersonName3);
        email=findViewById(R.id.editTextTextEmailAddress);
        phone=findViewById(R.id.editTextPhone2);
        editprofile=findViewById(R.id.buttonedit);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mfirestore=FirebaseFirestore.getInstance();
        user=mFirebaseAuth.getCurrentUser();
        userid= mFirebaseAuth.getCurrentUser().getUid();

        //display user details in firestre
        DocumentReference documentReference= mfirestore.collection("Users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phone.setText(documentSnapshot.getString("tele"));
                email.setText(documentSnapshot.getString("email"));
                fullname.setText(documentSnapshot.getString("fname"));
            }
        });

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
}