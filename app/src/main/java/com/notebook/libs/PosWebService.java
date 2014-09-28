package com.notebook.libs;

/**
 * Created by Hemanth on 9/28/2014.
 */

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


public class PosWebService implements AsyncWebServiceCallBackListener {

    private static String WEBSERVICE_URL = "";
    private static CookieStore PosCookieStore;
    private PosWebServiceCallBackListener userCallBack;
    private String webServiceMethod,type;

    public void CallAWebService(String webServiceMethod,List<NameValuePair> params,PosWebServiceCallBackListener userCallBack){

        // Set User Call Back
        this.userCallBack = userCallBack;
        this.webServiceMethod = webServiceMethod;

        //TODO : Check the webservice method and correspondinly change the URL and request type.
        type = AsyncWebServiceCall.METHOD_POST;

        // Create Params, if they don't exist
        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }
        
        // Add Webservice Method name
        params.add(new BasicNameValuePair("method", webServiceMethod));

        AsyncWebServiceCall asyncWebService = new AsyncWebServiceCall();
        asyncWebService.CallAWebService(WEBSERVICE_URL, params,type, PosCookieStore, this);

    }
    @Override
    public void AfterAsyncWebServiceCall(JSONObject jsonObj,CookieStore cookieStore) {

        // On Login and register set the cookies for subsequent calls
        if (webServiceMethod == PosWebService.WEBSERVICE_NAME_LOGIN || webServiceMethod == PosWebService.WEBSERVICE_NAME_REGISTER) {
            PosCookieStore = cookieStore;
        }
        // Remove Cookies on LogOut
        else if (webServiceMethod == PosWebService.WEBSERVICE_NAME_LOGOUT) {
            DeleteSavedCookies();
        }

        // Call the user's callback
        if (userCallBack != null) {
            this.userCallBack.AfterAsyncWebServiceCall(webServiceMethod,jsonObj);
        }
    }

    public void DeleteSavedCookies() {
        PosCookieStore = null;
    }


    // List of Web service Methods
    public final static String WEBSERVICE_NAME_LOGIN = "login";
    public final static String WEBSERVICE_NAME_REGISTER = "registration";
    public final static String WEBSERVICE_NAME_LOGOUT = "logout";

}

