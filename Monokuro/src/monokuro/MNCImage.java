/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monokuro;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Sinri
 */
public class MNCImage {

    private final int width;
    private final int height;
    private final byte grayBits;

    private final byte[] mncData;

    public MNCImage(CommonImage image, byte grayBits) {
        this.width = image.image.getWidth();
        this.height = image.image.getHeight();
        this.grayBits = grayBits;
        
        ArrayList<Byte> Bs=MNCImage.buildMNC(image, grayBits);
        
        this.mncData = new byte[Bs.size()];
        for(int i=0;i<Bs.size();i++){
            this.mncData[i]=(byte)Bs.get(i);
        }
    }

    public void saveMNCImageToFile(File file) throws FileNotFoundException, IOException {
        BufferedOutputStream bos = null;
        //create an object of FileOutputStream
        FileOutputStream fos = new FileOutputStream(file);
        //create an object of BufferedOutputStream
        bos = new BufferedOutputStream(fos);
        
        bos.write(this.mncData);

        bos.close();
    }

    /**
     *
     * @param image the CommonImage instance
     * @param grayBits [1,7] the number of bits within the ONE-BYTE unit ahea
     * @return d
     */
    public static ArrayList<Byte> buildMNC(CommonImage image, byte grayBits) {
        int width = image.image.getWidth();
        int height = image.image.getHeight();

        int[] pixels = image.getARGBArray(null);

        //DEBUG
        for (int i = 0; i < pixels.length; i++) {
            System.out.print(pixels[i] + " ");
            if ((i + 1) % (width) == 0) {
                System.out.println();
            }
        }

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

        return list;
    }

    /**
     *
     * @param a
     * @param r
     * @param g
     * @param b
     * @param grayBits [1,8]
     * @return 0xFF000000 as White TwoBits
     */
    public static int grayFromARGB(int a, int r, int g, int b, byte grayBits) {
        int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
        int result = (int) ((gray >> (8 - grayBits)) << (8 - grayBits));
        //System.out.println("grayFromARGB | "+a+","+r+","+g+","+b+"=>"+gray+" :: "+result);
        return result;
    }

    public static int argbValueFromGray(int gray, byte grayBits) {
        int v1 = gray;
        int v2 = v1 >> grayBits;
        int v3 = v2 >> grayBits;
        int v4 = v3 >> grayBits;
        int part = v1 | v2 | v3 | v4;
        //System.out.println("argbValueFromGray("+gray+","+grayBits+")="+v1+","+v2+","+v3+","+v4+" :: "+part);
        return part;
    }

    public static byte int2byte(int v) {
        return (byte) (v & 0x000000FF);
    }

    public static int byte2int(byte v) {
        return 0x000000FF & v;
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
