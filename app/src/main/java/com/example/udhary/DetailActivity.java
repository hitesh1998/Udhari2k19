package com.example.udhary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity  {

    private static final String TAG = "DetailActivity";
    private Fragment fragmentBlank,fragmenttotal;
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private  RecordAcccessAdapter myTabsAccessAdapter;
    private FirebaseUser currentUser ;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference Rootref;
    private String conectionId,conectionName;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        fragmentManager=getSupportFragmentManager();

        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Udhary");
        Rootref= FirebaseDatabase.getInstance().getReference();
        myViewPager=(ViewPager)findViewById(R.id.main_tabs_pager);
        myTabsAccessAdapter=new RecordAcccessAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessAdapter);

        myTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);



        conectionId=getIntent().getExtras().get("visit user id").toString();
        conectionName=getIntent().getExtras().get("visit user name").toString();


        Toast.makeText(this, "Add for "+conectionName, Toast.LENGTH_SHORT).show();




//


    }





    public String getCinectionId()
    {
        return conectionId;
    }




}
