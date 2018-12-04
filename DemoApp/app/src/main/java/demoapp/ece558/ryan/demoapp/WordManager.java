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

    private String info[] = new String[4];



    private static final WordManager ourInstance = new WordManager();

    // public constructor to return the instance of the class
    public static WordManager getInstance() {
        return ourInstance;
    }

    // private constructor
    private WordManager() {
        mWords = new ArrayList<>();
        info[0] = "Party Light";
        info[1]=  "Home Light";
        info[2]=  "Game Light";
        info[3]=  "Happy Light";
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

        info[0] = "Party Light";
        info[1]=  "Home Light";
        info[2]=  "Game Light";
        info[3]=  "Happy Light";

        //for (int i = 0; i < 4; i++) {
            Word word0 = new Word(info[0]);
            Word word1 = new Word(info[1]);
            Word word2 = new Word(info[2]);
            Word word3 = new Word(info[3]);
            mWords.add(word0);
            mWords.add(word1);
            mWords.add(word2);
            mWords.add(word3);
        //}

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

    public void setInfo(String[] info) {
        this.info = info;
    }

    public String[] getInfo() {
        return info;
    }
}