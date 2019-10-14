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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Paid extends Fragment {
    View v;
    FloatingActionButton flot;
    private RecyclerView myItemList;
    private EditText userItem, userAmount, date,UserDeuAmount;
    private DatabaseReference contactsRef,contactsRef1, UsersRef;
    private ProgressDialog loadingBar;
    private String cuurentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;
    public String cId;
    String Amount ,dueamount;



    public Paid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DetailActivity activity = (DetailActivity) getActivity();

        cId = activity.getCinectionId();
        v = inflater.inflate(R.layout.fragment_paid, container, false);

        myItemList = (RecyclerView) v.findViewById(R.id.paid_list);
        myItemList.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        cuurentUserID = mAuth.getCurrentUser().getUid();

        Log.d(TAG, cuurentUserID + " " + cId);
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Khata").child("Paid").child(cuurentUserID).child(cId);
        contactsRef1 = FirebaseDatabase.getInstance().getReference().child("Khata").child("Total1").child(cuurentUserID).child(cId);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");

        Rootref = FirebaseDatabase.getInstance().getReference();

        loadingBar = new ProgressDialog(getContext());
        flot = (FloatingActionButton) v.findViewById(R.id.fabpaid);
        flot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuider = new AlertDialog.Builder(getContext());
                mBuider.setTitle("Pyment");
                View mView = getLayoutInflater().inflate(R.layout.add_paid_pese, null);
                final EditText uAmount = (EditText) mView.findViewById(R.id.paidamount);
                final EditText uDueAmount = (EditText) mView.findViewById(R.id.dueAmount);
                mBuider.setView(mView);
                mBuider.setMessage("Enter your Detail")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                 Amount = uAmount.getText().toString();
                                 dueamount=uDueAmount.getText().toString();
                                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                                String currentTime = df.format(Calendar.getInstance().getTime());

                                if (TextUtils.isEmpty(uAmount.getText().toString())) {
                                    Toast.makeText(getContext(), "Please Write Paid Amount...", Toast.LENGTH_SHORT).show();
                                }

                                if (TextUtils.isEmpty(uDueAmount.getText().toString())) {
                                    Toast.makeText(getContext(), "Please Write Due Amount...", Toast.LENGTH_SHORT).show();}
                                else {

                                    final HashMap<String, String> profileMap = new HashMap<>();
                                    profileMap.put("uid", cuurentUserID);
                                    profileMap.put("pAmunt", "Paid Amount : "+Amount);
                                    profileMap.put("pDate", currentTime);
                                    Rootref.child("Khata").child("Paid").child(cuurentUserID).child(cId).push().setValue(profileMap)
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
                                    final HashMap<String, String> dueAmount = new HashMap<>();
                                    dueAmount.put("uid", cuurentUserID);
                                    dueAmount.put("tAmount", "Due Amount : "+dueamount);
                                    dueAmount.put("tDate", currentTime);
                                    Rootref.child("Khata").child("Total1").child(cuurentUserID).child(cId).push().setValue(dueAmount)
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
                                }



                            }


                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
                AlertDialog dialog = mBuider.create();
                dialog.show();
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<RetrivePayment>().setQuery(contactsRef, RetrivePayment.class).build();
        FirebaseRecyclerAdapter<RetrivePayment, Paid.ContactsViewHolder> adapter
                = new FirebaseRecyclerAdapter<RetrivePayment, Paid.ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Paid.ContactsViewHolder contactsViewHolder, int i, @NonNull RetrivePayment model) {

                String userIDS = getRef(i).getKey();
                Log.d(TAG, "onBindViewHolder: " + userIDS + model);
                String amounts = model.getpAmunt();
                String Date = model.getpDate();
                contactsViewHolder.userName.setText(amounts);
                contactsViewHolder.cDate.setText(Date);
            }

            @NonNull
            @Override
            public Paid.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paiddisplay, parent, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
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
    public String getAmount()
    {
        return Amount ;
  }
}
