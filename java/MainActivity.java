package com.example.clublink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<RequestsClass> list, filterList;
    private FirebaseAuth mAuth;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        EditText search = findViewById(R.id.search);




        ImageView profileImg = findViewById(R.id.profile_img);


        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String imgUrl = sharedPreferences.getString("image", "default");

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!imgUrl.equals("default")) {


                    Dialog builder = new Dialog(MainActivity.this);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                        }
                    });

                    ImageView imageView = new ImageView(MainActivity.this);
                    Picasso.get().load(imgUrl).into(imageView);

                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }


            }
        });

        ImageView profile = findViewById(R.id.profile_img);
        TextView name = findViewById(R.id.name);
        TextView role = findViewById(R.id.role);

        String image = sharedPreferences.getString("image", "");
        String username = sharedPreferences.getString("name", "");
        int id = sharedPreferences.getInt("role", -1);
        String userRole = "";


        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });


        if (id==0){
            userRole="Member";
        } else if (id==1){
            userRole="Club FA";
        }
        else if (id==2){
            userRole="Society FA";
        }
        else if (id==3){
            userRole="ChairSAP";
        } else if (id==4){
            userRole="Dean Students";
        }

        if(!image.equals("default")){
            Picasso.get().load(image).into(profile);
        }

        name.setText(username);
        role.setText(userRole);

        list = new ArrayList<>();
        filterList = new ArrayList<>();


        RecyclerView listView = findViewById(R.id.allrequests);
        adapter = new ListAdapter(list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        FloatingActionButton newRequest = findViewById(R.id.newRequest);
        newRequest.setVisibility(View.GONE);

        if (id==0){
            newRequest.setVisibility(View.VISIBLE);
        }

        FirebaseUser user = mAuth.getCurrentUser();


        newRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewRequest.class));

            }
        });



        FirebaseDatabase.getInstance().getReference().child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    String image, name, title, priority, datetime, uid, phase;
                    int role;

                    uid = snapshot1.getKey();
                    image = snapshot1.child("image").getValue(String.class);
                    name = snapshot1.child("name").getValue(String.class);
                    role = snapshot1.child("role").getValue(Integer.class);
                    title = snapshot1.child("title").getValue(String.class);
                    priority = snapshot1.child("priority").getValue(String.class);
                    phase = snapshot1.child("phase").getValue(String.class);
                    datetime = snapshot1.child("datetime").getValue(String.class);

                    list.add(new RequestsClass(uid, image, name, role, title, priority, phase, datetime));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String getUserRole(int id){


        String userRole="";
        if (id==0){
            userRole="Member";
        } else if (id==1){
            userRole="Club FA";
        }
        else if (id==2){
            userRole="Society FA";
        }
        else if (id==3){
            userRole="ChairSAP";
        } else if (id==4){
            userRole="Dean Students";
        }
        return userRole;

    }

    void filter(String text){
        ArrayList<RequestsClass> temp = new ArrayList();
        for(RequestsClass filter: list){
            if(filter.getName().toLowerCase().contains(text) ||
                    filter.getPriority().toLowerCase().contains(text) ||
                    filter.getPhase().toLowerCase().contains(text) ||
                    getUserRole(filter.getRole()).toLowerCase().contains(text) ||
                    filter.getTitle().toLowerCase().contains(text))
            {
                temp.add(filter);
            }
        }
        adapter.updateList(temp);
    }





}