package io.github.sinri.Monochrome.test.mosaic;

import io.github.sinri.Monochrome.CommonImage;
import io.github.sinri.Monochrome.mosaic.MosaicImage;

import java.io.File;
import java.io.IOException;

public class MosaicTest {
    public static void main(String[] args) {
        try {
            CommonImage commonImage = new CommonImage(
                    new File("/Users/leqee/code/Monochrome/debug/zipraw.jpg")
            );
            commonImage.cleanNoise(8);
            MosaicImage mosaicImage = new MosaicImage(commonImage, (byte) 1);

            mosaicImage.saveMosaicImageToFile(
                    new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic")
            );

            mosaicImage.saveZippedMNCImageToFile(
                    new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic.zip")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MosaicImage mosaicImage = new MosaicImage(new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic"));
            mosaicImage.convertToCommonImage().saveToFileWithFormat(new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic.png"), "png");

//            for (MosaicImagePiece piece:mosaicImage.getPieces()) {
//                piece.getMncImage().convertToCommonImage().saveToFileWithFormat(
//                        new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic_piece["+piece.getLocationX()+"_"+piece.getLocationY()+"].png"),
//                        "png"
//                );
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
