package com.cloudcoding.Component.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;

import com.cloudcoding.R;

import java.util.ArrayList;
import java.util.List;

import cn.jeesoft.widget.pickerview.CharacterPickerView;
import cn.jeesoft.widget.pickerview.OnOptionChangedListener;

/**
 * ==================================
 * Created by michael.carr on 21/10/2014.
 * ==================================
 */
public class DurationDialog extends BaseThemedDialog {

    int selectedHour;
    int selectedMinute;
    Callback durationCallback;

    public DurationDialog(){
        setLayout(R.layout.duration_dialog);
    }

    public static DurationDialog newInstance(
            int hour,
            int minute,
            Callback callback){


        DurationDialog fragment = new DurationDialog();

        Bundle bundle = new Bundle(1);
        fragment.setArguments(bundle);

        fragment.durationCallback = callback;
        fragment.selectedHour = hour;
        if(0 != hour){
            fragment.selectedMinute = minute/5;
        }else{
            fragment.selectedMinute = (minute/5 - 1);
            if(fragment.selectedMinute < 0 ){
                fragment.selectedMinute = 0;
            }
        }

        return fragment;

    }

    @Override
    protected void init() {
        headerRightText = getString(R.string.com_cloudcoding_save);
    }

    @Override
    protected void onRightButton() {

        if(durationCallback != null){

            if(selectedHour == 0){
                durationCallback.onResult(selectedHour,(selectedMinute + 1)*5);
            }
            else {
                durationCallback.onResult(selectedHour,selectedMinute*5);
            }
        }
        dismiss();
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setupViews() {
        super.setupViews();

        setTitleText(getString(R.string.com_cloudcoding_duration));

        CharacterPickerView duration_picker = (CharacterPickerView)mRootView.findViewById(R.id.duration_picker);
        String hours = getString(R.string.com_cloudcoding_hours);
        String min = getString(R.string.com_cloudcoding_min);

        ArrayList<String> hourList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourList.add(i+" "+hours);
        }

        ArrayList<String> minuteList = new ArrayList<>();
        for (int i = 0; i < 60; i = i + 5) {

            minuteList.add(i+" " + min);
        }
        ArrayList<String> minuteListForZeroHour = new ArrayList<>();
        for (int i = 5; i < 60; i = i + 5) {

            minuteListForZeroHour.add(i+" " + min);
        }

        List<List<String>> minuteListForHours = new ArrayList<>();
        for(int i = 0; i < hourList.size(); i++) {
            if(i == 0){
                minuteListForHours.add(minuteListForZeroHour);
            }else{
                minuteListForHours.add(minuteList);
            }
        }
        duration_picker.setPicker(hourList,minuteListForHours);
        duration_picker.setSelectOptions(selectedHour,selectedMinute);
        duration_picker.setOnOptionChangedListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                selectedHour = option1;
                selectedMinute = option2;
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(mGenericCallback != null){
            mGenericCallback.callBack();
        }
    }


    public  static class Callback {
        public void onResult(int hour, int minute) {

        }
    }
}
