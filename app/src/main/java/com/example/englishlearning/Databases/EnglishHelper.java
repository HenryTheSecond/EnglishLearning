package com.example.englishlearning.Databases;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class EnglishHelper extends SQLiteAssetHelper {
    public static final String DBNAME = "english_learning.db";
    public static final int DBVERSION = 1;

    public EnglishHelper(Context context){
        super(context,DBNAME,null,DBVERSION);
    }
}
