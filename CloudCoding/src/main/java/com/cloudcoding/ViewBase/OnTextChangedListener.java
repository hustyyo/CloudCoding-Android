package com.cloudcoding.ViewBase;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * ==================================
 * Created by michael.carr on 5/12/2014.
 * ==================================
 */
public class OnTextChangedListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Do Nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //Do nothing
    }
}
