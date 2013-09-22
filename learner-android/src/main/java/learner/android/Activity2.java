package learner.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import org.mili.learner.system.Engine;
import org.mili.learner.system.EngineListener;
import org.mili.learner.system.RoundObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class Activity2 extends Activity implements OnInitListener, EngineListener {

    private Engine engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        engine = new org.mili.learner.system.Engine();
        engine.setEngineListener(this);
        try {
            engine.start(getIntent().getStringExtra("from"), getIntent().getStringExtra("to"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button finishButton = (Button) findViewById(R.id.buttonFinish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Activity3.class);
                myIntent.putExtra("statistics", engine.getStatistics());
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

    @Override
    public void onInit(int status) {
    }

    @Override
    public void onRightAnswer() {

    }

    @Override
    public void onWrongAnswer() {
        TextView answerTextView = getAnswerTextView();
        answerTextView.setTextColor(Color.RED);
    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onNewRound(RoundObject roundObject) {
        TextView answerTextView = getAnswerTextView();
        answerTextView.setText(roundObject.getQuestion());
        answerTextView.setTextColor(Color.LTGRAY);

        List<String> answers = roundObject.getAnswers();

        setUpButton(answers.get(0), (Button) findViewById(R.id.buttonA));
        setUpButton(answers.get(1), (Button) findViewById(R.id.buttonB));
        setUpButton(answers.get(2), (Button) findViewById(R.id.buttonC));
        setUpButton(answers.get(3), (Button) findViewById(R.id.buttonD));
    }

    private void setUpButton(String text, final Button button) {
        button.setText(text);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                engine.answer((String) button.getText());
            }
        });
    }

    public TextView getAnswerTextView() {
        return (TextView) findViewById(R.id.question);
    }
}
