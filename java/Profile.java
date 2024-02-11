package com.example.clublink;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    FirebaseUser user;




    int PICK_IMAGE_REQUEST = 100;
    Bitmap bitmap;
    Bitmap croppedBitmap;

    Uri imageUri;
    TextView image_text;

    ImageView profileImg;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        TextView name, email, role;
        profileImg = findViewById(R.id.profile_image);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        role = findViewById(R.id.role);

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", "");
        int userRole = sharedPreferences.getInt("role", 0);
        String userEmail = sharedPreferences.getString("email", "");

        name.setText(userName);
        email.setText(userEmail);
        role.setText(getUserRole(userRole));

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String imgUrl = sharedPreferences.getString("image", "default");

        if (!imgUrl.equals("default")) {
            Picasso.get().load(imgUrl).into(profileImg);
        }




        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Profile Image")
                        .setMessage("Change or delete your profile image.")
                        .setPositiveButton("Change Image", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, PICK_IMAGE_REQUEST);

                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putString("image", "default");
                                editor.apply();
                                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid())
                                        .child("image").setValue("default");
                                FirebaseStorage.getInstance().getReference().child("profile_images")
                                        .child(user.getUid()+".jpg").delete();
                                profileImg.setImageResource(R.drawable.baseline_account_circle_24);


                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                int size = Math.min(selectedBitmap.getWidth(), selectedBitmap.getHeight());

                croppedBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(croppedBitmap);
                Rect srcRect = new Rect(
                        (selectedBitmap.getWidth() - size) / 2,
                        (selectedBitmap.getHeight() - size) / 2,
                        (selectedBitmap.getWidth() + size) / 2,
                        (selectedBitmap.getHeight() + size) / 2
                );
                Rect dstRect = new Rect(0, 0, size, size);
                canvas.drawBitmap(selectedBitmap, srcRect, dstRect, null);

                ProgressDialog progressDialog = new ProgressDialog(Profile.this);
                progressDialog.setTitle("Changing Profile Image");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();


                StorageReference storageRef;
                storageRef = FirebaseStorage.getInstance().getReference();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();
                StorageReference imageRef = storageRef.child("profile_image").child(user.getUid()+".jpg");
                UploadTask uploadTask = imageRef.putBytes(imageData);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String img = uri.toString();
                                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid())
                                        .child("image").setValue(img);
                                editor.putString("image", img);
                                editor.apply();

                                progressDialog.dismiss();

                                profileImg.setImageBitmap(croppedBitmap);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Err! Try again!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });



            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


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


}

