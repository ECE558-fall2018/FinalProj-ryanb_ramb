package a558.ece.ryan.project4_hw;

/*
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Word {

    //private UUID mId;
    private String mWord;
    private String mShowData;
    private int RED;
    private int GREEN;
    private int BLUE;

    private int mColorList [];
    private UUID mId;
    private List<Integer> ColorList;
    private List<String> ColorBox;

    public Word()
    {
        this.mWord = null;
        //mColorList = new int[8];
        ColorList = new ArrayList<>();
        ColorBox = new ArrayList<>();


    }

    public Word(int red, int green, int blue)
    {
        RED= red;
        GREEN = green;
        BLUE = blue;
    }

    public Word(String word) {
        mWord = word;
        mId = UUID.randomUUID();

        mColorList = new int[8];

        for (int i = 0; i < 8; ++i)
           mColorList[i] = 0;


    }


    public Word(String word, List<Integer> mColorList)
    {
        mWord = word;
        //mColorList = new int [8];
        ColorList = mColorList;



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


    public void setColor(int index,int color) {
        mColorList[index]=color;
    }

    public int getColor(int index)
    {
        return mColorList[index];
    }

    public int getCOLORS(int index)
    {
        return ColorList.get(index);
    }

    public UUID getId() {
        return mId;
    }


    public int[] getmColorList() {
        return mColorList;
    }

    public void setmColorList(int[] mColorList) {
        this.mColorList = mColorList;
    }

    public List<Integer> getColorList() {
        return ColorList;
    }

    public void setColorList(List<Integer> colorList) {
        ColorList = colorList;
    }

    public List<String> getColorBox() {
        return ColorBox;
    }

    public void setColorBox(List<String> colorBox) {
        ColorBox = colorBox;
    }
}

*/

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Word {


    private DatabaseReference databaseReference;
    private UUID mId;
    private String mWord;
    private String mShowData;
    private int mColorList [];
    private List<Integer> COLORS;

    public Word(String word) {
        mWord = word;
        mId = UUID.randomUUID();
        mColorList = new int [8];

        for (int i = 0; i < 8; ++i)
            mColorList[i] = 0;



    }

    public Word(String word, int colorlist)
    {
        mWord = word;

        //mColorList = new int [8];
        COLORS = new ArrayList<>();

        for (int i = 0; i < 8; ++i)
            COLORS.add(colorlist);
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

    public int[] getmColorList() {
        return mColorList;
    }

    public List<Integer> getCOLORS() {
        return COLORS;
    }
}
