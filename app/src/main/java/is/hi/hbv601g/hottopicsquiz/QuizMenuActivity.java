package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import is.hi.hbv601g.hottopicsquiz.custom.CompletedQuizAdapter;
import is.hi.hbv601g.hottopicsquiz.model.CompletedQuiz;
import is.hi.hbv601g.hottopicsquiz.model.Quiz;
import is.hi.hbv601g.hottopicsquiz.model.User;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkCallback;
import is.hi.hbv601g.hottopicsquiz.services.QuizResultsActivity;
import is.hi.hbv601g.hottopicsquiz.services.QuizService;

public class QuizMenuActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "hottopicsquiz.user";

    private QuizService mQuizService;

    private User mUser;
    private Quiz mThisWeeksQuiz;

    private AppCompatButton mPlayButton;
    private AppCompatTextView mGreeting;
    private AppCompatTextView mQuizTitle;
    private ListView mListView;

    public static Intent newIntent(Context c, User user) {
        Intent i = new Intent(c, QuizMenuActivity.class);
        i.putExtra(EXTRA_USER, (Parcelable) user);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmenu);

        fetchThisWeeksQuiz();

        mUser = getIntent().getParcelableExtra(EXTRA_USER);

        mPlayButton = findViewById(R.id.quizmenu_thisweeksquiz_playbutton);
        mGreeting = findViewById(R.id.quizmenu_greeting);
        mQuizTitle = findViewById(R.id.quizmenu_thisweeksquiz_title);
        mListView = findViewById(R.id.quizmenu_completedquizzes_listview);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = QuizPlayActivity.newIntent(QuizMenuActivity.this, mThisWeeksQuiz);
                startActivity(intent);
            }
        });

        String name = mUser.getName();
        if (name.contains(" ")) {
            String[] arr = name.split(" ");
            name = arr[0];
        }
        mGreeting.setText(getString(R.string.quizmenu_greeting, name));

        // need to wait for this week's quiz to be fetched
        if (mThisWeeksQuiz == null) {
            mPlayButton.setEnabled(false);
            mQuizTitle.setText(R.string.quizmenu_thisweeksquiz_loading);
        }

        CompletedQuizAdapter adapter = new CompletedQuizAdapter(this, R.layout.quizmenu_listview, mUser.getCompleted());
        mListView.setAdapter(adapter);
        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompletedQuiz quiz = (CompletedQuiz) mListView.getItemAtPosition(position);
                Intent i = QuizResultsActivity.newIntent(QuizMenuActivity.this, quiz);
                startActivity(i);
            }
        });
    }

    private void fetchThisWeeksQuiz() {
        // fetch quiz from backend
        mQuizService = QuizService.getInstance();
        mQuizService.getThisWeeksQuiz(this, new NetworkCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                String json = result.toString();
                Gson gson = new Gson();
                mThisWeeksQuiz = gson.fromJson(json, Quiz.class);
                mPlayButton.setEnabled(true);
                mQuizTitle.setText(mThisWeeksQuiz.getName());
            }

            @Override
            public void onFailure(VolleyError error) {
                Utils.standardError(error, QuizMenuActivity.this);
            }
        });
    }

}
