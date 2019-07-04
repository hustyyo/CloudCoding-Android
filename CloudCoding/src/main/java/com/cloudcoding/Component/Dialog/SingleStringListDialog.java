package com.cloudcoding.Component.Dialog;

import android.widget.ListView;

import com.cloudcoding.R;
import com.cloudcoding.ViewBase.ListAdapter.SingleStringListAdapter;

import java.util.ArrayList;

/**
 * ==================================
 * Created by michael.carr on 2/07/2014.
 * ==================================
 */
public class SingleStringListDialog extends BaseThemedDialog {

    public ArrayList<String> mObjects;
    public String mTitle;
    public ListView mListView;
    public SingleStringListAdapter mArrayAdapter;

    public SingleStringListDialog(){
        setLayout(R.layout.themed_single_list_dialog);
    }

    public static SingleStringListDialog newInstance(ArrayList objects, String title){

        SingleStringListDialog themedSingleListDialog = new SingleStringListDialog();

        themedSingleListDialog.mObjects = objects;
        themedSingleListDialog.mTitle = title;
        return themedSingleListDialog;

    }

    @Override
    protected void init(){
        setTitleText(mTitle);

    }

    @Override
    protected void findViews() {
        mListView = (ListView) findViewById(R.id.themed_single_list_dialog_listView);
    }

    @Override
    protected void setupViews() {

        mArrayAdapter = new SingleStringListAdapter(getActivity(), R.layout.themed_dialog_single_list_item, mObjects);

        mListView.setAdapter(mArrayAdapter);
    }



    public interface Callback {
        public void callBack(Object object, int index);
    }

}