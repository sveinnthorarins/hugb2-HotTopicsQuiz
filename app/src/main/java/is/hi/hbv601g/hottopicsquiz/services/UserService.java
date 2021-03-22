package is.hi.hbv601g.hottopicsquiz.services;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.ParseError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

import is.hi.hbv601g.hottopicsquiz.model.User;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkCallback;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkController;

public class UserService {

    private final String URL_LOGIN_POST = "/users/login";
    private final String URL_REGISTER_POST = "/users/register";
    private final String URL_SAVE_POST = "/users/save";

    private static UserService mUserService;

    public static synchronized UserService getInstance() {
        if (mUserService == null) {
            mUserService = new UserService();
        }
        return mUserService;
    }

    public void validateLogin(String username, String password, Context c, NetworkCallback<JSONObject> callback) {
        JSONObject jsonData;
        try {
            jsonData = new JSONObject()
                    .put("username", username)
                    .put("password", password);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        NetworkController net = NetworkController.getInstance(c);
        net.postJson(URL_LOGIN_POST, jsonData, callback);
    }

    public void registerUser(String name, String username, String password, Context c, NetworkCallback<JSONObject> callback) {
        JSONObject jsonData;
        try {
            jsonData = new JSONObject()
                    .put("name", name)
                    .put("username", username)
                    .put("password", password);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        NetworkController net = NetworkController.getInstance(c);
        net.postJson(URL_REGISTER_POST, jsonData, callback);
    }

    public void saveUser(User user, Context c, NetworkCallback<JSONObject> callback) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onFailure(new ParseError());
            return;
        }

        NetworkController net = NetworkController.getInstance(c);
        net.postJson(URL_SAVE_POST, jsonData, callback);
    }
}
