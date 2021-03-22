package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
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

import java.util.Vector;

import is.hi.hbv601g.hottopicsquiz.model.User;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkCallback;
import is.hi.hbv601g.hottopicsquiz.services.UserService;

public class SignupActivity extends AppCompatActivity {

    private UserService mUserService;

    private User mUser;

    private AppCompatEditText mNameField;
    private AppCompatEditText mUsernameField;
    private AppCompatEditText mPasswordField;
    private AppCompatButton mSignupButton;
    private AppCompatButton mLoginButton;

    public static Intent newIntent(Context c) {
        return new Intent(c, SignupActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUserService = UserService.getInstance();
        mNameField = findViewById(R.id.signup_name);
        mUsernameField = findViewById(R.id.signup_username);
        mPasswordField = findViewById(R.id.signup_password);
        mSignupButton = findViewById(R.id.signup_signup);
        mLoginButton = findViewById(R.id.signup_login);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close activity and switch back to LoginActivity again
                SignupActivity.this.finish();
            }
        });
    }

    private void signupUser() {
        Editable nameEditable = mNameField.getText();
        Editable usernameEditable = mUsernameField.getText();
        Editable passwordEditable = mPasswordField.getText();

        // Check if fields are empty
        Vector<String> emptyFields = new Vector<>();
        if (nameEditable == null) {
            emptyFields.add("name");
        }
        if (usernameEditable == null) {
            emptyFields.add("username");
        }
        if (passwordEditable == null) {
            emptyFields.add("password");
        }
        if (emptyFields.size() > 0) {
            StringBuilder message = new StringBuilder("You must type in " + emptyFields.get(0));
            int i = 1;
            for (; i < emptyFields.size()-1; i++) {
                message.append(", ").append(emptyFields.get(i));
            }
            if (i < emptyFields.size()) message.append(" and ").append(emptyFields.get(i)).append(".");
            else message.append(".");

            Toast toast = Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
            return;
        }

        // This is done solely for the IDE to be confident enough in our coding and stop complaining xd
        assert nameEditable != null;
        assert usernameEditable != null;
        assert passwordEditable != null;

        // Fields are not empty, let's validate the input
        String name = nameEditable.toString();
        String username = usernameEditable.toString();
        String password = passwordEditable.toString();
        // TODO: Implement input validation in correspondence with backend database server and relevant rules
//        if (username.length() > 32) {
//            Toast.makeText(this, "Username can't be more than 32 characters.", Toast.LENGTH_SHORT).show();
//        }
//        ...

        // Input has been validated, let's complete the registration
        mUserService.registerUser(name, username, password, this, new NetworkCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                String res = result.toString();
                Gson gson = new Gson();
                mUser = gson.fromJson(res, User.class);
                // TODO: Implement user passing on with switch to QuizMenuActivity
                Intent intent = QuizMenuActivity.getIntent(SignupActivity.this);
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
                    // to know that username is already taken, could be this error
                    message = "Username is already taken.";
                } else if (error instanceof ParseError) {
                    message = "Error parsing JSON, this one's for the devs!";
                }
                // Display a toast with the error
                Toast toast = Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }
        });
    }
}
