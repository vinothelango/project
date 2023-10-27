package com.example.cavosh;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

public static final String TAG = "TAG";
    private FirebaseAuth fAuth;
    private EditText mFullName,mEmail, mPassword, mConfirmpass;
    private Button mRegisterBtn;
    private TextView mLoginBtn;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);
        mFullName = findViewById(R.id.et_name);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mConfirmpass = findViewById(R.id.et_repassword);
        mRegisterBtn = findViewById(R.id.btn_register);
        mLoginBtn = findViewById(R.id.loginlnk);
        progressBar = findViewById(R.id.progress_bar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Loginpage.class);
                startActivity(intent);
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString().trim();
                String comfirmPass = mConfirmpass.getText().toString().trim();
                if(TextUtils.isEmpty(fullName)) {
                    mFullName.setError("Full Name is Required");
                    mFullName.setBackgroundResource(R.drawable.errorclr);
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    mEmail.setBackgroundResource(R.drawable.errorclr);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    mPassword.setBackgroundResource(R.drawable.errorclr);
                    return;
                }
                if (password.length()< 6) {
                    mPassword.setError("Password must be minimum 6 Characters");
                    mPassword.setBackgroundResource(R.drawable.errorclr);
                    return;
                } if (comfirmPass.isEmpty() || !password.equals(comfirmPass)) {
                        mFullName.setError("Invalid Password");
                    mFullName.setBackgroundResource(R.drawable.errorclr);
                        return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                startActivitySecond();
                                FirebaseUser fuser = fAuth.getCurrentUser();
                                assert fuser != null;
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Register.this, "verification Email Has been Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure Email not sent " + e.getMessage());
                                    }
                                });
                            } else {
                                Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
           };
        });
    }

    private void startActivitySecond() {
        Intent intent = new Intent(Register.this, Loginpage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    }
