package com.cloudcoding.Component.Dialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cloudcoding.DataClasses.ListItemWrapper;
import com.cloudcoding.R;
import com.cloudcoding.ViewBase.ListAdapter.SingleListAdapter;

import java.util.ArrayList;

/**
 * ==================================
 * Created by michael.carr on 2/07/2014.
 * ==================================
 */
public class SingleListDialog extends BaseThemedDialog {

    private ArrayList<ListItemWrapper> mObjects;
    private Callback mCallback;
    private String mTitle;
    private ListView mListView;
    private SingleListAdapter mArrayAdapter;
    private Object mSelectedObject;
    private boolean mAddAllOption;
    ListItemWrapper.ItemNameConverter mItemNameConverter;

    public SingleListDialog(){
        setLayout(R.layout.themed_single_list_dialog);
    }

    public static  SingleListDialog newInstance(ArrayList objects, Object selectedObject, String title, boolean addAllAtTop, Callback callback, ListItemWrapper.ItemNameConverter nameConverter ){

        SingleListDialog themedSingleListDialog = new SingleListDialog();

        themedSingleListDialog.mCallback = callback;
        themedSingleListDialog.mObjects = objects;
        themedSingleListDialog.mTitle = title;
        themedSingleListDialog.mSelectedObject = selectedObject;
        themedSingleListDialog.mAddAllOption = addAllAtTop;
        themedSingleListDialog.mItemNameConverter = nameConverter;
        return themedSingleListDialog;

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

        mArrayAdapter = new SingleListAdapter(getActivity(), R.layout.themed_dialog_single_list_item, mObjects, mItemNameConverter);

        if (mSelectedObject != null) {
            mArrayAdapter.setSelectedIndex(mObjects.indexOf(mSelectedObject));
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

        dismiss();

        if (mCallback != null) {
            mCallback.callBack(((ListItemWrapper)mArrayAdapter.getItem(itemPos)).dataObject,itemPos);
        }

    }

    public interface Callback {
        public void callBack(Object object, int index);
    }

}