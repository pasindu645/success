package com.example.sweethome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class MainActivity extends AppCompatActivity {
    public Button btnresetpwd;
    Button btnsignout;
    Button btnresetemail;
    Button btneditprofile;
    ImageView dp;
    TextView fullname,email,phonenumber;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mfirestore;
    String userid;
    FirebaseUser user;
    StorageReference storageReference;

    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Hiiiii");


        btnresetpwd=findViewById(R.id.button4);
        btnsignout=findViewById(R.id.button3);
        btnresetemail=findViewById(R.id.button5);
        btneditprofile=findViewById(R.id.button7);
        dp=findViewById(R.id.dp1);
        fullname=findViewById(R.id.textView3);
        email=findViewById(R.id.textView4);
        phonenumber=findViewById(R.id.textView5);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mfirestore=FirebaseFirestore.getInstance();
        user= mFirebaseAuth.getCurrentUser();
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



        DocumentReference documentReference= mfirestore.collection("Users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable  DocumentSnapshot documentSnapshot, @Nullable  FirebaseFirestoreException e) {
            phonenumber.setText(documentSnapshot.getString("tele"));
            email.setText(documentSnapshot.getString("email"));
            fullname.setText(documentSnapshot.getString("fname"));
            }
        });
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

        /*dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });*/

        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent c = new Intent(MainActivity.this, editprofile.class);
                startActivity(c);
            }
        });
        btnresetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  EditText resetpwd=new EditText(v.getContext());
                final AlertDialog.Builder passwordresetdialog=new AlertDialog.Builder(v.getContext());
                passwordresetdialog.setTitle("RESET PASSWORD");
                passwordresetdialog.setMessage("enter new password");
                passwordresetdialog.setView(resetpwd);



                passwordresetdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newpwd=resetpwd.getText().toString();
                        mFirebaseUser.updatePassword(newpwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"reset unsuccessful",Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(MainActivity.this,"reset successful",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                passwordresetdialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordresetdialog.create().show();
            }
        });
        btnresetemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  EditText resetemail=new EditText(v.getContext());
                final AlertDialog.Builder emailresetdialog=new AlertDialog.Builder(v.getContext());
                emailresetdialog.setTitle("RESET E-MAIL");
                emailresetdialog.setMessage("enter a new E-mail");
                emailresetdialog.setView(resetemail);


                emailresetdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newemail=resetemail.getText().toString();
                        mFirebaseUser.updateEmail(newemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"reset unsuccessful",Toast.LENGTH_SHORT).show();

                                }

                                else {
                                    Toast.makeText(MainActivity.this,"reset successful",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });}


                });
                emailresetdialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                emailresetdialog.create().show();

            }
        });
        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent b = new Intent(MainActivity.this, login.class);
                startActivity(b);

            }
        });

    }




}
