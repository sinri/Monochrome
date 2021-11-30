package io.github.sinri.Monochrome.test.zip;

import io.github.sinri.Monochrome.ZipHelper;

import java.io.File;
import java.io.IOException;

public class ZipTest {
    public static void main(String[] args) {
        try {
            ZipHelper.zip(
                    new File("/Users/leqee/code/Monochrome/debug/zipraw.jpg"),
                    new File("/Users/leqee/code/Monochrome/debug/zipraw.zip")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ZipHelper.unzip(
                    new File("/Users/leqee/code/Monochrome/debug/zipraw.zip"),
                    new File("/Users/leqee/code/Monochrome/debug/zipraw.jpg")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
