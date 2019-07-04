/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.doomonafireball.betterpickers.radialtimepicker;

import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout.OnValueSelectedListener;
import com.nineoldandroids.animation.ObjectAnimator;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Dialog to set a time.
 */
public class RadialTimePickerDialogWithSubText extends RadialTimePickerDialog {

    SubTextFragment subTextFragment;

    public void setSubText(String title, int titleColor, String value, int valueColor) {
        if(null == subTextFragment){
            subTextFragment = new SubTextFragment();
        }
        subTextFragment.setSubText(title,titleColor,value,valueColor);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getShowsDialog()) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        View view = inflater.inflate(R.layout.radial_time_picker_dialog_with_sub_text, null);

        initDialogView(savedInstanceState,view);

        if(subTextFragment != null){
            subTextFragment.initView(view);
        }

        return view;
    }
}
