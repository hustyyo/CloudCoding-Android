package com.cloudcoding.Component.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cloudcoding.CommonLib.Util;
import com.cloudcoding.Component.BaseActivity;
import com.cloudcoding.Component.BaseController;
import com.cloudcoding.Component.BaseFragment;
import com.cloudcoding.Component.SubFragPermission;
import com.cloudcoding.R;
import com.cloudcoding.ViewLib.TabHelper;
import com.cloudcoding.WebService.WebUtil.BaseWebApi;
import com.cloudcoding.WebService.WebUtil.BaseWebRequest;
import com.cloudcoding.WebService.WebUtil.WebResult;

import icepick.Icepick;

/**
 * ==================================
 * Created by michael.carr on 18/12/2014.
 * ==================================
 */
public class BaseDialog extends DialogFragment implements BaseController {

    protected ViewGroup mRootView;
    public IDialogGenericCallback mGenericCallback;
    public boolean isDestroyed = false;
    protected SubFragPermission subFragPermission;
    protected TabHelper tabHelper;
    protected BaseWebApi baseWebApi;

    @Override
    public BaseWebApi getWebApi() {
        return baseWebApi;
    }

    public void showProgress(String title,String message) {
        if(getBaseActivity() != null && !isDestroyed){
            getBaseActivity().showProgress(title,message);
        }
    }

    public void updateProgress(String message){
        if(getBaseActivity() != null && !isDestroyed){
            getBaseActivity().updateProgress(message);
        }
    }
    public void hideProgress(){
        if(getBaseActivity() != null && !isDestroyed){
            getBaseActivity().hideProgress();
        }
    }

    public BaseActivity getBaseActivity(){
        return (BaseActivity)getActivity();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(mGenericCallback!=null){
            mGenericCallback.callBack();
        }
    }

    public void showToast(String msg){
        if(isResumed() && getActivity()!=null){
            Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this,outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseWebApi = BaseWebApi.async();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
        if(BaseActivity.isRecovering(savedInstanceState)){
            dismiss();
            return;
        }
        else {
            initFragmentView(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(BaseActivity.isRecovering(savedInstanceState)){
            return null;
        }
        else {
            return createFragmentView(inflater,container,savedInstanceState);
        }
    }

    public View createFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }


    public void initFragmentView(Bundle savedInstanceState) {
        subFragPermission = new SubFragPermission(getBaseActivity(),this);
        init();
        findViews();
        setupViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(baseWebApi != null){
            baseWebApi.cancelAllRequest();
        }
        if(subFragPermission != null){
            subFragPermission.onDestroy();
        }
        isDestroyed = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void init(){

    }
    protected void findViews(){

    }
    protected void setupViews(){
        setLoadingView();
    }

    // -- Loading layout
    private View mLoadingView;
    public void setLoadingView(){
        if(null == mRootView){
            return;
        }
        setLoadingView(mRootView.findViewById(R.id.progress_layout));
    }
    public void setLoadingView(View view){
        mLoadingView = view;
        if(mLoadingView != null){
            mLoadingView.setVisibility(View.GONE);
        }
    }
    public void showLoadingLayout(){

        if (mLoadingView == null){ return; }

        mLoadingView.setVisibility(View.VISIBLE);

    }
    public void hideLoadingLayout(){
        if (mLoadingView == null){ return; }
        mLoadingView.setVisibility(View.GONE);
    }

    protected int dpToPx(int dp){
        return Util.dpToPx(dp, getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        subFragPermission.onActive();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        subFragPermission.onDestroy();
    }

    protected void enqueueRequest(BaseWebRequest request){
        if(baseWebApi != null){
            baseWebApi.enqueueRequest(request);
        }
    }

    /////////////////Base Controller//////////////
    public SubFragPermission getSubFragPermission(){
        return subFragPermission;
    }

    @Override
    public boolean isComponentInValid() {
        return (null == getActivity()
                || getBaseActivity().isComponentInValid()
                || isDestroyed()
                || isDetached());
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public FragmentManager getCurrentFragmentManager() {
        return getChildFragmentManager();
    }

    public void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onErrorResponse(WebResult webResult) {
        hideProgress();
        onWebError(webResult);
    }

    public void onWebError(WebResult webResult){
        String errorTitle = "";
        String errorText = "";
        errorText = webResult.getErrorString(getContext());
        errorTitle = webResult.getErrorTitle(getContext());

        ErrorDialog errorDialog = ErrorDialog.newInstance(
                errorTitle,
                errorText,
                getContext().getResources().getString(R.string.com_cloudcoding_ok));

        errorDialog.show(getChildFragmentManager(), "errorDialog");
    }

    public void addFragment(Fragment fragment, int container) {
        addFragment(fragment,container,false);
    }
    public void addFragment(Fragment fragment, int container, boolean backStack) {
        BaseFragment.addFragmentToConainer(getCurrentFragmentManager(),fragment,container,backStack);
    }
    public void removeFragment(int container) {
        BaseFragment.removeFragmentFromContainer(getCurrentFragmentManager(),container);
    }

    public void removeSelfFromParent() {

        if(getParentFragment() != null){
            Fragment fragment = getParentFragment();
            FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
            ft.remove(this);
            ft.commitNow();
            getChildFragmentManager().executePendingTransactions();
        }
    }

}
