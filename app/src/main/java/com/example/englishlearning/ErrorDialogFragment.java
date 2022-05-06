package com.example.englishlearning;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ErrorDialogFragment extends DialogFragment {
    private EditText edtContent;
    private EditText edtMeaning;
    private Spinner spinner;
    private View mButtonOne;
    private View mButtonTwo;
    private TextView mTitle;

    private TextView mLabelBtnOne;
    private TextView mLabelBtnTow;

    private boolean mIsAdd;
    private boolean mIsDisableBackButton;
    private boolean mIsNeedDismissAfterOnclick = false;

    public static ErrorDialogFragment newInstance(
            boolean mIsAdd,
            boolean mIsDisableBackButton,
            boolean isNeedDismissAfterOnclick) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        dialogFragment.mIsAdd = mIsAdd;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(mIsAdd){
            mTitle.setVisibility(View.VISIBLE);
            mButtonOne.setVisibility(View.VISIBLE);
            mTitle.setText("Add new word");
            mButtonOne.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), edtContent.getText().toString(), Toast.LENGTH_LONG).show();
                if (mIsNeedDismissAfterOnclick) {
                    dismiss();
                }
            });
            mLabelBtnOne.setText("Add");
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mButtonOne.setVisibility(View.VISIBLE);
            mTitle.setText("Edit word");
            mButtonOne.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), edtContent.getText().toString(), Toast.LENGTH_LONG).show();
                if (mIsNeedDismissAfterOnclick) {
                    dismiss();
                }
            });
            mLabelBtnOne.setText("Edit");
        }

        mButtonTwo.setVisibility(View.VISIBLE);
        mButtonTwo.setOnClickListener(v -> {
            if (mIsNeedDismissAfterOnclick) {
                dismiss();
            }
        });
        mLabelBtnTow.setText("Cancel");

    }
}
