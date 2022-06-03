package com.example.englishlearning.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.englishlearning.Constant;
import com.example.englishlearning.EssayNotification.UtilsEssay;
import com.example.englishlearning.EssaySubjectListActivity;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.example.englishlearning.UtilsNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DashBoard extends AppCompatActivity {

    Button btnListen, btnRead, btnWrite, btnTest, btnNote, btnReview, btnEssay;
    public static final String CALLING_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        UtilsNotification.deleteAlarm();

        btnTest = findViewById(R.id.btn_test);
        btnNote = findViewById(R.id.btn_note);
        btnReview = findViewById(R.id.btn_review);
        btnEssay = findViewById(R.id.btn_essay);

        btnEssay.setOnClickListener(view -> {
            if(Utils.isLoggedIn(DashBoard.this)){
                Intent intent = new Intent(DashBoard.this, EssaySubjectListActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(DashBoard.this, "You have to login to use this function", Toast.LENGTH_SHORT).show();
            }
        });

        btnTest.setOnClickListener(view -> {typeClick(view);});
        btnReview.setOnClickListener(view -> {typeClick(view);});

        btnNote.setOnClickListener(view ->{
            moveToActivity(NoteActivity.class);
        });

        Utils.createMenu(this);







        //testSendApi(UtilsEssay.getToken(this));
    }

    private void testSendApi(String token){
        String url = "https://fcm.googleapis.com/fcm/send";


        JSONObject data = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            data.put("user_name", "tuyen");
            data.put("description", "abda");


            jsonObject.putOpt("data", data);
            jsonObject.put("to", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        System.out.println(requestBody);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Success", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Fail", "Error: "
                                + error.getMessage());
                    }
                }) {

            @Override
            public byte[] getBody() {
                return requestBody == null ? null : requestBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "key=AAAAAmzWN6k:APA91bHAzv8KpF0kp_ex_9z0Mqj4cffbA3EamA4amllbexLDzvurBol31EuzpPYJknuGl9dGIP2nHVyV1lq_s9m6jG_nWnqHmpIm-JTOoe4NQ56fcsLR359-U9J2TendGu0cnTukIOn6");
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };


        requestQueue.add(jsonObjReq);
    }


    private void moveToActivity(Class activity) {
        Intent intent = new Intent(DashBoard.this, NoteActivity.class);
        startActivity(intent);
    }

    private void typeClick(View view){
        int viewId = view.getId();
        Intent intent = new Intent(DashBoard.this, ChooseTypeActivity.class);
        if(viewId == R.id.btn_test){
            intent.putExtra(CALLING_TYPE, "test");
        }else{
            intent.putExtra(CALLING_TYPE, "review");
        }
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "App paused", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "App stopped", Toast.LENGTH_SHORT).show();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "App destroyed", Toast.LENGTH_SHORT).show();
        UtilsNotification.day = Constant.initDayNoti;
        UtilsNotification.countAfterFirstNoti = -1;
        UtilsNotification.createAlarm();
        super.onDestroy();
    }
}