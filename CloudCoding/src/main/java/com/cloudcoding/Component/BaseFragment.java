package com.cloudcoding.Component;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudcoding.CommonLib.Util;
import com.cloudcoding.Component.Dialog.ErrorDialog;
import com.cloudcoding.R;
import com.cloudcoding.ViewLib.TabHelper;
import com.cloudcoding.WebService.WebUtil.BaseWebApi;
import com.cloudcoding.WebService.WebUtil.BaseWebRequest;
import com.cloudcoding.WebService.WebUtil.WebResult;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * ====================================
 * Created by michael.carr on 17/02/14.
 * ====================================
 */
public class BaseFragment extends Fragment implements BaseController {

    private static final Field sChildFragmentManagerField;
    private static final String LOGTAG = BaseFragment.class.getSimpleName();
    protected ViewGroup mRootView;
    public boolean isDestroyed = false;
    protected ViewGroup topBarView;
    public ViewGroup topBarSubView;
    protected TextView topBarTitle;
    protected View menuButton;
    protected BaseWebApi baseWebApi;

    SubFragPermission subFragPermission;

    protected TabHelper tabHelper;

    @Override
    public BaseWebApi getWebApi() {
        return baseWebApi;
    }

    public BaseActivity getBaseActivity(){
        return (BaseActivity)getActivity();
    }

    public void showErrDialog(String s) {
        showDialog(getContext(),getString(R.string.com_cloudcoding_error),s);
    }

    public void showToast(String msg){
        if(isResumed() && getActivity()!=null){
            Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
        }
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


    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(LOGTAG, "Error getting mChildFragmentManager field", e);
        }
        sChildFragmentManagerField = f;
    }

    protected  void initTopBar(){

    }

    protected void initTopBarWithParam(int layoutId, String title) {


        if(topBarSubView != null) {

            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View topbar = inflater.inflate(layoutId,null);

            topBarSubView.removeAllViews();

            topBarSubView.addView(topbar);

            topBarTitle.setText(title);

        }
    }

    protected void initTopBarWithParam(int layoutId, Spannable title) {
        if(topBarSubView != null) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View topbar = inflater.inflate(layoutId,null);
            topBarSubView.removeAllViews();
            topBarSubView.addView(topbar);
            topBarTitle.setText(title);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    void findTopBar() {
        topBarView = (ViewGroup)getActivity().findViewById(R.id.top_bar);
        if(topBarView!=null){
            topBarSubView = (ViewGroup)topBarView.findViewById(R.id.top_bar_sub);
            topBarTitle = (TextView) topBarView.findViewById(R.id.top_bar_title);
            menuButton = topBarView.findViewById(R.id.top_bar_button);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        isDestroyed = false;
        initFragmentView(savedInstanceState);
    }

    public void initFragmentView(Bundle savedInstanceState) {
        subFragPermission = new SubFragPermission(getBaseActivity(),this);

        findTopBar();
        init();
        findViews();
        initTopBar();
        setupViews();
    }

    protected View findViewById(int id){
        return mRootView.findViewById(id);
    }

    protected void init(){

    }
    protected void findViews(){

    }
    protected void setupViews(){
        setLoadingView();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.setAccessible(true);
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
                Log.e(LOGTAG, "Error setting mChildFragmentManager field", e);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseWebApi = BaseWebApi.async();
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
    public void onStop() {
        super.onStop();
        if(subFragPermission != null){
            subFragPermission.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(subFragPermission != null){
            subFragPermission.onActive();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void enqueueRequest(BaseWebRequest request){
        if(baseWebApi != null){
            baseWebApi.enqueueRequest(request);
        }
    }

    public void onErrorResponse(WebResult webResult) {
        onWebError(webResult);
        hideLoadingLayout();
    }

    public void onWebErrorAndHideProgress(String errStr) {
        onWebError(null,getString(R.string.com_cloudcoding_error),errStr);
        hideLoadingLayout();
    }

    protected int dpToPx(int dp){
        return Util.dpToPx(dp, getActivity());
    }

    public void onWebError(WebResult webResult){
        onWebError(webResult,"","");
    }

    public void onWebError(WebResult webResult, String title, String error){

        String errorTitle = "";
        String errorText = "";

        if(error != null && error.length()>0){
            errorText = error;
        }
        else {
            errorText = webResult.getErrorString(getContext());
        }

        if(title!=null && title.length()>0){
            errorTitle = title;
        }
        else {
            errorTitle = webResult.getErrorTitle(getContext());
        }

        ErrorDialog errorDialog = ErrorDialog.newInstance(
                errorTitle,
                errorText,
                getResources().getString(R.string.com_cloudcoding_ok));

        errorDialog.show(getChildFragmentManager(), "errorDialog");
    }

    public  void showDialog(Context context, String title,String msg){


        ErrorDialog errorDialog = ErrorDialog.newInstance(
                title,
                msg,
                context.getResources().getString(R.string.com_cloudcoding_ok));

        errorDialog.show(getChildFragmentManager(), "errorDialog");
    }
    public  void showDialog(Context context, String title,String msg,ErrorDialog.OnErrorCallback callback){


        ErrorDialog errorDialog = ErrorDialog.newInstance(
                title,
                msg,
                context.getResources().getString(R.string.com_cloudcoding_ok));

        errorDialog.setOnErrorCallback(callback);
        errorDialog.show(getChildFragmentManager(), "errorDialog");
    }

    protected ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
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


    //----- Fragment managing
    public void addFragment(Fragment fragment, int container) {
        addFragment(fragment,container,false);
    }
    public void addFragment(Fragment fragment, int container, boolean backStack) {
        BaseFragment.addFragmentToConainer(getCurrentFragmentManager(),fragment,container,backStack);
    }
    public void removeFragment(int container) {
        BaseFragment.removeFragmentFromContainer(getCurrentFragmentManager(),container);
    }

    public static void removeFragmentFromContainer(FragmentManager fragmentManager, int container) {

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(fragmentManager.findFragmentById(container) != null){
            ft.remove(fragmentManager.findFragmentById(container));
        }

        ft.commitNow();
    }

    public static void addFragmentToConainer(FragmentManager fragmentManager,
                                   Fragment fragment, int container, boolean backStack) {
        BaseFragment.removeFragmentFromContainer(fragmentManager,container);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(container,  fragment);
        if(backStack){
            ft.addToBackStack(null);
        }
        ft.commitNow();
    }

    public void removeSelfFromParent() {

        if(getParentFragment() != null){
            Fragment fragment = getParentFragment();
            FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
            ft.remove(this);
            ft.commitNow();
        }
    }



    // -- Loading layout
    private View mLoadingView;
    public void setLoadingView(){
        setLoadingView(mRootView.findViewById(R.id.progress_layout));
    }
    public void setLoadingView(View view){
        mLoadingView = view;
        if(mLoadingView != null){
            mLoadingView.setVisibility(View.GONE);
        }
    }
    public void showLoadingViewNonTransparent(){
        if(mLoadingView != null){
            mLoadingView.setBackgroundColor(getResources().getColor(R.color.com_cloudcoding_background_loading_gray_non_transparent));
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    public void showLoadingLayout(){

        if (mLoadingView == null){ return; }
//        if(mLoadingView != null){
//            mLoadingView.setBackgroundColor(getResources().getColor(R.color.com_cloudcoding_background_gray_transparent));
//        }
        mLoadingView.setVisibility(View.VISIBLE);

    }
    public void hideLoadingLayout(){
        if (mLoadingView == null){ return; }
        mLoadingView.setVisibility(View.GONE);
    }
}

