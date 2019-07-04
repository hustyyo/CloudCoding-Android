package com.cloudcoding.Component.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cloudcoding.CommonLib.Util;
import com.cloudcoding.R;

/**
 * ==================================
 * Created by michael.carr on 29/08/2014.
 * ==================================
 */
public class YesNoDialog extends DialogFragment{

    private ViewGroup mRootView;

    private TextView mTitleTextView;
    private TextView mDetailsTextView;
    private Button mAcceptButton;
    private Button mDeclineButton;

    private String mTitle;
    private String mContent;
    private String mAcceptButtonText;
    private String mDeclineButtonText;

    private Callback mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRootView = (ViewGroup) inflater.inflate(R.layout.yes_no_dialog, container);

        return mRootView;
    }

    public static YesNoDialog newInstance(String title, String content, String buttonAcceptText, String buttonDeclineText, Callback callback){

        YesNoDialog fragment = new YesNoDialog();

        if (title != null){
            fragment.mTitle = title;
        }

        if (content != null){
            fragment.mContent = content;
        }

        if (buttonAcceptText != null) {
            fragment.mAcceptButtonText = buttonAcceptText;
        }

        if (buttonDeclineText != null){
            fragment.mDeclineButtonText = buttonDeclineText;
        }

        if (callback != null){
            fragment.mCallback = callback;
        }

        return fragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setInitialValues();
        setListeners();
    }

    public void init(){

        getDialog().getWindow().setLayout(Util.dpToPx(400, getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);

        mTitleTextView = (TextView) mRootView.findViewById(R.id.yes_no_dialog_titleTextView);
        mAcceptButton = (Button) mRootView.findViewById(R.id.yes_no_dialog_saveButton);
        mDeclineButton = (Button) mRootView.findViewById(R.id.yes_no_dialog_cancelButton);
        mDetailsTextView = (TextView) mRootView.findViewById(R.id.yes_no_dialog_detailsTextView);

    }

    private void setInitialValues(){

        if (mTitle != null){
            mTitleTextView.setText(mTitle);
        } else {
            mTitleTextView.setText("");
        }

        if (mContent != null) {
            mDetailsTextView.setText(mContent);
        } else {
            mDetailsTextView.setHint("");
        }

        if (mAcceptButtonText != null){
            mAcceptButton.setText(mAcceptButtonText);
        } else {
            mAcceptButton.setText(getString(R.string.com_cloudcoding_ok));
        }

        if (mDeclineButtonText != null){
            mDeclineButton.setText(mDeclineButtonText);
        } else {
            mDeclineButton.setText(getString(R.string.com_cloudcoding_cancel));
        }
    }

    private void setListeners(){

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onAcceptPress();
                dismiss();
            }
        });

        mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onDeclinePress();
                dismiss();
            }
        });

    }

    public interface Callback{

        public void onAcceptPress();

        public void onDeclinePress();


    }
}
