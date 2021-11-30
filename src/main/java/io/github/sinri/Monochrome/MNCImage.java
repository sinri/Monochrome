package io.github.sinri.Monochrome;

import java.io.*;
import java.util.ArrayList;

public class MNCImage {
    private final int width;
    private final int height;
    private final byte grayBits;

    private final byte[] mncData;

    public MNCImage(CommonImage image, byte grayBits) {
        this.width = image.image.getWidth();
        this.height = image.image.getHeight();
        this.grayBits = grayBits;

        ArrayList<Byte> Bs = MNCImage.buildMNC(image, grayBits);

        this.mncData = new byte[Bs.size()];
        for (int i = 0; i < Bs.size(); i++) {
            this.mncData[i] = Bs.get(i);
        }
    }

    /**
     * @param file the File object
     */
    public MNCImage(File file) throws IOException {
        BufferedInputStream bis = null;
        FileInputStream fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);

        ArrayList<Byte> Bs = new ArrayList<>();

        int oneByte = bis.read();
        while (oneByte != -1) {
            Bs.add(MNCImage.int2byte(oneByte));
            oneByte = bis.read();
        }

        this.mncData = new byte[Bs.size()];
        for (int i = 0; i < Bs.size(); i++) {
            this.mncData[i] = Bs.get(i);
        }

        width = ((this.mncData[0] & 0x000000FF) << 24) |
                ((this.mncData[1] & 0x000000FF) << 16) |
                ((this.mncData[2] & 0x000000FF) << 8) |
                ((this.mncData[3] & 0x000000FF));
        height = ((this.mncData[4] & 0x000000FF) << 24) |
                ((this.mncData[5] & 0x000000FF) << 16) |
                ((this.mncData[6] & 0x000000FF) << 8) |
                ((this.mncData[7] & 0x000000FF));
        grayBits = this.mncData[8];

    }

    /**
     * @param image    the CommonImage instance
     * @param grayBits [1,7] the number of bits within the ONE-BYTE unit ahea
     * @return d
     */
    public static ArrayList<Byte> buildMNC(CommonImage image, byte grayBits) {
        int width = image.image.getWidth();
        int height = image.image.getHeight();

        int[] pixels = image.getARGBArray(null);

        //DEBUG
        /*
        for (int i = 0; i < pixels.length; i++) {
            System.out.print(pixels[i] + " ");
            if ((i + 1) % (width) == 0) {
                System.out.println();
            }
        }
        */

        ArrayList<Byte> list = new ArrayList<>();

        list.add(MNCImage.int2byte((width & 0xFF000000) >> 24));
        list.add(MNCImage.int2byte((width & 0x00FF0000) >> 16));
        list.add(MNCImage.int2byte((width & 0x0000FF00) >> 8));
        list.add(MNCImage.int2byte((width & 0x000000FF)));

        list.add(MNCImage.int2byte((height & 0xFF000000) >> 24));
        list.add(MNCImage.int2byte((height & 0x00FF0000) >> 16));
        list.add(MNCImage.int2byte((height & 0x0000FF00) >> 8));
        list.add(MNCImage.int2byte((height & 0x000000FF)));

        list.add(grayBits);

        int i = 0;
        int currentGray = 0;
        int limitLen = ((1 << (8 - grayBits)) - 1);
        int len = 0;
        while (i < pixels.length) {
            int pixel = pixels[i];

            int a = CommonImage.alphaFromARGB(pixel);
            int r = CommonImage.redFromARGB(pixel);
            int g = CommonImage.greenFromARGB(pixel);
            int b = CommonImage.blueFromARGB(pixel);
            int gray = MNCImage.grayFromARGB(a, r, g, b, grayBits);

            if (i == 0) {
                currentGray = gray;
                len = 1;
            } else {
                if (currentGray == gray && len < limitLen) {
                    len++;
                } else {
                    //add
                    byte unit = MNCImage.int2byte(currentGray | len);
                    //System.out.println("MID currentGray="+currentGray+" len="+len+" :: "+unit);
                    list.add(unit);

                    //
                    currentGray = gray;
                    len = 1;
                }
            }

            if ((++i) >= pixels.length) {
                //
                byte unit = MNCImage.int2byte(currentGray | len);
                //System.out.println("FIN currentGray="+currentGray+" len="+len+" :: "+unit);
                list.add(unit);
                break;
            }
        }

        //DEBUG
        /*
        System.out.println("===============");
        System.out.println("width=" + width + " height=" + height);
//        System.out.println((byte) (width & 0xFF000000));
//        System.out.println((byte) (width & 0x00FF0000));
//        System.out.println((byte) (width & 0x0000FF00));
//       System.out.println((byte) (width & 0x000000FF));
        System.out.println("===============");
        for (i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
            if ((i + 1) % (width) == 0) {
                System.out.println();
            }
        }
        * */

        return list;
    }

    /**
     * @param a        alpha
     * @param r        red
     * @param g        green
     * @param b        blue
     * @param grayBits [1,8]
     * @return 0xFF000000 as White TwoBits
     */
    public static int grayFromARGB(int a, int r, int g, int b, byte grayBits) {
        int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
        return (gray >> (8 - grayBits)) << (8 - grayBits);
    }

    public static int argbValueFromGray(int gray, byte grayBits) {
        //        int v1 = gray;
        //        int v2 = v1 >> grayBits;
        //        int v3 = v2 >> grayBits;
        //        int v4 = v3 >> grayBits;
        //        int part = v1 | v2 | v3 | v4;

        int part = 0;
        int v = gray;
        for (int i = 0; i < 8 / grayBits; i++) {
            part = part | v;
            v = v >> grayBits;
        }

        part = 0x000000FF & part;
        //part=0xFF000000 | (part | (part<<8) | (part<<16));
        part = CommonImage.colorWithARGB(255, part, part, part);
        //System.out.println("argbValueFromGray("+gray+","+grayBits+")="+v1+","+v2+","+v3+","+v4+" :: "+part);
        return part;
    }

    public static byte int2byte(int v) {
        return (byte) (v & 0x000000FF);
    }

    public static int byte2int(byte v) {
        return 0x000000FF & v;
    }

    public void saveMNCImageToFile(File file) throws IOException {
        BufferedOutputStream bos = null;
        //create an object of FileOutputStream
        FileOutputStream fos = new FileOutputStream(file);
        //create an object of BufferedOutputStream
        bos = new BufferedOutputStream(fos);

        bos.write(this.mncData);

        bos.close();
    }

    public CommonImage convertToCommonImage() {

        ArrayList<Integer> pal = new ArrayList<>();

        for (int i = 9; i < this.mncData.length; i++) {
            int unit = this.mncData[i] & 0x000000FF;
            int gray = (unit >> (8 - this.grayBits)) << (8 - this.grayBits);
            int len = ((unit << this.grayBits) & 0x000000FF) >> this.grayBits;

            int c = MNCImage.argbValueFromGray(gray, this.grayBits);

            //System.out.println("unit["+unit+"] gray="+gray+" len="+len+" color="+c );

            for (int j = 0; j < len; j++) {
                pal.add(c);
            }
        }

        int[] pixels = new int[pal.size()];
        for (int i = 0; i < pal.size(); i++) {
            pixels[i] = pal.get(i);
        }
        System.out.println("width=" + this.width + " height=" + this.height + " pixel size=" + pal.size());
        return new CommonImage(this.width, this.height, pixels);
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the grayBits
     */
    public byte getGrayBits() {
        return grayBits;
    }

    /**
     * @return the mncData
     */
    public byte[] getMncData() {
        return mncData;
    }
}
