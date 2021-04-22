package is.hi.hbv601g.hottopicsquiz.services;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.ParseError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import is.hi.hbv601g.hottopicsquiz.model.CompletedQuiz;
import is.hi.hbv601g.hottopicsquiz.model.User;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkCallback;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkController;

public class UserService {

    private final String URL_LOGIN_POST = "/users/login";
    private final String URL_REGISTER_POST = "/users/register";
    private final String URL_SAVE_POST = "/users/newcompletedquiz";

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

    public void saveNewCompletedQuiz(User user, CompletedQuiz quiz, Context c, NetworkCallback<JSONObject> callback) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        }).create();
        String jsonString = gson.toJson(quiz);
        JSONObject jsonData, jsonQuiz;
        try {
            jsonQuiz = new JSONObject(jsonString);
            jsonData = new JSONObject()
                    .put("userid", user.getId())
                    .put("compquiz", jsonQuiz);
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onFailure(new ParseError());
            return;
        }

        NetworkController net = NetworkController.getInstance(c);
        net.postJson(URL_SAVE_POST, jsonData, callback);
    }
}
