package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import is.hi.hbv601g.hottopicsquiz.model.CompletedQuiz;

public class QuizResultsActivity extends AppCompatActivity {

    private static final String EXTRA_QUIZRESULTS = "hottopicsquiz.quizresults";

    private CompletedQuiz mQuizResults;

    public static Intent newIntent(Context c, CompletedQuiz quiz) {
        Intent i = new Intent(c, QuizResultsActivity.class);
        i.putExtra(EXTRA_QUIZRESULTS, quiz);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizresults);

        mQuizResults = getIntent().getParcelableExtra(EXTRA_QUIZRESULTS);

        // TODO: Implement activity_quizresults.xml view and corresponding functionality
    }
}
