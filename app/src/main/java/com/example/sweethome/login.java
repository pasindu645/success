package com.example.sweethome;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    public EditText emailid, password;
    TextView forgotpassword;
    Button btnlogin;
    Button btnregister2;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("login form");
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailid = findViewById(R.id.edit10);
        password = findViewById(R.id.edit2);
        forgotpassword=findViewById(R.id.textView2);
        btnlogin = findViewById(R.id.button1);
        btnregister2 = findViewById(R.id.button2);
        /*authStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(login.this, "you are login", Toast.LENGTH_SHORT).show();
                    Intent y = new Intent(login.this, MainActivity.class);
                    startActivity(y);

                } else {
                    Toast.makeText(login.this, "plz login", Toast.LENGTH_SHORT).show();
                }
            }

            ;


        };*/
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailid.getText().toString();
                String pwd = password.getText().toString();

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



                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(login.this,"login successful",Toast.LENGTH_SHORT).show();
                                Intent z = new Intent(login.this, MainActivity.class);
                                startActivity(z);
                            }
                            else {
                                Toast.makeText(login.this,"login Failier " +
                                        " password or email is incorrect",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


               /* else{
                    Toast.makeText(login.this,"Error",Toast.LENGTH_SHORT).show();
                }*/


            }
        });
        btnregister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(login.this, signup.class);
                startActivity(x);
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  EditText resetmail=new EditText(v.getContext());
                final AlertDialog.Builder passwordresetdialog=new AlertDialog.Builder(v.getContext());
                passwordresetdialog.setTitle(" Reset password");
                passwordresetdialog.setMessage("Enter your email to reset password");
                passwordresetdialog.setView(resetmail);

                passwordresetdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail=resetmail.getText().toString();
                        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                            Toast.makeText(login.this,"enter valid email",Toast.LENGTH_SHORT).show();}
                        mFirebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(login.this,"sent unsuccessful",Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(login.this,"sent to your email successful",Toast.LENGTH_SHORT).show();
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

    }


}
