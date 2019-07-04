package com.cloudcoding.Component;

import android.support.v4.app.FragmentManager;

import com.cloudcoding.WebService.WebUtil.BaseWebApi;

/**
 * Created by steveyang on 30/11/16.
 */

public interface BaseController {

    FragmentManager getCurrentFragmentManager();
    SubFragPermission getSubFragPermission();
    public boolean isComponentInValid();
    public BaseWebApi getWebApi();

//    void showProgress(String title,String message);
//    void updateProgress(String message);
//    void hideProgress();
//    void showToast(String msg);
//    boolean checkCameraPermissionWithRun(Runnable runnable);
//    boolean checkFilePermissionWithRun(Runnable runnable);
//    void requestCameraPermission();
//    void requestFilePermission();


}
