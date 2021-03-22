package is.hi.hbv601g.hottopicsquiz.networking;

import com.android.volley.VolleyError;

public interface NetworkCallback<T> {

    void onSuccess(T result);

    void onFailure(VolleyError error);

}
