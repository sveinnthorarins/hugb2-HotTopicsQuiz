package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import is.hi.hbv601g.hottopicsquiz.model.CompletedQuiz;
import is.hi.hbv601g.hottopicsquiz.model.Question;

public class QuizResultsActivity extends AppCompatActivity {

    private static final String EXTRA_QUIZRESULTS = "hottopicsquiz.quizresults";

    private CompletedQuiz mQuizResults;

    private TextView mTitle;
    private TextView mScore;
    private LinearLayout mQuestionsContainer;
    private AppCompatButton mReturn;

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

        mTitle = findViewById(R.id.quizresults_title);
        mTitle.setText(mQuizResults.getQuiz().getName());

        mScore = findViewById(R.id.quizresults_score);
        mScore.setText(getString(R.string.quizresults_score, mQuizResults.getScore(), mQuizResults.getQuiz().getQuestions().size()));

        mQuestionsContainer = findViewById(R.id.quizresults_questions_container);
        int idx = 0;
        for (Question q : mQuizResults.getQuiz().getQuestions()) {
            View toAdd = View.inflate(this, R.layout.quizresults_questionresult, null);
            TextView question = toAdd.findViewById(R.id.quizresults_questions_question);
            question.setText(q.getText());
            TextView result = toAdd.findViewById(R.id.quizresults_questions_result);
            if (mQuizResults.getCorrectAnswers()[idx]) {
                result.setText("✓");
                result.setTextColor(getResources().getColor(R.color.holo_green_dark, getTheme()));
            } else {
                result.setText("✗");
                result.setTextColor(getResources().getColor(R.color.holo_red_dark, getTheme()));
            }
            mQuestionsContainer.addView(toAdd);
            View infoToAdd = View.inflate(this, R.layout.quizresults_questioninfourl, null);
            TextView info = infoToAdd.findViewById(R.id.quizresults_questions_infourl);
            String urlElement = "<a href='" + q.getInfoUrl() + "'>Read more here</a>";
            info.setText(Html.fromHtml(urlElement, Html.FROM_HTML_MODE_COMPACT));
            info.setMovementMethod(LinkMovementMethod.getInstance());
            mQuestionsContainer.addView(infoToAdd);
            idx++;
        }

        mReturn = findViewById(R.id.quizresults_return);
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
