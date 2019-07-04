package com.cloudcoding.Component.Dialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudcoding.CommonLib.Util;
import com.cloudcoding.R;

import java.util.ArrayList;

/**
 * ==================================
 * Created by michael.carr on 8/10/2014.
 * ==================================
 */
public class BaseThemedDialog extends BaseDialog {

    protected static final int DIALOG_SHADOW_WIDTH = 16;
    protected static final int LOLLIPOP_DIALOG_SHADOW_WIDTH = 32;
    protected static final int VIBRATION_DURATION_MS = 10;
    protected static final int TITLE_BAR_HEIGHT = 60;

    protected ViewGroup mLayoutView;
    protected int layoutResource;
    protected RelativeLayout mMainLayout;
    protected ArrayList<String> mRequests;
    protected int mLayoutWidth;
    protected Vibrator mVibrator;
    protected ArrayList<View> mVibrateButtons;
    public TextView base_themed_dialog_right_text_view;
    public TextView base_themed_dialog_left_text_view;
    public ImageView base_themed_dialog_left_image_view;

    public String headerRightText;
    public String headerLeftText;

    protected View base_themed_dialog_titleView;
    protected String mTitle;
    protected boolean isWideDialog = false;

    protected void onLeftButton() {

    }

    protected void onRightButton() {

    }

    @Override
    protected void setupViews() {
        super.setupViews();

        //right text button
        base_themed_dialog_right_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightButton();
            }
        });
        base_themed_dialog_left_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftButton();
            }
        });
        base_themed_dialog_left_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftButton();
            }
        });

        base_themed_dialog_left_text_view.setEnabled(false);
        base_themed_dialog_right_text_view.setEnabled(false);
        if(headerRightText != null && headerRightText.length()>0){
            base_themed_dialog_right_text_view.setVisibility(View.VISIBLE);
            base_themed_dialog_left_text_view.setVisibility(View.VISIBLE);
            base_themed_dialog_right_text_view.setText(headerRightText);
            base_themed_dialog_right_text_view.setEnabled(true);
        }

        if(headerLeftText != null && headerLeftText.length()>0){
            base_themed_dialog_left_text_view.setVisibility(View.VISIBLE);
            base_themed_dialog_right_text_view.setVisibility(View.VISIBLE);
            base_themed_dialog_left_text_view.setText(headerLeftText);
            base_themed_dialog_left_text_view.setEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = (ViewGroup) inflater.inflate(R.layout.base_themed_dialog, null);
        setViewParams();

        inflateLayout(inflater);

        mVibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        mVibrateButtons = new ArrayList<>();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        base_themed_dialog_titleView = mRootView.findViewById(R.id.base_themed_dialog_titleView);
        base_themed_dialog_right_text_view = (TextView)mRootView.findViewById(R.id.base_themed_dialog_right_text_view);
        base_themed_dialog_left_text_view = (TextView)mRootView.findViewById(R.id.base_themed_dialog_left_text_view);
        base_themed_dialog_left_image_view = mRootView.findViewById(R.id.base_themed_dialog_left_image_view);
        return mRootView;
    }

    public BaseThemedDialog(){
        mRequests = new ArrayList<>();
    }

    public void setLayout(int layoutResource){
        this.layoutResource = layoutResource;
    }

    public void setTitleText(String titleText){
        if (titleText != null) {
            ((TextView) mRootView.findViewById(R.id.base_themed_dialog_titleTextView)).setText(titleText);
        }
    }

    protected void inflateLayout(LayoutInflater inflater){

        if (layoutResource > 0) {
            mMainLayout = (RelativeLayout) mRootView.findViewById(R.id.base_themed_dialog_mainRelativeLayout);
            mLayoutView = (ViewGroup) inflater.inflate(layoutResource, mMainLayout, true);

            if(!isWideDialog){
                mLayoutView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mLayoutWidth = mLayoutView.getMeasuredWidth();
            }else{
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                mLayoutWidth = (int)(screenWidth*0.94);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mLayoutWidth = mLayoutWidth + dpToPx(LOLLIPOP_DIALOG_SHADOW_WIDTH);
            } else {
                mLayoutWidth = mLayoutWidth + dpToPx(DIALOG_SHADOW_WIDTH);
            }
        }


        mVibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        mVibrateButtons = new ArrayList<>();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        base_themed_dialog_titleView = mRootView.findViewById(R.id.base_themed_dialog_titleView);
        base_themed_dialog_right_text_view = (TextView)mRootView.findViewById(R.id.base_themed_dialog_right_text_view);
        base_themed_dialog_left_text_view = (TextView)mRootView.findViewById(R.id.base_themed_dialog_left_text_view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViewParams();

    }

    protected void setViewParams(){
        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setLayout(mLayoutWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    protected View findViewById(int viewId){
        return mLayoutView.findViewById(viewId);
    }

    protected int getCurrentWidth(){
        return mRootView.getMeasuredWidth();
    }

    protected int getCurrentHeight(){
        return mRootView.getMeasuredHeight() - dpToPx(TITLE_BAR_HEIGHT);
    }

    protected void vibrateOnPress(View view){

        mVibrateButtons.add(view);

        for (final View b : mVibrateButtons){
            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            vibrate();
                            break;
                    }

                    return false;
                }
            });
        }
    }

    protected void setLayoutClickable(View view, boolean enabled) {
        Util.setLayoutClickable(view, enabled);
    }


    protected String getStringFromID(int stringID){
        return getActivity().getResources().getString(stringID);
    }

    protected void vibrate(){
        vibrate(VIBRATION_DURATION_MS);
    }

    protected void vibrate(int duration){
        mVibrator.vibrate(Long.valueOf(String.valueOf(duration)));
    }
}