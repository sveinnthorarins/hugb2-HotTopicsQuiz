package is.hi.hbv601g.hottopicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mButtonTrue;
    private Button mButtonFalse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonTrue =(Button) findViewById(R.id.True_Button);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,R.string.Toast_Correct,Toast.LENGTH_SHORT).show();
            }

        });
        mButtonFalse =(Button) findViewById(R.id.False_Button);
        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,R.string.Toast_Incorrect,Toast.LENGTH_SHORT).show();
            }
        });
    }
}