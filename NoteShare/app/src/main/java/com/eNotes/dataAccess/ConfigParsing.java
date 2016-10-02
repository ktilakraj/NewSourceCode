package com.eNotes.dataAccess;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by tilakrajverma on 02/10/16.
 */

public class ConfigParsing {






    private RequestQueue mRequestQueue;

    public void getVolleyResponse(final String url, Context mContext ) {

        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        mRequestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dataAvailable(url, response);

                        // Log.d("Volley Response",""+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.d("Volley Error",""+error.toString());
                //  mOnResponseReceive.onResponseError();
               // mMultiSportSDKEventListener.onSportsDataError(error.toString());

            }
        });

        mRequestQueue.add(stringRequest);
    }

    public void dataAvailable(String url, String dataResponse) {

        if(dataResponse!=null && !dataResponse.isEmpty()) {

            getConfigSettings(dataResponse);
        }
    }

    private void getConfigSettings(String dataResponse) {

        try {
            String dataStr = dataResponse;//volleyJsonResponse(url);
            if(dataStr!=null)
            {
                JSONObject jsonData = new JSONObject(dataStr);


                DataManager.sharedDataManager().setShare_url(jsonData.optString("share_url"));
                DataManager.sharedDataManager().setVersion_no( jsonData.optString("version_no"));
                DataManager.sharedDataManager().setNew_feature( jsonData.optString("new_feature"));

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
