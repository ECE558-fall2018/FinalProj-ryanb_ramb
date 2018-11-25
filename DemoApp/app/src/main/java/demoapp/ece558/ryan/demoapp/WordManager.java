package demoapp.ece558.ryan.demoapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WordManager {

    private static final String TAG = "WordManager";

    private static WordManager sWordManager;
    private Context mContext;
    private List<Word> mWords;

    private static final WordManager ourInstance = new WordManager();

    // public constructor to return the instance of the class
    public static WordManager getInstance() {
        return ourInstance;
    }

    // private constructor
    private WordManager() {
        mWords = new ArrayList<>();
        generateList();
    }

    public void addWord (int index, String word) {
        Word newWord = new Word(word);
        mWords.add(index, newWord);
    }

    // return the list of words
    public List<Word> getWords() {
        return mWords;
    }

    // generate a dummy list for the recyclerview
    private void generateList() {

        for (int i = 0; i < 24; i++) {
            Word word = new Word("word test " + i);
            mWords.add(word);
        }

        Log.d(TAG, "Generated list.");
    }

    public Word getWord(UUID id){
        for(Word word : mWords) {
            if(word.getId().equals(id))
                return word;
        }
        return null;
    }

    public Word getWord(int pos){
        return mWords.get(pos);
    }

    public int getPos(UUID id) {
        return mWords.indexOf(getWord(id));
    }
}
