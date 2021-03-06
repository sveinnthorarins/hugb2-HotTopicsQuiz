package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import is.hi.hbv601g.hottopicsquiz.model.CompletedQuiz;
import is.hi.hbv601g.hottopicsquiz.model.Question;
import is.hi.hbv601g.hottopicsquiz.model.Quiz;

public class QuizPlayActivity extends AppCompatActivity {

    private static final String EXTRA_QUIZ = "hottopicsquiz.thisweeksquiz";
    public static final String EXTRA_COMPQUIZ = "hottopicsquiz.completedquiz";
    private static final String KEY_CURRENTQUESTIONINDEX = "hottopicsquiz.currentquestionindex";
    private static final String KEY_CORRECTLYANSWERED = "hottopicsquiz.correctlyanswered";
    private static final String KEY_SCORE = "hottopicsquiz.score";

    private Quiz mQuiz;
    private Question mCurrentQuestion;
    private int mCurrentQuestionIndex = 0;
    private boolean[] mCorrectlyAnswered;
    private int mScore = 0;
    
    private AppCompatTextView mQuestionText;
    private RadioGroup mAnswerGroup;
    private RadioButton mAnswerOption1;
    private RadioButton mAnswerOption2;
    private RadioButton mAnswerOption3;
    private AppCompatButton mNext;
    
    private final int[] mAnswerOptionIds = {
            R.id.quizplay_answer1,
            R.id.quizplay_answer2,
            R.id.quizplay_answer3
    };

    public static Intent newIntent(Context c, Quiz quiz) {
        Intent i = new Intent(c, QuizPlayActivity.class);
        i.putExtra(EXTRA_QUIZ, quiz);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizplay);

        if (savedInstanceState != null) {
            mQuiz = savedInstanceState.getParcelable(EXTRA_QUIZ);
            mCurrentQuestionIndex = savedInstanceState.getInt(KEY_CURRENTQUESTIONINDEX);
            mScore = savedInstanceState.getInt(KEY_SCORE);
            mCorrectlyAnswered = savedInstanceState.getBooleanArray(KEY_CORRECTLYANSWERED);
        } else {
            mQuiz = getIntent().getParcelableExtra(EXTRA_QUIZ);
            mCorrectlyAnswered = new boolean[mQuiz.getQuestions().size()];
        }

        mQuestionText = findViewById(R.id.quizplay_question);
        mAnswerGroup = findViewById(R.id.quizplay_answergroup);
        mAnswerOption1 = findViewById(R.id.quizplay_answer1);
        mAnswerOption2 = findViewById(R.id.quizplay_answer2);
        mAnswerOption3 = findViewById(R.id.quizplay_answer3);
        mNext = findViewById(R.id.quizplay_next);
        
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = mAnswerGroup.getCheckedRadioButtonId();

                if (checkedId == -1) {
                    Toast toast = Toast.makeText(QuizPlayActivity.this, R.string.quizplay_mustselect_error, Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                mAnswerGroup.clearCheck();

                boolean correct = false;
                for (int i = 0; i < mAnswerOptionIds.length; i++) {
                    if (mAnswerOptionIds[i] == checkedId) {
                        if (mCurrentQuestion.getCorrectAnswers()[i]) correct = true;
                        break;
                    }
                }

                Toast toast = Toast.makeText(QuizPlayActivity.this, correct ? R.string.quizplay_correct : R.string.quizplay_incorrect, Toast.LENGTH_SHORT);
                toast.show();

                mCorrectlyAnswered[mCurrentQuestionIndex] = correct;
                if (correct) mScore++;

                mCurrentQuestionIndex++;
                prepareQuestion();
            }
        });

        prepareQuestion();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(EXTRA_QUIZ, mQuiz);
        outState.putInt(KEY_CURRENTQUESTIONINDEX, mCurrentQuestionIndex);
        outState.putInt(KEY_SCORE, mScore);
        outState.putBooleanArray(KEY_CORRECTLYANSWERED, mCorrectlyAnswered);

        super.onSaveInstanceState(outState);
    }

    private void prepareQuestion() {
        if (mCurrentQuestionIndex == mQuiz.getQuestions().size()) {
            endQuiz();
            return;
        }
        mCurrentQuestion = mQuiz.getQuestions().get(mCurrentQuestionIndex);
        mQuestionText.setText(mCurrentQuestion.getText());
        mAnswerOption1.setText(mCurrentQuestion.getAnswers().get(0));
        mAnswerOption2.setText(mCurrentQuestion.getAnswers().get(1));
        mAnswerOption3.setText(mCurrentQuestion.getAnswers().get(2));
    }

    private void endQuiz() {
        CompletedQuiz compQuiz = new CompletedQuiz();
        compQuiz.setQuiz(mQuiz);
        compQuiz.setCorrectAnswers(mCorrectlyAnswered);
        compQuiz.setScore(mScore);

        // Set result data and finish, putting us back to QuizMenuActivity
        Intent intent = new Intent();
        intent.putExtra(EXTRA_COMPQUIZ, compQuiz);
        setResult(RESULT_OK, intent);
        finish();
    }

}
