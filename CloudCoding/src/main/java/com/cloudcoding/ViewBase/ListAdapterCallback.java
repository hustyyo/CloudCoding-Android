package com.cloudcoding.ViewBase;

/**
 * Created by steveyang on 3/8/17.
 */

public class ListAdapterCallback<T> {
    public CharSequence getSubText(T object){
        return null;
    }

    public CharSequence getTitleText(T object){
        return null;
    }

    public boolean isTwoObjectSame(T object, T selectedObject){
        return (object == selectedObject);
    }

    public boolean isRowEnableSelection(T object, int index) {
        return true;
    }

    public void onClickDisabledRow(T object, int index){

    }
}
