package com.cloudcoding.ViewBase.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudcoding.R;
import com.cloudcoding.ViewBase.GenericSelectableWrapper;

import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 6/02/14.
 * ====================================
 */
public class MultiListAdapter<T> extends ArrayAdapter<GenericSelectableWrapper<T>> {

    Context context;
    int layoutResourceId;
    ArrayList<GenericSelectableWrapper<T>> data = null;

    public MultiListAdapter(Context context, int layoutResourceId, ArrayList<GenericSelectableWrapper<T>> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ListHolder holder = null;

        if (row == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListHolder();

            holder.textView = (TextView) row.findViewById(R.id.themed_dialog_multi_list_item_textView);
            holder.checkBox = (ImageView) row.findViewById(R.id.themed_multi_list_dialog_checkBox);
            holder.relativeLayout = (RelativeLayout) row.findViewById(R.id.themed_dialog_multi_list_item_RL);

            row.setTag(holder);

        } else {

            holder = (ListHolder)row.getTag();
        }

        final GenericSelectableWrapper<T> item = getItem(position);
        if (item.getObject() instanceof CharSequence) {
            holder.textView.setText((CharSequence)item.getObject());
        } else {
            holder.textView.setText(item.getObject().toString());
        }

        holder.checkBox.setSelected(item.isSelected());

       final ImageView finalCheck = holder.checkBox;

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.setSelected(!item.isSelected());

                finalCheck.setSelected(item.isSelected());
            }
        });

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
        ImageView checkBox;
        RelativeLayout relativeLayout;
    }
}
