package com.cloudcoding.ViewLib;

import android.content.Context;

import com.cloudcoding.R;

/**
 * Created by steve.yang on 19/09/16.
 */
public class TabSegmentHelper extends TabHelper{


    public TabSegmentHelper(Context context) {
        super(context);

        selectedTextColor = R.color.com_cloudcoding_tab_seg_selected_text;
        unselectedTextColor = R.color.com_cloudcoding_tab_seg_selected_not_text;
        selectedBackgroundColor = R.color.com_cloudcoding_tab_seg_selected_bg;
        unselectedBackgroundColor = R.color.com_cloudcoding_tab_seg_selected_not_bg;
        tabVerticalLineColor = R.color.com_cloudcoding_orange_lighter_1;
    }

}
