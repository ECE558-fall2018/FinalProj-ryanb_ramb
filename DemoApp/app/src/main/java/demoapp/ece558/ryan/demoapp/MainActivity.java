package demoapp.ece558.ryan.demoapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements WordListFragment.OnViewClickListener {

    private static final String TAG = "Main";
    public static final String EXTRA_WORD_ID = "ece.558.final.project.word_id";
    public static final int NUM_COLORS = 8;
    public static final int NUM_ROWS = 10;



    // RecyclerView Item Select
    // word list fragment is notifying that a word was clicked so we can change the fragments
    @Override
    public void onViewSelected (int wordPosition) {
        replaceFragment(wordPosition);
    }

    // When the word list fragment is attached, we have to link the custom listener method from the
    // fragment to the activity
    @Override
    public void onAttachFragment(Fragment fragment){
        if (fragment instanceof WordListFragment) {
            WordListFragment wordListFragment = (WordListFragment) fragment;
            wordListFragment.setOnViewClickListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // write to the database
        DatabaseManager db = new DatabaseManager();
        db.writeStateToDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if (findViewById(R.id.portrait) != null) {

            // Start the fragment manager and begin the fragment transaction
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setReorderingAllowed(true);

            // Add the top fragment with the recycler view
            Fragment wordFragment = fm.findFragmentById(R.id.fragment_container_top);
            if (wordFragment == null) {
                wordFragment = new WordListFragment();
                ft.add(R.id.fragment_container_top, wordFragment);
                Log.d(TAG, "Creating new wordlist fragment.");

                // Add the bottom fragment with the control buttons
                Fragment controlFragment = fm.findFragmentById(R.id.fragment_container_bottom);
                if (controlFragment == null) {
                    controlFragment = new ControlFragment();
                    ft.add(R.id.fragment_container_bottom, controlFragment);
                }

                Log.d(TAG, "Committing new fragment.");
                ft.commit();

            }
       }

        if(findViewById(R.id.landscape) != null) {

            // Start the fragment manager and begin the fragment transaction
            FragmentManager fmLand = this.getSupportFragmentManager();
            FragmentTransaction ftLand = fmLand.beginTransaction();

            // Add the top fragment with the recycler view
            Fragment wordFragmentLand = fmLand.findFragmentById(R.id.fragment_container_top);
            if (wordFragmentLand == null) {
                wordFragmentLand = new WordListFragment();
                ftLand.add(R.id.fragment_container_top, wordFragmentLand);
                //ftLand.show(fmLand.findFragmentById(R.id.fragment_container_top));
                Log.d(TAG, "Creating new wordlist fragment.");

                // Add the bottom fragment with the control buttons
                Fragment controlFragment = fmLand.findFragmentById(R.id.fragment_container_bottom);
                if (controlFragment == null) {
                    controlFragment = new ControlFragment();
                    ftLand.add(R.id.fragment_container_bottom, controlFragment);
                }

                Log.d(TAG, "Committing new fragment.");
                ftLand.commit();

            }
        }




    }

    // Method to abstract away the fragment replacement process when switching from the wordlist to
    // a word fragment
    public void replaceFragment(int wordPosition){
        // access the fragment manager and begin transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // create word fragment and get the word from the recycler view fragment
        // word position is the list position of the word in the list of words
        WordFragment wordFragment = new WordFragment();
        Bundle args = new Bundle();
        args.putInt(WordFragment.WORD_INDEX, wordPosition);
        wordFragment.setArguments(args);
        // replace the word list with the word fragment
        ft.replace(R.id.fragment_container_top, wordFragment);
        ft.addToBackStack(null);

        ft.commit();
    }
}
