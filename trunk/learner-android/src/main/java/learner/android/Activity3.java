package learner.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.mili.learner.system.Engine;
import org.mili.learner.system.EngineListener;
import org.mili.learner.system.RoundObject;
import org.mili.learner.system.Statistics;

import java.io.IOException;
import java.util.List;

/**
 */
public class Activity3 extends Activity {

    private Statistics statistics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        statistics = (Statistics) getIntent().getSerializableExtra("statistics");

        TextView textView = (TextView) findViewById(R.id.textFieldNumberOfTotalAnswers);
        textView.setText("" + statistics.getNumberOfTotalAnswers());
        textView = (TextView) findViewById(R.id.textFieldNumberOfRightAnswers);
        textView.setText("" + statistics.getNumberOfRightAnswers());

        textView = (TextView) findViewById(R.id.avgRoundTimeInSeconds);
        textView.setText(statistics.getAvgRoundTime() + " s");

        Button againButton = (Button) findViewById(R.id.buttonTryAgain);
        againButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Activity1.class);
                startActivityForResult(myIntent, 0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
