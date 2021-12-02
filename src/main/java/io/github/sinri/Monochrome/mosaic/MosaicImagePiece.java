package io.github.sinri.Monochrome.mosaic;

import io.github.sinri.Monochrome.MNCImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MosaicImagePiece {
    private final MNCImage mncImage;
    private final int locationX;
    private final int locationY;

    public MosaicImagePiece(int x, int y, MNCImage mncImage) {
        this.locationX = x;
        this.locationY = y;
        this.mncImage = mncImage;
    }

    public static ArrayList<MosaicImagePiece> decodeToPieces(byte[] bytes, int index) {
        ArrayList<MosaicImagePiece> pieces = new ArrayList<>();
//        System.out.println("decodeToPieces bytes.length="+bytes.length+" index="+index);
        if (index < bytes.length) {
            int start = index;
            int ptr = start;

//            System.out.println("decodeToPieces index="+index+" start="+start+" ptr="+ptr);

            int baseX = 0;
            int baseY = 0;
            while (ptr < bytes.length) {
                // START READING PIECE HEADER
                baseX = MNCImage.bytes2int(bytes, ptr);
                ptr += 4;
                baseY = MNCImage.bytes2int(bytes, ptr);
                ptr += 4;
                // MNC HEADER
                int width = MNCImage.bytes2int(bytes, ptr);
                ptr += 4;
                int height = MNCImage.bytes2int(bytes, ptr);
                ptr += 4;
                byte grayBits = bytes[ptr];
                // GB 1 -> 0b01111111
                // GB 2 -> 0b00111111
                // ...
                // GB 7 -> 0b00000001
                byte mask = (byte) (0xFF >> grayBits);
                ptr += 1;

//                System.out.println("decodeToPieces read one MNC Header: baseX="+baseX+" baseY="+baseY+" width="+width+" height="+height+" grayBits="+grayBits+" now ptr="+ptr);

                if (baseX < 0 || baseY < 0) {
                    throw new RuntimeException("!!!");
                }

                // MNC BODY READING START

                int total = width * height;
                int count = 0;

                while (count < total) {
                    byte x = bytes[ptr];
                    // how many cells it covers?
                    int coveredCellCount = x & mask;
                    count += coveredCellCount;
                    ptr += 1;

//                    System.out.println("decodeToPieces reading mnc body: count="+count+" now ptr="+ptr);
                }
                if (count > total) {
                    throw new RuntimeException("!! count=" + count + " > total=" + total);
                }

                // MNC BODY READING END

                byte[] oneMNCData = new byte[ptr - start - 8];
                for (int i = start + 8; i < ptr; i++) {
                    oneMNCData[i - start - 8] = bytes[i];
                }
                MNCImage mncImage = new MNCImage(oneMNCData);
//                System.out.println("decodeToPieces [MosaicImage+Piece] x="+baseX+" y="+baseY);
                MosaicImagePiece piece = new MosaicImagePiece(baseX, baseY, mncImage);
                pieces.add(piece);
                try {
                    piece.getMncImage().convertToCommonImage().saveToFileWithFormat(
                            new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic_piece[" + piece.getLocationX() + "_" + piece.getLocationY() + "].png"),
                            "png"
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // FOR NEXT
                start = ptr;
            }

//            System.out.println("decodeToPieces start="+start);
        }

        return pieces;
    }

    public MNCImage getMncImage() {
        return mncImage;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public ArrayList<Byte> encodeToByteArrayList() {
        ArrayList<Byte> result = new ArrayList<>();
        // 4 bytes: X, 4 bytes: Y
        result.addAll(MNCImage.int2bytes(locationX));
        result.addAll(MNCImage.int2bytes(locationY));
//        System.out.println("[MosaicImagePiece] x="+locationX+" y="+locationY);
        // mnc
//        System.out.println("[MosaicImagePiece] mnc width="+mncImage.getWidth()+" height="+mncImage.getHeight()+" grayBits="+mncImage.getGrayBits());
        byte[] mnc = mncImage.getMncData();
        for (byte b : mnc) {
            result.add(b);
        }
        return result;
    }
}
