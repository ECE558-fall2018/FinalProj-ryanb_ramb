package demoapp.ece558.ryan.demoapp;

import java.util.UUID;

public class Word {



    private UUID mId;
    private String mWord;
    private String mShowData;
    private int mColorList [];

    public Word(String word) {
        mWord = word;
        mId = UUID.randomUUID();
        mColorList = new int [8];

        for (int i = 0; i < 8; ++i)
            mColorList[i] = 0;

    }


    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getShowData() {
        return mShowData;
    }

    public void setShowData(String showData) {
        mShowData = showData;
    }

    public int getColor(int index) {
        return mColorList[index];
    }

    public void setColor(int index, int color) {
        mColorList[index] = color;
    }

    public UUID getId() {
        return mId;
    }
}
