package io.github.sinri.Monochrome.test.noise;

import io.github.sinri.Monochrome.CommonImage;
import io.github.sinri.Monochrome.MNCImage;

import java.io.File;
import java.io.IOException;

public class NoiseCleanTest {
    public static void main(String[] args) {
        for (int blockSize = 2; blockSize <= 10; blockSize++) {
            routine(blockSize);
        }
    }

    private static void routine(int blockSize) {
        try {
            CommonImage commonImage = new CommonImage(new File("/Users/leqee/code/Monochrome/debug/zipraw.jpg"));
            commonImage.cleanNoise(blockSize);
            commonImage.saveToFileWithFormat(new File("/Users/leqee/code/Monochrome/debug/clean_noise_" + blockSize + ".jpg"), "jpg");

            for (byte x = 1; x <= 7; x++) {
                MNCImage mncImage = new MNCImage(commonImage, x);
                mncImage.saveMNCImageToFile(new File("/Users/leqee/code/Monochrome/debug/clean_noise_" + blockSize + "_" + x + ".mnc"));
                mncImage.convertToCommonImage().saveToFileWithFormat(
                        new File("/Users/leqee/code/Monochrome/debug/clean_noise_" + blockSize + "_" + x + ".png"),
                        "png"
                );
                mncImage.saveZippedMNCImageToFile(new File("/Users/leqee/code/Monochrome/debug/clean_noise_" + blockSize + "_" + x + ".mnc.zip"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
