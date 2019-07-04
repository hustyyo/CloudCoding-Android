package com.cloudcoding.ViewBase.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudcoding.DataClasses.ListItemWrapper;
import com.cloudcoding.R;
import com.cloudcoding.ViewBase.ListAdapterCallback;
import com.cloudcoding.ViewBase.ListHolderWithSub;

import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 6/02/14.
 * ====================================
 */
public class SimpleListAdapterWithSub<T> extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    ArrayList<T> data = null;
    ListAdapterCallback<T> mListAdapterCallback;

    public SimpleListAdapterWithSub(Context context, ArrayList<T> data, ListAdapterCallback<T> listAdapterCallback){
        super(context, R.layout.list_item_simple_with_sub, data);
        layoutResourceId = R.layout.list_item_simple_with_sub;
        this.context = context;
        this.data = data;
        mListAdapterCallback = listAdapterCallback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ListHolderWithSub holder = null;

        if (row == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListHolderWithSub();

            holder.textView = (TextView)row.findViewById(R.id.main_text);
            holder.subTextView = (TextView)row.findViewById(R.id.sub_text);

            row.setTag(holder);

        } else {

            holder = (ListHolderWithSub)row.getTag();
        }

        T item = getItem(position);
        CharSequence title = "";
        CharSequence sub = "";
        if(mListAdapterCallback != null){
            sub = mListAdapterCallback.getSubText(item);
            title = mListAdapterCallback.getTitleText(item);
            holder.textView.setText((title!=null)?title:item.toString());
            holder.subTextView.setText(sub);
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


}
