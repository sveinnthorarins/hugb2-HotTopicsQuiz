package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class Utils {

    public static void standardError(VolleyError error, Context context) {
        String message = "Unknown error.";
        if (error instanceof NoConnectionError) {
            message = "No connection to server.";
        } else if (error instanceof TimeoutError) {
            message = "Connection timed out.";
        } else if (error instanceof ParseError) {
            message = "Error parsing JSON, this one's for the devs!";
        }
        // Display a toast with the error
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void standardErrorWithAuthFailureMessage(VolleyError error, Context context, String authFailureMessage) {
        String message = "Unknown error.";
        if (error instanceof NoConnectionError) {
            message = "No connection to server.";
        } else if (error instanceof TimeoutError) {
            message = "Connection timed out.";
        } else if (error instanceof AuthFailureError) {
            message = authFailureMessage;
        } else if (error instanceof ParseError) {
            message = "Error parsing JSON, this one's for the devs!";
        }
        // Display a toast with the error
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

}
