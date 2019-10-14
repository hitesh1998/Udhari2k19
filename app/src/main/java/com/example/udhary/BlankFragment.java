package com.example.udhary;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    private static final String TAG = "Udhar";



    View v;


    FloatingActionButton flot;
    private RecyclerView myItemList;
    private EditText userItem,userAmount,date;
    private DatabaseReference contactsRef,UsersRef;
    private ProgressDialog loadingBar;
    private String cuurentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;
    public String cId;
    public String Amount="";




    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        DetailActivity activity = (DetailActivity) getActivity();

         cId =activity.getCinectionId();

              v = inflater.inflate(R.layout.fragment_blank, container, false);



        myItemList=(RecyclerView) v.findViewById(R.id.item_list);
        myItemList.setLayoutManager(new LinearLayoutManager(getContext()));




        mAuth= FirebaseAuth.getInstance();
        cuurentUserID=mAuth.getCurrentUser().getUid();

        Log.e(TAG, cuurentUserID+" "+cId);
        contactsRef= FirebaseDatabase.getInstance().getReference().child("Khata").child("Udhar").child(cuurentUserID).child(cId);
        UsersRef=FirebaseDatabase.getInstance().getReference().child("User");

        Rootref= FirebaseDatabase.getInstance().getReference();

        loadingBar=new ProgressDialog(getContext());



        flot=(FloatingActionButton)v.findViewById(R.id.fabudhar);
        flot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuider=new AlertDialog.Builder(getContext());
                mBuider.setTitle("Udhari Item");
                View mView =getLayoutInflater().inflate(R.layout.add_udhari_item,null);
                final EditText uItem=(EditText) mView.findViewById(R.id.udhari_item);
                final EditText uAmount=(EditText) mView.findViewById(R.id.udhari_amount);


                mBuider.setView(mView);
                mBuider.setMessage("Enter your Detail")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String Item=uItem.getText().toString();
                                 Amount=uAmount.getText().toString();
                                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                                String currentTime = df.format(Calendar.getInstance().getTime());



                                if(TextUtils.isEmpty(uItem.getText().toString()) )
                                {
                                    Toast.makeText(getContext(), "Please Write Your Item...", Toast.LENGTH_SHORT).show();
                                }
                                if(TextUtils.isEmpty(uAmount.getText().toString()))
                                {
                                    Toast.makeText(getContext(), "Please Write Your Amount...", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {

                                    final HashMap<String, String> profileMap = new HashMap<>();
                                    profileMap.put("uid", cuurentUserID);
                                    profileMap.put("uItem", "Item : "+Item);
                                    profileMap.put("uAmount","Due Amount : "+ Amount);
                                    profileMap.put("uDate",currentTime);
                                    Rootref.child("Khata").child("Udhar").child(cuurentUserID).child(cId).push().setValue(profileMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Add Successfully...", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();



                                                    } else {
                                                        String message = task.getException().toString();
                                                        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }

                                                }
                                            });
//                                    Total ldf = new Total ();
//                                    Bundle args = new Bundle();
//                                    args.putString("YourKey",Amount);
//                                    ldf.setArguments(args);
//
//
//                                    getFragmentManager().beginTransaction().add(R.id.container_a, ldf).commit();
                                }




                            }


                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                AlertDialog dialog =mBuider.create();
                dialog.show();
            }




        });



        // Inflate the layout for this fragment
        return  v;
    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<RetriveItem>().setQuery(contactsRef,RetriveItem.class).build();

        FirebaseRecyclerAdapter<RetriveItem, BlankFragment.ContactsViewHolder> adapter
                =new FirebaseRecyclerAdapter<RetriveItem, BlankFragment.ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ContactsViewHolder contactsViewHolder, int i, @NonNull RetriveItem model) {

                String userIDS=getRef(i).getKey();
                Log.d(TAG, "onBindViewHolder: " + userIDS + model);

                String items = model.getuItem();
                String amounts = model.getuAmount();
                String Date=model.getuDate();
                contactsViewHolder.userName.setText(items);
                contactsViewHolder.userStatus.setText(amounts);
                contactsViewHolder.cDate.setText(Date);
            }




            @NonNull
            @Override
            public BlankFragment.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display,parent,false);
                ContactsViewHolder viewHolder=new ContactsViewHolder(view);

                return viewHolder;

            }

        };


        myItemList.setAdapter(adapter);
        myItemList.smoothScrollToPosition(myItemList.getAdapter().getItemCount());
        adapter.startListening();



    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName,userStatus,cDate;
        CircleImageView profileImage;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.items);
            userStatus=itemView.findViewById(R.id.amounts);
            cDate=itemView.findViewById(R.id.date);



        }
    }
    public String getAmount()
    {
        return Amount;
    }


    }


