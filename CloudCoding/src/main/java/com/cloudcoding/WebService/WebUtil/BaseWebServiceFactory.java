package com.cloudcoding.WebService.WebUtil;

/**
 * Created by steveyang on 5/6/17.
 */

public interface BaseWebServiceFactory {
    BaseWebApi getSyncWebApi();
    BaseWebApi getAsyncWebApi();
}
