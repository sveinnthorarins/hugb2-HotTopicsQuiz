package is.hi.hbv601g.hottopicsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import is.hi.hbv601g.hottopicsquiz.model.User;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkCallback;
import is.hi.hbv601g.hottopicsquiz.services.UserService;

public class LoginActivity extends AppCompatActivity {

    private UserService mUserService;

    private User mUser;

    private AppCompatEditText mUsernameField;
    private AppCompatEditText mPasswordField;
    private AppCompatButton mLoginButton;
    private AppCompatButton mSignupButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserService = UserService.getInstance();
        mUsernameField = findViewById(R.id.login_username);
        mPasswordField = findViewById(R.id.login_password);
        mLoginButton = findViewById(R.id.login_login);
        mSignupButton = findViewById(R.id.login_signup);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to SignupActivity
                Intent intent = SignupActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        Editable usernameEditable = mUsernameField.getText();
        Editable passwordEditable = mPasswordField.getText();

        // Check if fields are empty
        if (usernameEditable == null && passwordEditable == null) {
            Toast toast = Toast.makeText(this, getString(R.string.must_type_username_and_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (usernameEditable == null) {
            Toast toast = Toast.makeText(this, getString(R.string.must_type_username), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (passwordEditable == null) {
            Toast toast = Toast.makeText(this, getString(R.string.must_type_password), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Fields are not empty, put their contents in strings
        String username = usernameEditable.toString();
        String password = passwordEditable.toString();

        // Validate the login and try to authenticate our user
        mUserService.validateLogin(username, password, this, new NetworkCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                String json = result.toString();
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString());
                    }
                }).create();
                mUser = gson.fromJson(json, User.class);
                Intent intent = QuizMenuActivity.newIntent(LoginActivity.this, mUser);
                startActivity(intent);
            }

            @Override
            public void onFailure(VolleyError error) {
                Utils.standardErrorWithAuthFailureMessage(error, LoginActivity.this, "Invalid username and password combination.");
            }
        });
    }

}
