package com.cloudcoding.Component;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudcoding.CommonLib.NetworkDetect;
import com.cloudcoding.Component.Dialog.CustomProgressDialog;
import com.cloudcoding.Component.Dialog.ErrorDialog;
import com.cloudcoding.R;
import com.cloudcoding.ViewLib.TabHelper;
import com.cloudcoding.WebService.WebUtil.BaseWebApi;
import com.cloudcoding.WebService.WebUtil.BaseWebRequest;
import com.cloudcoding.WebService.WebUtil.WebResult;

public class BaseActivity extends AppCompatActivity implements BaseController,
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    final static String TAG = BaseActivity.class.getSimpleName();

    protected boolean isActivityDestroyed = false;

    //Offline mode
    protected GestureDetector mGestureDetector = null;
    protected boolean isStarted = false;
    protected boolean isResumed = false;

    protected TabHelper tabHelper;
    protected SubFragPermission subFragPermission;

    protected CustomProgressDialog mCustomProgressDialog;
    protected ViewGroup topBarView;
    public ViewGroup topBarSubView;
    protected TextView topBarTitle;
    protected View menuButton;
    private View mLoadingView;
    protected BaseWebApi baseWebApi;

    static AppRecoverCallback appRecoverCallback;

    public boolean isActivityDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isDestroyed();
        }
        else {
            return isActivityDestroyed;
        }

    }

    @Override
    public BaseWebApi getWebApi() {
        return baseWebApi;
    }

    public BaseWebApi getBaseWebApi() {
        return baseWebApi;
    }

    public static class AppRecoverCallback {
        public boolean isAppRecovering() {
            return false;
        }
    }

    public static void setAppRecoverCallback(AppRecoverCallback callback) {
        appRecoverCallback = callback;
    }

    public static boolean isRecovering(Bundle savedInstanceState) {
        boolean recover = (savedInstanceState != null);

        if(appRecoverCallback != null){
            recover = (recover && appRecoverCallback.isAppRecovering());
        }
        return recover;
    }

    public void enqueueRequest(BaseWebRequest request){
        baseWebApi.enqueueRequest(request);
    }

    public void setFullscreenMode(){
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public int getScreenWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        subFragPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    public void hideProgressWithDelay(String message,Runnable runnable){

        if(message!=null && message.length()>0){
            updateProgress(message);
        }

        if(runnable != null){
            runLater(runnable,300);
        }else{
            hideProgress();

        }
    }

    void findTopBar() {
        topBarView = findViewById(R.id.top_bar);
        if(topBarView!=null){
            topBarSubView = (ViewGroup)topBarView.findViewById(R.id.top_bar_sub);
            topBarTitle = (TextView) topBarView.findViewById(R.id.top_bar_title);
            menuButton = topBarView.findViewById(R.id.top_bar_button);

            if(menuButton != null) {
                menuButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLeftMenuClicked();
                    }
                });
            }
        }
    }

    public void onLeftMenuClicked(){
        onBackButton();
    }

    public void onBackButton() {
        onBackPressed();
    }

    public void onWebErrorAndHideProgress(WebResult webResult){
        hideProgress();
        onWebError(webResult,null,null);
    }

    public void onWebErrorAndHideLoadingLayout(WebResult webResult,String title, String error){
        hideLoadingLayout();
        onWebError(webResult,title,error);
    }

    public void onWebErrorAndHideProgress(WebResult webResult,String title, String error){
        hideProgress();
        onWebError(webResult,title,error);
    }
    public void onWebError(WebResult webResult){
        onWebError(webResult,null,null);
    }

    public void onErrorResponse(WebResult webResult) {
        hideProgress();
        hideLoadingLayout();
        onWebError(webResult);
    }

    public void onWebError(WebResult webResult, String title, String error){


        String errorTitle = "";
        String errorText = "";

        if(error != null && error.length()>0){
            errorText = error;
        }
        else {
            errorText = webResult.getErrorString(this);
        }

        if(title!=null && title.length()>0){
            errorTitle = title;
        }
        else {
            errorTitle = webResult.getErrorTitle(this);
        }

        ErrorDialog errorDialog = ErrorDialog.newInstance(
                errorTitle,
                errorText,
                getResources().getString(R.string.com_cloudcoding_ok));

        errorDialog.show(getSupportFragmentManager(), "errorDialog");
    }

    public void showDialog(String title, String msg, String tag){

        if(getCurrentFragmentManager().findFragmentByTag(tag) != null){
            return;
        }

        ErrorDialog errorDialog = ErrorDialog.newInstance(
                title,
                msg,
                getResources().getString(R.string.com_cloudcoding_ok));

        errorDialog.show(getSupportFragmentManager(), tag);
    }

    public void showDialog(String title, String msg){

        ErrorDialog errorDialog = ErrorDialog.newInstance(
                title,
                msg,
                getResources().getString(R.string.com_cloudcoding_ok));

        errorDialog.show(getSupportFragmentManager(), "errorDialog");
    }

    protected void initKeyboard() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    protected void showKeyboard(final View view){
        runLater(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        },200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void finishWithGoodResult(){
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onRecover(Bundle savedInstanceState){
        //// TODO: 8/7/17 handle activity recover
    }

    protected View findViewInTopBar(int viewId) {
        if(topBarSubView != null){
            return topBarSubView.findViewById(viewId);
        }
        return null;
    }

    public void initTopBar(){
        findTopBar();
    }

    protected void initTopBarWithParam(int layoutId, String title) {

        findTopBar();

        if(topBarSubView != null) {
            if(layoutId > 0){
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View topbar = inflater.inflate(layoutId,null);
                topBarSubView.removeAllViews();
                topBarSubView.addView(topbar);
            }

            topBarTitle.setText(title);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivityDestroyed = false;
        baseWebApi = BaseWebApi.async();
        subFragPermission = new SubFragPermission(this,this);
        isStarted = false;
        initKeyboard();
        setBestOrientation();
        checkNetworkStatus();
    }

    protected void initActivity(Bundle savedInstanceState) {

    }

    public void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mGestureDetector != null){
            mGestureDetector.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;

    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    public static SpannableString createProgressDialogMessage(String msg){
        SpannableString ss = new SpannableString(msg);
        ss.setSpan(new RelativeSizeSpan(1.28f), 0, ss.length(), 0);
        return ss;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStarted = true;

    }

    @Override
    protected void onResume(){
        super.onResume();
        isResumed = true;
        isStarted = true;
        subFragPermission.onActive();
        registerNetworkStatus();

        updateOfflineView();
    }

    @Override
    protected void onPause(){
        super.onPause();
        isResumed = false;
        if(baseWebApi != null){
            baseWebApi.cancelAllRequest();
        }
        unregisterNetworkStatus();
    }



    @Override
    protected void onStop(){
        super.onStop();
        isStarted = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(baseWebApi != null){
            baseWebApi.cancelAllRequest();
        }
        isActivityDestroyed = true;
        subFragPermission.onDestroy();
    }

    public void backToHome(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void runLater(final Runnable runnable, long delayMillis){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isStarted) {
                    try {
                        runnable.run();
                    } catch (Exception e) {

                    }
                }
            }
        }, delayMillis);
    }

    public void runLaterNoCheck(final Runnable runnable, long delayMillis){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {

                }
            }
        }, delayMillis);
    }

    public void setBestOrientation() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        double density = dm.density * 160;
//        double x = Math.pow(dm.widthPixels / density, 2);
//        double y = Math.pow(dm.heightPixels / density, 2);
//        double screenInches = Math.sqrt(x + y);
//
//        if(screenInches >=7 ){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    protected void showImageLoading(ImageView view){
        if(null == view){
            return;
        }
        view.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)view.getDrawable();
        animationDrawable.start();
    }

    protected void hideImageLoading(ImageView view){
        if(view != null){
            view.setVisibility(View.GONE);
        }

    }


    /////////////////Base Controller//////////////
    public SubFragPermission getSubFragPermission(){
        return subFragPermission;
    }

    @Override
    public boolean isComponentInValid() {
        return isActivityDestroyed();
    }

    @Override
    public FragmentManager getCurrentFragmentManager() {
        return getSupportFragmentManager();
    }

    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }

    protected void showProgressDialog(){
        if(null == mCustomProgressDialog){
            return;
        }
        mCustomProgressDialog.setCancelable(false);
        mCustomProgressDialog.show(getSupportFragmentManager(),"customProgressDialog");
    }

    public void showProgress(String title,String message) {

        hideProgress();

        mCustomProgressDialog = CustomProgressDialog.newInstance(title, message);
        showProgressDialog();
    }

    public void updateProgress(String message){
        if(mCustomProgressDialog != null){
            mCustomProgressDialog.updateText(message);
        }
    }

    public void hideProgress(){
        if(mCustomProgressDialog != null){
            mCustomProgressDialog.dismiss();
        }
    }


    //----- Fragment managing

    public void addFragment(Fragment fragment, int container) {
        if(isActivityDestroyed()){
            return;
        }
        addFragment(fragment,container,false);
    }
    public void addFragment(Fragment fragment, int container, boolean backStack) {
        if(isActivityDestroyed()){
            return;
        }
        BaseFragment.addFragmentToConainer(getCurrentFragmentManager(),fragment,container,backStack);
    }
    public void removeFragment(int container) {
        BaseFragment.removeFragmentFromContainer(getCurrentFragmentManager(),container);
    }


    // -- Loading layout
    public void setLoadingView(){
        setLoadingView(findViewById(R.id.progress_layout));
    }

    public void setLoadingView(View view){
        if(view != null){
            mLoadingView = view;
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


    //Monitor Network Status
    protected void unregisterNetworkStatus() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStateReceiver);
    }
    BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onNetworkChanged();
        }
    };
    protected void checkNetworkStatus() {
        if(!NetworkDetect.isNetworkConnected(this)){
            onNetworkDisconnected();
        }else {
            onNetworkConnected();
        }
    }

    protected void updateOfflineView() {
        if(!NetworkDetect.isNetworkConnected(this)){
            showOfflineView();
        }else {
            hideOfflineView();
        }
    }

    protected void registerNetworkStatus() {


        IntentFilter filter = new IntentFilter();
        filter.addAction("network_connected");
        filter.addAction("network_disconnected");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkStateReceiver, filter);
    }
    public void onNetworkDisconnected() {

        showOfflineView();

        showDialog("Internet Connection Lost",
                "Your device has lost internet connectivity. " +
                        "You can still use the app, but with limited functionality.");
    }


    public void onNetworkConnected() {
        hideOfflineView();
    }

    @Override
    public void setContentView(int layoutId) {

        setRootView(layoutId);

        //setOfflineView();
    }
    protected void setRootView(int layoutId) {
        //super.setContentView(layoutId);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Root frame layout
        FrameLayout screenRootView = new FrameLayout(this);
        screenRootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));


        //Linear layout view
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        //Offline view
        View topView = inflater.inflate(R.layout.offline_view,null);

        //Content view
        View screenView = inflater.inflate(layoutId, null);
        screenView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1));
        linearLayout.addView(topView);
        linearLayout.addView(screenView);
        screenRootView.addView(linearLayout);

        addCustomLayout(screenRootView);

        //Add loading layout
        View loadingLayout = inflater.inflate(R.layout.progress_layout,null);
        screenRootView.addView(loadingLayout);
        mLoadingView = loadingLayout;
        super.setContentView(screenRootView);

        setOfflineView();

        hideLoadingLayout();
    }

    protected void addCustomLayout(ViewGroup rootFrameLayout){

    }

    protected void showOfflineView() {
        if(offline_header != null){
            offline_header.setVisibility(View.VISIBLE);
        }
    }

    protected void hideOfflineView() {
        if(offline_header != null){
            offline_header.setVisibility(View.GONE);
        }
    }

    protected View offline_header;
    protected void setOfflineView() {
        offline_header = findViewById(R.id.offline_header);
    }

    protected void onNetworkChanged() {

        if(!NetworkDetect.isNetworkConnected(this)){
            onNetworkDisconnected();
        }else {
            onNetworkConnected();
        }
    }
}
