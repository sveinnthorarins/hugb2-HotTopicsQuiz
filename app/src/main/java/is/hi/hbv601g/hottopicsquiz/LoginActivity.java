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

import org.json.JSONObject;

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
            Toast toast = Toast.makeText(this, "You must type in username and password.", Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
            return;
        }
        if (usernameEditable == null) {
            Toast toast = Toast.makeText(this, "You must type in a username.", Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
            return;
        }
        if (passwordEditable == null) {
            Toast toast = Toast.makeText(this, "You must type in a password.", Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
            return;
        }

        // Fields are not empty, let's validate the input
        String username = usernameEditable.toString();
        String password = passwordEditable.toString();
        // TODO: Implement input validation in correspondence with backend database server and relevant rules
//        if (username.length() > 32) {
//            Toast.makeText(this, "Username can't be more than 32 characters.", Toast.LENGTH_SHORT).show();
//        }
//        ...

        // Input has been validated, let's validate the login
        // and try to authenticate our user
        mUserService.validateLogin(username, password, this, new NetworkCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                String json = result.toString();
                Gson gson = new Gson();
                mUser = gson.fromJson(json, User.class);
                // TODO: Implement user passing on with switch to QuizMenuActivity
                Intent intent = QuizMenuActivity.getIntent(LoginActivity.this);
                startActivity(intent);
            }

            @Override
            public void onFailure(VolleyError error) {
                String message = "";
                if (error instanceof NoConnectionError) {
                    message = "No connection to server.";
                } else if (error instanceof TimeoutError) {
                    message = "Connection timed out.";
                } else if (error instanceof AuthFailureError) {
                    // TODO: Need to find out which error corresponds to HTTP code 401
                    // to know that username and password is invalid, could be this error
                    message = "Invalid username and password combination.";
                } else if (error instanceof ParseError) {
                    message = "Error parsing JSON, this one's for the devs!";
                }
                // Display a toast with the error
                Toast toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }
        });
    }

}
