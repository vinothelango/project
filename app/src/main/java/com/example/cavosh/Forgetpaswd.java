package com.example.cavosh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Forgetpaswd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpaswd);
        final EditText inputMobile = findViewById(R.id.forget_password_phone_number);
        final Button buttonGetOTP = findViewById(R.id.obtn);
        final ProgressBar progressBar = findViewById(R.id.progressbar);

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputMobile.getText().toString().trim().isEmpty()){
                    Toast.makeText(Forgetpaswd.this,"Enter Mobile",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonGetOTP.setVisibility(View.VISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+inputMobile.getText().toString(),
                        60,
                        TimeUnit.SECONDS, Forgetpaswd.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.GONE);
                            Toast.makeText(Forgetpaswd.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken forceResendingToken){
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), OTPscreen.class);
                                intent.putExtra("mobile",inputMobile.getText().toString());
                                intent.putExtra("verificationId",verificationId);
                                startActivity(intent);
                            }
                        });

                }
            });
        }
    }

