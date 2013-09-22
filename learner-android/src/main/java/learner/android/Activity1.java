package learner.android;

import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences.*;
import android.os.*;
import android.preference.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import org.mili.learner.system.EngineListener;
import org.mili.learner.system.RoundObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 */
public class Activity1 extends Activity implements OnInitListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        List<String> locales = new ArrayList<String>();
        try {
            locales = org.mili.learner.system.Engine.getLanguages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] languages = new String[locales.size()];
        for (int i = 0; i < locales.size(); i ++) {
            languages[i] = locales.get(i);
        }

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,
                languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        Button startButton = (Button) findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Activity2.class);
                myIntent.putExtra("from", spinner1.getSelectedItem().toString());
                myIntent.putExtra("to", spinner2.getSelectedItem().toString());
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

}
