package com.example.udhary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SattingActivity extends AppCompatActivity {
    private Button UpdateAccountSetting;
    private EditText userName,userStatus;
    private ProgressDialog loadingBar;
    private CircleImageView userProfileImage;
    private String cuurentUserID;
    private FirebaseAuth mAuth;
    private Uri resultProfile;
    private StorageReference UserProfileImageRef;
    private DatabaseReference Rootref;
    private static final int GalleryPic =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satting);

        mAuth=FirebaseAuth.getInstance();
        UserProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");
        cuurentUserID=mAuth.getCurrentUser().getUid();
        Rootref=FirebaseDatabase.getInstance().getReference();
        UpdateAccountSetting=(Button)findViewById(R.id.update_settings_button);
        userName=(EditText)findViewById(R.id.set_user_name);
        loadingBar=new ProgressDialog(this);
        userStatus=(EditText)findViewById(R.id.set_profile_status);
        userProfileImage=(CircleImageView)findViewById(R.id.set_profile_image);
        UpdateAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udpadesetting();

            }
        });
        RetriveUserInfo();
        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPic);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==GalleryPic && resultCode==RESULT_OK && data!=null)
            {
                Uri ImageUri=data.getData();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                       .setAspectRatio(1,1)
                        .start(this);
            }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                resultProfile = result.getUri() ;
                Picasso.get().load(resultProfile).into(userProfileImage);

            }
        }
    }

    private void udpadesetting() {




            if(TextUtils.isEmpty(userName.getText().toString()) )
            {
                Toast.makeText(this, "Please Write Your User Name...", Toast.LENGTH_SHORT).show();
            }
        if(TextUtils.isEmpty(userStatus.getText().toString()))
        {
            Toast.makeText(this, "Please Write Your User Status...", Toast.LENGTH_SHORT).show();
        }
        else {
            if (resultProfile == null) {
                if (!(TextUtils.isEmpty(userName.getText().toString()))) {
                    Rootref.child("User").child(cuurentUserID).child("name").setValue(userName.getText().toString());

                }
                if (!(TextUtils.isEmpty(userName.getText().toString()))) {
                    Rootref.child("User").child(cuurentUserID).child("status").setValue(userStatus.getText().toString());

                }

                Intent goBackMain = new Intent(SattingActivity.this, MainActivity.class);
                startActivity(goBackMain);



            }


        else
                {
                loadingBar.setTitle("Set Profile Image");
            loadingBar.setMessage("Please Wait ,Your Profile Image Updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            final Uri resultUri = resultProfile;
            final StorageReference filePath = UserProfileImageRef.child(cuurentUserID + ".jpg");
            filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl = uri.toString();
                            Rootref.child("User").child(cuurentUserID).child("image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(SattingActivity.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                                HashMap<String, String> profileMap = new HashMap<>();
                                                profileMap.put("uid", cuurentUserID);
                                                profileMap.put("name", userName.getText().toString());
                                                profileMap.put("status", userStatus.getText().toString());
                                                profileMap.put("image", downloadUrl);


                                                Rootref.child("User").child(cuurentUserID).setValue(profileMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(SattingActivity.this, "Profile Upadated Successfully...", Toast.LENGTH_SHORT).show();

                                                                  finish();  SendUseToMainActivity();
                                                                } else {
                                                                    String message = task.getException().toString();
                                                                    Toast.makeText(SattingActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                loadingBar.dismiss();

                                            } else {
                                                String message = task.getException().toString();
                                                Toast.makeText(SattingActivity.this, "Error Occurred..." + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }

                                        }
                                    });

                        }
                    });
                }
            });

        }

        }
    }

    private void RetriveUserInfo() {
        Rootref.child("User").child(cuurentUserID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")&& (dataSnapshot.hasChild("image")))){

                    String retriveUserName=dataSnapshot.child("name").getValue().toString();
                    String retriveUserStatus=dataSnapshot.child("status").getValue().toString();
                    String retriveProfileImage=dataSnapshot.child("image").getValue().toString();

                    userName.setText(retriveUserName);
                    userStatus.setText(retriveUserStatus);
                    Picasso.get().load(retriveProfileImage).into(userProfileImage);

                }
              else  if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
                    String retriveUserName=dataSnapshot.child("name").getValue().toString();
                    String retriveUserStatus=dataSnapshot.child("status").getValue().toString();


                    userName.setText(retriveUserName);
                    userStatus.setText(retriveUserStatus);

                }

                else
                {
                    Toast.makeText(SattingActivity.this, "Please Set and Update Your Profile..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void SendUseToMainActivity() {
        Intent mainIntent =new Intent(SattingActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
