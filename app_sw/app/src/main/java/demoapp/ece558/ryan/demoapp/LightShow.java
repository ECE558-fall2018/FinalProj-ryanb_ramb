package demoapp.ece558.ryan.demoapp;

public class LightShow {

    private static final int NUM_COLORS = 8;
    private static final int NUM_STEPS = 32;


    private int [][] mColorArray;     // stores the selected colors for the lightshow
    private int mCurrentStep;
    private byte [] showData;       // the actual bytes to send to the TLC5940

    public LightShow (int [] colorArray) {
        mColorArray = new int [NUM_COLORS][3];

        for (int i = 0; i < NUM_COLORS; i++){
            mColorArray[i][0] = convertRed(colorArray[i]);
            mColorArray[i][1] = convertGreen(colorArray[i]);
            mColorArray[i][2] = convertBlue(colorArray[i]);
        }
    }

    /*  12-bit PWM data + 6-bit DC data for each channel
        18-bit x 16 = 36 bytes
     */
    public byte [] getNextData() {
        byte [] nextData = new byte [36];

        // compute the next red values
        for (int i = 0; i < NUM_COLORS - 1; i++){
            nextData[i] = computeNextValue(mColorArray[i][0], mColorArray[i+1][0]);
        }

        // compute the next greem values
        for (int i = 0; i < NUM_COLORS - 1; i++){
            nextData[i + NUM_COLORS] = computeNextValue(mColorArray[i][1], mColorArray[i+1][1]);
        }


        // compute the next blue values
        for (int i = 0; i < NUM_COLORS - 1; i++){
            nextData[i + NUM_COLORS*2] = computeNextValue(mColorArray[i][2], mColorArray[i+1][2]);
        }

        return nextData;
    }


    private byte computeNextValue(int colorFrom, int colorTo){
        return (byte) (((colorTo - colorFrom) / NUM_STEPS) * mCurrentStep);
    }


    private int convertRed(int color){
        return (color >> 16) & 0xFF;
    }

    private int convertGreen(int color){
        return (color >> 8) & 0xFF;
    }

    private int convertBlue(int color){
        return color & 0xFF;
    }
}
