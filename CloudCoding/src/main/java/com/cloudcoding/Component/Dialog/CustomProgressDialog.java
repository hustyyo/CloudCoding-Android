package com.cloudcoding.Component.Dialog;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudcoding.CommonLib.Util;
import com.cloudcoding.R;

/**
 * ==================================
 * Created by michael.carr on 9/09/2014.
 * ==================================
 */
public class CustomProgressDialog extends BaseThemedDialog {

    private static final String TITLE = "TITLE";
    private static final String DETAILS = "DETAILS";

    private String mTitle;
    private String mDetails;

    private TextView mDetailsTextView;

    public CustomProgressDialog(){
        setLayout(R.layout.custom_progress_dialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            mTitle = getArguments().getString(TITLE);
            mDetails = getArguments().getString(DETAILS);
        }
    }

    public static CustomProgressDialog newInstance(String title, String details){

        CustomProgressDialog fragment = new CustomProgressDialog();

        Bundle bundle = new Bundle(2);
        bundle.putString(TITLE, title);
        bundle.putString(DETAILS, details);
        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    protected void init() {
        getDialog().getWindow().setLayout(Util.dpToPx(500, getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void findViews() {
        mDetailsTextView = (TextView) mRootView.findViewById(R.id.custom_progress_dialog_detailsTextView);
    }

    @Override
    protected void setupViews() {
        if (mTitle != null){
            setTitleText(mTitle);
        } else {
            setTitleText(getString(R.string.com_cloudcoding_one_moment));
        }

        if (mDetails != null){
            mDetailsTextView.setText(mDetails);
        } else {
            mDetailsTextView.setText("...");
        }

        if(mTitle==null || mTitle.length()==0){
            base_themed_dialog_titleView.setVisibility(View.GONE);
        }
    }

    public void updateText(String message){

        if (mDetailsTextView != null && isResumed()){
            mDetailsTextView.setText(message);
        }
    }
}
