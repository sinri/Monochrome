package io.github.sinri.Monochrome;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CommonImage {
    public BufferedImage image = null;

    public CommonImage(File file) throws IOException {
        image = ImageIO.read(file);
    }

    public CommonImage(int width, int height, int[] pixels) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setImageWithARGBArray(width, height, pixels);
    }

    public static int alphaFromARGB(int cARGB) {
        return ((cARGB >> 24) & 0x000000FF);
    }

    public static int redFromARGB(int cARGB) {
        return ((cARGB >> 16) & 0x000000FF);
    }

    public static int greenFromARGB(int cARGB) {
        return ((cARGB >> 8) & 0x000000FF);
    }

    public static int blueFromARGB(int cARGB) {
        return ((cARGB) & 0x000000FF);
    }

    public static String hexOfColor(int cARGB) {
//        int alpha=alphaFromARGB(cARGB);
//        int red=alphaFromARGB(cARGB);
//        int green=alphaFromARGB(cARGB);
//        int blue=alphaFromARGB(cARGB);
        return "0x" + Integer.toHexString(cARGB);

    }

    public static int colorWithARGB(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * @param file   the File object
     * @param format jpg, png and gif
     * @throws IOException when writing file is not well enough
     */
    public void saveToFileWithFormat(File file, String format) throws IOException {
        ImageIO.write(image, format, file);
    }

    /**
     * @param pixels null I think also works
     * @return an array of integer for color parameters
     */
    public int[] getARGBArray(int[] pixels) {
        int x = 0;
        int y = 0;
        int width = image.getWidth();
        int height = image.getHeight();


        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
        } else {
            return image.getRGB(x, y, width, height, pixels, 0, width);
        }
    }

    public final void setImageWithARGBArray(int width, int height, int[] pixels) {
        int x = 0;
        int y = 0;
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }

    public final void cleanNoise(int blockSize) {
        System.out.println("cleanNoise blockSize=" + blockSize + " W=" + image.getWidth() + " H=" + image.getHeight());
        int x = 0;
        int y = 0;

        BufferedImage mirror = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        while (x < image.getWidth()) {
            while (y < image.getHeight()) {
                int mainColor = image.getRGB(x, y);
                int main_red = redFromARGB(mainColor);
                int main_green = greenFromARGB(mainColor);
                int main_blue = greenFromARGB(mainColor);

                int score = 0;

                for (int i = 1 - blockSize; i < blockSize; i++) {
                    if (i == 0) continue;
                    if (x + i < 0 || x + i >= image.getWidth()) continue;
                    int t = image.getRGB(x + i, y);
                    int t_red = redFromARGB(t);
                    int t_green = greenFromARGB(t);
                    int t_blue = greenFromARGB(t);

                    if (
                            Math.abs(main_red - t_red) < 16
                                    && Math.abs(main_green - t_green) < 16
                                    && Math.abs(main_blue - t_blue) < 16
                    ) {
                        score++;
                    }
                }
                for (int i = 1 - blockSize; i < blockSize; i++) {
                    if (i == 0) continue;
                    if (y + i < 0 || y + i >= image.getHeight()) continue;
                    int t = image.getRGB(x, y + i);
                    int t_red = redFromARGB(t);
                    int t_green = greenFromARGB(t);
                    int t_blue = greenFromARGB(t);

                    if (
                            Math.abs(main_red - t_red) < 16
                                    && Math.abs(main_green - t_green) < 16
                                    && Math.abs(main_blue - t_blue) < 16
                    ) {
                        score++;
                    }
                }

                int mirrorColor = mainColor;
                if (score < blockSize / 2) {
                    // this point should be empty
                    mirrorColor = 0xFFFFFF;
                }

                mirror.setRGB(x, y, mirrorColor);
//                System.out.println("Decide Mirror ("+x+","+y+") "+hexOfColor(mainColor)+" -> "+hexOfColor(mirrorColor));

                // finally
                y++;
//                System.out.println("y++ as "+y);
            }

            x++;
//            System.out.println("x++ as "+x);
            y = 0;
        }

        image = mirror;
    }
}
