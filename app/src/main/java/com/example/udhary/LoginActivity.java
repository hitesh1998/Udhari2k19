package com.example.udhary;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button sendVerificationCodeButton , verifyBotton;
    private EditText inputPhoneNumber,inputVerificationCode;
    private  String mVerificationId;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RootRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        sendVerificationCodeButton=(Button)findViewById(R.id.send_ver_code_button);
        verifyBotton=(Button)findViewById(R.id.verify_button);
        inputPhoneNumber=(EditText)findViewById(R.id.phone_number_input);
        inputVerificationCode=(EditText)findViewById(R.id.verification_code_input);
        loadingBar=new ProgressDialog(this);

        sendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String phoneNumber=inputPhoneNumber.getText().toString();
                if(TextUtils.isEmpty(phoneNumber))
                {
                    Toast.makeText(LoginActivity.this,"Please Enter Your Phone Number First...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Phone verifiaction");
                    loadingBar.setMessage("please wait,while we are authenticating your phone...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            LoginActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });


            verifyBotton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendVerificationCodeButton.setVisibility(View.INVISIBLE);
                    inputPhoneNumber.setVisibility(View.INVISIBLE);
                    String verificationcode=inputVerificationCode.getText().toString();
                    if(TextUtils.isEmpty(verificationcode))
                    {
                        Toast.makeText(LoginActivity.this,"Please write verification code first...",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        loadingBar.setTitle("Code verifiaction");
                        loadingBar.setMessage("please wait,while we are verifying verification code...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                        signInWithPhoneAuthCredential(credential);
                    }
                }
            });

        callbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();

                    Toast.makeText(LoginActivity.this,"Invalid number please enter the correct phone number with your country code ",Toast.LENGTH_SHORT).show();
                sendVerificationCodeButton.setVisibility(View.VISIBLE);
                inputPhoneNumber.setVisibility(View.VISIBLE);
                verifyBotton.setVisibility(View.INVISIBLE);
                inputVerificationCode.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                loadingBar.dismiss();

                Toast.makeText(LoginActivity.this, "Code Has been sent, Please check...", Toast.LENGTH_SHORT).show();
                sendVerificationCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);
                verifyBotton.setVisibility(View.VISIBLE);
                inputVerificationCode.setVisibility(View.VISIBLE);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                          final  String cuurentUserID=mAuth.getCurrentUser().getUid();

                            DatabaseReference userCheck = FirebaseDatabase.getInstance().getReference().child("User");

                            userCheck.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     if(!dataSnapshot.hasChild(cuurentUserID)){
                                         RootRef.child("User").child(cuurentUserID).setValue("");
                                     }

                                         loadingBar.dismiss();
                                         Toast.makeText(LoginActivity.this,"verification Successfull... ",Toast.LENGTH_SHORT).show();
                                         SendUseToMainActivity();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                                String massage=task.getException().toString();
                                Toast.makeText(LoginActivity.this,"Error: Plese write the Number with country code..",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SendUseToMainActivity() {
        Intent mainIntent =new Intent(LoginActivity.this,MainActivity.class);
       mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }




    private void SendUserToMainActivity() {
        Intent mainIntent =new Intent(LoginActivity.this ,MainActivity.class);
        startActivity(mainIntent);
    }
}
