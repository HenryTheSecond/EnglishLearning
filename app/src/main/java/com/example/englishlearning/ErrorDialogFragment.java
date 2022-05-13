package com.example.englishlearning;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.englishlearning.Activity.NoteActivity;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.NotedWord;


public class ErrorDialogFragment extends DialogFragment {
    private EditText edtContent;
    private EditText edtMeaning;
    private Spinner spinner;
    private View mButtonOne;
    private View mButtonTwo;
    private TextView mTitle;

    private TextView mLabelBtnOne;
    private TextView mLabelBtnTow;

    private boolean mIsDisableBackButton;
    private boolean mIsNeedDismissAfterOnclick = false;

    private NoteAdapter noteAdapter;
    private ArrayAdapter<NotedWord.Type> adapter;

    public static ErrorDialogFragment newInstance(
            NoteAdapter noteAdapter,
            boolean mIsDisableBackButton,
            boolean isNeedDismissAfterOnclick) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        dialogFragment.noteAdapter = noteAdapter;
        dialogFragment.mIsDisableBackButton = mIsDisableBackButton;
        dialogFragment.mIsNeedDismissAfterOnclick = isNeedDismissAfterOnclick;
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        // Add back button listener
        dialog.setOnKeyListener((dialog1, keyCode, event) -> mIsDisableBackButton);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = view.findViewById(R.id.title);
        mButtonOne = view.findViewById(R.id.btnOne);
        mButtonTwo = view.findViewById(R.id.btnTwo);
        mLabelBtnOne = view.findViewById(R.id.labelBtnOne);
        mLabelBtnTow = view.findViewById(R.id.labelBtnTow);
        edtContent = view.findViewById(R.id.edt_content);
        edtMeaning = view.findViewById(R.id.edt_meaning);
        spinner = view.findViewById(R.id.spinner_type);
        adapter = new ArrayAdapter<NotedWord.Type>(view.getContext(), android.R.layout.simple_list_item_1, NotedWord.Type.values());
        spinner.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            mTitle.setVisibility(View.VISIBLE);
            mButtonOne.setVisibility(View.VISIBLE);
            mTitle.setText("Add new word");
            mButtonOne.setOnClickListener(v -> {
                UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
                SQLiteDatabase database = helper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("content", edtContent.getText().toString());
                contentValues.put("meaning", edtMeaning.getText().toString());
                contentValues.put("type", spinner.getSelectedItem().toString());
                long idInsert = database.insert("note_word", null, contentValues);

                if(noteAdapter != null) {
                    NotedWord item = new NotedWord((int) idInsert, edtContent.getText().toString(), edtMeaning.getText().toString(), NotedWord.Type.parseType(spinner.getSelectedItem().toString()));
                    noteAdapter.getList().add(item);
                }
                if (mIsNeedDismissAfterOnclick) {
                    dismiss();
                }
            });
            mLabelBtnOne.setText("Add");


        mButtonTwo.setVisibility(View.VISIBLE);
        mButtonTwo.setOnClickListener(v -> {
            if (mIsNeedDismissAfterOnclick) {
                dismiss();
            }
        });
        mLabelBtnTow.setText("Cancel");

    }
}
