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
 * limitations under the License.
 */

package com.doomonafireball.betterpickers.calendardatepicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.radialtimepicker.SubTextFragment;

import java.util.Calendar;

/**
 * Dialog allowing users to select a date.
 */
public class CalendarDatePickerDialogWithSubText extends CalendarDatePickerDialog {

    SubTextFragment subTextFragment;
    ViewGroup left_container_view;

    public void setSubText(String title, int titleColor, String value, int valueColor) {
        if(null == subTextFragment){
            subTextFragment = new SubTextFragment();
        }
        subTextFragment.setSubText(title,titleColor,value,valueColor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.calendar_date_picker_dialog_simple, null);

        left_container_view = view.findViewById(R.id.left_container_view);
        if(left_container_view != null){
            left_container_view.setVisibility(View.GONE);
            if(subViewCallback != null) {
                subViewCallback.initLeftView(left_container_view);
            }
        }

        initView(savedInstanceState,view);

        if(subTextFragment != null){
            subTextFragment.initView(view);
        }

        return view;
    }

    SubViewCallback subViewCallback;

    public void setSubViewCallback(SubViewCallback callback) {
        this.subViewCallback = callback;
    }

    int mYear, mMonth, mDay;

    protected void updateDisplay(boolean announce) {
        super.updateDisplay(announce);

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        if(subViewCallback != null) {
            subViewCallback.updateSubText(year,month,day);
            subViewCallback.onDateChanged(year,month,day);
        }
        mYear = year;
        mMonth = month;
        mDay = day;
    }
    public static class SubViewCallback {
        public void updateSubText(int year, int month, int day) {
        }
        public void initLeftView(ViewGroup viewGroup){

        }
        public void onDateChanged(int year, int month, int day) {

        }
    }
}
