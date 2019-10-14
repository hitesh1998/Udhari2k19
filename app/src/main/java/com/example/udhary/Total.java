package com.example.udhary;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Total extends Fragment {
    private static final String TAG ="Udhari" ;


    TextView total;
    View v;
    public String cId;
    private DatabaseReference contactsRef,UsersRef;
    private String cuurentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;
    String value;


    public Total() {
        // Required empty public constructor
        value = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DetailActivity activity = (DetailActivity) getActivity();

        cId =activity.getCinectionId();

        mAuth= FirebaseAuth.getInstance();
        cuurentUserID=mAuth.getCurrentUser().getUid();
        contactsRef= FirebaseDatabase.getInstance().getReference().child("Khata").child("Total").child(cuurentUserID).child(cId);
        UsersRef=FirebaseDatabase.getInstance().getReference().child("User");

        Rootref= FirebaseDatabase.getInstance().getReference();



        v= inflater.inflate(R.layout.fragment_total2, container, false);

        value = getArguments().getString("YourKey");
        Log.d(TAG, "onCreateView: dfdff"+ value);
        total=(TextView) v.findViewById(R.id.totalPayment);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String currentTime = df.format(Calendar.getInstance().getTime());

        final HashMap<String, String> profileMap = new HashMap<>();
        profileMap.put("uid", cuurentUserID);

        profileMap.put("DeuAmount","Deu AMount : "+value);
        profileMap.put("uDate",currentTime);
        Rootref.child("Khata").child("Total").child(cuurentUserID).child(cId).push().setValue(profileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Add Successfully...", Toast.LENGTH_SHORT).show();




                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                return v;

    }




}
