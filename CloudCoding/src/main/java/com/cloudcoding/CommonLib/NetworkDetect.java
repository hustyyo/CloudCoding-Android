package com.cloudcoding.CommonLib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by steveyang on 9/07/2015.
 */
public class NetworkDetect {

    static public boolean isNetworkConnected (Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return ((networkInfo != null) && (networkInfo.isConnected()));
    }

}
