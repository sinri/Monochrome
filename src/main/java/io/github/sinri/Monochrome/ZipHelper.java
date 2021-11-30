package io.github.sinri.Monochrome;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipHelper {
    public static void unzip(File zipFile, File outputFile) throws IOException {
        System.out.println("unzipping " + zipFile.getAbsolutePath() + " to " + outputFile.getAbsolutePath());

        ZipFile z = new ZipFile(zipFile);
        ZipEntry entry = z.getEntry(outputFile.getName());
        try (
                InputStream input = z.getInputStream(entry);
                OutputStream output = new FileOutputStream(outputFile)
        ) {
            int temp;
            long size = 0;
            while ((temp = input.read()) != -1) {
                output.write(temp);
                size++;
            }
            System.out.println("unzipped raw bytes: " + size);
        }
    }

    public static byte[] unzipToBytes(File zipFile, String outputFileName) throws IOException {
        System.out.println("unzipping " + zipFile.getAbsolutePath() + " to bytes");

        ZipFile z = new ZipFile(zipFile);
        ZipEntry entry = z.getEntry(outputFileName);
        try (
                InputStream input = z.getInputStream(entry)
        ) {
            ArrayList<Byte> al = new ArrayList<>();
            byte[] temp = new byte[1024];
            while (true) {
                int read = input.read(temp);
                if (read == -1) {
                    break;
                }
                for (byte t : temp) {
                    al.add(t);
                }
                temp = new byte[1024];
            }
            temp = new byte[al.size()];
            for (int i = 0; i < al.size(); i++) {
                temp[i] = al.get(i);
            }
            return temp;
        }
    }

    public static void zip(byte[] content, File zipFile) throws IOException {
        try (
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))
        ) {
            String mncZipName = zipFile.getName();
            String mncName = mncZipName.substring(0, mncZipName.lastIndexOf("."));
            zipOut.putNextEntry(new ZipEntry(mncName));
            zipOut.write(content);
        }
    }

    public static void zip(File inputFile, File zipFile) throws IOException {
        System.out.println("zipping " + inputFile.getAbsolutePath() + " to " + zipFile.getAbsolutePath());

        try (
                InputStream input = new FileInputStream(inputFile);
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))
        ) {
            zipOut.putNextEntry(new ZipEntry(inputFile.getName()));
            int temp;
            long size = 0;
            while ((temp = input.read()) != -1) {
                zipOut.write(temp);
                size++;
            }
            System.out.println("zipped raw bytes: " + size);
        }
    }
}
