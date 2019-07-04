package com.cloudcoding.Component;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.cloudcoding.Component.BaseActivity;
import com.cloudcoding.Component.BaseController;
import com.cloudcoding.Component.BaseSubFragment;
import com.cloudcoding.R;

import java.util.HashMap;


/**
 * ==================================
 * Created by michael.carr on 18/12/2014.
 * ==================================
 */
public class SubFragPermission extends BaseSubFragment {

    private final static String PERMISSION_ACTION = "com.cloudcoding.PERMISSION_ACTION";
    private final static String PERMISSION_NAME_KEY = "com.cloudcoding.PERMISSION_NAME_KEY";
    private HashMap<String,Runnable> mapPendingRunnables = new HashMap<>();
    private HashMap<String,Runnable> mapPendingFailedRunnables = new HashMap<>();
    private final static int REQUEST_CODE_PERMISSION = 100;

    public SubFragPermission(BaseActivity baseActivity, BaseController baseController) {
        super(baseController,baseActivity);
    }

    public void onFeatureEnabled(String requestPermission){
        Runnable runnable = mapPendingRunnables.get(requestPermission);
        if (runnable != null) {
            runnable.run();
            mapPendingRunnables.remove(requestPermission);
            mapPendingFailedRunnables.remove(requestPermission);
        }

        if(mBaseController instanceof BaseActivity){
            Intent intent = new Intent(PERMISSION_ACTION);
            intent.putExtra(PERMISSION_NAME_KEY, requestPermission);
            intent.putExtra("enabled", true);
            LocalBroadcastManager.getInstance(mBaseActivity).sendBroadcast(intent);
        }

    }
    public void onFeatureDisabled(String requestPermission){

        Runnable runnable = mapPendingFailedRunnables.get(requestPermission);
        if (runnable != null) {
            runnable.run();
            mapPendingRunnables.remove(requestPermission);
            mapPendingFailedRunnables.remove(requestPermission);
        }
        else {
            mBaseActivity.showToast(mBaseActivity.getString(R.string.com_cloudcoding_no_permission));
        }

        if(mBaseController instanceof BaseActivity){
            Intent intent = new Intent(PERMISSION_ACTION);
            intent.putExtra(PERMISSION_NAME_KEY, requestPermission);
            intent.putExtra("enabled", false);
            LocalBroadcastManager.getInstance(mBaseActivity).sendBroadcast(intent);
        }

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(mBaseController instanceof BaseActivity){
                return;
            }

            if (intent.getAction().equals(PERMISSION_ACTION)) {
                String requestCode = intent.getStringExtra(PERMISSION_NAME_KEY);
                if (intent.hasExtra("enabled") && intent.getBooleanExtra("enabled",false)) {
                    onFeatureEnabled(requestCode);
                } else {
                    onFeatureDisabled(requestCode);
                }
            }

        }
    };

    public void onActive() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PERMISSION_ACTION);
        LocalBroadcastManager.getInstance(mBaseActivity).registerReceiver(broadcastReceiver, intentFilter);
    }

    public void onHidden(){
    }

    public void onDestroy() {
        LocalBroadcastManager.getInstance(mBaseActivity).unregisterReceiver(broadcastReceiver);
    }

    public static boolean isFilePermissionEnabled(Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkFilePermissionWithRun(Runnable runnable) {

        return checkPermissionWithRun(Manifest.permission.WRITE_EXTERNAL_STORAGE,runnable,null);
    }

    public boolean checkCameraPermissionWithRun(Runnable runnable) {

        return checkPermissionWithRun(Manifest.permission.CAMERA,runnable,null);
    }

    public boolean checkLocationPermissionWithRun(Runnable runnable) {

        return checkPermissionWithRun(Manifest.permission.ACCESS_FINE_LOCATION,runnable,null);
    }

    public boolean checkFilePermissionWithRun(Runnable runnable, Runnable notGrantedRunnable) {

        return checkPermissionWithRun(Manifest.permission.WRITE_EXTERNAL_STORAGE,runnable,notGrantedRunnable);
    }

    public boolean checkCameraPermissionWithRun(Runnable runnable, Runnable notGrantedRunnable) {

        return checkPermissionWithRun(Manifest.permission.CAMERA,runnable,notGrantedRunnable);
    }

    public boolean checkLocationPermissionWithRun(Runnable runnable, Runnable notGrantedRunnable) {

        return checkPermissionWithRun(Manifest.permission.ACCESS_FINE_LOCATION,runnable,notGrantedRunnable);
    }


    public boolean checkPermissionWithRun(String permissionName, Runnable runnable, Runnable notGrantedRunnable) {

        if (ContextCompat.checkSelfPermission(mBaseActivity, permissionName) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mBaseActivity.shouldShowRequestPermissionRationale(permissionName)) {
                    if(notGrantedRunnable != null) {notGrantedRunnable.run();}
                }
            }
            mapPendingRunnables.put(permissionName,runnable);
            mapPendingFailedRunnables.put(permissionName,notGrantedRunnable);
            requestFilePermission(permissionName);
            return true;
        } else {
            if (runnable != null) {
                runnable.run();
            }
            return true;
        }
    }

    public void requestFilePermission(String permissonName) {
        if (ContextCompat.checkSelfPermission(mBaseActivity, permissonName) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mBaseActivity, new String[]{permissonName}, REQUEST_CODE_PERMISSION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                if (permissions.length > 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onFeatureEnabled(permissions[0]);
                } else if(permissions.length > 0){
                    onFeatureDisabled(permissions[0]);
                }
            }
        }
    }
}
