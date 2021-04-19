package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.hottopicsquiz.model.Quiz;

public class QuizPlayActivity extends AppCompatActivity {

    private static final String EXTRA_QUIZ = "hottopicsquiz.thisweeksquiz";

    private Quiz mQuiz;

    public static Intent newIntent(Context c, Quiz quiz) {
        Intent i = new Intent(c, QuizPlayActivity.class);
        i.putExtra(EXTRA_QUIZ, (Parcelable) quiz);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizplay);

        mQuiz = getIntent().getParcelableExtra(EXTRA_QUIZ);

        // TODO: Implement activity_quizmenu.xml view and corresponding functionality

    }
}
