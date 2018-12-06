package demoapp.ece558.ryan.demoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.widget.Toast.makeText;

public class SplashActivity extends AppCompatActivity {

    DatabaseManager mDatabaseManager;
    WordManager mWordManager;
    List<Word> mWordList;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // start an asyncronous task to get the word list from the database
        ReadFromDatabase readTask = new ReadFromDatabase();
        readTask.execute();
    }




    // <Params, Progress, Result>
    private class ReadFromDatabase extends AsyncTask<Void, Void, List<Word>> {

        @Override
        protected List<Word> doInBackground(Void... params) {
            int attempts = 0;
            mWordManager = WordManager.getInstance();
            mDatabaseManager = new DatabaseManager();

            mWordList = null;
            while (mWordList == null && attempts < 5) {
                // see if the wordlist has been retrieved from the database
                mWordList = mDatabaseManager.getWordList();

                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                ++attempts;
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {

        }




        @Override
        protected void onPostExecute(List<Word> result) {

            // if we got the word list from the database,
            if (mWordList != null) {
                //  write it to the word manager
                mWordManager.setWordListFromDatabase(mWordList);
                //  go to main activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast toast = makeText(SplashActivity.this, "Unable to read from database.", Toast.LENGTH_SHORT);
            }
        }
    }
}
