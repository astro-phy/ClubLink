package com.example.clublink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email, password, name;
    RadioGroup role;
    final String[] userRole = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        TextView member, clubfa, societyfa, chairsap, dean;
        member = findViewById(R.id.member);
        clubfa = findViewById(R.id.clubfa);
        societyfa = findViewById(R.id.societyfa);
        chairsap = findViewById(R.id.chairsap);
        dean = findViewById(R.id.dean);

        member.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        userRole[0] = "Member";


        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                clubfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                societyfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                chairsap.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                dean.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));

                userRole[0] = "Member";

            }
        });

        clubfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                clubfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                societyfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                chairsap.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                dean.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));

                userRole[0] = "Club FA";

            }
        });

        societyfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                clubfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                societyfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                chairsap.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                dean.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));

                userRole[0] = "Society FA";

            }
        });

        chairsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                clubfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                societyfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                chairsap.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                dean.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));

                userRole[0] = "ChairSAP";

            }
        });
        dean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                clubfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                societyfa.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                chairsap.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bg));
                dean.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                userRole[0] = "Dean Students";

            }
        });


        mAuth = FirebaseAuth.getInstance();







        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);





        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = email.getEditableText().toString();
                String userPassword = password.getEditableText().toString();
                String userName = name.getEditableText().toString();


                if (userEmail.endsWith("iitmandi.ac.in") && userEmail.contains(("@"))){
                    if (userPassword.length()>=6){
                        login(userName, userEmail, userPassword);
                    }
                    else{
                        Toast.makeText(Login.this, "Password should be atleast 6 or more characters.", Toast.LENGTH_SHORT).show();

                    }
                }

                else{
                    Toast.makeText(Login.this, "Make sure you are using institute email", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public int getUserType(){
        int type=-1;
        if (userRole[0].equals("Member")){
            type=0;
        } else if (userRole[0].equals("Club FA")){
            type=1;
        }
        else if (userRole[0].equals("Society FA")){
            type=2;
        }
        else if (userRole[0].equals("ChairSAP")){
            type=3;
        } else if (userRole[0].equals("Dean Students")){
            type=4;
        }

        return type;
    }



    public void login(String name, String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            int role = getUserType();
                            String image = "default";

                            Toast.makeText(Login.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                            userRef.child("role").setValue(role);
                            userRef.child("name").setValue(name);
                            userRef.child("image").setValue(image);


                            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", name);
                            editor.putInt("role", role);
                            editor.putString("image", "default");
                            editor.putString("email", email);
                            editor.commit();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {
                            // Account creation failed
                            Toast.makeText(Login.this, "Account creation failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }




}