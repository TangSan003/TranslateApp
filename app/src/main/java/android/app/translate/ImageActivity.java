package android.app.translate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    TextView textViewTabTranslate,textViewTabVoice,textViewTabTextRecognition;
    private Button TranlasteEnglish,TranlasteVietnamese ;
    private EditText TextTranlaste;
    private TextView ResultTranlaste;

//    Click Button Image

    ImageView selectImage;
    Uri image;
    byte[] bytes;

    Button detectPicture;
    Bitmap bitmap;
    TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        setActivityTab();
        clickButtonTranlaste();
        clickImageButton();
    }

    private void clickImageButton() {
        editText= findViewById(R.id.edit_text_langue_translate_image);
        selectImage = findViewById(R.id.image_text_recognition);
        detectPicture = findViewById(R.id.detect_picture);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        detectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectPic();
            }
        });
    }

    private void  pickImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 2 && resultCode == RESULT_OK){
//                Log.i("image",data.toString());
                image = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                    selectImage.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    bytes = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void detectPic(){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
                if(blockList.size() >0 ){
                    String text="";
                    for(FirebaseVisionText.TextBlock block: blockList) {
                        text += block.getText() +"\n";
                        System.out.println(text);
                        editText.setText(text);
                    }
                }

            }
        });


    }



    private void clickButtonTranlaste() {
        TranlasteEnglish = findViewById(R.id.button_translate_english_image);
        TranlasteVietnamese = findViewById(R.id.button_translate_tieng_viet_image);
        TextTranlaste = findViewById(R.id.edit_text_langue_translate_image);
        ResultTranlaste = findViewById(R.id.result_translate_image);

        TranlasteEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultTranlaste.setText("");
                if (TextTranlaste.getText().toString().isEmpty()){
                    Toast.makeText(ImageActivity.this,"Please enter you text to translate",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ImageActivity.this,"Please enter you text to translate",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ImageActivity.this,"Failed to Translate: " +e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ImageActivity.this,"Failed to Download languge Modal: " +e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                });
            }
        });
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