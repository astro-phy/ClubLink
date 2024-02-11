package com.example.clublink;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewRequest extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ImageView upload;
    TextView fileName;
    String priority = "";
    String phase = "";
    String url="";
    Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        mAuth = FirebaseAuth.getInstance();


        TextView phase1, phase2, phase3, priority1, priority2, priority3;

        upload = findViewById(R.id.upload);
        fileName = findViewById(R.id.fileName);


        phase1 = findViewById(R.id.phase1);
        phase2 = findViewById(R.id.phase2);
        phase3 = findViewById(R.id.phase3);
        priority1 = findViewById(R.id.priority1);
        priority2 = findViewById(R.id.priority2);
        priority3 = findViewById(R.id.priority3);

        phase1.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        priority1.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        phase = "Initial Report";
        priority = "Regular";






        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfSelect.launch("application/pdf");
            }
        });



        phase1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phase1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                        phase2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                        phase3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                        phase = "Initial Report";

                    }
                });

        phase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phase1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                phase2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                phase3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                phase = "Comparative List";

            }
        });

        phase3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phase1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                phase2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                phase3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                phase = "Fund Approval";

            }
        });

        priority1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                priority2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                priority3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                priority = "Regular";
            }
        });

        priority2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                priority2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                priority3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                priority = "Urgent";

            }
        });

        priority3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                priority2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                priority3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                priority = "Immediate";

            }
        });




        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();


        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView submit, showTags;
        EditText title, content, tags;

        title = findViewById(R.id.post_title);
        content = findViewById(R.id.post_content);

        submit = findViewById(R.id.submit);





        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleText, contentText;
                titleText = title.getEditableText().toString();
                contentText = content.getEditableText().toString();
                if (titleText.length()<5){
                    Toast.makeText(NewRequest.this, "Subject can't be less than 5 characters", Toast.LENGTH_SHORT).show();
                } else if (contentText.length()<10){
                    Toast.makeText(NewRequest.this, "Content should not be less than 10 characters", Toast.LENGTH_SHORT).show();
                } else {


                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = dateFormat.format(currentDate);

                    String post_uuid = UUID.randomUUID().toString();

                    Map<String, Object> map = new HashMap<>();

                    SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                    String name = sharedPreferences.getString("name", "");
                    int role = sharedPreferences.getInt("role", -1);
                    String image = sharedPreferences.getString("image", "");



                    map.put("name", name);
                    map.put("role", role);
                    map.put("image", image);
                    map.put("title", titleText);
                    map.put("priority", priority);
                    map.put("phase", phase);
                    map.put("content", contentText);
                    map.put("datetime", formattedDateTime);
                    map.put("timestamp", ServerValue.TIMESTAMP);

                    if (fileUri!=null){

                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference ref = storageRef.child("files/"+fileUri.getLastPathSegment());
                        UploadTask uploadTask = ref.putFile(fileUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileName.setText("Uploaded.");
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUrl) {


                                        map.put("file" ,downloadUrl.toString());
                                        databaseReference.child(post_uuid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(NewRequest.this, "Submitted successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                                onBackPressed();
                                            }
                                        });

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                fileName.setText("Failed to upload.");
                                Log.d("test", exception.getMessage().toString());
                                Toast.makeText(NewRequest.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                fileName.setText("Uploading, please wait...");
                            }
                        });


                    }
                    else{
                        map.put("file", "null");
                        databaseReference.child(post_uuid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(NewRequest.this, "Submitted successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                                onBackPressed();
                            }
                        });
                    }








                }



            }
        });


    }

    private ActivityResultLauncher<String> pdfSelect = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            file -> {
                if (file!=null) {
                    fileUri = file;
                    Cursor returnCursor =
                            getContentResolver().query(fileUri, null, null, null, null);

                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();

                    fileName.setText(returnCursor.getString(nameIndex));
                }
            }
    );





}

