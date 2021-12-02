package io.github.sinri.Monochrome.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class TransformBinaryFileToHex {
    public static void main(String[] args) {
        try {
            work(
                    new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic"),
                    new File("/Users/leqee/code/Monochrome/debug/mosaic/g2.mosaic.hex")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void work(File binaryFile, File hexFile) throws IOException {
        byte[] bytes;
        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(binaryFile))
        ) {
            bytes = bis.readAllBytes();
            if (bytes == null) {
                throw new IOException("read bytes but null found");
            }
        }

        try (
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(hexFile))
        ) {
            for (int i = 0; i < bytes.length; i++) {
                if (i > 0 && i % 8 == 0) {
                    // new line
                    bos.write("\n".getBytes(StandardCharsets.UTF_8));
                }
                if (i % 64 == 0) {
                    bos.write(("# " + i + "\n").getBytes(StandardCharsets.UTF_8));
                }
                int v = bytes[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    bos.write("0".getBytes(StandardCharsets.UTF_8));
                }
                bos.write((hv + " ").toUpperCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
