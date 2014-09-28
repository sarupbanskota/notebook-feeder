package com.notebook.libs;

/**
 * Created by Hemanth on 9/28/2014.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncWebServiceCall extends AsyncTask<Void, Void, JSONObject> {

    private AsyncWebServiceCallBackListener callBackListener = null;
    private CookieStore cookieStore;
    private String method;
    private List<NameValuePair> params;
    private String url;

    public final static String METHOD_POST = "POST";
    public final static String METHOD_GET = "GET";

    public void CallAWebService(String url, List<NameValuePair> params,
                                String method, CookieStore cookieStore,
                                AsyncWebServiceCallBackListener callBackListener){
        this.url = url;
        this.callBackListener = callBackListener;
        this.cookieStore = cookieStore;
        this.method = method;
        this.params = params;
        // Execute Async Call
        this.execute();
    }

    @Override
    protected JSONObject doInBackground(Void... arg0) {
        JSONObject jsonObj = null;
        try {
            jsonObj = this.MakeHttpRequest();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObj;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObj) {
        callBackListener.AfterAsyncWebServiceCall(jsonObj,cookieStore);
    }

    public JSONObject MakeHttpRequest() {
        JSONObject jsonObj = null;
        InputStream inputStream = null;
        String json_str = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            // Set Cookies
            if (cookieStore != null) {
                httpClient.setCookieStore(cookieStore);
            }
            if (method == METHOD_POST) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpClient.execute(httpPost);
                Log.d("params",response.getParams().toString());
                inputStream = response.getEntity().getContent();

            } else if (method == METHOD_GET) {
                // Handle GET requests here.
            }

            // Get Cookies after the call
            cookieStore = httpClient.getCookieStore();

        } catch (UnsupportedEncodingException e) {
            Log.d("deb", "UnsupportedEncodingException");
        } catch (ClientProtocolException e) {
            Log.d("deb", "ClientProtocolException");
        } catch (IOException e) {
            Log.d("deb", "IOException");
            jsonObj = new JSONObject();
            try {
                jsonObj.put("network_error", "true");
                return jsonObj;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        try {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();
                json_str = sb.toString();
            }
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            if (json_str != null) {
                jsonObj = new JSONObject(json_str);
                json_str = null;
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jsonObj;

    }

}

