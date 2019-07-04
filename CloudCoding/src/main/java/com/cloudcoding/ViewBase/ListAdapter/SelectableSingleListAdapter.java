package com.cloudcoding.ViewBase.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloudcoding.R;

import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 6/02/14.
 * ====================================
 */
public class SelectableSingleListAdapter<T> extends ArrayAdapter<T> {

    Context context;
    int layoutResourceId;
    ArrayList<T> data = null;
    private Integer mSelectedIndex;

    public SelectableSingleListAdapter(Context context, ArrayList<T> data){

        super(context, R.layout.selectable_single_list_item, data);

        this.context = context;
        this.layoutResourceId = R.layout.selectable_single_list_item;
        this.data = data;
    }

    public SelectableSingleListAdapter(Context context, int layoutResourceId, ArrayList<T> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    int mSelectedColor = R.color.com_cloudcoding_list_selected;


    public SelectableSingleListAdapter(Context context, int layoutResourceId, int selectedColor, ArrayList<T> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.mSelectedColor = selectedColor;
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

            row.setTag(holder);

        } else {

            holder = (ListHolder)row.getTag();
        }

        if (mSelectedIndex != null) {

            if (mSelectedIndex == position) {
                row.setBackgroundResource(mSelectedColor);
            } else {
                row.setBackgroundResource(R.color.com_cloudcoding_list_not_selected);
            }
        }

        if (getItem(position) == null){

            holder.textView.setText(context.getString(R.string.com_cloudcoding_all));

        } else {

            T item = getItem(position);

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
    public T getItem(int i) {
        return data.get(i);
    }

    static class ListHolder{
        TextView textView;
    }
}
