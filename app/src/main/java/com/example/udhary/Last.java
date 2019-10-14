package com.example.udhary;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Last extends Fragment {
    View v;
    //FloatingActionButton flot;
    private RecyclerView myItemList;
    private EditText userItem, userAmount, date;
    private DatabaseReference contactsRef, UsersRef;
    private ProgressDialog loadingBar;
    private String cuurentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;
    public String cId;
    String Amount;


    public Last() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DetailActivity activity = (DetailActivity) getActivity();

        cId = activity.getCinectionId();

        v= inflater.inflate(R.layout.fragment_last, container, false);
        myItemList = (RecyclerView) v.findViewById(R.id.total_list);
        myItemList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        cuurentUserID = mAuth.getCurrentUser().getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Khata").child("Total1").child(cuurentUserID).child(cId);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");

        Rootref = FirebaseDatabase.getInstance().getReference();

        loadingBar = new ProgressDialog(getContext());
  //      flot = (FloatingActionButton) v.findViewById(R.id.fabtotal);
//        flot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder mBuider = new AlertDialog.Builder(getContext());
//                mBuider.setTitle("Due Pyment");
//                View mView = getLayoutInflater().inflate(R.layout.add_total_amount, null);
//                final EditText tAmount = (EditText) mView.findViewById(R.id.totalamount);
//                mBuider.setView(mView);
//                mBuider.setMessage("Enter your Payment")
//                        .setCancelable(false)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                Amount = tAmount.getText().toString();
//                                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
//                                String currentTime = df.format(Calendar.getInstance().getTime());
//
//                                if (TextUtils.isEmpty(tAmount.getText().toString())) {
//                                    Toast.makeText(getContext(), "Please Write Your Amount...", Toast.LENGTH_SHORT).show();
//                                } else {
//
//                                    final HashMap<String, String> profileMap = new HashMap<>();
//                                    profileMap.put("uid", cuurentUserID);
//                                    profileMap.put("tAmount", "Due Amount : "+Amount);
//                                    profileMap.put("tDate", currentTime);
//                                    Rootref.child("Khata").child("Total1").child(cuurentUserID).child(cId).push().setValue(profileMap)
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        Toast.makeText(getContext(), "Add Successfully...", Toast.LENGTH_SHORT).show();
//                                                        loadingBar.dismiss();
//
//
//                                                    } else {
//                                                        String message = task.getException().toString();
//                                                        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
//                                                        loadingBar.dismiss();
//                                                    }
//
//                                                }
//                                            });
//                                }
//
//
//
//                            }
//
//
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Action for 'NO' Button
//                        dialog.cancel();
//
//                    }
//                });
//                AlertDialog dialog = mBuider.create();
//                dialog.show();
//
//            }
//        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<TotalAmount>().setQuery(contactsRef,TotalAmount.class).build();
        FirebaseRecyclerAdapter<TotalAmount,Last.ContactsViewHolder> adapter
                =new FirebaseRecyclerAdapter<TotalAmount, Last.ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Last.ContactsViewHolder contactsViewHolder, int i, @NonNull TotalAmount model) {
                String userIDS = getRef(i).getKey();
                String amounts = model.gettAmount();
                String Date = model.gettDate();

                contactsViewHolder.userName.setText(amounts);
                contactsViewHolder.cDate.setText(Date);

            }

            @NonNull
            @Override
            public Last.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paiddisplay, parent, false);
                ContactsViewHolder viewHolder=new ContactsViewHolder(view);
                return viewHolder;
            }
        };
        myItemList.setAdapter(adapter);
        myItemList.smoothScrollToPosition(myItemList.getAdapter().getItemCount());
        adapter.startListening();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView userName, cDate;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.paidpese);
            cDate = itemView.findViewById(R.id.paiddate);


        }
    }
}
