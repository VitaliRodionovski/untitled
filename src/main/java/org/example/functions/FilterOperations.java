package org.example.functions;

public class FilterOperations {
    public static float[] grayScale(float[] rgb) {
        final float mean = (rgb[0] + rgb[1] + rgb[2]) / 3;
        rgb[0] = mean; //красный
        rgb[1] = mean; //зелёный
        rgb[2] = mean; //синий

        return rgb;
    }

    public static float[] onlyRed(float[] rgb) {
        rgb[1] = 0;
        rgb[2] = 0;

        return rgb;
    }

    public static float[] onlyGreen(float[] rgb) {
        rgb[0] = 0;
        rgb[2] = 0;

        return rgb;
    }

    public static float[] onlyBlue(float[] rgb) {
        rgb[0] = 0;
        rgb[1] = 0;
        return rgb;
    }
}
