package com.example.sweethome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    public Button btnresetpwd;
    Button btnsignout;
    Button btnresetemail;
    Button btneditprofile;
    TextView fullname,email,phonenumber;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mfirestore;
    String userid;
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
        fullname=findViewById(R.id.textView3);
        email=findViewById(R.id.textView4);
        phonenumber=findViewById(R.id.textView5);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mfirestore=FirebaseFirestore.getInstance();
        userid= mFirebaseAuth.getCurrentUser().getUid();

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
