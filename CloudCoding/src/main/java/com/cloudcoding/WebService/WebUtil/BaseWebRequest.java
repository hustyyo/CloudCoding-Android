/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudcoding.WebService.WebUtil;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cloudcoding.CommonLib.GsonHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * @param <T> JSON type of response expected
 */
abstract public class BaseWebRequest<T > extends Request<T>  {
    static String TAG = BaseWebRequest.class.getSimpleName();

    public BaseHttpClient mHttpClient;
    private Priority mPriority = Priority.NORMAL;
    public Class<T> webClazz;
    public Map<String, String> webHeaders;
    public String msgId;
    public int webMethod;
    public String webUrl;
    public JSONObject jsonRequest;
    protected boolean canceledByUser;
    public WebListener<T> webListener;

    BaseWebApiCallback baseWebApiCallback;

    public void setBaseWebApiCallback(BaseWebApiCallback callback){
        baseWebApiCallback = callback;
    }


    /** Charset for request. */
    static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    public static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", new Object[]{"utf-8"});


    @Override
    public void cancel() {
        super.cancel();
        canceledByUser = true;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public Priority getPriority() {
        return mPriority;
    }


    public void setHttpClient(BaseHttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    @Override
    public Response<T> parseNetworkResponse(NetworkResponse networkResponse) {

        try {
            T result = webClazz.newInstance();
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(networkResponse));
        }
        catch (InstantiationException e){
            Log.e(TAG, "Could not instantiate class T");
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (IllegalAccessException e){
            Log.e(TAG, "Illegal access of class T");
            return Response.error(new ParseError(e));
        }

    }

    /**
     * @deprecated Use {@link #getBodyContentType()}.
     */
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {

        try {
            if(jsonRequest != null){
                String requestBody = jsonRequest.toString();
                return requestBody.getBytes(PROTOCOL_CHARSET);
            }
            return null;

        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes using %s", PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(webHeaders != null){
            return webHeaders;
        }
        else {
            return super.getHeaders();
        }
    }

    protected void setCommonParameters(JSONObject jsonObject) {
    }

    protected void setCommonHeaders() {
    }

    public String getMsgId(){
        return msgId;
    }


    public void onSucceed(T response){
        if(webListener != null){

            onSucceedListener(response);

            if(mHttpClient != null){
                removeRequest();
            }
        }
    }



    public void onConnectFailed(VolleyError volleyError){
        if(webListener != null){
            WebResult webResult = new WebResult();
            webResult.volleyError = volleyError;
            onFailedListener(webResult);
            removeRequest();
        }
    }

    protected void removeRequest() {
        if(mHttpClient != null) {
            mHttpClient.removeRequest(this);
        }
    }

    protected boolean ignoreResponse(T response){
        if(canceledByUser){
            return true;
        }
        return false;
    }

    @Override
    protected void deliverResponse(T response) {

        if (ignoreResponse(response)){
            return;
        }

        onSucceed(response);

    }

    public void deliverResult(int statusCode, String response) {

    }

    public void deliverError(VolleyError error) {
        onConnectFailed(error);
    }

    public BaseWebRequest<T> update() {
        return this;
    }


    public BaseWebRequest(int method, String url, Object parameter, Class<T> clazz, Map<String, String> headers,
                          WebListener<T> listener) {

        super(method, url, null);

        JSONObject jsonObject = null;
        if(null != parameter){
            jsonObject = GsonHelper.fromObject(parameter);
        }

        init(method,url,jsonObject,clazz,headers,listener);
    }

    public BaseWebRequest(int method, String url, JSONObject parameter, Class<T> clazz, Map<String, String> headers,
                          WebListener<T> listener) {

        super(method, url, null);
        init(method,url,parameter,clazz,headers,listener);
    }

    public BaseWebRequest(int method, String url,Class<T> clazz, Map<String, String> headers,
                          WebListener<T> listener) {

        super(method, url, null);
        init(method,url,null,clazz,headers,listener);
    }

    public void init(int method, String url, JSONObject jsonObject, Class<T> clazz, Map<String, String> headers,
                     WebListener<T> listener) {


        setCommonParameters(jsonObject);
        webHeaders = headers;
        msgId = url + System.currentTimeMillis()/1000;
        webMethod = method;
        webUrl = url;
        webListener = listener;
        webClazz = clazz;
        jsonRequest = jsonObject;
    }

    public void onContentFailed(WebResult webResult){

        if(webListener != null){
            onFailedListener(webResult);
            removeRequest();
        }
    }

    public void onRequestFailed(WebResult webResult){

        if(webListener != null){
            onFailedListener(webResult);
            removeRequest();
        }
    }

    void onSucceedListener(T response) {
        if(baseWebApiCallback != null && baseWebApiCallback.isCanceled()){
            return;
        }
        webListener.onSucceed(response);
    }
    void onFailedListener(WebResult webResult){
        if(baseWebApiCallback != null && baseWebApiCallback.isCanceled()){
            return;
        }
        webListener.onFailed(webResult);
    }
}
