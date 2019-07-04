package com.cloudcoding.ViewLib;

import android.view.View;
import android.widget.TextView;

/**
 * Created by steve.yang on 19/09/16.
 */
public class TabSegment {


    View[] mTabs;
    View[] mContentViews;
    TabCallback mTabCallback;
    public int activeTabIndex = -1;

    public TabSegment() {

    }

    public static class TabCallback {

        public void onTabActive(View tab, View contentView, int index, boolean active){

        }

    }

    public void initTab(final View[] tabs, final View[] contentViews, String[] tabText,int initActiveIndex, final TabCallback callback) {
        initTab(tabs,contentViews,null,tabText,initActiveIndex,callback);
    }

    public void initTab(final View[] tabs, final View[] contentViews, boolean[] visibleList, String[] tabText,
                                                     int initActiveIndex, final TabCallback callback) {


        if(tabs == null){
            return;
        }

        mTabs = tabs;
        mContentViews = contentViews;
        mTabCallback = callback;

        if(null == visibleList){
            visibleList = new boolean[tabs.length];
            for(int i = 0; i <visibleList.length;i++){
                visibleList[i] = true;
            }
        }

        if(!visibleList[initActiveIndex]){
            initActiveIndex = -1;
            for(int i = 0; i <visibleList.length;i++){
                if(visibleList[i]){
                    initActiveIndex = i;
                    break;
                }
            }
        }

        if(contentViews != null) {
            for(int i = 0; i<contentViews.length; i++){
                contentViews[i].setVisibility(View.GONE);
            }
        }

        for(int i =0; i<tabs.length;i++){
            final View ct = tabs[i];
            boolean visible = visibleList[i];

            if(visible){
                ct.setVisibility(View.VISIBLE);

                TextView view = (TextView)(ct);
                if ( view != null) {
                    view.setText(tabText[i]);
                }

                final int ai = i;
                ct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setTabActive(ai,true);

                    }
                });


            }else{
                ct.setVisibility(View.GONE);
            }

        }

        setTabActive(initActiveIndex,true);
    }

    public void clearTabSelection(View[] tabs){
        for(int i =0; i<tabs.length;i++){
            final View ct = tabs[i];
            updateTabActive(ct,false);
        }
    }

    public void setTabActive(int index, boolean active) {

        if(mTabs == null){
            return;
        }
        if(index < 0){
            return;
        }

        activeTabIndex = index;

        for(int i =0; i<mTabs.length;i++){
            View ct = mTabs[i];

            if(i != index){

                updateTabActive(ct,!active);

                View cv = null;
                if(mContentViews!=null){
                    cv = mContentViews[i];
                    cv.setVisibility(View.GONE);
                }

                if(mTabCallback != null){
                    mTabCallback.onTabActive(ct,cv,i,!active);
                }
            }
        }

        for(int i =0; i<mTabs.length;i++){
            View ct = mTabs[i];

            if(i == index){

                updateTabActive(ct,active);

                View cv = null;
                if(mContentViews!=null){
                    cv = mContentViews[i];
                    cv.setVisibility(View.VISIBLE);
                }

                if(mTabCallback != null){
                    mTabCallback.onTabActive(ct,cv,i,active);
                }
            }
        }

    }


    private void updateTabActive(View tab, boolean active) {

        if ( tab != null ) {

            tab.setSelected(active);
        }

    }


}
