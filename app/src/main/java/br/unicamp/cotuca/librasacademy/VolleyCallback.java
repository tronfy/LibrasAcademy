package br.unicamp.cotuca.librasacademy;

import org.json.JSONObject;

public interface VolleyCallback{
    void onSuccess(JSONObject result);
    void onError(JSONObject result);
}