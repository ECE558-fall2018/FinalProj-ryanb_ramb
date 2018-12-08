package demoapp.ece558.ryan.demoapp;

import android.arch.lifecycle.ViewModel;

public class WordViewModel extends ViewModel {

    private boolean mWordChanged;
    private String mWordFromText;

    public WordViewModel () {
        mWordChanged = false;
    }

    public String getWordFromText() {
        if (mWordChanged) {
            mWordChanged = false;
            return mWordFromText;
        }
        return null;
    }

    public void setWordFromText(String wordFromText) {
        mWordFromText = wordFromText;
        mWordChanged = true;
    }

}
