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
    private FragmentActivity mActivity;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
        Log.d("Before","Getactivity");
        WordViewModel model = ViewModelProviders.of(mActivity).get(WordViewModel.class);
        model.setWordFromText(mWordFromSpeech);

        //add.setWord(mWordFromSpeech);
        List <Integer> Color = new ArrayList<>();
        for(int i=0;i<8;++i)
        {
            Color.add(0);
        }

        //Word add = new Word(mWordFromSpeech,Color);

       // databaseReference.child(mWordFromSpeech).setValue(add);

        // Add word to database and to the top of the recycler view

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
