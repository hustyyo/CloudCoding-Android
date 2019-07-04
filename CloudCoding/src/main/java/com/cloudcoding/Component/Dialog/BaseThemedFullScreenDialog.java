package com.cloudcoding.Component.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.cloudcoding.R;

/**
 * ==================================
 * Created by michael.carr on 8/10/2014.
 * ==================================
 */
public class BaseThemedFullScreenDialog extends BaseThemedDialog {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = (ViewGroup) inflater.inflate(R.layout.base_themed_dialog_full_screen, null);

        inflateLayout(inflater);

        return mRootView;
    }
}