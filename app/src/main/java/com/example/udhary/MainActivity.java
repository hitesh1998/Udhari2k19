package com.example.udhary;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
        private Toolbar mToolbar;
        private ViewPager myViewPager;
        private TabLayout myTabLayout;
        private  TabsAccessAdapter myTabsAccessAdapter;
        private FirebaseUser currentUser ;
       private FirebaseAuth mAuth=FirebaseAuth.getInstance();
        private DatabaseReference Rootref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Udhary");
        Rootref= FirebaseDatabase.getInstance().getReference();
        myViewPager=(ViewPager)findViewById(R.id.main_tabs_pager);
        myTabsAccessAdapter=new TabsAccessAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessAdapter);

        myTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null)
        {
            SendUserToLoginActivity();
       }
        else
        {
            VerifyUserExistanc();
        }
   }

    private void VerifyUserExistanc() {
        String CurrentUserID=mAuth.getCurrentUser().getUid();
        Rootref.child("User").child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                {
                    Toast.makeText(MainActivity.this, "Wellcome", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                    SendUserToSettingActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.main_logout)
         {
             if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
             {

                 FirebaseAuth.getInstance().signOut();
                 finish();
                 SendUserToLoginActivity();
                 //do something. loadwebview.
             }
             else
             {
                 Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
             }

         }

        if(item.getItemId()==R.id.main_new_connection_option)
        {
            if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
            {

                SendUserToConectionActivity();
            }
            else
            {
                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }

        }

        if(item.getItemId()==R.id.main_search)
        {
            searchUser();
        }


        if(item.getItemId()==R.id.main_settings_option)
        {
            if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
            {

                SendUserToSettingActivity();
            }
            else
            {
                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }

        }



        if(item.getItemId()==R.id.main_contect_us)
        {

        }
        return true;

    }

    private void searchUser() {

    }

    private void SendUserToConectionActivity() {
        Intent newConection =new Intent(MainActivity.this ,NewconectionActivity.class);
        startActivity(newConection);

    }

    private void SendUserToLoginActivity() {

        Intent loginIntent =new Intent(MainActivity.this ,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void SendUserToSettingActivity() {

        Intent SettingIntent =new Intent(MainActivity.this ,SattingActivity.class);
        startActivity(SettingIntent);

    }
}
