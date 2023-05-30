package android.app.translate;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Locale;

public class VoiceActivity extends AppCompatActivity {

    TextView textViewTabTranslate,textViewTabVoice,textViewTabTextRecognition;
    private Button TranlasteEnglish,TranlasteVietnamese ;
    private EditText TextTranlaste;
    private TextView ResultTranlaste;
    private TextView ImageButton;
    private int REQUEST_PERMISSION_CODE =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        setActivityTab();
        clickButtonTranlaste();
        clickSongImage();
    }
    private void clickSongImage(){
        ImageButton = findViewById(R.id.textView_voice_tab_voice);
        ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak convert into text");
                try {
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(VoiceActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void clickButtonTranlaste() {
        TranlasteEnglish = findViewById(R.id.button_translate_english_voice);
        TranlasteVietnamese = findViewById(R.id.button_translate_tieng_viet_voice);
        TextTranlaste = findViewById(R.id.edit_text_langue_voice);
        ResultTranlaste = findViewById(R.id.result_translate_voice);

        TranlasteEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultTranlaste.setText("");
                if (TextTranlaste.getText().toString().isEmpty()){
                    Toast.makeText(VoiceActivity.this,"Please enter you text to translate",Toast.LENGTH_SHORT).show();
                }
                else {
                    tranlasteText(FirebaseTranslateLanguage.VI,FirebaseTranslateLanguage.EN,TextTranlaste.getText().toString());
                }
            }
        });
        TranlasteVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultTranlaste.setText("");
                if (TextTranlaste.getText().toString().isEmpty()){
                    Toast.makeText(VoiceActivity.this,"Please enter you text to translate",Toast.LENGTH_SHORT).show();
                }
                else {
                    tranlasteText(FirebaseTranslateLanguage.EN,FirebaseTranslateLanguage.VI,TextTranlaste.getText().toString());
                }
            }
        });
    }

    private void tranlasteText(int sourceLanguage,int targetLanguage, String source){

        ResultTranlaste.setText("Downloading Modal...");
        FirebaseTranslatorOptions options = new  FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(targetLanguage)
                .build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ResultTranlaste.setText("Translasting...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        ResultTranlaste.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VoiceActivity.this,"Failed to Translate: " +e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VoiceActivity.this,"Failed to Download languge Modal: " +e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                });
            }
        });
    }
    private void setActivityTab() {
        textViewTabTranslate = findViewById(R.id.textView_tab_translate);
        textViewTabVoice =findViewById(R.id.textView_tab_voice);
        textViewTabTextRecognition = findViewById(R.id.textView_tab_text_recognition);

        textViewTabTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoiceActivity.this, TranslateActivity.class);
                startActivity(intent);
            }
        });
        textViewTabTextRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoiceActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });

    }
}