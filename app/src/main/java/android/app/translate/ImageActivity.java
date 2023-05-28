package android.app.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ImageActivity extends AppCompatActivity {

    TextView textViewTabTranslate,textViewTabVoice,textViewTabTextRecognition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        setActivityTab();
    }
    private void setActivityTab() {
        textViewTabTranslate = findViewById(R.id.textView_tab_translate);
        textViewTabVoice =findViewById(R.id.textView_tab_voice);
        textViewTabTextRecognition = findViewById(R.id.textView_tab_text_recognition);


        textViewTabVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this, VoiceActivity.class);
                startActivity(intent);
            }
        });
        textViewTabTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this, TranslateActivity.class);
                startActivity(intent);
            }
        });

    }
}