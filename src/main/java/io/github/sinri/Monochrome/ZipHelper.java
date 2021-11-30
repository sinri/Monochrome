package io.github.sinri.Monochrome;

import java.io.*;
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

    /**
     * @param zipPath
     * @param outFilePath
     * @throws IOException
     * @deprecated
     */
    public static void unzip(String zipPath, String outFilePath) throws IOException {
        System.out.println("unzipping " + zipPath + " to " + outFilePath);
        File file = new File(zipPath);//压缩文件路径和文件名
        File outFile = new File(outFilePath);//解压后路径和文件名
        String filename = outFile.getName();
        ZipFile zipFile = new ZipFile(file);
        ZipEntry entry = zipFile.getEntry(filename);//所解压的文件名
        InputStream input = zipFile.getInputStream(entry);
        OutputStream output = new FileOutputStream(outFile);
        int temp;
        long size = 0;
        while ((temp = input.read()) != -1) {
            output.write(temp);
            size++;
        }
        System.out.println("unzipped raw bytes: " + size);
        input.close();
        output.close();
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

    /**
     * @param filePath
     * @param zipPath
     * @throws IOException
     * @deprecated
     */
    public static void zip(String filePath, String zipPath) throws IOException {
        System.out.println("zipping " + filePath + " to " + zipPath);
        File file = new File(filePath);
        File zipFile = new File(zipPath);
        InputStream input = new FileInputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOut.putNextEntry(new ZipEntry(file.getName()));
        int temp;
        long size = 0;
        while ((temp = input.read()) != -1) {
            zipOut.write(temp);
            size++;
        }
        System.out.println("zipped raw bytes: " + size);
        input.close();
        zipOut.close();
    }
}
