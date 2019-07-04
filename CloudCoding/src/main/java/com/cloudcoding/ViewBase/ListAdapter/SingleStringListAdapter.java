package com.cloudcoding.ViewBase.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudcoding.R;

import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 6/02/14.
 * ====================================
 */
public class SingleStringListAdapter extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    ArrayList data = null;
    private Integer mSelectedIndex;


    public SingleStringListAdapter(Context context, int layoutResourceId, ArrayList data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
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

        if (getItem(position) == null){

            holder.textView.setText(context.getString(R.string.com_cloudcoding_all));

        } else {

            Object item = getItem(position);

            if (item instanceof CharSequence) {
                holder.textView.setText((CharSequence) item);
            } else {
                holder.textView.setText(item.toString());
            }
        }

        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    static class ListHolder{
        TextView textView;
        ImageView isSelectedImageView;
    }
}
