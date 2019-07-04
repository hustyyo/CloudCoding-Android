package com.cloudcoding.Component.Dialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cloudcoding.R;
import com.cloudcoding.ViewBase.ListAdapter.ThemedDialogSingleListAdapter;
import com.cloudcoding.ViewBase.ListAdapterCallback;

import java.util.ArrayList;

/**
 * ==================================
 * Created by michael.carr on 2/07/2014.
 * ==================================
 */
public class ThemedSingleListDialog<T> extends BaseThemedDialog {

    protected ArrayList<T> mObjects;
    protected Callback<T> mCallback;
    protected ListAdapterCallback<T> mListAdapterCallback = null;
    protected String mTitle;
    protected ListView mListView;
    protected ThemedDialogSingleListAdapter<T> mArrayAdapter;
    protected T mSelectedObject;
    protected boolean mAddAllOption;


    public ThemedSingleListDialog(){
        setLayout(R.layout.themed_single_list_dialog);
    }

    public static <T> ThemedSingleListDialog newInstance(ArrayList<T> objects, T selectedObject,
                                                         String title, boolean addAllAtTop,
                                                         Callback<T> callback){

        ThemedSingleListDialog themedSingleListDialog = new ThemedSingleListDialog();

        themedSingleListDialog.mCallback = callback;
        themedSingleListDialog.mObjects = objects;
        themedSingleListDialog.mTitle = title;
        themedSingleListDialog.mSelectedObject = selectedObject;
        themedSingleListDialog.mAddAllOption = addAllAtTop;

        return themedSingleListDialog;

    }

    public static <T> ThemedSingleListDialog newInstance(ArrayList<T> objects,
                                                         T selectedObject, String title,
                                                         boolean addAllAtTop,
                                                         Callback<T> callback,
                                                         ListAdapterCallback<T> subCallback){

        ThemedSingleListDialog dialog = ThemedSingleListDialog.newInstance(objects,selectedObject,title,addAllAtTop,callback);
        dialog.mListAdapterCallback = subCallback;
        return dialog;
    }

    @Override
    protected void init(){
        setTitleText(mTitle);

        if (mAddAllOption){
            mObjects.add(0, null);
        }
    }

    @Override
    protected void findViews() {
        mListView = (ListView) findViewById(R.id.themed_single_list_dialog_listView);
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        mArrayAdapter = new ThemedDialogSingleListAdapter<>(getActivity(),
                R.layout.themed_dialog_single_list_item, mObjects, mListAdapterCallback);

        if (mSelectedObject != null) {
            if(mObjects.indexOf(mSelectedObject) >= 0){
                mArrayAdapter.setSelectedIndex(mObjects.indexOf(mSelectedObject));
            }
            else if(mListAdapterCallback != null){
                for (int i=0; i<mObjects.size(); i++){
                    T object = mObjects.get(i);
                    if(mListAdapterCallback.isTwoObjectSame(object,mSelectedObject)){
                        mArrayAdapter.setSelectedIndex(i);
                        break;
                    }
                }
            }

        } else {
            //If something crashes, blame this
            if (mAddAllOption){
                mArrayAdapter.setSelectedIndex(0);
            }
        }

        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(position);

            }
        });
    }

    private void onClick(int itemPos){

        T itemObject = mArrayAdapter.getItem(itemPos);
        if(mListAdapterCallback != null
                && !mListAdapterCallback.isRowEnableSelection(itemObject,itemPos)){
            mListAdapterCallback.onClickDisabledRow(itemObject,itemPos);
        }
        else {
            dismiss();
            if (mCallback != null) {
                mCallback.callBack(mArrayAdapter.getItem(itemPos),itemPos);
            }
        }
    }

    public static class Callback<T> {
        public void callBack(T object, int index){

        }
    }

}