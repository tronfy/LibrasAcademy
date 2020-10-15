package br.unicamp.cotuca.librasacademy;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HttpManager {

    public static void get(Context context, String url, final VolleyCallback callback) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.err.println("Erro de Volley: " + error);
                        }
                    });

            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void post(Context context, String url, HashMap params, final VolleyCallback callback) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String err = response.getString("err");
                                callback.onError(response);
                            } catch (JSONException e) {
                                callback.onSuccess(response);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.err.println("Erro de Volley: " + error);
                        }
                    });

            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
