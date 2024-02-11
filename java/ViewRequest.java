package com.example.clublink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ViewRequest extends AppCompatActivity {

    ApprovalListAdapter adapter;
    ArrayList<ApprovalClass> list;
    private FirebaseAuth mAuth;
    String authorimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        mAuth = FirebaseAuth.getInstance();
        ImageView profile_image;
        TextView title, content, author, datetime, phase, priority;


        title = findViewById(R.id.post_title);
        content = findViewById(R.id.post_content);
        datetime = findViewById(R.id.post_at);
        phase = findViewById(R.id.phase);
        author = findViewById(R.id.post_by);
        priority = findViewById(R.id.priority);


        profile_image = findViewById(R.id.profile_image);


        View approve = findViewById(R.id.includeApprove);

        TextView fileNameTextView;

        fileNameTextView = findViewById(R.id.fileName);

        TextView approvals = findViewById(R.id.approvals);

        ImageView close = findViewById(R.id.close);
        TextView titleText = findViewById(R.id.title);


        TextView approvalTextView = findViewById(R.id.text1);
        TextView approvedOrNot = findViewById(R.id.showApproved);
        RecyclerView approvalsList = findViewById(R.id.approvals_recycler_view);

        approvalTextView.setVisibility(View.GONE);
        approvalsList.setVisibility(View.GONE);
        approve.setVisibility(View.GONE);
        approvedOrNot.setVisibility(View.GONE);


        FirebaseUser user = mAuth.getCurrentUser();
        String user_uid = user.getUid();

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String image = sharedPreferences.getString("image", "");
        String username = sharedPreferences.getString("name", "");
        int id = sharedPreferences.getInt("role", -1);



        String uid = getIntent().getStringExtra("uid");
        String mainTitle = getIntent().getStringExtra("title");
        String userRole = "";


        titleText.setText(mainTitle);



        if (id == 0) {
            userRole = "Member";
        } else if (id == 1) {
            userRole = "Club_FA";
        } else if (id == 2) {
            userRole = "Society_FA";
        } else if (id == 3) {
            userRole = "ChairSAP";
        } else if (id == 4) {
            userRole = "Dean_Students";
        }

        ArrayList<String> order = new ArrayList<>();
        order.add("Member");
        order.add("Club_FA");
        order.add("Society_FA");
        order.add("ChairSAP");
        order.add("Dean_Students");


        if (id == 0) {
            approvalsList.setVisibility(View.VISIBLE);
            approvalTextView.setVisibility(View.VISIBLE);
        } else if (id >= 1) {

            String oneLevelBelow = order.get(order.indexOf(userRole) - 1);


            FirebaseDatabase.getInstance().getReference().child("requests").child(uid).child("approvals").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild(oneLevelBelow) || oneLevelBelow.equals("Member")) {
                        approve.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            Button yes, no;


            EditText comment;

            comment = findViewById(R.id.approvalComment);
            yes = findViewById(R.id.approve);
            no = findViewById(R.id.decline);

            String finalUserRole = userRole;
            String finalUserRole1 = userRole;
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String commentText = comment.getEditableText().toString();

                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = dateFormat.format(currentDate);


                    Map<String, Object> map = new HashMap<>();

                    String priority = "";

                    map.put("name", username);
                    map.put("role", id);
                    map.put("image", image);
                    map.put("comment", commentText);
                    map.put("datetime", formattedDateTime);
                    map.put("timestamp", ServerValue.TIMESTAMP);
                    map.put("approved", "yes");


                    FirebaseDatabase.getInstance().getReference().child("requests").child(uid).child("approvals")
                            .child(finalUserRole).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ViewRequest.this, "Submitted successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    onBackPressed();
                                }
                            });


                }

            });


            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String commentText = comment.getEditableText().toString();

                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = dateFormat.format(currentDate);


                    Map<String, Object> map = new HashMap<>();

                    String priority = "";

                    map.put("name", username);
                    map.put("role", id);
                    map.put("image", image);
                    map.put("comment", commentText);
                    map.put("datetime", formattedDateTime);
                    map.put("timestamp", ServerValue.TIMESTAMP);
                    map.put("approved", "no");


                    FirebaseDatabase.getInstance().getReference().child("requests").child(uid).child("approvals")
                            .child(finalUserRole).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ViewRequest.this, "Submitted successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    onBackPressed();
                                }
                            });


                }
            });

            String finalUserRole2 = userRole;


            FirebaseDatabase.getInstance().getReference().child("requests").child(uid).child("approvals").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(finalUserRole1)) {
                        approve.setVisibility(View.GONE);
                        approvedOrNot.setVisibility(View.VISIBLE);
                        String approved = snapshot.child(finalUserRole2).child("approved").getValue(String.class);
                        approvedOrNot.setText((approved.equals("yes")) ? "You have approved this." : "You declined this.");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        list = new ArrayList<>();





        FirebaseDatabase.getInstance().getReference().child("requests").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name, titleText, priorityText, dateTime, contentText, phaseText, fileUrl;
                int id;


                authorimage = snapshot.child("image").getValue(String.class);
                name = snapshot.child("name").getValue(String.class);
                id = snapshot.child("role").getValue(Integer.class);
                titleText = snapshot.child("title").getValue(String.class);
                priorityText = snapshot.child("priority").getValue(String.class);
                phaseText = snapshot.child("phase").getValue(String.class);
                dateTime = snapshot.child("datetime").getValue(String.class);
                contentText = snapshot.child("content").getValue(String.class);

                fileUrl = snapshot.child("file").getValue(String.class);

                if (!authorimage.equals("default")) {
                    Picasso.get().load(authorimage).into(profile_image);
                }

                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!authorimage.equals("default")) {

                            Dialog builder = new Dialog(ViewRequest.this);
                            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            builder.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                }
                            });

                            ImageView imageView = new ImageView(ViewRequest.this);
                            Picasso.get().load(authorimage).into(imageView);

                            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            builder.show();
                        }
                    }
                });


                String userRole = "";
                if (id == 0) {
                    userRole = "Member";
                } else if (id == 1) {
                    userRole = "Club FA";
                } else if (id == 2) {
                    userRole = "Society FA";
                } else if (id == 3) {
                    userRole = "ChairSAP";
                } else if (id == 4) {
                    userRole = "Dean Students";
                }

                if (fileUrl.equals("null")) {
                    fileNameTextView.setText("None");
                } else {
                    fileNameTextView.setText("Click to see attachment");
                    fileNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(fileUrl), "application/pdf");

                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // PDF viewer app is not installed, handle the exception
                                Toast.makeText(ViewRequest.this, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


                title.setText(titleText);
                content.setText(contentText);
                datetime.setText(DateTime.getTime(dateTime));
                author.setText("By: " + name + ", " + userRole);
                priority.setText("Priority: " + priorityText);
                phase.setText("Phase: " + phaseText);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter = new ApprovalListAdapter(list, uid);
        approvalsList.setLayoutManager(new LinearLayoutManager(this));
        approvalsList.setAdapter(adapter);


//        FirebaseDatabase.getInstance().getReference().child("requests").child(uid).child("approvals").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot clubSnapshot : dataSnapshot.getChildren()) {
//                    String clubId = clubSnapshot.getKey();
//                    String name = clubSnapshot.child("name").getValue(String.class);
//                    String time = clubSnapshot.child("datetime").getValue(String.class);
//                    Toast.makeText(ViewRequest.this, "1", Toast.LENGTH_SHORT).show();
//
//                    Log.d("Approval Info", "Club ID: " + clubId + ", Name: " + name + ", Time: " + time);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors
//                Log.e("Firebase", "Error fetching approvals", databaseError.toException());
//            }
//        });


        FirebaseDatabase.getInstance().getReference().child("requests").child(uid).child("approvals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();


                for (DataSnapshot snapshot2 : snapshot.getChildren()) {


                    String image, role, datetime, comment, name;
                    String approved;
                    image = snapshot2.child("image").getValue(String.class);
                    name = snapshot2.child("name").getValue(String.class);
                    role = snapshot2.getKey();
                    datetime = snapshot2.child("datetime").getValue(String.class);
                    approved = snapshot2.child("approved").getValue(String.class);
                    comment = snapshot2.child("comment").getValue(String.class);


                    ApprovalClass approval = new ApprovalClass(name, image, role, datetime, comment, approved);

                    list.add(approval);


                }
                if (list.size() == 0) {
                    approvals.setVisibility(View.VISIBLE);
                } else if (list.size() > 0) {
                    approvals.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}

