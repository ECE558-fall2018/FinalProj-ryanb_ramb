package demoapp.ece558.ryan.demoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.toIntExact;


public class WordManager {

    private static final String TAG = "WordManager";
    private static final String DB_FLAG = "db_flag_update";

    private static WordManager sWordManager;
    private Context mContext;
    private List<Word> mWordList;

    private String info[] = new String[4];

    private DatabaseReference mDatabaseReference;

    // singleton constructor/manager
    private static final WordManager ourInstance = new WordManager();

    // public constructor to return the instance of the class
    public static WordManager getInstance() {
        return ourInstance;
    }

    // private constructor
    private WordManager() {
        // create word list
        mWordList = new ArrayList<Word>();

        // get the known words from the database
        //createListenerForWordList();
        //getWordListFromDatabase();
    }

    public void setWordListFromDatabase(List<Word> wordList){
        // set flag in database to trigger the word manager to read the database and get the word list
        //mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //mDatabaseReference.child(DB_FLAG).setValue("update");
        mWordList = wordList;
    }

    // On start-up, get the word list from the database
    private void createListenerForWordList() {
        DatabaseManager db = new DatabaseManager();
        mWordList = db.getWordList();
        Log.d(TAG, "WordList received from database");
    }


    public void updateWord(Word newWord) {
        Word oldWord = getWord(newWord.getWord());

        mWordList.remove(oldWord);
        mWordList.add(newWord);
    }


    public void addWord (int index, String word) {
        Word newWord = new Word(word);
        mWordList.add(index, newWord);
    }


    public void addWord (Word newWord) {
        mWordList.add(0, newWord);
    }


    // return the list of words
    public List<Word> getWordList() {
        return mWordList;
    }

    public Word getWord(UUID id){
        for(Word word : mWordList) {
            if(word.getId().equals(id))
                return word;
        }
        return null;
    }

    public Word getWord(int pos){
        return mWordList.get(pos);
    }

    private Word getWord(String word){
        for(Word w : mWordList){
            if (w.getWord() == word)
                return w;
        }
        return null;
    }

    public int getPos(UUID id) {
        return mWordList.indexOf(getWord(id));
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public String[] getInfo() {
        return info;
    }
}