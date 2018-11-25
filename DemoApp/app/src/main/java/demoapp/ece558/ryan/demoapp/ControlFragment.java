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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ControlFragment extends Fragment {

    private static final int REQ_CODE_SPEECH_INPUT = 1;
    private static final int REQ_CODE_SPEECH_RESULT = 2;

    private String mWordFromSpeech;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    public void promptSpeechInput() {
        try {
            startActivityForResult(setIntentData(), REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            anfe.printStackTrace();
        }
    }

    public Intent setIntentData () {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");

        return intent;
    }

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

        // store the word in the view model to update the recycler view fragment
        WordViewModel model = ViewModelProviders.of(getActivity()).get(WordViewModel.class);
        model.setWordFromText(mWordFromSpeech);

        // Add word to database and to the top of the recycler view

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }


}
