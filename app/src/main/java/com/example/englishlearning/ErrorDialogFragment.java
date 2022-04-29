package com.example.englishlearning;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ErrorDialogFragment extends DialogFragment {

    private View.OnClickListener onClickBtnOne;
    private View.OnClickListener onCLickBtnTwo;

    private String mValueTitle;
    private String mValueMessage;

    private TextView mTitle;
    private TextView mMessage;

    private View mButtonOne;
    private View mButtonTwo;

    private TextView mLabelBtnOne;
    private TextView mLabelBtnTow;

    private String labelBtnOne;
    private String labelBtnTwo;

    private boolean mIsDisableBackButton;

    private boolean mIsNeedDismissAfterOnclick = false;

    public static ErrorDialogFragment newInstance(
            String valueTitle,
            String valueMessage,
            String labelBtnOne,
            String labelBtnTow,
            View.OnClickListener onClickBtnOne,
            View.OnClickListener onClickBtnTwo) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        dialogFragment.labelBtnTwo = labelBtnTow;
        dialogFragment.labelBtnOne = labelBtnOne;
        dialogFragment.mValueTitle = valueTitle;
        dialogFragment.mValueMessage = valueMessage;
        dialogFragment.onClickBtnOne = onClickBtnOne;
        dialogFragment.onCLickBtnTwo = onClickBtnTwo;
        return dialogFragment;
    }

    public static ErrorDialogFragment newInstance(
            String valueTitle,
            String valueMessage,
            String labelBtnOne,
            String labelBtnTwo,
            View.OnClickListener onClickBtnOne,
            View.OnClickListener onCLickBtnTwo,
            boolean mIsDisableBackButton,
            boolean isNeedDismissAfterOnclick) {
        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance(
                valueTitle,
                valueMessage,
                labelBtnOne,
                labelBtnTwo,
                onClickBtnOne,
                onCLickBtnTwo
        );
        errorDialogFragment.mIsDisableBackButton = mIsDisableBackButton;
        errorDialogFragment.mIsNeedDismissAfterOnclick = isNeedDismissAfterOnclick;
        return errorDialogFragment;
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
        mMessage = view.findViewById(R.id.message);
        mButtonOne = view.findViewById(R.id.btnOne);
        mButtonTwo = view.findViewById(R.id.btnTwo);
        mLabelBtnOne = view.findViewById(R.id.labelBtnOne);
        mLabelBtnTow = view.findViewById(R.id.labelBtnTow);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (TextUtils.isEmpty(mValueTitle)) {
            mTitle.setVisibility(View.GONE);
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(mValueTitle);
        }

        if (TextUtils.isEmpty(mValueMessage)) {
            mMessage.setVisibility(View.GONE);
        } else {
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setText(mValueMessage);
        }

        if (onClickBtnOne != null && !TextUtils.isEmpty(labelBtnOne)) {
            mButtonOne.setVisibility(View.VISIBLE);
            mButtonOne.setOnClickListener(v -> {
                onClickBtnOne.onClick(v);
                if (mIsNeedDismissAfterOnclick) {
                    dismiss();
                }
            });
            mLabelBtnOne.setText(labelBtnOne);
        }

        if (onCLickBtnTwo != null && !TextUtils.isEmpty(labelBtnTwo)) {
            mButtonTwo.setVisibility(View.VISIBLE);
            mButtonTwo.setOnClickListener(v -> {
                onCLickBtnTwo.onClick(v);
                if (mIsNeedDismissAfterOnclick) {
                    dismiss();
                }
            });
            mLabelBtnTow.setText(labelBtnTwo);
        }
    }
}
