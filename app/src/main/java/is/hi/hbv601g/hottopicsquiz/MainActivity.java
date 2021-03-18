package is.hi.hbv601g.hottopicsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static is.hi.hbv601g.hottopicsquiz.R.string.correct_toast;
import static is.hi.hbv601g.hottopicsquiz.R.string.incorrect_toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private Button mButtonTrue;
    private Button mButtonFalse;
    private Button mButtonNext;
    private TextView mTextViewQuestion;
    private Question [] mQuestionBank = new Question[]{
            new Question(R.string.question_ak, false),
            new Question(R.string.question_es, false),
            new Question(R.string.question_rvk, true)
    };
    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }

        updateQuestions();

        mButtonTrue =(Button) findViewById(R.id.True_Button);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }

        });
        mButtonFalse =(Button) findViewById(R.id.False_Button);
        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mButtonNext = (Button) findViewById(R.id.next_button);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) %mQuestionBank.length;
               updateQuestions();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onSaveInstanceState( Bundle SavedInstanceState) {
        super.onSaveInstanceState(SavedInstanceState);
        SavedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }

    //Updates question text to current question
    private void updateQuestions(){
        mTextViewQuestion = (TextView) findViewById(R.id.question_text);
        mTextViewQuestion.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        if(userPressedTrue == answerIsTrue){
            Toast.makeText(this, correct_toast,Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, incorrect_toast,Toast.LENGTH_SHORT).show();
        }
    }
}