package com.cloudcoding.WebService.WebUtil;

/**
 * Created by steveyang on 5/6/17.
 */

public class BaseWebApi {
    protected boolean isSynchronous = false;
    protected BaseHttpClient httpClient;
    public BaseWebApiCallback baseWebApiCallback;

    public void setBaseWebApiCallback(BaseWebApiCallback callback){
        baseWebApiCallback = callback;
    }

    public static BaseWebApi sync() {

        if(mWebServiceFactory != null){
            return mWebServiceFactory.getSyncWebApi();
        }
        return null;
    }

    public static BaseWebApi async() {
        if(mWebServiceFactory != null){
            return mWebServiceFactory.getAsyncWebApi();
        }

        return null;
    }

    public void cancelAllRequest() {
        if(httpClient != null){

            httpClient.cancelMyRequests();
        }
    }

    public void enqueueRequest(BaseWebRequest webRequest){
        if(httpClient != null){
            webRequest.setBaseWebApiCallback(baseWebApiCallback);
            httpClient.enqueueRequest(webRequest);
        }
    }

    private static BaseWebServiceFactory mWebServiceFactory;
    public static void init(BaseWebServiceFactory webServiceFactory){
        mWebServiceFactory = webServiceFactory;
    }

}
