package a558.ece.ryan.project4_hw;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.things.pio.PeripheralManager;

public class MainActivity extends Activity {

    // Private Defines
    private final int RED = 0;
    private final int GREEN = 1;
    private final int BLUE = 2;
    private static final int UPDATE_INTERVAL_MS = 5000;
    private Handler mColorHandler;

    // Peripheral members
    private PeripheralManager mPeripheralManager;
    Tlc5940 mTlc5940;

    //private DatabaseReference mDatabaseReference;
    int mColorValues[][];
    int mColorIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize data members
        mColorValues = new int [8][3];
        mColorIndex = 0;

        // initialize peripheral mPeripheralManager and resources
        mPeripheralManager = PeripheralManager.getInstance();
        mTlc5940 = new Tlc5940(mPeripheralManager);

        // initialize database connection

        // initialize color values
        setTestColors();

        // set handler for updating the colors on the Arduino
        mColorHandler = new Handler();
        mColorHandler.post(UpdateColor);

    }


    private Runnable UpdateColor = new Runnable() {
        @Override
        public void run() {

            mTlc5940.updateColorDisplay(mColorValues[mColorIndex]);

            ++mColorIndex;
            if (mColorIndex == 8)
                mColorIndex = 0;

            mColorHandler.postDelayed(UpdateColor, UPDATE_INTERVAL_MS);
        }
    };

    private void setTestColors() {
        mColorValues[0][RED] = 255;
        mColorValues[0][GREEN] = 0;
        mColorValues[0][BLUE] = 0;

        mColorValues[1][RED] = 0;
        mColorValues[1][GREEN] = 255;
        mColorValues[1][BLUE] = 0;

        mColorValues[2][RED] = 0;
        mColorValues[2][GREEN] = 0;
        mColorValues[2][BLUE] = 255;

        mColorValues[3][RED] = 255;
        mColorValues[3][GREEN] = 0;
        mColorValues[3][BLUE] = 0;

        mColorValues[4][RED] = 0;
        mColorValues[4][GREEN] = 255;
        mColorValues[4][BLUE] = 0;

        mColorValues[5][RED] = 0;
        mColorValues[5][GREEN] = 0;
        mColorValues[5][BLUE] = 255;

        mColorValues[6][RED] = 255;
        mColorValues[6][GREEN] = 0;
        mColorValues[6][BLUE] = 0;

        mColorValues[7][RED] = 0;
        mColorValues[7][GREEN] = 255;
        mColorValues[7][BLUE] = 0;
    }

    // get the colors for the word from the database
    private void getWordColors() {

    }
}
