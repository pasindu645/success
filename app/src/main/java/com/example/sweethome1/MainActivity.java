package com.example.sweethome1;

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
import android.widget.Toast;

import com.example.sweethome.R;
import com.example.sweethome.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public Button btnresetpwd;
    Button btnsignout;
    Button btnresetemail;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Hiiiii");
        mFirebaseAuth=FirebaseAuth.getInstance();
        btnresetpwd=findViewById(R.id.button4);
        btnsignout=findViewById(R.id.button3);
        btnresetemail=findViewById(R.id.button5);
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
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
                final String newemail = resetemail.getText().toString();

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