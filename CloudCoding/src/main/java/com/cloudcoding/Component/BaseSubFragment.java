package com.cloudcoding.Component;

import android.view.View;
import android.view.ViewGroup;

import com.cloudcoding.R;
import com.cloudcoding.WebService.WebUtil.BaseWebApi;
import com.cloudcoding.WebService.WebUtil.BaseWebRequest;


/**
 * Created by steveyang on 6/12/16.
 */

public class BaseSubFragment {

    protected ViewGroup mRootView;
    protected BaseWebApi baseWebApi;
    protected BaseController mBaseController;
    protected BaseActivity mBaseActivity;
    protected boolean isHidden = true;
    View mLoadingView;

    protected void enqueueRequest(BaseWebRequest request){
        baseWebApi.enqueueRequest(request);
    }

    protected void setLoadingView(){
        setLoadingView(mRootView.findViewById(R.id.progress_layout));
    }

    protected void setLoadingView(View view){
        mLoadingView = view;
        if(mLoadingView != null){
            mLoadingView.setVisibility(View.GONE);
        }
    }

    protected void showLoadingLayout(){

        if (mLoadingView == null){ return; }

        mLoadingView.setVisibility(View.VISIBLE);

    }

    protected void hideLoadingLayout(){

        if (mLoadingView == null){ return; }

        mLoadingView.setVisibility(View.GONE);

    }

    public  BaseSubFragment(BaseActivity baseActivity){
        mBaseActivity = baseActivity;
        baseWebApi = BaseWebApi.async();
    }
    public BaseSubFragment(BaseController baseController,BaseActivity activity) {

        mBaseActivity = activity;
        mBaseController = baseController;
        baseWebApi = BaseWebApi.async();
    }

    public void onActive(View rootView) {
        mRootView = (ViewGroup) rootView;
        setLoadingView();
        isHidden = false;
    }

    public boolean isComponentInValid() {
        if(mBaseController != null){
            return mBaseController.isComponentInValid();
        }
        else if(mBaseActivity != null){
            return mBaseActivity.isComponentInValid();
        }
        return false;
    }

    public void onActive() {
        isHidden = false;
    }

    public boolean isActive(){
        return !isHidden;
    }

    public void onHidden() {
        isHidden = true;
    }


    public void onDestroy() {

    }
}
