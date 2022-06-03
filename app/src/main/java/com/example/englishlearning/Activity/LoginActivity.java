package com.example.englishlearning.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishlearning.Activity.Admin.AdminDashBoardActivity;
import com.example.englishlearning.EssayNotification.UtilsEssay;
import com.example.englishlearning.ListWaitingEssayActivity;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private RadioGroup radioGroup;
    private TextView tvGuest;
    private Button btnLogin;
    private Button btnRegister;

    DatabaseReference database;
    public static final int REGISTER_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        radioGroup = findViewById(R.id.radio_group);
        tvGuest = findViewById(R.id.tv_login_with_guest);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(view -> {
            if(Utils.checkInternet(view.getContext()))
                btnLoginClicked();
        });

        tvGuest.setOnClickListener(view -> {
            moveToDashBoard("Normal");
        });

        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            startActivityForResult(intent, REGISTER_REQUEST_CODE);
        });

        initMoveToDashBoard();
    }

    private void btnLoginClicked(){
        RadioButton radioUser = findViewById(R.id.radio_user);
        RadioButton radioAdmin = findViewById(R.id.radio_admin);

        database = FirebaseDatabase.getInstance().getReference("user");
        if(radioUser.isChecked()){
            database.orderByChild("username").equalTo(etUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if(Utils.HashPassword( etPassword.getText().toString().trim() ).equals( snapshot.child(etUsername.getText().toString()).child("password").getValue() ) )
                            login();
                        else
                            Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(radioAdmin.isChecked()){
            //Admin log in
            database.orderByChild("username").equalTo(etUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() ){
                        DataSnapshot user = snapshot.child(etUsername.getText().toString());
                        System.out.println(user);
                        if(Utils.HashPassword( etPassword.getText().toString().trim() ).equals( user.child("password").getValue() )
                           && user.child("isAdmin").exists() && user.child("isAdmin").getValue(Boolean.class))
                            loginAdmin();
                        else
                            Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            database.orderByChild("username").equalTo(etUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() ){
                        DataSnapshot user = snapshot.child(etUsername.getText().toString());
                        System.out.println(user);
                        if(Utils.HashPassword( etPassword.getText().toString().trim() ).equals( user.child("password").getValue() )
                                && user.child("isTeacher").exists() && user.child("isTeacher").getValue(Boolean.class))
                            loginTeacher();
                        else
                            Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void loginTeacher(){
        Utils.saveLoginTeacher(this, etUsername.getText().toString());
        if(getIntent().getBooleanExtra("isSynchronize", false)){
            Utils.synchronizeData(this);
        }
        moveToDashBoard("Teacher");
    }

    private void loginAdmin(){
        Utils.saveLoginAdmin(this, etUsername.getText().toString());
        if(getIntent().getBooleanExtra("isSynchronize", false)){
            Utils.synchronizeData(this);
        }
        moveToDashBoard("Admin");
    }

    private void login(){
        Utils.saveLogin(this, etUsername.getText().toString());
        if(getIntent().getBooleanExtra("isSynchronize", false)){
            Utils.synchronizeData(this);
        }
        database.child(etUsername.getText().toString()).child("token").setValue(UtilsEssay.getToken(this));

        moveToDashBoard("Normal");
    }

    private boolean validate(){
        return !(etUsername.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("") );
    }

    private void initMoveToDashBoard(){
        if(Utils.isLoggedIn(this)){
            if(Utils.isAdmin(this))
                moveToDashBoard("Admin");
            else if(Utils.isTeacher(this))
                moveToDashBoard("Teacher");
            else
                moveToDashBoard("Normal");
        }
    }

    private void moveToDashBoard(String type){
        Intent intent;
        if(type.equals("Normal"))
            intent = new Intent(LoginActivity.this, DashBoard.class);
        else if(type.equals("Admin"))
            intent = new Intent(LoginActivity.this, AdminDashBoardActivity.class);
        else{
            intent = new Intent(LoginActivity.this, ListWaitingEssayActivity.class);
        }
        startActivity(intent);
        finish();
    }
}