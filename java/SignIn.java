package com.example.clublink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {


    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }

        EditText email, password;

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);

        Button signin = findViewById(R.id.buttonLogin);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = email.getEditableText().toString();
                String userPassword = password.getEditableText().toString();


                if (userEmail.endsWith("iitmandi.ac.in") && userEmail.contains(("@"))){
                    if (userPassword.length()>=6){
                        signIn(userEmail, userPassword);
                    }
                    else{
                        Toast.makeText(SignIn.this, "Password should be atleast 6 or more characters.", Toast.LENGTH_SHORT).show();

                    }
                }

                else{
                    Toast.makeText(SignIn.this, "Make sure you are using institute email", Toast.LENGTH_SHORT).show();
                }


            }
        });

        TextView signUp;
        signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, Login.class));
            }
        });






    }



    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String image = snapshot.child("image").getValue(String.class);
                                    int role = snapshot.child("role").getValue(Integer.class);
                                    String name = snapshot.child("name").getValue(String.class);

                                    SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("name", name);
                                    editor.putInt("role", role);
                                    editor.putString("image", image);
                                    editor.putString("email", email);
                                    editor.commit();

                                    Toast.makeText(SignIn.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignIn.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        } else {
                            Toast.makeText(SignIn.this, "Error!", Toast.LENGTH_SHORT).show();
                        }

                    }


                });
    }


}