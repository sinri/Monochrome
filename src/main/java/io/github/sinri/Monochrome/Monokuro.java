package io.github.sinri.Monochrome;

import java.io.File;
import java.io.IOException;

public class Monokuro {
    public static void showUsage() {
        System.out.println("Usage:\n" +
                "java -jar Monokuro.jar [mnc] GRAY_BYTES [file|dir] INPUT_IMAGE_PATH OUTPUT_MNC[.ZIP]_PATH\n" +
                "java -jar Monokuro.jar [jpg|png|gif] [file|dir] INPUT_MNC[.ZIP]_PATH OUTPUT_IMAGE_PATH\n" +
                "GRAY_BYTES is an integer amongst [1,7]");
    }

    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                Monokuro.showUsage();
                throw new RuntimeException("ARGS LACK");
            }

            String action = args[0];

            if (
                    action.equalsIgnoreCase("mnc")
                            || action.equalsIgnoreCase("mnc.zip")
            ) {
                if (args.length < 5) {
                    Monokuro.showUsage();
                    throw new RuntimeException("ARGS LACK");
                }

                byte gray_bytes = Byte.parseByte(args[1]);
                if (gray_bytes < 1 || gray_bytes > 7) {
                    throw new RuntimeException("gray_bytes should be amongst [1,7]!");
                }

                String fs_type = args[2];
                String input_path = args[3];
                String output_path = args[4];
                if (fs_type.equalsIgnoreCase("file")) {
                    encodeCommonImageToMncFile(gray_bytes, input_path, output_path);
                } else if (fs_type.equalsIgnoreCase("dir")) {
                    encodeCommonImagesInDirToMncFiles(gray_bytes, input_path, output_path);
                } else {
                    throw new RuntimeException("fs_type should be `file` or `dir`!");
                }
                System.out.println("Generated File: " + output_path);
            } else if (
                    action.equalsIgnoreCase("jpg")
                            || action.equalsIgnoreCase("png")
                            || action.equalsIgnoreCase("gif")
            ) {
                String fs_type = args[1];
                String input_path = args[2];
                String output_path = args[3];
                if (fs_type.equalsIgnoreCase("file")) {
                    decodeMncFileToCommonImage(input_path, output_path, action);
                } else if (fs_type.equalsIgnoreCase("dir")) {
                    decodeMncFilesInDirToCommonImages(input_path, output_path, action);
                } else {
                    throw new RuntimeException("fs_type should be `file` or `dir`!");
                }
                System.out.println("Generated File: " + output_path);
            } else {
                throw new RuntimeException("action should be `encode` or `decode`!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            showUsage();
        }
    }

    protected static void encodeCommonImageToMncFile(byte gray_bytes, String commonImageFilePath, String mncFilePath) throws IOException {
        CommonImage oriImage = new CommonImage(new File(commonImageFilePath));
        MNCImage mncImage = new MNCImage(oriImage, gray_bytes);
        if (mncFilePath.endsWith(".zip")) {
            mncImage.saveZippedMNCImageToFile(new File(mncFilePath));
            System.out.println("[DEBUG] mnc.zip file [" + mncFilePath + "] created: " + (new File(mncFilePath)).exists());
        } else {
            mncImage.saveMNCImageToFile(new File(mncFilePath));
            System.out.println("[DEBUG] mnc file [" + mncFilePath + "] created: " + (new File(mncFilePath)).exists());
        }
    }

    protected static void decodeMncFileToCommonImage(String mncFilePath, String commonImageFilePath, String format) throws IOException {
        MNCImage mncImage;
        if (mncFilePath.endsWith(".zip")) {
            String x = (new File(commonImageFilePath)).getName();
            x = x.substring(0, x.lastIndexOf("."));
            byte[] bytes = ZipHelper.unzipToBytes(new File(mncFilePath), x + ".mnc");
            mncImage = new MNCImage(bytes);
        } else {
            mncImage = new MNCImage(new File(mncFilePath));
        }

        CommonImage commonImage = mncImage.convertToCommonImage();
        commonImage.saveToFileWithFormat(new File(commonImageFilePath), format);
    }

    protected static void encodeCommonImagesInDirToMncFiles(byte gray_bytes, String commonImageDirPath, String mncDirPath) throws IOException {
        File inputFolder = new File(commonImageDirPath);
        File outputFolder = new File(mncDirPath);

        if (!inputFolder.isDirectory()) {
            throw new RuntimeException("input dir is not there!");
        }

        File[] subFiles = inputFolder.listFiles();
        if (subFiles == null || subFiles.length <= 0) {
            throw new RuntimeException("input dir empty!");
        }
        if (outputFolder.isDirectory() || outputFolder.mkdirs()) {
            for (File file : subFiles) {
                if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                    System.out.println("Processing " + file.getAbsolutePath() + "...");

                    encodeCommonImageToMncFile(
                            gray_bytes,
                            file.getAbsolutePath(),
                            outputFolder.getAbsolutePath() + File.separator + file.getName() + ".mnc"
                    );
                }
            }
        } else {
            throw new RuntimeException("output dir is not there!");
        }
    }

    protected static void decodeMncFilesInDirToCommonImages(String mncDirPath, String commonImageDirPath, String format) throws IOException {
        File inputFolder = new File(mncDirPath);
        File outputFolder = new File(commonImageDirPath);

        if (!inputFolder.isDirectory()) {
            throw new RuntimeException("input dir is not there!");
        }

        File[] subFiles = inputFolder.listFiles();
        if (subFiles == null || subFiles.length <= 0) {
            throw new RuntimeException("input dir empty!");
        }
        if (outputFolder.isDirectory() || outputFolder.mkdirs()) {
            for (File file : subFiles) {
                if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                    System.out.println("Processing " + file.getAbsolutePath() + "...");

                    decodeMncFileToCommonImage(
                            file.getAbsolutePath(),
                            outputFolder.getAbsolutePath() + File.separator + file.getName() + "." + format,
                            format
                    );
                }
            }
        } else {
            throw new RuntimeException("output dir is not there!");
        }
    }
}
