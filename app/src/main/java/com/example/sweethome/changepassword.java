package com.example.sweethome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class changepassword extends AppCompatActivity {

    EditText newp,confirmp,currentp;
    TextView hideview;
    Button setpassword;

    FirebaseFirestore mfirestore;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    String userid;
    ImageView dp;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        newp=findViewById(R.id.editTextTextPassword3);
        currentp=findViewById(R.id.editTextTextPassword5);
        confirmp=findViewById(R.id.editTextTextPassword4);
        setpassword=findViewById(R.id.setp);
        hideview=findViewById(R.id.hideview);


        mFirebaseAuth=FirebaseAuth.getInstance();
        mfirestore=FirebaseFirestore.getInstance();
        user=mFirebaseAuth.getCurrentUser();
        userid= mFirebaseAuth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference();

        DocumentReference documentReference= mfirestore.collection("Users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                hideview.setText(documentSnapshot.getString("pwd"));

            }
        });

        setpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newpws = newp.getText().toString();
                String conpws = confirmp.getText().toString();
                String currpws=currentp.getText().toString();
                final FirebaseUser user = mFirebaseAuth.getCurrentUser();


                if(!hideview.getText().equals(currpws)) {
                    currentp.setError("Current password is not match");
                    return;
                }

                if (newpws.length()<6) {
                    newp.setError("Password must be at least 6 characters");
                    return;
                }


                if (!newpws.equals(conpws)) {
                    confirmp.setError("Please enter correct password");
                    return;
                }

                if (newp.getText().toString().isEmpty() || currentp.getText().toString().isEmpty() || confirmp.getText().toString().isEmpty()){
                    Toast.makeText(changepassword.this, "Fileds are empty ", Toast.LENGTH_SHORT).show();
                    return;
                }


                String password=newp.getText().toString();
                user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = mfirestore.collection("Users").document(userid);
                        Map<String,Object> edit= new HashMap<>();
                        edit.put("pwd",password);

                        documentReference.update(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(changepassword.this, "profile update successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(changepassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });






    }
}