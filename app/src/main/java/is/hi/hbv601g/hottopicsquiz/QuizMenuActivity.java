package is.hi.hbv601g.hottopicsquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class QuizMenuActivity extends AppCompatActivity {

    public static Intent getIntent(Context c) {
        // TODO: Implement transference of user with Parcelable
        return new Intent(c, QuizMenuActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmenu);
    }

    // TODO: All core functionality is yet to be implemented
}
