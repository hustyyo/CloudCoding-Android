package com.cloudcoding.ViewBase.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudcoding.Component.Dialog.ThemedMultiListDialog;
import com.cloudcoding.R;
import com.cloudcoding.ViewBase.GenericSelectableWrapper;
import com.cloudcoding.ViewBase.ListAdapterCallback;

import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 6/02/14.
 * ====================================
 */
public class ThemedDialogMultiListAdapter<T> extends ArrayAdapter<GenericSelectableWrapper<T>> {

    Context context;
    int layoutResourceId;
    ArrayList<GenericSelectableWrapper<T>> data = null;
    ListAdapterCallback<T> mListAdapterCallback;
    boolean mShowSubText;


    public ThemedDialogMultiListAdapter(Context context, int layoutResourceId, ArrayList<GenericSelectableWrapper<T>> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public ThemedDialogMultiListAdapter(Context context, int layoutResourceId, ArrayList<GenericSelectableWrapper<T>> data,
                                        ListAdapterCallback<T> subCallback){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.mListAdapterCallback = subCallback;
        if(subCallback != null){
            this.mShowSubText = true;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ListHolder holder = null;

        if (row == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListHolder();

            holder.textView = (TextView) row.findViewById(R.id.themed_dialog_multi_list_item_textView);
            holder.checkBox = (ImageView) row.findViewById(R.id.themed_multi_list_dialog_checkBox);
            holder.themed_dialog_list_item_subtextView = (TextView)row.findViewById(R.id.themed_dialog_list_item_subtextView);
            holder.relativeLayout =  row.findViewById(R.id.themed_dialog_multi_list_item_RL);

            row.setTag(holder);

        } else {

            holder = (ListHolder)row.getTag();
        }

        final GenericSelectableWrapper<T> item = getItem(position);
        final T itemObject = item.getObject();
        if (item instanceof CharSequence) {
            holder.textView.setText((CharSequence) itemObject);
        } else {
            if (mListAdapterCallback != null) {
                CharSequence title = mListAdapterCallback.getTitleText(itemObject);
                holder.textView.setText((title != null) ? title : itemObject.toString());
            } else {
                holder.textView.setText(itemObject.toString());
            }
        }

        holder.checkBox.setSelected(item.isSelected());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListAdapterCallback != null && !mListAdapterCallback.isRowEnableSelection(itemObject,position)){
                    mListAdapterCallback.onClickDisabledRow(itemObject,position);
                }
                else {
                    item.setSelected(!item.isSelected());
                    notifyDataSetChanged();
                }

            }
        });

        if(mShowSubText){
            holder.themed_dialog_list_item_subtextView.setVisibility(View.VISIBLE);
            if(mListAdapterCallback != null){
                CharSequence sub = mListAdapterCallback.getSubText(item.getObject());
                holder.themed_dialog_list_item_subtextView.setText((sub!=null)?sub:"");
            }
            else {
                holder.themed_dialog_list_item_subtextView.setText("");
            }
        }else{
            holder.themed_dialog_list_item_subtextView.setVisibility(View.GONE);
        }

        if(mListAdapterCallback != null && !mListAdapterCallback.isRowEnableSelection(item.getObject(),position)){
            row.setAlpha(0.3f);
        }
        else {
            row.setAlpha(1.0f);
        }
        
        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GenericSelectableWrapper<T> getItem(int i) {
        return data.get(i);
    }

    static class ListHolder{
        TextView textView;
        TextView themed_dialog_list_item_subtextView;
        ImageView checkBox;
        View relativeLayout;
    }
}
