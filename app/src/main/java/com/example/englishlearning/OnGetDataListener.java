package com.example.englishlearning;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListener {
    void onSuccess(DataSnapshot snapshot);
    void onStart();
    void onFailure();
}
