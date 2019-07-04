package com.cloudcoding.ViewLib;

import android.content.Context;

import com.cloudcoding.R;

/**
 * Created by steve.yang on 19/09/16.
 */
public class TabSegmentWhiteHelper extends TabHelper{


    public TabSegmentWhiteHelper(Context context) {
        super(context);

        selectedTextColor = R.color.com_cloudcoding_tab_seg_selected_not_text;
        unselectedTextColor = R.color.com_cloudcoding_white;
        selectedBackgroundColor = R.color.com_cloudcoding_white;
        unselectedBackgroundColor = R.color.com_cloudcoding_tab_bg_clear;
        tabVerticalLineColor = R.color.com_cloudcoding_white;
    }

}
