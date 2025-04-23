package com.example.viennaroutefinderdsaca2.DataStructure;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Objects;

public class BWConverter {
    public static final Color roadColor = Color.rgb(255, 254, 255);
    public static Image BWImage;
    private static int[] pixelArray;

    public static int[] getPixelArray() {
        return pixelArray;
    }

    public static Image convert() {
        int width = 750;
        int height = 500;
        WritableImage convertedMap = new WritableImage(width, height);
        PixelWriter pw = convertedMap.getPixelWriter();

        Image mapViewImage = new Image(Objects.requireNonNull(BWConverter.class.getResourceAsStream("/com/example/viennauroutefinderdsaca2/resources/greyMap.png")));
        PixelReader pr = mapViewImage.getPixelReader();

        pixelArray = new int[width * height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color pixelColor = pr.getColor(col, row);

                if (isColorInRange(pixelColor)) {
                    pw.setColor(col, row, Color.WHITE);
                    pixelArray[row * width + col] = 0;
                } else {
                    pw.setColor(col, row, Color.BLACK);
                    pixelArray[row * width + col] = -1;
                }
            }
        }
        BWImage = convertedMap;
        return BWImage;
    }

    private static boolean isColorInRange(Color color) {
        int redDiff = Math.abs((int)(color.getRed() * 255) - (int)(roadColor.getRed() * 255));
        int greenDiff = Math.abs((int)(color.getGreen() * 255) - (int)(roadColor.getGreen() * 255));
        int blueDiff = Math.abs((int)(color.getBlue() * 255) - (int)(roadColor.getBlue() * 255));
        return (redDiff <= 28 && greenDiff <= 28 && blueDiff <= 28);
    }
}
