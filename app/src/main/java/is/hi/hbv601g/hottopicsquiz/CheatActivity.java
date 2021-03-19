package is.hi.hbv601g.hottopicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";
    private static final String EXTRA_ANSWER_IS_TRUE = "s.hi.hbv601g.hottopicsquiz.isAnswerTrue";
    private boolean mAnswerIsTrue;
    private Button mButtonShowAnswer;
    private TextView mTextViewAnswer;


    public static Intent newIntent(Context packagecontext, boolean isAnswerTrue){
        Intent intent = new Intent(packagecontext, CheatActivity.class);
        intent.putExtra("EXTRA_ANSWER_IS_TRUE", isAnswerTrue);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);//Todo gefur alltaf false, þarf að laga það
        Log.d(TAG, "onCreate: " + mAnswerIsTrue);

        mButtonShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mTextViewAnswer = (TextView) findViewById(R.id.show_answer_text);
        mButtonShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue)
                    mTextViewAnswer.setText(R.string.True_Button);
                else
                    mTextViewAnswer.setText(R.string.False_Button);
            }
        });
    }
}