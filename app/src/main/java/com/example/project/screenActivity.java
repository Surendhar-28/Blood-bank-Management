package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.UserAdapter;
import com.example.project.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class screenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_profile_image;
    private TextView nav_fullname,nav_email,nav_bloodgroup,nav_type;

    private DatabaseReference useRef;

    private RecyclerView recyclerView;
    private ProgressBar progressbar;

    private List<User> userList;
    private UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Donation App");

        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(screenActivity.this, drawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        progressbar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(screenActivity.this,userList);

        recyclerView.setAdapter(userAdapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();
                if(type.equals("donor")){
                    readRecipients();
                }else{
                    readDonors();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname = nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodgroup = nav_view.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_type = nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);

        useRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        useRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    nav_fullname.setText(name);

                    String email=snapshot.child("Email").getValue().toString();
                    nav_email.setText(email);

                    String bloodgroup=snapshot.child("bloodGroup").getValue().toString();
                    nav_bloodgroup.setText(bloodgroup);

                    String type=snapshot.child("type").getValue().toString();
                    nav_type.setText(type);

                   if (snapshot.hasChild("profilepictureurl")) {
                       String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                       Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
                   }else{
                       nav_profile_image.setImageResource(R.drawable.profile_image);
                   }

                   Menu nav_menu = nav_view.getMenu();

                   if(type.equals("donor")){
                       nav_menu.findItem(R.id.Email).setTitle("Received Emails");
                       nav_menu.findItem(R.id.notifications).setVisible(true);
                   }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    Toast.makeText(screenActivity.this, "No recipients", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readRecipients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    Toast.makeText(screenActivity.this, "No recipients", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.aplus:
                Intent intent3 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent3.putExtra("group","A+");
                startActivity(intent3);
                break;
            case R.id.aminus:
                Intent intent4 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent4.putExtra("group","A-");
                startActivity(intent4);
                break;
            case R.id.bplus:
                Intent intent5 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent5.putExtra("group","B+");
                startActivity(intent5);
                break;
            case R.id.bminus:
                Intent intent6 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent6.putExtra("group","B-");
                startActivity(intent6);
                break;
            case R.id.oplus:
                Intent intent7 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent7.putExtra("group","O+");
                startActivity(intent7);
                break;
            case R.id.ominus:
                Intent intent8 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent8.putExtra("group","O-");
                startActivity(intent8);
                break;
            case R.id.abplus:
                Intent intent9 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent9.putExtra("group","AB+");
                startActivity(intent9);
                break;
            case R.id.abminus:
                Intent intent10 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent10.putExtra("group","AB-");
                startActivity(intent10);
                break;

            case R.id.compatible:
                Intent intent11 = new Intent(screenActivity.this,CategorySelectedActivity.class);
                intent11.putExtra("group","Compatible with me");
                startActivity(intent11);
                break;

            case R.id.Email:
                Intent intent12 = new Intent(screenActivity.this,SentEmailActivity.class);
                startActivity(intent12);
                break;


            case R.id.notifications:
                Intent intent13 = new Intent(screenActivity.this,NotificationActivity.class);
                startActivity(intent13);
                break;


            case R.id.profile:
                Intent intent = new Intent(screenActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2=new Intent(screenActivity.this, LoginActivity.class);
                startActivity(intent2);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}