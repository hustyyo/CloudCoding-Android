package com.cloudcoding.ViewLib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * ==================================
 * Created by michael.carr on 21/01/2015.
 * ==================================
 */
public class CenterLockHorizontalScrollView extends HorizontalScrollView {

    Context context;
    int prevIndex = 0;

    public CenterLockHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setSmoothScrollingEnabled(true);
    }

    public void setCenter(int index) {

        ViewGroup parent = (ViewGroup) getChildAt(0);

        View preView = parent.getChildAt(prevIndex);
        preView.setBackgroundColor(Color.parseColor("#64CBD8"));
        android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        preView.setLayoutParams(lp);

        View view = parent.getChildAt(index);
        view.setBackgroundColor(Color.RED);

        int screenWidth = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();

        int scrollX = (view.getLeft() - (screenWidth / 2))
                + (view.getWidth() / 2);
        this.smoothScrollTo(scrollX, 0);
        prevIndex = index;
    }

}
