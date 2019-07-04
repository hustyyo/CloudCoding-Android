package com.cloudcoding.Component.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cloudcoding.CommonLib.Util;
import com.cloudcoding.R;

/**
 * ==================================
 * Created by michael.carr on 7/07/2014.
 * ==================================
 */
public class ErrorDialog extends BaseDialog {

    private TextView mTitleTextView;
    private TextView mErrorText;
    private TextView mCloseButton;

    private String mTitle;
    private String mContent;
    private String mCloseButtonText;

    private OnErrorCallback mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRootView = (ViewGroup) inflater.inflate(R.layout.error_dialog, container);

        return mRootView;
    }

    public static ErrorDialog newInstance(String title, String content, String buttonText){

        ErrorDialog fragment = new ErrorDialog();

        if (title != null){
            fragment.mTitle = title;
        }

        if (content != null){
            fragment.mContent = content;
        }

        if (buttonText != null) {
            fragment.mCloseButtonText = buttonText;
        }

        return fragment;

    }

    public void setOnErrorCallback(OnErrorCallback callback){
        mCallback = callback;
    }

    @Override
    protected void init(){

        getDialog().getWindow().setLayout(Util.dpToPx(400, getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    protected void findViews() {

        mTitleTextView = (TextView) mRootView.findViewById(R.id.error_dialog_titleTextView);
        mCloseButton = (TextView) mRootView.findViewById(R.id.error_dialog_closeTextView);
        mErrorText = (TextView) mRootView.findViewById(R.id.error_dialog_errorTextView);

    }

    @Override
    protected void setupViews() {

        if (mTitle != null){
            mTitleTextView.setText(mTitle);
        } else {
            mTitleTextView.setText("");
        }

        if (mContent != null) {
            mErrorText.setText(mContent);
        } else {
            mErrorText.setHint("");
        }

        if (mCloseButtonText != null){
            mCloseButton.setText(mCloseButtonText);
        } else {
            mCloseButton.setText(getString(R.string.com_cloudcoding_ok));
        }

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                if (mCallback != null){
                    mCallback.onDismiss();
                }
            }
        });
    }

    public interface OnErrorCallback {
        public void onDismiss();
    }

}
