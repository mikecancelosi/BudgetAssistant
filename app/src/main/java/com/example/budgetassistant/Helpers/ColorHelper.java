package com.example.budgetassistant.Helpers;

import android.graphics.Color;
import android.util.Log;

public class ColorHelper {

    public static int[] createColorValueGradient(int originalColor, int numberOfValues) {
        int[] colors = new int[numberOfValues];

        int[] baseRGB = new int[]{
                Color.red(originalColor),
                Color.green(originalColor),
                Color.blue(originalColor),
                };

        float[] baseHSV = new float[3];
        Color.RGBToHSV(baseRGB[0],baseRGB[1],baseRGB[2],baseHSV);

        for(int i=1;i<=numberOfValues;i++){
            float[] hsv = new float[3];
            float value = (1f/numberOfValues) * i;
            hsv[0] = baseHSV[0];
            hsv[1] = baseHSV[1];
            hsv[2] = value;


            colors[i-1] =  Color.HSVToColor(hsv);
        }

        return colors;
    }


}
