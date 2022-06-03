package com.example.englishlearning.EssayNotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.englishlearning.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UtilsEssay {
    private static final String FILE_NAME_TOKEN = "DeviceToken";
    private static final String KEY = "token";

    public static void saveToken(Context context, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, token);
        editor.commit();
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY, "");
    }

    public static void sendNotification(String subject, String point, String comment){
        String url = "https://fcm.googleapis.com/fcm/send";


        JSONObject data = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            data.put("subject", subject);
            data.put("point", point);
            data.put("comment", comment);


            jsonObject.putOpt("data", data);
            jsonObject.put("to", UtilsEssay.getToken(MyApplication.getAppContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        System.out.println(requestBody);

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
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
}
