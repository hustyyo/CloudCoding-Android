package com.cloudcoding.WebService.WebUtil;

import android.content.Context;

import com.android.volley.VolleyError;
import com.cloudcoding.R;

/**
 * Created by steveyang on 28/6/17.
 */

public class WebResult {

    public VolleyError volleyError;
    public String error;

    public static WebResult defaultInstance(Context context) {
        WebResult webResult = new WebResult();
        webResult.error = context.getResources().getString(R.string.com_cloudcoding_error);
        return webResult;
    }

    public int getStatusCode(){
        if(volleyError != null && volleyError.networkResponse != null){
            return volleyError.networkResponse.statusCode;
        }

        return 0;
    }

    public boolean isNetworkError(){
        if(getStatusCode() == 0){
            return true;
        }
        return false;
    }

    public String getErrorTitle(Context context){

        String errorTitle = "";
        if(null == context){
            return "Error";
        }

        if (volleyError != null){

            if (volleyError.networkResponse != null){
                errorTitle = errorTitle +" "+ String.valueOf(volleyError.networkResponse.statusCode);
            }
            volleyError.printStackTrace();
        }

        if(errorTitle.length() == 0){
            errorTitle = context.getResources().getString(R.string.com_cloudcoding_network_error);
        }

        return errorTitle;
    }

    public String getErrorString(Context context) {

        String errorText = "";
        if(null == context){
            return "Error";
        }

        if(error != null && error.length()>0){
            errorText = error;
            return errorText;
        }

        if (volleyError != null){
            if (volleyError.getMessage() != null && volleyError.getMessage().length() > 0) {
                errorText = volleyError.getMessage();
            }
            else if (volleyError.networkResponse != null){
                errorText = "Error " + String.valueOf(volleyError.networkResponse.statusCode);
            }
        }

        if(errorText.length() > 0){
            return errorText;
        }

        errorText = context.getResources().getString(R.string.com_cloudcoding_web_service_failed);;


        return errorText;
    }
}
