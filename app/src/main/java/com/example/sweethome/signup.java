package com.example.sweethome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class signup extends AppCompatActivity {


    public EditText full_name,telephone,emailid,password,confirmpassword;
    Button btnregister;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    FirebaseAnalytics mFirebaseAnalytics;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("sign form");

        mFirebaseAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        emailid=findViewById(R.id.editTextTextEmailAddress2);
        password=findViewById(R.id.editTextTextPassword2);
        btnregister=findViewById(R.id.button6);
        full_name=findViewById(R.id.editTextTextPersonName);
        telephone=findViewById(R.id.editTextPhone);
        confirmpassword=findViewById(R.id.editTextTextPassword);

        if (mFirebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(signup.this ,MainActivity.class));
            finish();}
        btnregister.setOnClickListener(v -> {
          //  Bundle NewUserDataBundleReceived= getIntent().getExtras();
            String email = emailid.getText().toString();
            String pwd = password.getText().toString();
            String fname=full_name.getText().toString();
            String cpwd=confirmpassword.getText().toString();
            String tele=telephone.getText().toString();

            if (fname.isEmpty()) {
                full_name.setError("Please enter full name");
                return;
            }

             if (tele.isEmpty()) {
                telephone.setError("Please enter telephone number");
                return;
            }
             if (!telephone.getText().toString().matches("[0-9]{10}")){
                telephone.setError("Please enter valid number");
                return;
            }


             if (email.isEmpty()) {
                emailid.setError("Please enter email ld");
                return;
            }
             if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailid.setError("Please enter a valid email address");
                return;

            }

             if (pwd.isEmpty()) {
                password.setError("Please enter your password");
                return;

            }

             if (pwd.length()<5) {
                password.setError("Password must be at least 5 characters");
                return;
            }

             if (cpwd.isEmpty()) {
                confirmpassword.setError("Please enter again password");
                return;
            }
             if (!pwd.equals(cpwd)) {
                confirmpassword.setError("Please enter correct password");
                return;
            }

             /*
             Map<String, Object> user = new HashMap<>();
user.put("first", "Ada");
user.put("last", "Lovelace");
user.put("born", 1815);

// Add a new document with a generated ID
db.collection("users")
        .add(user)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
            */
           /* Bundle NewUserDataBundleReceived= getIntent().getExtras();
            final String fulname  = NewUserDataBundleReceived.getString("fullname");
            final String mobileNo = NewUserDataBundleReceived.getString("MobileNo");
            final String emailid = NewUserDataBundleReceived.getString("Email");
            final String password = NewUserDataBundleReceived.getString("Password");*/

            mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d("TAG","User created Successfully");
                Toast.makeText(signup.this, "signup successful ", Toast.LENGTH_SHORT).show();

                userid = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                DocumentReference documentReference = fstore.collection("Users").document(userid);
                Map<String,Object> user= new HashMap<>();
                user.put("fname",fname);
                user.put("tele",tele);
                user.put("email",email);
                user.put("pwd",pwd);

                documentReference.set(user).addOnSuccessListener(aVoid -> {
                    Log.d("TAG","upload success");
                    Toast.makeText(signup.this,"User details: done",Toast.LENGTH_SHORT).show();

                })
                        .addOnFailureListener(e -> {
                            Log.w("TAG","Failed");
                            Toast.makeText(signup.this,"user details: failed",Toast.LENGTH_SHORT).show();

                        });
                startActivity(new Intent(signup.this ,MainActivity.class));
            }
            else {
                if(task.getException()instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(signup.this,"Email is already registered",Toast.LENGTH_LONG).show();
                }
                Log.w("TAG","failed");
                Toast.makeText(signup.this, "signup falier ", Toast.LENGTH_SHORT).show();
            }
            });




        });
    }
}