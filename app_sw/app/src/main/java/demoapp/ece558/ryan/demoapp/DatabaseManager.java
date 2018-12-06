package demoapp.ece558.ryan.demoapp;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static final String DB_FLAG = "db_flag_update";
    private static final String TAG = "DatabaseManager";

    private WordManager mWordManager;
    private DatabaseReference mDatabaseReference;
    List<Word> mWordList;

    public DatabaseManager () {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setDatabaseReader();

        mWordList = new ArrayList<Word>();
    }

    public void signalPlayShow() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("flag_play_show").setValue("update");
    }


    private void setDatabaseReader() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("words");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    // Database stores words and color data as multi-level hashmap
                    Map <String, Map<String, ArrayList<Long>>> wordList = (Map <String, Map <String, ArrayList<Long>>>) dataSnapshot.getValue();

                    for (Map.Entry<String, Map <String, ArrayList<Long>>> entry : wordList.entrySet()) {

                        // get the word String
                        Word word = new Word(entry.getKey());

                        // get the color data
                        for (Map.Entry<String, ArrayList<Long>> colorEntry : entry.getValue().entrySet()) {
                            try {
                                ArrayList<Long> colorList = colorEntry.getValue();
                                // iterate through the colors and add them to the word

                                for (int i = 0; i < 8; ++i) {
                                    try {
                                        long color = Long.valueOf(colorList.get(i));
                                        word.setColor(i, (int)color);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                mWordList.add(word);
                                Log.d(TAG, "Word added.");
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                catch (NullPointerException npe) {
                    Log.d(TAG, "No data found.");
                }
                Log.d(TAG, "Finished reading from database." + mWordList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    public void addWordToDataBase () {

        mWordManager = WordManager.getInstance();
        mWordList = mWordManager.getWordList();
        int count = 0;
        int size = mWordList.size();
        HashMap colorMap;
        ArrayList<Long> colorList;

        // the root level hash map for all the words
        HashMap wordMap = new HashMap(size);

        // for each word in the word list, make an entry in the word HashMap
        for (Word w : mWordList) {
            // build the array list of the color information
            colorList = new ArrayList<Long>();
            for (int i = 0; i < 8; ++i) {
                colorList.add(i, (long)mWordList.get(0).getColor(i));
            }
            // add the color array list to the 1 item hash map for "colors"
            colorMap = new HashMap(1);
            colorMap.put("colors", colorList);

            // hashmap.put(key, value) for each word
            wordMap.put(w.getWord(), colorMap);
            ++count;
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("words");
        mDatabaseReference.setValue(wordMap);


        Log.d(TAG, "test hash map");
    }

    public List<Word> getWordList(){
        if (mWordList.size() == 0)
            return null;

        return mWordList;
    }

    public void writeStateToDatabase() {
        // get the word manager and the current word list
        mWordManager = WordManager.getInstance();
        mWordList = mWordManager.getWordList();
        int size = mWordList.size();

        int wordCount = 0;
        HashMap colorMap;
        ArrayList<Long> colorList;

        // the root level hash map for all the words
        HashMap wordMap = new HashMap(size);

        // for each word in the word list, make an entry in the word HashMap
        for (Word w : mWordList) {
            // build the array list of the color information
            colorList = new ArrayList<Long>();
            for (int i = 0; i < 8; ++i) {
                colorList.add(i, (long)mWordList.get(wordCount).getColor(i));
            }
            // add the color array list to the 1 item hash map for "colors"
            colorMap = new HashMap(1);
            colorMap.put("colors", colorList);

            // hashmap.put(key, value) for each word
            wordMap.put(w.getWord(), colorMap);
            ++wordCount;
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("words");
        mDatabaseReference.setValue(wordMap);
    }

}
