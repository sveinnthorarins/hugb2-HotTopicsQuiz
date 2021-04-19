package is.hi.hbv601g.hottopicsquiz.services;

import android.content.Context;

import org.json.JSONObject;

import is.hi.hbv601g.hottopicsquiz.networking.NetworkCallback;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkController;

public class QuizService {

    private final String URL_QUIZ_GET = "/quiz";

    private static QuizService mQuizService;

    public static synchronized QuizService getInstance() {
        if (mQuizService == null) {
            mQuizService = new QuizService();
        }
        return mQuizService;
    }

    public void getThisWeeksQuiz(Context c, NetworkCallback<JSONObject> callback) {
        NetworkController net = NetworkController.getInstance(c);
        net.getJson(URL_QUIZ_GET, null, callback);
    }

}
