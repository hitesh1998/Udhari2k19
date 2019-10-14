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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NewconectionActivity extends AppCompatActivity {

    EditText conectioName,conectionNumber;
    Button addConection;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;
    private ProgressDialog loadingBar;
    private String cuurentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newconection);

        conectioName=(EditText)findViewById(R.id.conection_name);
        conectionNumber=(EditText)findViewById(R.id.conection_number);
        loadingBar=new ProgressDialog(this);
        addConection=(Button)findViewById(R.id.add_conection);
        mAuth=FirebaseAuth.getInstance();
        cuurentUserID=mAuth.getCurrentUser().getUid();
        Rootref= FirebaseDatabase.getInstance().getReference();

        addConection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addNewConection();
            }
        });
    }

    private void addNewConection() {
        String newName=conectioName.getText().toString();
        String newNumber=conectionNumber.getText().toString();

        if(TextUtils.isEmpty(newName ))
        {
            Toast.makeText(this, "Please Enter the  Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(newNumber))
        {
            Toast.makeText(this, "Please Enter the mobile number", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setTitle("Creat New Conection");
            loadingBar.setMessage("Please Wait ,Your conection is being Ready..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", cuurentUserID);
            profileMap.put("cName", newName);
            profileMap.put("cNumber", newNumber);
            Rootref.child("Conections").child(cuurentUserID).push().setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewconectionActivity.this, "Conection Add succefully", Toast.LENGTH_SHORT).show();
                                SendUseToMainActivity();
                                loadingBar.dismiss();


                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(NewconectionActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }

                        private void SendUseToMainActivity() {
                            Intent mainIntent =new Intent(NewconectionActivity.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                    });

        }
    }
}
