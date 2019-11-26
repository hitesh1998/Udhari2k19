package com.example.udhary;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    private static final String TAG = "ContactsFragment";


   private View Contactsview;
    private RecyclerView myContactList;
   private DatabaseReference contactsRef,UsersRef;
   private FirebaseAuth mAuth;
    private String cuurentUserID;


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
       Contactsview=  inflater.inflate(R.layout.fragment_contacts, container, false);

        myContactList=(RecyclerView) Contactsview.findViewById(R.id.contacts_list);
       myContactList.setLayoutManager(new LinearLayoutManager(getContext()));

      mAuth=FirebaseAuth.getInstance();
       cuurentUserID=mAuth.getCurrentUser().getUid();

        contactsRef= FirebaseDatabase.getInstance().getReference().child("Conections").child(cuurentUserID);
        UsersRef=FirebaseDatabase.getInstance().getReference().child("User");



       return Contactsview;


    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(contactsRef,Contacts.class)
                .build();


        FirebaseRecyclerAdapter<Contacts,ContactsViewHolder> adapter
                =new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
           protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Contacts model) {

                final String userIDS=getRef(position).getKey();
//                DetailActivity detailActivity=(DetailActivity) getActivity();
//               detailActivity.uid(userIDS);

                Log.d(TAG, "onBindViewHolder: " + userIDS + model);

                final String conectionName = model.getcName();
               final String conectionNumber = model.getcNumber();
                holder.userName.setText(conectionName);
                holder.userStatus.setText(conectionNumber);
                holder.profileImage.setImageResource(R.drawable.profile_image);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), DetailActivity.class);
                        i.putExtra("visit user id",userIDS);
                        i.putExtra("visit user name",conectionName);
                     startActivity(i);

                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder mBuider=new AlertDialog.Builder(getContext());
                        mBuider.setTitle("Remove Contacts");
                        mBuider.setMessage("For Remove Press Remove else Press Cancle")
                                .setCancelable(false)
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        Query deleteQuery = ref.child("Conections").child(cuurentUserID).child(userIDS);

                                        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().removeValue();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }


                                })
                                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });
                        AlertDialog dialog =mBuider.create();
                        dialog.show();
                        return false;
                    }
                });

            }
            @NonNull
           @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layou,viewGroup,false);
               ContactsViewHolder viewHolder=new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        myContactList.setAdapter(adapter);
        adapter.startListening();
    }


    public class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName,userStatus;
        CircleImageView profileImage;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_profile_name);
            userStatus=itemView.findViewById(R.id.user_status);
            profileImage=itemView.findViewById(R.id.users_profile_image);

//           itemView.setOnClickListener(new View.OnClickListener() {
//               @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(getActivity(), DetailActivity.class);
//                    startActivity(i);
//                   ((Activity) getActivity()).overridePendingTransition(0, 0);
//
//
//
//                }
//          });
        }
    }
}

