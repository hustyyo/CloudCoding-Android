package com.cloudcoding.ViewBase.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudcoding.Component.Dialog.ThemedSingleListDialog;
import com.cloudcoding.R;
import com.cloudcoding.ViewBase.ListAdapterCallback;

import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 6/02/14.
 * ====================================
 */
public class ThemedDialogSingleListAdapter<T> extends ArrayAdapter<T> {

    Context context;
    int layoutResourceId;
    ArrayList<T> data = null;
    private Integer mSelectedIndex;
    boolean mShowSubText;
    ListAdapterCallback<T> mListAdapterCallback;

    public ThemedDialogSingleListAdapter(Context context, int layoutResourceId, ArrayList<T> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public ThemedDialogSingleListAdapter(Context context, int layoutResourceId, ArrayList<T> data,
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


    public void setSelectedIndex(int index){
        mSelectedIndex = index;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ListHolder holder = null;

        if (row == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListHolder();

            holder.textView = (TextView)row.findViewById(R.id.themed_dialog_single_list_item_textView);
            holder.themed_dialog_single_list_item_subtextView = (TextView)row.findViewById(R.id.themed_dialog_single_list_item_subtextView);
            holder.isSelectedImageView = (ImageView) row.findViewById(R.id.themed_dialog_single_list_item_imageView);

            row.setTag(holder);

        } else {

            holder = (ListHolder)row.getTag();
        }

        if (mSelectedIndex != null) {

            if (mSelectedIndex == position) {
                holder.isSelectedImageView.setVisibility(View.VISIBLE);
            } else {
                holder.isSelectedImageView.setVisibility(View.GONE);
            }
        }

        T item = getItem(position);

        if (item == null){

            holder.textView.setText(context.getString(R.string.com_cloudcoding_all));

        } else {

            if (item instanceof CharSequence) {
                holder.textView.setText((CharSequence) item);
            } else {
                if (mListAdapterCallback != null) {
                    CharSequence title = mListAdapterCallback.getTitleText(item);
                    holder.textView.setText((title != null) ? title : item.toString());
                } else {
                    holder.textView.setText(item.toString());
                }
            }
        }
        if(mListAdapterCallback != null && !mListAdapterCallback.isRowEnableSelection(item,position)){
            row.setAlpha(0.3f);
        }
        else {
            row.setAlpha(1.0f);
        }

        if(mShowSubText){
            holder.themed_dialog_single_list_item_subtextView.setVisibility(View.VISIBLE);
            CharSequence sub = mListAdapterCallback.getSubText(item);
            holder.themed_dialog_single_list_item_subtextView.setText((sub!=null)?sub:"");
        }else{
            holder.themed_dialog_single_list_item_subtextView.setVisibility(View.GONE);
        }


        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    static class ListHolder{
        TextView textView;
        TextView themed_dialog_single_list_item_subtextView;
        ImageView isSelectedImageView;
    }
}
