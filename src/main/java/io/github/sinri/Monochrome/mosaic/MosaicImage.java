package io.github.sinri.Monochrome.mosaic;

import io.github.sinri.Monochrome.CommonImage;
import io.github.sinri.Monochrome.MNCImage;
import io.github.sinri.Monochrome.ZipHelper;

import java.io.*;
import java.util.ArrayList;

public class MosaicImage {
    private int backgroundColor;
    private int width;
    private int height;
    private ArrayList<MosaicImagePiece> pieces = new ArrayList<>();

    public MosaicImage(byte[] bytes) {
        loadMosaicImageFromBytes(bytes);
    }

    public MosaicImage(CommonImage image, byte grayBytes, int blockSize) {
        this.backgroundColor = 0xFFFFFFFF;
        this.width = image.image.getWidth();
        this.height = image.image.getHeight();
        loadCommonImageToMosaicImage(image, grayBytes,blockSize);
    }

    public MosaicImage(File file) throws IOException {
        try (
                BufferedInputStream bis = new BufferedInputStream(
                        new FileInputStream(file)
                )
        ) {
            ArrayList<Byte> Bs = new ArrayList<>();

            int oneByte = bis.read();
            while (oneByte != -1) {
                Bs.add(MNCImage.int2byte(oneByte));
                oneByte = bis.read();
            }

            byte[] bytes = new byte[Bs.size()];
            for (int i = 0; i < Bs.size(); i++) {
                bytes[i] = Bs.get(i);
            }

            loadMosaicImageFromBytes(bytes);
        }
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<MosaicImagePiece> getPieces() {
        return pieces;
    }

    protected void loadMosaicImageFromBytes(byte[] bytes) {
        this.backgroundColor = MNCImage.bytes2int(bytes, 0);
        this.width = MNCImage.bytes2int(bytes, 4);
        this.height = MNCImage.bytes2int(bytes, 8);
        this.pieces = MosaicImagePiece.decodeToPieces(bytes, 12);
    }

    private void loadCommonImageToMosaicImage(CommonImage image, byte grayBytes,int blockSize) {
        for (int x = 0; x < image.image.getWidth(); x += blockSize) {
            for (int y = 0; y < image.image.getHeight(); y += blockSize) {
                // rect (x,y) - (x+blockSize-1,y+blockSize-1)
                if (isMNCImageRectEmpty(image, x, y, blockSize)) {
                    // this block is empty, ignore
                    continue;
                }

                // make mnc for this piece
                MNCImage mncImagePiece = new MNCImage(
                        new CommonImage(
                                image.image.getSubimage(
                                        x,
                                        y,
                                        Math.min(blockSize, image.image.getWidth() - x),
                                        Math.min(blockSize, image.image.getHeight() - y)
                                )
                        ),
                        grayBytes
                );
                pieces.add(new MosaicImagePiece(x, y, mncImagePiece));
            }
        }

    }

    private boolean isMNCImageRectEmpty(CommonImage image, int x0, int y0, int blockSize) {
        for (int x = x0; x < Math.min(x0 + blockSize, image.image.getWidth()); x++) {
            for (int y = y0; y < Math.min(y0 + blockSize, image.image.getHeight()); y++) {
                int color = image.image.getRGB(x, y);
                if (!CommonImage.almostSameColor(color, backgroundColor)) {
                    return false;
                }
            }
        }
        return true;
    }

    public final ArrayList<Byte> encodeToByteArrayList() {
        ArrayList<Byte> result = new ArrayList<>();
        // the first 12 bytes: color, width, height

        result.addAll(MNCImage.int2bytes(backgroundColor));
        result.addAll(MNCImage.int2bytes(width));
        result.addAll(MNCImage.int2bytes(height));

//        System.out.println("[MosaicImage] backgroundColor="+backgroundColor+" width="+width+" height="+height);

        // the next: pieces
        for (MosaicImagePiece piece : pieces) {
            // MNC
            ArrayList<Byte> byteArrayList = piece.encodeToByteArrayList();
            result.addAll(byteArrayList);
//            System.out.println("[MosaicImage] piece at ("+piece.getLocationX()+","+piece.getLocationY()+") size="+byteArrayList.size());
        }

        return result;
    }

    public CommonImage convertToCommonImage() {
        int[] pixels = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            pixels[i] = backgroundColor;
        }

        for (MosaicImagePiece piece : pieces) {
            CommonImage commonImagePiece = piece.getMncImage().convertToCommonImage();
            for (int x = 0; x < piece.getMncImage().getWidth(); x++) {
                int tx = piece.getLocationX() + x;
                for (int y = 0; y < piece.getMncImage().getHeight(); y++) {
                    int ty = piece.getLocationY() + y;

//                    System.out.println("[MosaicImage.convertToCommonImage] x="+x+" y="+y+" piece at ("+piece.getLocationX()+","+piece.getLocationY()+")"+" tx="+tx+" ty="+ty);

                    pixels[ty * width + tx] = commonImagePiece.image.getRGB(x, y);
                }
            }
        }

        return new CommonImage(width, height, pixels);
    }

    public void saveMosaicImageToFile(File file) throws IOException {
        try (
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            ArrayList<Byte> bytes = encodeToByteArrayList();
            for (Byte b : bytes) {
                bos.write(b);
            }
        }
    }

    public void saveZippedMNCImageToFile(File file) throws IOException {
        ArrayList<Byte> bytes = encodeToByteArrayList();
        byte[] buffer = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            buffer[i] = bytes.get(i);
        }
        ZipHelper.zip(buffer, file);
    }
}
