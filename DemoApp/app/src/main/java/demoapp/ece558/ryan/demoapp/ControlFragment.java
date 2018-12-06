package demoapp.ece558.ryan.demoapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ControlFragment extends Fragment {

    private static final int REQ_CODE_SPEECH_INPUT = 1;

    private String mWordFromSpeech;
    private TextToSpeech textToSpeech;
    private FragmentActivity mActivity;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status == TextToSpeech.SUCCESS)
                {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if(result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(getContext(),"This Language is not supported",Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        //imageButton.setEnabled(true);
                        textToSpeech.setPitch(0.8f);
                        textToSpeech.setSpeechRate(0.9f);
                        speak();
                    }

                }
            }
        });


    }

    private void speak()
    {
        //String text = editText.getText().toString();
        textToSpeech.speak(mWordFromSpeech,TextToSpeech.QUEUE_FLUSH,null,null);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        // Add the buttons to the view
        ImageButton ib = (ImageButton) view.findViewById(R.id.button_play_show);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Play show", Toast.LENGTH_SHORT).show();
            }
        });

        ib = (ImageButton) view.findViewById(R.id.button_record_text);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Record Text", Toast.LENGTH_SHORT).show();
                promptSpeechInput();
            }
        });

        return view;
    }


    // Begin the speech-to-text process
    public void promptSpeechInput() {
        try {
            startActivityForResult(setIntentData(), REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getActivity(), "Error Recording Speech", Toast.LENGTH_SHORT).show();
            anfe.printStackTrace();
        }
    }

    // Create the intent for the speech to text
    public Intent setIntentData () {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        return intent;
    }

    // Get the String of the decoded speech from text-to-speech
    public String getDecodedSpeech(int resultCode, Intent data) {

        if (resultCode == RESULT_OK && data != null){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            return result.get(0);
        }
        else
            return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get the converted word from the speech activity intent
        mWordFromSpeech = getDecodedSpeech(resultCode, data);

        //textToSpeech.speak("I think I heard"+mWordFromSpeech,TextToSpeech.QUEUE_FLUSH,null,null);

        // create new word
        Word newWord = new Word(mWordFromSpeech);

        // add the word to the word manager wordlist
        //WordManager mWordManager = new WordManager();//WordManager.getInstance();

       // Log.d("WORDS","WORDS"+mWordManager.getWordList());
        //mWordManager.addWord(newWord);

        // Add word to database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("words");
        //databaseReference.push().setValue(mWordFromSpeech);
        // add color child entry
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("words").child(mWordFromSpeech);
        //databaseReference.push().setValue("colors");

        DatabaseManager db = new DatabaseManager();
        db.addWordToDataBase();

        // store the word in the view model to update the recycler view fragment
        Log.d("Before","Getactivity");
        WordViewModel model = ViewModelProviders.of(mActivity).get(WordViewModel.class);
        model.setWordFromText(mWordFromSpeech);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
