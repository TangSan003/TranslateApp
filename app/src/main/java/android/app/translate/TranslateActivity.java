package android.app.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TranslateActivity extends AppCompatActivity {

    TextView textViewTabTranslate,textViewTabVoice,textViewTabTextRecognition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        setActivityTab();
    }
    private void setActivityTab() {
        textViewTabTranslate = findViewById(R.id.textView_tab_translate);
        textViewTabVoice =findViewById(R.id.textView_tab_voice);
        textViewTabTextRecognition = findViewById(R.id.textView_tab_text_recognition);

        textViewTabVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TranslateActivity.this, VoiceActivity.class);
                startActivity(intent);
            }
        });
        textViewTabTextRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TranslateActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });

    }
}