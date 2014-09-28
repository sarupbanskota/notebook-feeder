package com.notebook.libs;

/**
 * Created by Hemanth on 9/28/2014.
 */
import org.json.JSONObject;
public interface PosWebServiceCallBackListener {
    void AfterAsyncWebServiceCall(String webServiceMethod, JSONObject jsonObj);
}
