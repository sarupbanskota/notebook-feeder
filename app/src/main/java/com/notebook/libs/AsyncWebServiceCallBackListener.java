package com.notebook.libs;

/**
 * Created by Hemanth on 9/28/2014.
 */
import org.apache.http.client.CookieStore;
import org.json.JSONObject;

public interface AsyncWebServiceCallBackListener {
    void AfterAsyncWebServiceCall(JSONObject jsonObj, CookieStore cookieStore);
}
