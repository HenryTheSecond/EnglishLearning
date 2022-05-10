package com.example.englishlearning.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etEmail, etPhone;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);


        btnRegister.setOnClickListener(view -> {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("user");

            database.orderByChild("username").equalTo(etUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(Utils.checkInternet( RegisterActivity.this )){
                        if(validate()){
                            if(snapshot.exists()){
                                Toast.makeText(RegisterActivity.this, "Username is already existed", Toast.LENGTH_SHORT).show();
                            }else{
                                database.child( etUsername.getText().toString() ).child("username").setValue(etUsername.getText().toString());
                                database.child( etUsername.getText().toString() ).child("password").setValue( Utils.HashPassword(etPassword.getText().toString())  );
                                database.child( etUsername.getText().toString() ).child("email").setValue(etEmail.getText().toString());
                                database.child( etUsername.getText().toString() ).child("phone").setValue(etPhone.getText().toString());
                            }
                        }else
                            Toast.makeText(RegisterActivity.this, "Fulfill all the fields", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    private boolean validate(){
        return !(etUsername.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("") ||
                etEmail.getText().toString().trim().equals("") || etPhone.getText().toString().trim().equals(""));
    }
}