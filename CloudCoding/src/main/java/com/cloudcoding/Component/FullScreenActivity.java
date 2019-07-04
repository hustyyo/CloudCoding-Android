package com.cloudcoding.Component;

import android.os.Bundle;

import com.cloudcoding.R;

import icepick.Icepick;

/**
 * ====================================
 * Created by michael.carr on 24/06/2014.
 * ====================================
 */
public class FullScreenActivity extends BaseActivity {

    public static final String FS_ITEM_TYPE = "FS_ITEM_TYPE";
    public static final String FS_ITEM_PATH = "FS_ITEM_PATH";

    private int mItemType;
    private String mItemPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey(FS_ITEM_TYPE)){
                mItemType = extras.getInt(FS_ITEM_TYPE);
            }
            if (extras.containsKey(FS_ITEM_PATH)){
                mItemPath = extras.getString(FS_ITEM_PATH);
            }
        }

        setFullscreenMode();
    }
}
