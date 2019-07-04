package com.cloudcoding.Component.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cloudcoding.R;
import com.cloudcoding.ViewBase.GenericSelectableWrapper;
import com.cloudcoding.ViewBase.ListAdapter.ThemedDialogMultiListAdapter;
import com.cloudcoding.ViewBase.ListAdapterCallback;

import java.util.ArrayList;

/**
 * ==================================
 * Created by michael.carr on 2/07/2014.
 * ==================================
 */
public class ThemedMultiListDialog<T> extends BaseThemedDialog {

    protected ArrayList<GenericSelectableWrapper<T>> mObjects;
    protected ArrayList<T> mOriginalObjects;
    protected ArrayList<T> mSelectedObjects;
    protected ArrayList<Integer> mSelectedIndexes;
    protected Callback<T> mCallback;
    protected String mTitle;
    protected ListView mListView;
    protected Button mOkButton;
    protected Button mCancelButton;
    protected ThemedDialogMultiListAdapter<T> mArrayAdapter;
    protected ListAdapterCallback<T> mListAdapterCallback;

    protected View.OnClickListener headerRightListener;

    public ThemedMultiListDialog<T> setSubCallback(ListAdapterCallback<T> subCallback) {
        mListAdapterCallback = subCallback;
        return this;
    }

    public ThemedMultiListDialog(){
        setLayout(R.layout.themed_multi_list_dialog);
    }

    public static <T> ThemedMultiListDialog newInstance(ArrayList<T> objects,
                                                        ArrayList<T> selectedObjects,
                                                        String title,
                                                        Callback<T> callback){

        ThemedMultiListDialog themedMultiListDialog = new ThemedMultiListDialog();

        themedMultiListDialog.mCallback = callback;
        themedMultiListDialog.mTitle = title;
        themedMultiListDialog.mOriginalObjects = objects;
        themedMultiListDialog.mSelectedObjects = selectedObjects;

        return themedMultiListDialog;
    }

    public static <T> ThemedMultiListDialog newInstance(ArrayList<T> objects,
                                                        ArrayList<T> selectedObjects,
                                                        String title,
                                                        String rightText,
                                                        View.OnClickListener rightListener,
                                                        Callback<T> callback){

        ThemedMultiListDialog themedMultiListDialog = new ThemedMultiListDialog();

        themedMultiListDialog.mCallback = callback;
        themedMultiListDialog.mTitle = title;
        themedMultiListDialog.mOriginalObjects = objects;
        themedMultiListDialog.mSelectedObjects = selectedObjects;
        themedMultiListDialog.headerRightText = rightText;
        themedMultiListDialog.headerRightListener = rightListener;

        return themedMultiListDialog;
    }


    public static <T> ThemedMultiListDialog newInstance(String title,
                                                        ArrayList<GenericSelectableWrapper<T>> objects,
                                                        Callback<T> callback){

        ThemedMultiListDialog themedMultiListDialog = new ThemedMultiListDialog();

        themedMultiListDialog.mCallback = callback;
        themedMultiListDialog.mTitle = title;
        themedMultiListDialog.mObjects = objects;

        return themedMultiListDialog;
    }

    public static <T> ThemedMultiListDialog newInstance(ArrayList<T> objects, String title,
                                                        ArrayList<Integer> selectedIndices,
                                                        Callback<T> callback){

        ThemedMultiListDialog themedMultiListDialog = new ThemedMultiListDialog();

        themedMultiListDialog.mCallback = callback;
        themedMultiListDialog.mTitle = title;
        themedMultiListDialog.mOriginalObjects = objects;
        themedMultiListDialog.mSelectedIndexes = selectedIndices;

        return themedMultiListDialog;
    }

    @Override
    protected void init(){

        setTitleText(mTitle);

        mObjects = new ArrayList<GenericSelectableWrapper<T>>();

        for (T object : mOriginalObjects){
            mObjects.add(new GenericSelectableWrapper<T>(object));
        }

        if (mSelectedObjects != null){
            for (GenericSelectableWrapper<T> object : mObjects){
                if (mSelectedObjects.contains(object.getObject())){
                    object.setSelected(true);
                }

            }
        }

        if (mSelectedObjects != null && mListAdapterCallback != null){
            for (GenericSelectableWrapper<T> object : mObjects){

                for(T selectedObject : mSelectedObjects){

                    if(mListAdapterCallback.isTwoObjectSame(object.getObject(),selectedObject)){
                        object.setSelected(true);

                    }
                }
            }
        }


        if (mSelectedIndexes != null){
            for (GenericSelectableWrapper<T> object : mObjects) {
                for (Integer i : mSelectedIndexes) {
                    if (mObjects.indexOf(object) == i) {
                        object.setSelected(true);
                    }
                }
            }
        }
    }

    @Override
    protected void findViews() {
        mListView = (ListView) mRootView.findViewById(R.id.themed_multi_list_dialog_listView);
        mOkButton = (Button) mRootView.findViewById(R.id.themed_multi_list_dialog_okButton);
        mCancelButton = (Button) mRootView.findViewById(R.id.themed_multi_list_dialog_cancelButton);
    }

    public void updateData(ArrayList<T> objects, ArrayList<T> selectedObjects){
        mObjects = new ArrayList<GenericSelectableWrapper<T>>();

        for (T object : mOriginalObjects){
            mObjects.add(new GenericSelectableWrapper<T>(object));
        }

        if (mSelectedObjects != null){
            for (GenericSelectableWrapper<T> object : mObjects){
                if (mSelectedObjects.contains(object.getObject())){
                    object.setSelected(true);
                }
            }
        }

        if (mSelectedIndexes != null){
            for (GenericSelectableWrapper<T> object : mObjects) {
                for (Integer i : mSelectedIndexes) {
                    if (mObjects.indexOf(object) == i) {
                        object.setSelected(true);
                    }
                }
            }
        }


        mArrayAdapter = new ThemedDialogMultiListAdapter<T>(getActivity(), R.layout.themed_dialog_multi_list_item, mObjects, mListAdapterCallback);
        mListView.setAdapter(mArrayAdapter);
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setupViews() {

        super.setupViews();

        mArrayAdapter = new ThemedDialogMultiListAdapter<T>(getActivity(), R.layout.themed_dialog_multi_list_item, mObjects,mListAdapterCallback);
        mListView.setAdapter(mArrayAdapter);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void onSave(){

        dismiss();

        ArrayList<T> selectedItems = new ArrayList<T>();
        ArrayList<Integer> selectedIndex = new ArrayList<Integer>();

        for (int i=0; i<mObjects.size(); i++){

            GenericSelectableWrapper<T> selectableWrapper = mObjects.get(i);

            if (selectableWrapper.isSelected()){

                selectedIndex.add(new Integer(i));
                selectedItems.add(selectableWrapper.getObject());

            }
        }

        if (mCallback != null) {
            mCallback.callBack(selectedItems,selectedIndex);
        }
    }

    public static class Callback<T> {

        public void callBack(ArrayList<T> selectedObjects, ArrayList<Integer> selectedIndexArray){

        }
    }

}