package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

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

import is.hi.hbv601g.hottopicsquiz.custom.CompletedQuizAdapter;
import is.hi.hbv601g.hottopicsquiz.model.CompletedQuiz;
import is.hi.hbv601g.hottopicsquiz.model.Quiz;
import is.hi.hbv601g.hottopicsquiz.model.User;
import is.hi.hbv601g.hottopicsquiz.networking.NetworkCallback;
import is.hi.hbv601g.hottopicsquiz.services.QuizService;
import is.hi.hbv601g.hottopicsquiz.services.UserService;

public class QuizMenuActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "hottopicsquiz.user";
    private static final String KEY_SAVED = "hottopicsquiz.userissaved";
    private static final String KEY_LASTCOMPQUIZ = "hottopicsquiz.lastcompletedquiz";
    private static final int REQ_CODE_PLAY = 0;

    private QuizService mQuizService;
    private UserService mUserService;

    private User mUser;
    private Quiz mThisWeeksQuiz;
    private boolean mQuizProgressSaved = true;
    private CompletedQuiz mLastCompletedQuiz;

    private AppCompatButton mPlayButton;
    private AppCompatTextView mGreeting;
    private AppCompatTextView mQuizTitle;
    private ListView mListView;
    private CompletedQuizAdapter mListViewAdapter;

    public static Intent newIntent(Context c, User user) {
        Intent i = new Intent(c, QuizMenuActivity.class);
        i.putExtra(EXTRA_USER, user);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmenu);

        mQuizService = QuizService.getInstance();
        mUserService = UserService.getInstance();

        mPlayButton = findViewById(R.id.quizmenu_thisweeksquiz_playbutton);
        mGreeting = findViewById(R.id.quizmenu_greeting);
        mQuizTitle = findViewById(R.id.quizmenu_thisweeksquiz_title);
        mListView = findViewById(R.id.quizmenu_completedquizzes_listview);

        fetchThisWeeksQuiz();

        if (savedInstanceState != null) {
            mUser = savedInstanceState.getParcelable(EXTRA_USER);
            mQuizProgressSaved = savedInstanceState.getBoolean(KEY_SAVED);
            if (!mQuizProgressSaved) {
                mLastCompletedQuiz = savedInstanceState.getParcelable(KEY_LASTCOMPQUIZ);
                mUserService.saveNewCompletedQuiz(mUser, mLastCompletedQuiz, this, new NetworkCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        mQuizProgressSaved = true;
                    }

                    @Override
                    public void onFailure(VolleyError error) {
                        // Tell user we couldn't save quiz progress and to check internet connection.
                        Toast toast = Toast.makeText(QuizMenuActivity.this, R.string.error_save, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        } else {
            mUser = getIntent().getParcelableExtra(EXTRA_USER);
        }

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = QuizPlayActivity.newIntent(QuizMenuActivity.this, mThisWeeksQuiz);
                startActivityForResult(intent, REQ_CODE_PLAY);
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

        mListViewAdapter = new CompletedQuizAdapter(this, R.layout.quizmenu_listview, mUser.getCompleted());
        mListView.setAdapter(mListViewAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_PLAY) {
            if (resultCode == RESULT_OK && data != null) {
                mLastCompletedQuiz = data.getParcelableExtra(QuizPlayActivity.EXTRA_COMPQUIZ);
                mUser.addCompletedQuiz(mLastCompletedQuiz);
                mListViewAdapter.notifyDataSetChanged();
                mQuizProgressSaved = false;
                mUserService = UserService.getInstance();
                mUserService.saveNewCompletedQuiz(mUser, mLastCompletedQuiz, this, new NetworkCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        mQuizProgressSaved = true;
                    }

                    @Override
                    public void onFailure(VolleyError error) {
                        // Tell user we couldn't save quiz progress and to check internet connection.
                        Toast toast = Toast.makeText(QuizMenuActivity.this, R.string.error_save, Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                configurePlayButton();
                Intent i = QuizResultsActivity.newIntent(this, mLastCompletedQuiz);
                startActivity(i);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(EXTRA_USER, mUser);
        outState.putBoolean(KEY_SAVED, mQuizProgressSaved);
        if (!mQuizProgressSaved) outState.putParcelable(KEY_LASTCOMPQUIZ, mLastCompletedQuiz);

        super.onSaveInstanceState(outState);
    }

    private void fetchThisWeeksQuiz() {
        // fetch quiz from backend
        mQuizService.getThisWeeksQuiz(this, new NetworkCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                String json = result.toString();
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString());
                    }
                }).create();
                mThisWeeksQuiz = gson.fromJson(json, Quiz.class);
                mQuizTitle.setText(mThisWeeksQuiz.getName());
                configurePlayButton();
            }

            @Override
            public void onFailure(VolleyError error) {
                Utils.standardError(error, QuizMenuActivity.this);
            }
        });
    }

    private void configurePlayButton() {
        // If this week's quiz is already in user's completed list
        if (mUser.getCompleted().size() > 0 && mUser.getCompleted().get(mUser.getCompleted().size()-1).getQuiz().getName().equals(mThisWeeksQuiz.getName())) {
            mPlayButton.setEnabled(false);
            mPlayButton.setText(R.string.quizmenu_thisweeksquiz_playbutton_error);
            mPlayButton.setBackgroundTintList(getResources().getColorStateList(R.color.holo_red_light, getTheme()));
        } else {
            mPlayButton.setEnabled(true);
            mPlayButton.setText(R.string.quizmenu_thisweeksquiz_playbutton_text);
            mPlayButton.setBackgroundTintList(getResources().getColorStateList(R.color.holo_green_light, getTheme()));
        }
    }

}
