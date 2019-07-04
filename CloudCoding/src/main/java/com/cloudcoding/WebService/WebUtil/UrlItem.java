package com.cloudcoding.WebService.WebUtil;

/**
 * Created by steveyang on 1/7/17.
 */

public class UrlItem {
    public String url;
    public int method;

    public UrlItem(int httpMethod, String webUrl) {
        method = httpMethod;
        url = webUrl;
    }

    @Override
    public String toString() {
        return url;
    }
}
