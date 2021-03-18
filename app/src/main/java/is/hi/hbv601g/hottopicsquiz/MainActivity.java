package is.hi.hbv601g.hottopicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static is.hi.hbv601g.hottopicsquiz.R.string.correct_toast;
import static is.hi.hbv601g.hottopicsquiz.R.string.incorrect_toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
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
        setContentView(R.layout.activity_main);
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