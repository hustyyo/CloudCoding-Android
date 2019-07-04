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
import com.cloudcoding.ViewBase.ListClickCallback;

import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 6/02/14.
 * ====================================
 */
public class ListAdapterWithIcon extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;

    int mIconResourceId;

    ArrayList<String> mDataList = new ArrayList<>();

    public ListAdapterWithIcon(Context context, int iconResourceId, ArrayList<String> data){
        super(context, R.layout.list_item_with_icon, data);
        this.context = context;
        this.layoutResourceId = R.layout.list_item_with_icon;

        mDataList = data;
        mIconResourceId  = iconResourceId;
    }

    ListClickCallback mIconClickListener;

    public ListAdapterWithIcon(Context context, int iconResourceId, ArrayList<String> data, ListClickCallback iconClickListener){
        super(context, R.layout.list_item_with_icon, data);
        this.context = context;
        this.layoutResourceId = R.layout.list_item_with_icon;

        mDataList = data;
        mIconResourceId  = iconResourceId;
        mIconClickListener = iconClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ListHolder holder = null;

        if (row == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListHolder();

            holder.title = (TextView) row.findViewById(R.id.text);

            holder.icon = (ImageView) row.findViewById(R.id.icon);


            row.setTag(holder);

        } else {

            holder = (ListHolder)row.getTag();
        }

        holder.title.setText(getItem(position));
        holder.icon.setImageResource(mIconResourceId);

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIconClickListener != null){
                    mIconClickListener.onClick(position,getItem(position));
                }
            }
        });

        return row;
    }



    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int i) {
        return mDataList.get(i);
    }

    class ListHolder{
        TextView title;
        TextView sub;
        ImageView icon;
    }
}