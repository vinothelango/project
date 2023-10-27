package com.example.cavosh;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginpage extends AppCompatActivity {
    private FirebaseAuth fauth;
   private EditText mEmail, mPassword;
  private Button mLoginbtn;
   private TextView mCreatebtn,forgettextlink,mregister;
   ProgressBar progressBar;
    @SuppressLint({"WrongViewCast","MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        forgettextlink = findViewById(R.id.forgrt1);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mLoginbtn = findViewById(R.id.btn_login);
        mregister = findViewById(R.id.lnkregsrt);
        progressBar = findViewById(R.id.progressbarlog);
        mCreatebtn = findViewById(R.id.forgrt1);
        fauth = FirebaseAuth.getInstance();
        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loginpage.this, Register.class);
                startActivity(intent);
            }
        });
        mCreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loginpage.this, Forgetpaswd.class);
                startActivity(intent);
            }
        });
        mLoginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    mEmail.setBackgroundResource(R.drawable.errorclr);
                } else if (TextUtils.isEmpty(password)) {
                    mPassword.setError("password is required");
                    mPassword.setBackgroundResource(R.drawable.errorclr);
                } else if (password.length() < 6) {
                    mPassword.setError("password must be >=6 charasters");
                    mPassword.setBackgroundResource(R.drawable.errorclr);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fuser = fauth.getCurrentUser();
                                assert fuser != null;
                                if (fuser.isEmailVerified()) {
                                    progressBar.setVisibility(View.GONE);
                                    startActivitySecond();
                                    finish();
                                } else {
                                    Toast.makeText(Loginpage.this, "please verify", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }


                            } else {
                                Toast.makeText(Loginpage.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Log.e("VersionInfo", "Exception");
                            }
                        }
                    });
                    mLoginbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final EditText resetMail = new EditText(v.getContext());
                            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                            passwordResetDialog.setTitle("Reset Password ?");
                            passwordResetDialog.setMessage("Enter Your Email to received Reset link.");
                            passwordResetDialog.setView(resetMail);

                            passwordResetDialog.setPositiveButton("send", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String mail = resetMail.getText().toString();
                                    fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Loginpage.this, "reset Link sent to your email", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Loginpage.this, "Error ! reset your link is not sent", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });
                            passwordResetDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            passwordResetDialog.create().show();
                        }

                    });
                    mCreatebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), Register.class));
                        }
                    });

                    }
                }
            });
        }


    private void startActivitySecond() {
        Intent intent = new Intent(Loginpage.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}




