package is.hi.hbv601g.hottopicsquiz.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NetworkController {

    private final String BASE_URL = "http://10.0.2.2:8080"; // This url is emulator's parent device (i.e. our PC)
    private static NetworkController networkController;
    private final RequestQueue mQueue;

    public static NetworkController getInstance(Context c) {
        if (networkController == null) {
            networkController = new NetworkController(c);
        }
        return networkController;
    }

    NetworkController(Context c) {
        mQueue = Volley.newRequestQueue(c);
    }

    public void getJson(String url, JSONObject obj, NetworkCallback<JSONObject> callback) {

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + url,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure(error);
                    }
                }
        );
        mQueue.add(req);
    }

    public void postJson(String url, JSONObject obj, NetworkCallback<JSONObject> callback) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + url,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure(error);
                    }
                }
        );
        mQueue.add(req);
    }
}
