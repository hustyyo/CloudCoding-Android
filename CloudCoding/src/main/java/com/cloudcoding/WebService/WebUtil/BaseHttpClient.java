package com.cloudcoding.WebService.WebUtil;

/**
 * ====================================
 * Created by michael.carr on 23/10/13.
 * ====================================
 */

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cloudcoding.CommonLib.NetworkDetect;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Map;

/**
 * Singleton class, in charge of logging in to the webservice multiple times throughout
 * the app. Callback to original calling activity after login completed, whether successful
 * or unsuccessful
 */
abstract public class BaseHttpClient {

    public static final String TAG = BaseHttpClient.class.getSimpleName();
    public static final int REQUEST_TIMEOUT_MS = 60000; //60 seconds
    private static RequestQueue mRequestQueue;
    private ArrayDeque<Request> mWebRequestQueue;
    public boolean isSynchronous = false;
    Context mContext;

    public BaseHttpClient(Context context){
        mWebRequestQueue = new ArrayDeque<>();
        initRequestQueue(context);
        mContext = context;
    }

    public void initRequestQueue(Context pContext){

        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(pContext);
        }
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue != null) {

            return mRequestQueue;

        } else {

            throw new IllegalStateException("RequestQueue not initialized");

        }
    }


    public void addRequest(BaseWebRequest request) {

        request.setTag(TAG);

        request.setHttpClient(this);

        getRequestQueue().add(request);
    }

    public void sendRequest(BaseWebRequest request){

        if(!NetworkDetect.isNetworkConnected(mContext)){
            request.onConnectFailed(null);
        }
        else {
            request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            getRequestQueue().getCache().clear();
            request.setTag(request.getMsgId());
            mWebRequestQueue.push(request);
            addRequest(request);
        }
    }

    public void removeRequest(BaseWebRequest request){

        mWebRequestQueue.remove(request);
    }

    public void cancelMyRequests(){

        for (Request request : mWebRequestQueue){
            request.cancel();
        }

        mWebRequestQueue.clear();
    }

    public void clearAllRequest() {
        if( mRequestQueue != null){
            mRequestQueue.cancelAll(TAG);
        }
    }

    public boolean hasPendingRequest(String pRequestID){

        for (Request request : mWebRequestQueue) {

            if(request instanceof BaseWebRequest) {
                BaseWebRequest prsWebRequest = (BaseWebRequest)request;

                if (prsWebRequest.getMsgId().equalsIgnoreCase(pRequestID)) {

                    return true;
                }
            }
        }

        return false;
    }

    protected void setCommonData(JSONObject jsonObject){

    }

    public void retryOnAuthFail(final BaseWebRequest msg){

    }


    public void enqueueRequest(BaseWebRequest webRequest){
        if(isSynchronous){
            syncSendRequest(webRequest);
        }
        else {
            sendRequest(webRequest);
        }
    }
    protected void syncSendRequest(final BaseWebRequest gsonRequest) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        int serverResponseCode = 0;
        String response = null;

        try
        {
            URL url = new URL(gsonRequest.webUrl);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("accept","application/json");
            connection.setRequestProperty("Content-Type",gsonRequest.getBodyContentType());
            try {
                Map<String, String> headers = gsonRequest.getHeaders();
                if(headers != null){
                    for(Map.Entry<String,String> entry : headers.entrySet()){
                        connection.setRequestProperty(entry.getKey(),entry.getValue());
                    }
                }
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
            if(gsonRequest.webMethod == Request.Method.GET){
                connection.setRequestMethod("GET");
                connection.connect();
            }
            else if(gsonRequest.webMethod == Request.Method.POST){
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                outputStream = new DataOutputStream( connection.getOutputStream() );
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayOutputStream.write(gsonRequest.getBody());
                byteArrayOutputStream.writeTo(outputStream);
                outputStream.flush();
                outputStream.close();
            }
            serverResponseCode = connection.getResponseCode();
            InputStream inputStream = connection.getInputStream();
            if (inputStream != null) {
                // Converts Stream to String with max length of 500.
                response = readStream(inputStream, 1024*1024);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        gsonRequest.deliverResult(serverResponseCode,response);
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    protected static String readStream(InputStream stream, int maxLength) throws IOException {
        String result = null;
        // Read InputStream using the UTF-8 charset.
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        // Create temporary buffer to hold Stream data with specified max length.
        char[] buffer = new char[maxLength];
        // Populate temporary buffer with Stream data.
        int numChars = 0;
        int readSize = 0;
        while (numChars < maxLength && readSize != -1) {
            numChars += readSize;
            readSize = reader.read(buffer, numChars, buffer.length - numChars);
        }
        if (numChars != -1) {
            // The stream was not empty.
            // Create String that is actual length of response body if actual length was less than
            // max length.
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }
        return result;
    }
}